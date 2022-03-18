package br.mil.defesa.sisgeodef.hgt;

public class Tile {
	private String name;
	private String bbox;
	private double l;
	private double b;
	private double t;
	private double r;
	public final static int HGT_ROW_LENGTH = 3601;
	public final static int HGT_VOID = -32768;	
	private final double PIXEL_SIZE = 0.000277700638711469; // 1 degree / 3601 pixels length
	
	public Tile( String name ) {
		this.name = name.replace(".hgt", "");
		this.bbox = getTileBBox( name ); 
	}
	
	public String getName() {
		return name;
	}
	
	public String getImageName() {
		return this.name + ".png";
	}
	
	public String getTileName() {
		return this.name + ".hgt";
	}

	public String getBbox() {
		return bbox;
	}
	
	public double getL() {
		return l;
	}

	public double getB() {
		return b;
	}

	public double getT() {
		return t;
	}

	public double getR() {
		return r;
	}
	
	public String getPixelCoordinates( double row, double col ) {
		double lon = this.l + ( col * PIXEL_SIZE );
		double lat = this.t + ( row * PIXEL_SIZE );
		return lat + "," + lon;
	}
	
	private String getTileBBox( String tileName ) {
    	String signalLat = "";
    	String signalLon = "";
    	String tmp = tileName.replace("S", "").replace("N", "");
    	String latS = tmp.substring(0,2);
    	String lonS = tmp.substring(4,6);
    	int lat = Integer.valueOf( latS );
    	int lon = Integer.valueOf( lonS );

    	
    	int latF = 0;
    	int lonF = 0;
    	if ( tileName.contains("S") ) {
    		signalLat = "-";
    		latF = lat - 1;
    	} 
    	
    	if ( tileName.contains("N") ) {
    		latF = lat + 1;
    	}
    	
    	if ( tileName.contains("W") ) {
    		signalLon = "-";
    		lonF = lon - 1;
    	} 
    	
    	if ( tileName.contains("E") ) {
    		lonF = lon + 1;
    	}
    	
    	String lonFS = String.valueOf( lonF );
    	String latFS = String.valueOf( latF );
    	
    	this.t = Double.valueOf( signalLat + latFS );
    	this.r = Double.valueOf( signalLon + lonFS );
    	this.b = Double.valueOf( signalLat + latS );
    	this.l = Double.valueOf( signalLon + lonS );
    	
    	return signalLon + lonS + "," + signalLat + latS + "," + signalLon + lonFS + "," + signalLat + latFS ;
    }	
		
	
}
