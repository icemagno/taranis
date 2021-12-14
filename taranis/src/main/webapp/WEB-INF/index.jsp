<!DOCTYPE HTML>
<html>

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no">
    <title>Taranis - Sandbox</title>
    
    <link href="/resources/Cesium/Widgets/widgets.css" rel="stylesheet">
    <script src="/resources/Cesium/Cesium.js" type="text/javascript"></script>
    <script src="/resources/js/jquery-3.3.1.min.js"></script>

    <style>
        html, body, #cesiumContainer {
            width: 100%; height: 100%; margin: 0; padding: 0; overflow: hidden;background-color: #000000;
        }
        input[type=text]{
            width: 30px;
            height: 10px;
        }
        input[type=range] {
            width: 170px;
        }
    </style>
</head>

<body >
	<div style="position:relative" id="cesiumContainer"></div>

	<script type="text/javascript">
	
		function getMapPosition3D2D( movement ){
			if ( viewer.scene.mode == Cesium.SceneMode.SCENE2D ) {
				var position = viewer.camera.pickEllipsoid(movement, scene.globe.ellipsoid);
				if (position) {
					return position;
				} 
			}
			if ( viewer.scene.mode == Cesium.SceneMode.SCENE3D ) {
				var ray = viewer.camera.getPickRay(movement);
				var position = viewer.scene.globe.pick(ray, viewer.scene);
				if (Cesium.defined(position)) {
					return position;
				} 
			}
		}
	
	
		var west = -80.72;
		var south = -37.16;
		var east = -31.14;
		var north = 11.79;	
		var homeLocation = Cesium.Rectangle.fromDegrees(west, south, east, north);        
			
		var baseOsmProvider = new Cesium.OpenStreetMapImageryProvider({
			url : 'https://a.tile.openstreetmap.org/'
		});				
		
		var viewer = new Cesium.Viewer('cesiumContainer',{
			imageryProvider: baseOsmProvider,
			timeline: false,
			skyAtmosphere: false,
			animation: false,
			fullscreenButton : false,
			
			geocoder : false,
			homeButton : false,
			infoBox : false,
			skyBox : false,
			selectionIndicator : false,
			navigationHelpButton : false,
			shouldAnimate : false,
		});
		
		//viewer.extend(Cesium.viewerCesium3DTilesInspectorMixin);
		
		var scene = viewer.scene;
		var center = Cesium.Rectangle.center( homeLocation );
		
		var initialPosition = Cesium.Cartesian3.fromRadians(center.longitude, center.latitude, 1150000);
		var initialOrientation = new Cesium.HeadingPitchRoll.fromDegrees(0, -90, 0);
		
		/*
		scene.camera.setView({
		    destination: initialPosition,
		    orientation: initialOrientation,
		    endTransform: Cesium.Matrix4.IDENTITY
		});	
		*/
	
		scene.globe.depthTestAgainstTerrain = true;
		
		
		
		var layers = scene.imageryLayers;
		scene.highDynamicRange = false;
		scene.globe.enableLighting = false;
		scene.globe.baseColor = Cesium.Color.WHITE;
		scene.screenSpaceCameraController.enableLook = false;
		scene.screenSpaceCameraController.enableCollisionDetection = true;
		
		scene.screenSpaceCameraController.inertiaZoom = 0;
		scene.screenSpaceCameraController.inertiaTranslate = 0;
		scene.screenSpaceCameraController.inertiaSpin = 0;
		
		scene.globe.maximumScreenSpaceError = 1;
		scene.globe.tileCacheSize = 250;
		
		scene.pickTranslucentDepth = true;
		scene.useDepthPicking = true;
		
	
	
		$( window ).resize(function() {
		  //doTheJob();
		});
		//doTheJob();
			
		scene.camera.moveStart.addEventListener(function() { 
			mapIsMoving = true;
		});
		
		scene.camera.moveEnd.addEventListener(function() { 
			mapIsMoving = false;
			//doTheJob();
		});		
		
			
		/*********************************   **********************************************/	
	
		var tileset = new Cesium.Cesium3DTileset({
			url: 'pointdata/tileset.json',
			maximumScreenSpaceError : 1.8,
			eyeDomeLightingStrength : 0.5,
			eyeDomeLightingRadius : 1.0,
			skipLevelOfDetail : true,
			
			/*
			maximumScreenSpaceError: 128,
			
			baseScreenSpaceError : 1024,
			skipScreenSpaceErrorFactor : 16,
			skipLevels : 1,
			immediatelyLoadDesiredLevelOfDetail : false,
			loadSiblings : false,
			cullWithChildrenBounds : true	  
			*/
		});
	    //tileset.pointCloudShading.maximumAttenuation = undefined; // Will be based on maximumScreenSpaceError instead
	    //tileset.pointCloudShading.baseResolution = undefined;
	    tileset.pointCloudShading.geometricErrorScale = 0.5;
	    tileset.pointCloudShading.attenuation = true;
	    tileset.pointCloudShading.eyeDomeLighting = true;
	
		
		/*
		tileset.loadProgress.addEventListener(function(numberOfPendingRequests, numberOfTilesProcessing) {
			if ((numberOfPendingRequests === 0) && (numberOfTilesProcessing === 0)) {
				console.log('Stopped loading');
				return;
			}
			//console.log('Loading: requests: ' + numberOfPendingRequests + ', processing: ' + numberOfTilesProcessing);
		});	
		tileset.initialTilesLoaded.addEventListener(function() {
			console.log('Initial tiles are loaded');
		});
		*/
	
	/*
		tileset.style = new Cesium.Cesium3DTileStyle({
		  color: {
			conditions: [
			  ["${temperature} > 50", "color('red')"],       
			]
		  }
		});
	*/
		
		tileset.readyPromise.then(function(tileset) {
			tileset._geometricError = 1000000000.0;
			window.setTimeout(function() {
				tileset._root._content._pointSize = 100.0;
			}, 500);
		});	
		
		var pp = viewer.scene.primitives.add(tileset);	
		
		viewer.zoomTo(tileset);
	
	
	
	
	
	
	/*
		var promise = Cesium.GeoJsonDataSource.load(
			"/json/SBRJ-predios.geojson" , { clampToGround: true }
		);
	  
		promise.then(function (dataSource) {
			viewer.dataSources.add(dataSource);
			var entities = dataSource.entities.values;
			for (var i = 0; i < entities.length; i++) {
				var entity = entities[i];
				if( entity.properties.height != null && entity.properties.height !== 'null' ) {
					try {
						entity.polygon.material = Cesium.Color.RED.withAlpha(1);
						//entity.polygon.outline = false;
						//entity.polygon.extrudedHeight = entity.properties.height.getValue();
					} catch( e ) {
						coonsole.log( e );
					}
					
				}
			}
			viewer.zoomTo(dataSource);
		});	
	*/	
		
			
	</script>	

</body>

</html>

