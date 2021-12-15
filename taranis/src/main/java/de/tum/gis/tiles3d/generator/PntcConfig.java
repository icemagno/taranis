/*
 * Cesium Point Cloud Generator
 * 
 * Copyright 2017 - 2018
 * Chair of Geoinformatics
 * Technical University of Munich, Germany
 * https://www.gis.bgu.tum.de/
 * 
 * The Cesium Point Cloud Generator is developed at Chair of Geoinformatics,
 * Technical University of Munich, Germany.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.tum.gis.tiles3d.generator;

import de.tum.gis.tiles3d.model.Refine;

public class PntcConfig {
	private String inputPath;
	private String outputFolderPath;
	private double tileSize;
	private double zOffset;
	private String srid;
	private String separatorCharacter;
	private double zScaleFactor;
	private int colorBitSize;	
	private int maxNumOfPointsPerTile;
	private Refine refinamentModel;
	private String targetSrid;
	private boolean mustReproject;
	private String jobId;
	
	private String connectionString;
	private String userName;
	private String userPassword;
	
	public void setMustReproject(boolean mustReproject) {
		this.mustReproject = mustReproject;
	}

	public int getColorBitSize() {
		return colorBitSize;
	}

	public void setColorBitSize(int colorBitSize) {
		this.colorBitSize = colorBitSize;
	}

	public String getInputPath() {
		return inputPath;
	}

	public void setInputPath(String inputPath) {
		this.inputPath = inputPath;
	}

	public String getOutputFolderPath() {
		return outputFolderPath;
	}

	public void setOutputFolderPath(String outputFolderPath) {
		this.outputFolderPath = outputFolderPath;
	}

	public double getTileSize() {
		return tileSize;
	}

	public void setTileSize(double tileSize) {
		this.tileSize = tileSize;
	}

	public double getzOffset() {
		return zOffset;
	}

	public void setzOffset(double zOffset) {
		this.zOffset = zOffset;
	}

	public String getSrid() {
		return srid;
	}

	public void setSrid(String srid) {
		this.srid = srid;
	}

	public String getSeparatorCharacter() {
		return separatorCharacter;
	}

	public void setSeparatorCharacter(String separatorCharacter) {
		this.separatorCharacter = separatorCharacter;
	}

	public double getZScaleFactor() {
		return zScaleFactor;
	}

	public void setZScaleFactor(double zScaleFactor) {
		this.zScaleFactor = zScaleFactor;
	}

	public int getMaxNumOfPointsPerTile() {
		return maxNumOfPointsPerTile;
	}

	public void setMaxNumOfPointsPerTile(int maxNumOfPointsPerTile) {
		this.maxNumOfPointsPerTile = maxNumOfPointsPerTile;
	}

	public Refine getRefinamentModel() {
		return this.refinamentModel;
	}

	public void setRefinamentModel(Refine refinamentModel) {
		this.refinamentModel = refinamentModel;
	}

	public void setTargetSrid(String srid) {
		this.targetSrid = srid;
	}

	public String getTargetSrid() {
		return targetSrid;
	}
	
	public boolean getMustReproject() {
		return this.mustReproject;
	}

	public String getConnectionString() {
		return connectionString;
	}

	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	
	
}
