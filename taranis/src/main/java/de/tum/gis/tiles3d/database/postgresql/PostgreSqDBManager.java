/*
 * Cesium Point Cloud Generator
 * 
 * Copyright 2017 - 2018
 * Chair of Geoinformatics
 * Technical University of Munich, Germany
 * https://www.gis.bgu.tum.de/
 * 
 * The Cesium Point Cloud Generator is developed at Chair of Geoinformatics,
 * Technical University of Munich, Germany.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.tum.gis.tiles3d.database.postgresql;

import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;

import de.tum.gis.tiles3d.concurrent.PntcQueryResult;
import de.tum.gis.tiles3d.database.DBManager;
import de.tum.gis.tiles3d.generator.PntcConfig;
import de.tum.gis.tiles3d.model.BoundingBox2D;
import de.tum.gis.tiles3d.model.PointCloudModel;
import de.tum.gis.tiles3d.model.PointObject;
import de.tum.gis.tiles3d.model.Region;
import de.tum.gis.tiles3d.util.CoordianteConverter;
import de.tum.gis.tiles3d.util.CoordinateConversionException;

public class PostgreSqDBManager implements DBManager {
	private PntcConfig config;
	private Connection connection;	
	public final static int batchInsertionSize = 1000;
	final static int fetchSize = 1000;
	final static String tempDbName = "tmp";
	
	public PostgreSqDBManager(PntcConfig config) throws Exception {
		this.config = config;
		this.initDB();
	}
	
	private void initDB() throws Exception {
		this.connection  =  DriverManager.getConnection( config.getConnectionString(), config.getUserName(), config.getUserPassword() );
	}

	public BoundingBox2D calculateGlobalBoundingbox() throws SQLException {
		String queryCommand = "SELECT MIN(x) as minX, MIN(y) as minY, "
				+ "MAX(x) as maxX, MAX(y) as maxY FROM point_table where jobid = '" + config.getJobId() + "';" ;
		Statement stmt = null;
		ResultSet result = null;
		try {		
			stmt = connection.createStatement();
			result = stmt.executeQuery(queryCommand);	
			if ( result.next() ) {
				double minX = result.getDouble("minX");
				double minY = result.getDouble("minY");
				double maxX = result.getDouble("maxX");
				double maxY = result.getDouble("maxY");
				return new BoundingBox2D(minY, minX, maxY, maxX);
			} 
			throw new SQLException("No records found.");
		} catch (SQLException e) {
			throw new SQLException("Faild to query data from database.", e);
		} finally {
			if (result != null)
				result.close();
			if (stmt != null)
				stmt.close();			
		}	
	}

	public PntcQueryResult queryPointEntities(PointCloudModel pntModel) throws SQLException {
		BoundingBox2D boundingbox = pntModel.getOwnerTileBoundingBox();
		int numberLimit = pntModel.getMaximumNumberOfPoints();
		
		double minX = boundingbox.getMinX();
		double maxX = boundingbox.getMaxX();
		double minY = boundingbox.getMinY();
		double maxY = boundingbox.getMaxY();
		
		ResultSet result = null;
		Statement stmt = null;
		String queryCommand = "SELECT * FROM point_table where jobid = '" + config.getJobId() + "' and " 
				+ "x > " + minX + " and "
				+ "x < " + maxX + " and "
				+ "y > " + minY + " and "
				+ "y < " + maxY + " ORDER BY random() LIMIT " + numberLimit;
		
		List<Coordinate> coordinateList = new ArrayList<Coordinate>();
		List<Color> colorList = new ArrayList<Color>();
		try {	
			stmt = connection.createStatement();
			stmt.setFetchSize(fetchSize);
			result = stmt.executeQuery(queryCommand);
			
			double minZ = Integer.MAX_VALUE;
			double maxZ = Integer.MIN_VALUE;
			
			while (result.next()) {	
	        	double x = result.getDouble("x");
	        	double y = result.getDouble("y");
	        	double z = result.getDouble("z");  
	        	Coordinate inputCoordinate = new Coordinate(x, y, z);
				Coordinate ecefCoordinate = CoordianteConverter.convertPointToWGS84Cartesian( inputCoordinate, config.getSrid() );	
				coordinateList.add(ecefCoordinate);
				
	        	if (z < minZ)
	        		minZ = z;
	        	if (z > maxZ)
	        		maxZ = z;   
	        	
	        	int r = result.getInt("r");
	        	int g = result.getInt("g");
	        	int b = result.getInt("b");
	        	Color color = new Color(r, g, b);
				colorList.add(color);
	        }
						
			if (coordinateList.size() == 0) {	
				pntModel.setOwnerTileBoundingVolume(null);
			}
			else {
				Region region = CoordianteConverter.convert2DBoundingboxToWGS84Region(
						new BoundingBox2D(minY, minX, maxY, maxX), config.getSrid());
				
		        region.setMinZ(minZ);
		        region.setMaxZ(maxZ);	
		        pntModel.setOwnerTileBoundingVolume(region);
			}    			
		} catch (CoordinateConversionException e) {
			throw new SQLException("Error occured while converting coordinates values fetched from database.", e);
		} finally {
			if (result != null)
				result.close();
			if (stmt != null)
				stmt.close();			
		}
		
		if (coordinateList.size() != 0) {
			PntcQueryResult queryResult = new PntcQueryResult();
			queryResult.setCoordinateList(coordinateList);
			queryResult.setColorList(colorList);
			return queryResult;
		}
		else
			return null;
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public void KillConnection() {
		try {
			if (!connection.isClosed())
				connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}

	@Override
	public void createConnection() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createDataTable() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createIndexes() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void importIntoDatabase(List<PointObject> pointList) throws SQLException {
		// TODO Auto-generated method stub
		
	}

}
