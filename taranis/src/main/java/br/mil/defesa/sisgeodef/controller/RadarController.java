package br.mil.defesa.sisgeodef.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.mil.defesa.sisgeodef.services.RadarService;

@RestController
public class RadarController {

	@Autowired
	RadarService radarService; 
	
	@RequestMapping(value = "/radar", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_UTF8_VALUE )
	public @ResponseBody String getBuildings( @RequestParam(value="count",required=true) String count,
			@RequestParam(value="l",required=true) String l,
			@RequestParam(value="r",required=true) String r,
			@RequestParam(value="t",required=true) String t,
			@RequestParam(value="b",required=true) String b) {
		return radarService.getRadar(count, l, r, t, b);
	}
	
}
