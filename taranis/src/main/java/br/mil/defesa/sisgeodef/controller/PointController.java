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
	
	// tileSize = Tamanho quadrado do ladrilho na distância da unidade de medida da projeção usada.
	// Para EPSG:4326 usar 0.15 graus
	// Para EPSG:2994 usar 100 metros
	
	// http://localhost:36700/populate?jobid=2016-04-18-23-07-24&datahora=2016-04-18 23:07:24&selector=dbz
	// http://localhost:36700/generatepointcloud?jobid=2016-04-18-23-07-24&maxpoints=250000&tilesize=0.30&srid=4326&geratio=1
		   
	// http://localhost:36700/populate?jobid=2016-04-18-22-55-27&datahora=2016-04-18 22:55:27&selector=dbz
	// http://localhost:36700/generatepointcloud?jobid=2016-04-18-22-55-27&maxpoints=250000&tilesize=0.30&srid=4326&geratio=1
	
	@Autowired
	private PointService ptService;
	
	@RequestMapping( value = "/populate", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE ) 
    public String populateDbz( @RequestParam(value="jobid",required=true) String jobId,
    		@RequestParam(value="datahora",required=true) String datahora,
    		@RequestParam(value="selector",required=true) String selector ) {
		ptService.populatePointTableFromRadarData(jobId, datahora, selector);
		return "ok";
    }	

	@RequestMapping( value = "/generatepointcloud", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE ) 
    public String generatePointCloud( @RequestParam(value="jobid",required=true) String jobId,
    		@RequestParam(value="srid",required=true) String srid,
    		@RequestParam(value="maxpoints",required=true) Integer maxPointsPerTile,
    		@RequestParam(value="tilesize",required=true) Double tileSize,
    		@RequestParam(value="geratio",required=true) Double geometricErrorRatio) {
		ptService.readSourcePointData(jobId, maxPointsPerTile, tileSize, srid, geometricErrorRatio );
		return "ok";
    }	
	
	
	@RequestMapping( value = "/jobstatus", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE ) 
    public String status( @RequestParam(value="jobid",required=true) String jobId ) {
		return ptService.getStatus(jobId);
    }
	
	@RequestMapping( value = "/generatefromfile", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE ) 
    public String populateFromFile( @RequestParam(value="jobid",required=true) String jobId,
    		@RequestParam(value="srid",required=true) String srid,
    		@RequestParam(value="maxpoints",required=true) Integer maxPointsPerTile,
    		@RequestParam(value="tilesize",required=true) Double tileSize ) {
		ptService.populatePointTableFromFile(jobId, maxPointsPerTile, tileSize, srid);
		return "ok";
    }	
	
	
	
}
