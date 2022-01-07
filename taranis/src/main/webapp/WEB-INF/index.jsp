<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no">
    <title>Cesium Branco</title>
    <link href="Cesium/Widgets/widgets.css" rel="stylesheet">
    <script src="Cesium/Cesium.js"></script>
    <script src="jquery.js"></script>

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
<body>
<div style="position:relative" id="cesiumContainer"></div>
<script type="text/javascript">

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
	var layers = scene.imageryLayers;

	scene.globe.depthTestAgainstTerrain = true;
	scene.highDynamicRange = false;
	scene.globe.enableLighting = false;
	scene.globe.baseColor = Cesium.Color.WHITE;
	scene.screenSpaceCameraController.enableLook = false;
	scene.screenSpaceCameraController.enableCollisionDetection = true;
	scene.screenSpaceCameraController.inertiaZoom = 0;
	scene.screenSpaceCameraController.inertiaTranslate = 0;
	scene.screenSpaceCameraController.inertiaSpin = 0;
	//scene.globe.maximumScreenSpaceError = 1;

		
	/*********************************   **********************************************/

	// 34 valores
	let radarHeights = [200,400,600,800,1000,1200,1400,1600,1800,2000,2500,3000,3500,4000,4500,5000,5500,6000,6500,7000,7500,8000,8500,9000,9500,10000,
		11000,12000,13000,14000,15000,16000,17000,18000,19000];
	let index = 0;
	let theStyle = {
		pointSize : 2.0,
	}

	// ========================================================
	var tileset1 = new Cesium.Cesium3DTileset({
		url: 'pointdata/2016-04-18-22-55-27/tileset.json',
		skipLevelOfDetail : false,
		style : new Cesium.Cesium3DTileStyle( theStyle )
	});
	var tileset2 = new Cesium.Cesium3DTileset({
		url: 'pointdata/2016-04-18-23-07-24/tileset.json',
		skipLevelOfDetail : false,
		style : new Cesium.Cesium3DTileStyle( theStyle )
	});
	// ========================================================
	tileset1.readyPromise.then(function(tileset) {
		console.log("T1 Pronto");
		tileset1._geometricError = 10000;
	});
	tileset2.readyPromise.then(function(tileset) {
		console.log("T2 Pronto");
		tileset2._geometricError = 10000;
	});
	// ========================================================
	viewer.scene.primitives.add(tileset1);	
	viewer.scene.primitives.add(tileset2);	
	// ========================================================


	window.setTimeout(function() {
		tileset1.show = false;
		
		/*
		window.setInterval(function() {
			tileset2.show = !tileset2.show;
			tileset1.show = !tileset1.show;
		}, 2000);
		*/
		
		let x = 0;
		window.setInterval(function() {
			if( x < radarHeights.length ) x++;
			setLevel( 0, x );
			if( x >= radarHeights.length ) x = 0;
		}, 20);
		
		
	},10000 );
	
	viewer.zoomTo(tileset1, new Cesium.HeadingPitchRange(0.0, -90.0, 350000.0));
	setLevel( 0, 0 );

	function setLevel( levelStartIndex, levelEndIndex ){
		if ( levelStartIndex > levelEndIndex ) levelStartIndex = levelEndIndex -1;
		if ( levelStartIndex < 0 ) levelStartIndex = 0;
		
		if ( levelEndIndex < levelStartIndex ) levelEndIndex = levelStartIndex + 1;
		if ( levelEndIndex >= radarHeights.length ) levelEndIndex = radarHeights.length -1;  
	
	
		let altiMin = radarHeights[levelStartIndex] -1;
		let altiMax = radarHeights[levelEndIndex] +1;
		let showCondition = "${altitude} > " + altiMin + " && ${altitude} < " + altiMax;
		theStyle.show = showCondition;
		tileset1.style = new Cesium.Cesium3DTileStyle( theStyle );
		tileset2.style = new Cesium.Cesium3DTileStyle( theStyle );
	}
	


		
</script>
</body>
</html>