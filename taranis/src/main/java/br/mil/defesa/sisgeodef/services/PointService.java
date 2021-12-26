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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private Logger logger = LoggerFactory.getLogger( PointService.class );
	
	@Value("${spring.datasource.url}")
	private String connectionString;	

	@Value("${spring.datasource.username}")
	private String userName;	

	@Value("${spring.datasource.password}")
	private String userPassword;		

	@Value("${taranis.sourcepath}")
	private String sourcePath;	

	@Value("${taranis.outputpath}")
	private String outputPath;	
	
	private DBZColorRamp dbzRamp = new DBZColorRamp();
	private Map<String,Boolean> runningJobs = new HashMap<String,Boolean>();
	
	@PostConstruct
	private void init() {
		if( !sourcePath.endsWith("/") ) sourcePath = sourcePath + "/";
		if( !outputPath.endsWith("/") ) outputPath = outputPath + "/";
	}

	public void readSourcePointData( String jobId, int maxPointsPerTile, double tileSize, String srid ) {

		Boolean isRunning = runningJobs.get( jobId );
		if ( isRunning != null && isRunning ) {
			logger.info( "Job " + jobId + " already running." );
			return;
		}
		logger.info( "Job " + jobId + " start." );
		
		PntcConfig config = new PntcConfig();		
		config.setInputPath( sourcePath + jobId ) ;
		config.setSrid( srid );
		config.setTargetSrid( "4326" );
		config.setMustReproject( false );
		config.setSeparatorCharacter( " " );
		config.setColorBitSize( 16 );
		config.setZScaleFactor( 0.1 );
		config.setTileSize( tileSize );
		config.setMaxNumOfPointsPerTile( maxPointsPerTile );
		config.setOutputFolderPath( outputPath + jobId );
		config.setRefinamentModel( Refine.ADD );
		config.setzOffset( 0 );
		config.setConnectionString(connectionString);
		config.setUserName(userName);
		config.setUserPassword(userPassword);
		config.setJobId( jobId );
		
		logger.info( "Tile size: " + tileSize );
		logger.info( "Max Points per Tile: " + maxPointsPerTile );
		
		new File( config.getOutputFolderPath() ).mkdirs();
		
		PostgreSqlDBManagerFactory dbManagerFactory = new PostgreSqlDBManagerFactory(config);		
		
		
		runningJobs.put( jobId, true );
		new Thread() {
			@Override
			public void run() {
				generateCloud( dbManagerFactory, config );
				notifyWhenDone( jobId );
			}
		}.start();		
		
		
	}
	
	
	private void generateCloud( PostgreSqlDBManagerFactory dbManagerFactory, PntcConfig config ) {
		boolean success = false;
		try {
			PntcGenerator generator = new PntcGenerator(config, dbManagerFactory);	
			success = generator.doProcess();
		} catch ( Exception e ) {
			logger.info(e.getMessage());
			Throwable cause = e.getCause();
			while (cause != null) {
				cause = cause.getCause();
			}
		}	
		if (success) {
			logger.info("Finished.");
		} else {
			logger.info("Export aborted.");
		}		
	}

	private void generateCloudFromFile( PostgreSqlDBManagerFactory dbManagerFactory, PntcConfig config ) {
		boolean success = false;
		try {
			PntcGenerator generator = new PntcGenerator(config, dbManagerFactory);
			generator.readSourcePointData();
			success = generator.doProcess();
		} catch ( Exception e ) {
			logger.info(e.getMessage());
			Throwable cause = e.getCause();
			while (cause != null) {
				cause = cause.getCause();
			}
		}	
		if (success) {
			logger.info("Finished.");
		} else {
			logger.info("Export aborted.");
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
			logger.info( e.getMessage() );
		}
		
	}

	private void emptyByJob( String jobId ) {
		logger.info("Cleaning job " + jobId + " from database.");
		String emptyJob = "delete from point_table where jobid = '" + jobId + "'";
		try {
			Connection sqlConnection  =  DriverManager.getConnection(connectionString, userName, userPassword);
			PreparedStatement ps = sqlConnection.prepareStatement( emptyJob ); 
		    ps.executeUpdate(); 
		    logger.info("done.");
		} catch ( Exception e ) {
			logger.error("Can't delete data from job " + jobId );
			logger.error( e.getMessage() );
		}		
	}
	
	
	public void populatePointTableFromRadarData( String jobId, String dataHora, String selector ) {
		Boolean isRunning = runningJobs.get( jobId );
		if ( isRunning != null && isRunning ) {
			logger.warn( "Job " + jobId + " already running." );
			return;
		}
		logger.info( "Job " + jobId + " start." );
		logger.info( "Selecting all " + selector + " from " + dataHora );
		runningJobs.put( jobId, true );
		new Thread() {
			@Override
			public void run() {
				// selector = dbz_
				runJob ( jobId, dataHora, selector );
				notifyWhenDone( jobId );
			}
		}.start();		
	}
	
	private void notifyWhenDone( String jobId ) {
		runningJobs.put( jobId, false );
		logger.info( "Job " + jobId + " done." );
	}
	
	
	private void runJob ( String jobId, String dataHora, String selector ) {
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
					"	data_hora = timestamp '" + dataHora + "'";
		
		try (
			Connection sqlConnection  =  DriverManager.getConnection(connectionString, userName, userPassword);
			PreparedStatement ps = sqlConnection.prepareStatement( sql ); 
	        ResultSet rs = ps.executeQuery() ) {
			ResultSetMetaData meta = rs.getMetaData();
			int ncols = meta.getColumnCount();
			logger.info("Starting to convert records to point data. Wait...");
		    while (rs.next() ) {
		    	double lat = rs.getDouble("latitude");
		    	double lon = rs.getDouble("longitude");
		    	for (int colno = 1; colno <= ncols; ++colno) {
		    		String name = meta.getColumnName(colno);
		    		if( name.startsWith( selector ) ) {
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
		    				insertPs.setDouble(8, height );
		    				insertPs.executeUpdate();
		    			}
		    		}
		    	}
		    }
		    logger.info("done importing job " + jobId );
        } catch (SQLException e) {
        	logger.error("Error importing data.");
        	logger.error( e.getMessage() );
	    }		    
	}

	public String getStatus(String jobId) {
		Boolean isRunning = runningJobs.get( jobId );
		if( isRunning == null ) return "Job not found";
		if ( isRunning ) return "Job is running";
		return "Job is finished";
		
	}

	public void populatePointTableFromFile(String jobId, int maxPointsPerTile, double tileSize, String srid ) {

		Boolean isRunning = runningJobs.get( jobId );
		if ( isRunning != null && isRunning ) {
			logger.info( "Job " + jobId + " already running." );
			return;
		}
		logger.info( "Job " + jobId + " start." );
		
		
		PntcConfig config = new PntcConfig();		
		config.setInputPath( sourcePath + jobId ) ;
		config.setSrid( srid );
		config.setTargetSrid( "4326" );
		config.setMustReproject( false );
		config.setSeparatorCharacter( " " );
		config.setColorBitSize( 16 );
		config.setZScaleFactor( 0.1 );
		config.setTileSize( tileSize );
		config.setMaxNumOfPointsPerTile( maxPointsPerTile );
		config.setOutputFolderPath( outputPath + jobId );
		config.setRefinamentModel( Refine.REPLACE );
		config.setzOffset( 0 );
		config.setConnectionString(connectionString);
		config.setUserName(userName);
		config.setUserPassword(userPassword);
		config.setJobId( jobId );
		
		logger.info( "Tile size: " + tileSize );
		logger.info( "Max Points per Tile: " + maxPointsPerTile );
		
		new File( config.getOutputFolderPath() ).mkdirs();
		
		PostgreSqlDBManagerFactory dbManagerFactory = new PostgreSqlDBManagerFactory(config);		
		
		
		runningJobs.put( jobId, true );
		new Thread() {
			@Override
			public void run() {
				generateCloudFromFile( dbManagerFactory, config );
				notifyWhenDone( jobId );
			}
		}.start();		
	}
	
	
}
