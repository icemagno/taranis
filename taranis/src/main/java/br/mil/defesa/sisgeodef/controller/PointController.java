package br.mil.defesa.sisgeodef.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.mil.defesa.sisgeodef.services.PointService;

@RestController
public class PointController {
	
	@Autowired
	private PointService ptService;
	
	@RequestMapping( value = "/populatedbz", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE ) 
    public String populateDbz( @RequestParam(value="jobid",required=true) String jobId ) {
		ptService.populatePointTableWithDBZ(jobId);
		return "ok";
    }	

	@RequestMapping( value = "/generatepointcloud", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE ) 
    public String generatePointCloud( @RequestParam(value="jobid",required=true) String jobId ) {
		ptService.readSourcePointData(jobId);
		return "ok";
    }	
	
	
	@RequestMapping( value = "/jobstatus", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE ) 
    public String status( @RequestParam(value="jobid",required=true) String jobId ) {
		return ptService.getStatus(jobId);
    }	    
}
