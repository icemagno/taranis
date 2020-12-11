package br.mil.defesa.sisgeodef.misc;

import java.util.NavigableMap;
import java.util.TreeMap;

/*
		-20 5   #404040
		5   10  #483d8b
		10	20	#005a00
		20	30	#007000
		30	35	#087fdb
		35	36	#1c47e8
		36	39	#6e0dc6
		39	42	#c80f86
		42	45	#c06487
		45	48	#d2883b
		48	51	#fac431
		51	54	#fefa03
		54	57	#fe9a58
		57	60	#fe5f05
		60	65	#fd341c
		65	70	#808080 
		70	80	#d3d3d3	
*/	


public class DBZColorRamp {
	private NavigableMap<Double, String> colorRamp;
	
	public DBZColorRamp() {
		colorRamp = new TreeMap<Double, String>();
		colorRamp.put(-20.0,"#404040");
		colorRamp.put(5.0,  "#483d8b");
		colorRamp.put(10.0, "#005a00");
		colorRamp.put(20.0, "#007000");
		colorRamp.put(30.0, "#087fdb");
		colorRamp.put(35.0, "#1c47e8");
		colorRamp.put(36.0, "#6e0dc6");
		colorRamp.put(39.0, "#c80f86");
		colorRamp.put(42.0, "#c06487");
		colorRamp.put(45.0, "#d2883b");
		colorRamp.put(48.0, "#fac431");
		colorRamp.put(51.0, "#fefa03");
		colorRamp.put(54.0, "#fe9a58");
		colorRamp.put(57.0, "#fe5f05");
		colorRamp.put(60.0, "#fd341c");
		colorRamp.put(65.0, "#808080");
		colorRamp.put(70.0, "#d3d3d3");
	}
	
	public String getColor( double value ) {
		if( value <= -20.0 ) return "#404040";
		if( value >= 70 ) return "#d3d3d3";
	    double above = colorRamp.ceilingKey(value);
	    double below = colorRamp.floorKey(value);
	    double key = (value - below > above - value ? above : below);
	    String result = colorRamp.get(key);
	    return result;
	}
	
	
}
