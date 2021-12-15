package br.mil.defesa.sisgeodef.services;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.mil.defesa.sisgeodef.misc.DBZColorRamp;
import br.mil.defesa.sisgeodef.misc.Point;
import de.tum.gis.tiles3d.database.postgresql.PostgreSqlDBManagerFactory;
import de.tum.gis.tiles3d.generator.PntcConfig;
import de.tum.gis.tiles3d.generator.PntcGenerator;
import de.tum.gis.tiles3d.model.Refine;

@Service
public class PointService {
	
	@Value("${spring.datasource.url}")
	private String connectionString;	

	@Value("${spring.datasource.username}")
	private String userName;	

	@Value("${spring.datasource.password}")
	private String userPassword;		
	
	private DBZColorRamp dbzRamp = new DBZColorRamp();
	private Map<String,Boolean> runningJobs = new HashMap<String,Boolean>();
	

	public void readSourcePointData( String jobId ) {

		Boolean isRunning = runningJobs.get( jobId );
		if ( isRunning != null && isRunning ) {
			System.out.println( "Job " + jobId + " already running." );
			return;
		}
		System.out.println( "Job " + jobId + " start." );
		runningJobs.put( jobId, true );
		new Thread() {
			@Override
			public void run() {
				generateCloud( jobId );
				notifyWhenDone( jobId );
			}
		}.start();		
		
		
	}
	
	
	private void generateCloud( String jobId ) {
		
		PntcConfig config = new PntcConfig();		
		config.setInputPath( "/pointgen/pointsource" ) ;
		config.setSrid( "4326" );
		config.setTargetSrid( "4326" );
		config.setMustReproject( false );
		config.setSeparatorCharacter( " " );
		config.setColorBitSize( 16 );
		config.setZScaleFactor( 0.1 );
		config.setTileSize( 256 );
		config.setMaxNumOfPointsPerTile( 10000 );
		config.setOutputFolderPath( "/pointgen/pointdata" );
		config.setRefinamentModel( Refine.REPLACE );
		config.setzOffset( 0 );
		config.setConnectionString(connectionString);
		config.setUserName(userName);
		config.setUserPassword(userPassword);
		config.setJobId( jobId );
		
		new File( config.getInputPath() ).mkdirs();
		new File( config.getOutputFolderPath() ).mkdirs();
		
		PostgreSqlDBManagerFactory dbManagerFactory = new PostgreSqlDBManagerFactory(config);
		
		boolean success = false;
		try {
			PntcGenerator generator = new PntcGenerator(config, dbManagerFactory);	
			success = generator.doProcess();
		} catch ( Exception e ) {
			System.out.println(e.getMessage());
			Throwable cause = e.getCause();
			while (cause != null) {
				cause = cause.getCause();
			}
		}	
		
		if (success) {
			System.out.println("Finished.");
		} else {
			System.out.println("Export aborted.");
		}		
	}

	
	
	@PostConstruct
	public void initTable() {
		String createTableCommand = "CREATE TABLE point_table ( "
				//+ "CONSTRAINT print_accountings_pkey PRIMARY KEY (jobId), "
				+ "jobid character varying(255) NOT NULL, "
				+ "x double precision, "
				+ "y double precision, "
				+ "z double precision, "
				+ "r integer, "
				+ "g integer, "
				+ "b integer,"
				+ "temperature integer )";
		try {
			Connection sqlConnection  =  DriverManager.getConnection(connectionString, userName, userPassword);
			PreparedStatement ps = sqlConnection.prepareStatement( createTableCommand ); 
		    ps.executeUpdate(); 
		} catch ( Exception e ) {
			System.out.println( e.getMessage() );
		}
		
	}

	private void emptyByJob( String jobId ) {
		String emptyJob = "delete from point_table where jobid = " + jobId;
		try {
			Connection sqlConnection  =  DriverManager.getConnection(connectionString, userName, userPassword);
			PreparedStatement ps = sqlConnection.prepareStatement( emptyJob ); 
		    ps.executeUpdate(); 
		} catch ( Exception e ) {
			//
		}		
	}
	
	
	public void populatePointTableWithDBZ( String jobId ) {
		Boolean isRunning = runningJobs.get( jobId );
		if ( isRunning != null && isRunning ) {
			System.out.println( "Job " + jobId + " already running." );
			return;
		}
		System.out.println( "Job " + jobId + " start." );
		runningJobs.put( jobId, true );
		new Thread() {
			@Override
			public void run() {
				runJob ( jobId );
				notifyWhenDone( jobId );
			}
		}.start();		
	}
	
	private void notifyWhenDone( String jobId ) {
		runningJobs.put( jobId, false );
		System.out.println( "Job " + jobId + " done." );
	}
	
	
	private void runJob ( String jobId ) {
		emptyByJob(jobId);
		String sql = "select" + 
					"	ST_Y( ST_Transform( ST_Centroid( spg.the_geom )::geometry, 4326) ) as \"latitude\", " + 
					"	ST_X( ST_Transform( ST_Centroid( spg.the_geom )::geometry, 4326) ) as \"longitude\"," + 
					"	ST_Transform( ST_Centroid(spg.the_geom)::geometry, 4326) as \"ponto\"," + 
					"	sp.* " + 
					"from " + 
					"	sipam_grade_sbbe spg " + 
					"join " + 
					"	sipam_sbbe sp on sp.id_grid = spg.id " + 
					"where " + 
					//"	data_hora = timestamp '2016-04-18 22:55:27'" + 
					//"and" + 
					"	dbz_03000 is not null";
		try (
			Connection sqlConnection  =  DriverManager.getConnection(connectionString, userName, userPassword);
			PreparedStatement ps = sqlConnection.prepareStatement( sql ); 
	        ResultSet rs = ps.executeQuery() ) {
			ResultSetMetaData meta = rs.getMetaData();
			int ncols = meta.getColumnCount();			
		    while (rs.next() ) {
		    	double lat = rs.getDouble("latitude");
		    	double lon = rs.getDouble("longitude");
		    	for (int colno = 1; colno <= ncols; ++colno) {
		    		String name = meta.getColumnName(colno);
		    		if( name.startsWith("dbz_") ) {
		    			double val = rs.getDouble( name );
		    			if ( val > 0.0 ) {
		    				int height = Integer.valueOf( name.substring(4) );
		    				Point pt = new Point( lat, lon, height, val, dbzRamp.getColorAsColor(val) );
		    				String insertSql = "insert into point_table(jobid,x,y,z,r,g,b,temperature) values(?,?,?,?,?,?,?,?);";
		    				PreparedStatement insertPs = sqlConnection.prepareStatement( insertSql );
		    				
		    				insertPs.setString(1, jobId);
		    				insertPs.setDouble(2, lon);
		    				insertPs.setDouble(3, lat);
		    				insertPs.setDouble(4, height);
		    				insertPs.setDouble(5, pt.getColor().getRed() );
		    				insertPs.setDouble(6, pt.getColor().getGreen() );
		    				insertPs.setDouble(7, pt.getColor().getBlue() );
		    				insertPs.setDouble(8, 99 );
		    				
		    				int row = insertPs.executeUpdate();
		    				
		    			}
		    		}
		    	}
		    }
        } catch (SQLException e) {
        	e.printStackTrace();
	    }		    
	}

	public String getStatus(String jobId) {
		Boolean isRunning = runningJobs.get( jobId );
		if( isRunning == null ) return "Job not found";
		if ( isRunning ) return "Job is running";
		return "Job is finished";
		
	}
	
	
}
