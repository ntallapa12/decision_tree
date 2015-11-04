package com.forum.ml.dtree.data;

import java.util.Arrays;

public class Instance {
	private String userID;
	private String outputIndicator;
	private String[] userFeatures;
	/**
	 * @return the userID
	 */
	public String getUserID() {
		return userID;
	}
	/**
	 * @param userID the userID to set
	 */
	public void setUserID(String userID) {
		this.userID = userID;
	}
	/**
	 * @return the outputIndicator
	 */
	public String getOutputIndicator() {
		return outputIndicator;
	}
	/**
	 * @param outputIndicator the outputIndicator to set
	 */
	public void setOutputIndicator(String outputIndicator) {
		this.outputIndicator = outputIndicator;
	}
	/**
	 * @return the userFeatures
	 */
	public String[] getUserFeatures() {
		return userFeatures;
	}
	/**
	 * @param userFeatures the userFeatures to set
	 */
	public void setUserFeatures(String[] userFeatures) {
		this.userFeatures = userFeatures;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return userID+";"+outputIndicator+";"+Arrays.toString(userFeatures);
	}
}
