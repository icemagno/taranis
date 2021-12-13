package br.mil.defesa.sisgeodef.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PointService {
	
	@Value("${spring.datasource.url}")
	private String connectionString;	

	@Value("${spring.datasource.username}")
	private String userName;	

	@Value("${spring.datasource.password}")
	private String userPassword;		
	
	@PostConstruct
	public void initTable() {
		String createTableCommand = "CREATE TABLE POINT_TABLE ( "
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
			e.printStackTrace();
		}
		
	}

}
