	
	
	var url = "/radar?l={l}&r={r}&t={t}&b={b}";
	var radarProvider = new MagnoMetocRadarProvider({
	  debugTiles : false,
	  viewer : viewer,
	  activationLevel : 5,
	  sourceUrl : url,
	  featuresPerTile : 1000,
	  rampStart : '#b9e1fa',
	  rampEnd : '#039eff',
	  rampCountStart : -30,
	  rampCountEnd : 50,
	  whenFeaturesAcquired : function( entities ){
		console.log( entities.length + " celulas recebidas." );
	  }
	});
	viewer.imageryLayers.addImageryProvider( radarProvider );	