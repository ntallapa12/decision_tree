package com.forum.ml.dtree.data;

/**
 * Object representation of feature
 * 
 * @author ntallapa
 *
 */
public class Feature {
	private String fName;
	private int idx;
	private String[] fOptions;
	
	/**
	 * @return the idx
	 */
	public int getIdx() {
		return idx;
	}
	/**
	 * @param idx the idx to set
	 */
	public void setIdx(int idx) {
		this.idx = idx;
	}

	/**
	 * @return the fName
	 */
	public String getfName() {
		return fName;
	}
	/**
	 * @param fName the fName to set
	 */
	public void setfName(String fName) {
		this.fName = fName;
	}
	/**
	 * @return the fOptions
	 */
	public String[] getfOptions() {
		return fOptions;
	}
	/**
	 * @param fOptions the fOptions to set
	 */
	public void setfOptions(String[] fOptions) {
		this.fOptions = fOptions;
	}
	
	@Override
	public String toString() {
		return this.fName;
	}
}
