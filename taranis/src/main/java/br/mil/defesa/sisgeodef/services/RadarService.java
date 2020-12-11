package br.mil.defesa.sisgeodef.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import br.mil.defesa.sisgeodef.misc.Point;

@Service
public class RadarService {
	
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	
	private String connectionString = "jdbc:postgresql://taranis-db:5432/taranis?ApplicationName=Taranis";  	
	private String user = "postgres";  	
	private String password = "admin";  	
	
	public String getRadar( String count, String l, String r, String t, String b ) {
		String result = "{}";
		String sql = "select * from public.getsipamradar( "+count+", "+l+", "+b+", "+r+", "+t+") as resultset";
		
		System.out.println( sql );
		
		try (
			Connection sqlConnection  =  DriverManager.getConnection(connectionString, user, password);
			PreparedStatement ps = sqlConnection.prepareStatement( sql ); 
	        ResultSet rs = ps.executeQuery() ) {
		    while (rs.next() ) {
		        result = rs.getString("resultset");
		        JSONObject job = new JSONObject( result );
		        // Testa Features para nunca ir como NULL para o frontend. 
		        // Devera sempre ir como array ( ou array vazio )
		        try {
		        	//@SuppressWarnings("unused")
					JSONArray jarr = job.getJSONArray("features");
		        	for( int x=0; x < jarr.length(); x++ ) {
		        		JSONObject feature = jarr.getJSONObject( x );
		        		JSONArray coordinates = feature.getJSONObject("geometry").getJSONArray("coordinates");

		        		JSONObject props = feature.getJSONObject("properties").getJSONObject("getproperties");
		        		Iterator<String> keys = props.keys();
		        		while(keys.hasNext()) {
		        			
		        			String key = keys.next();
		        			if( key.startsWith("dbz_") ) {
		        				int height = Integer.valueOf( key.substring(4) );
		        				double value = props.getDouble(key);
		        				
				        		Point pt = new Point( coordinates.getDouble(1), coordinates.getDouble(0), height, value, "red" );
				        		simpMessagingTemplate.convertAndSend("/points/dbz", pt );
				        		
		        			}
		        			
		        		}
		        		
		        		
		        	}
		        } catch ( JSONException je ) { 
		        	job.put("features", new JSONArray("[]") );
		        }
		        result = job.toString();
			}
        } catch (SQLException e) {
        	e.printStackTrace();
	    }
		
		return result;
	}	
	
}
