package br.mil.defesa.sisgeodef.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.mil.defesa.sisgeodef.misc.DBZColorRamp;
import br.mil.defesa.sisgeodef.misc.Point;

@Service
public class PointService {
	
	@Value("${spring.datasource.url}")
	private String connectionString;	

	@Value("${spring.datasource.username}")
	private String userName;	

	@Value("${spring.datasource.password}")
	private String userPassword;		
	
	private DBZColorRamp dbzRamp = new DBZColorRamp();
	
	@PostConstruct
	public void initTable() {
		String createTableCommand = "CREATE TABLE point_table ( "
				+ "CONSTRAINT print_accountings_pkey PRIMARY KEY (jobId), "
				+ "jobId character varying(255) NOT NULL, "
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
		
		populatePointTable();
	}

	
	public void populatePointTable() {
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
				"	data_hora = timestamp '2016-04-18 22:55:27'" + 
				"and" + 
				"	dbz_03000 is not null limit 100";
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
		    				Point pt = new Point( lat, lon, height, val, dbzRamp.getColor(val) );
		    				System.out.println( lat + "," + lon + " || " + height + " = " + val );
		    				
		    				// Inserir na tabela de pontos do PointGen ... 
		    			}
		    		}
		    	}
		    }
        } catch (SQLException e) {
        	e.printStackTrace();
	    }		    
		
	}
	
	
}
