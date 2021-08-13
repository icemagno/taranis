

var baseOsmProvider = new Cesium.OpenStreetMapImageryProvider({
    url : 'https://a.tile.openstreetmap.org/'
});		

var viewer = new Cesium.Viewer('cesiumContainer',{
	timeline: false,
	animation: false,
	baseLayerPicker: false,
	skyAtmosphere: false,
	fullscreenButton : false,
	geocoder : false,
	homeButton : false,
	infoBox : false,
	skyBox : false,
	sceneModePicker : false,
	selectionIndicator : false,
	navigationHelpButton : false,
	requestRenderMode : true,
    imageryProvider: baseOsmProvider,
    
});

camera = viewer.camera;
scene = viewer.scene;
scene.highDynamicRange = false;
scene.globe.enableLighting = false;
scene.globe.baseColor = Cesium.Color.WHITE;
scene.screenSpaceCameraController.enableLook = false;
scene.screenSpaceCameraController.enableCollisionDetection = true;

scene.screenSpaceCameraController.inertiaZoom = 0;
scene.screenSpaceCameraController.inertiaTranslate = 0;
scene.screenSpaceCameraController.inertiaSpin = 0;

//scene.globe.maximumScreenSpaceError = 1;
scene.globe.depthTestAgainstTerrain = false;
//scene.globe.tileCacheSize = 250;
scene.pickTranslucentDepth = true;
scene.useDepthPicking = true;

var width = viewer.scene.drawingBufferWidth;
var height = viewer.scene.drawingBufferHeight;
console.log('Resolução ' + width + ' x ' + height );	

$(".cesium-viewer-bottom").hide();
$(".cesium-viewer-navigationContainer").hide();
$(".navigation-controls").hide();


var url = "/radar?l={l}&r={r}&t={t}&b={b}";
var radarProvider = new MagnoMetocRadarProvider({
  debugTiles : true,
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










