package com.forum.ml.dtree;

import com.forum.ml.dtree.DecisionTree.DecisionTreeNodeType;
import com.forum.ml.dtree.data.Feature;

/**
 * Node representation of Decision Tree
 * 
 * @author ntallapa
 *
 */
public class DTNode {
	private DTNode leftNode;
	private DTNode rightNode;
	private Feature feature;
	private String log;
	private String outputLabel;
	private DecisionTreeNodeType nodeType;
	
	/**
	 * @return the leftNode
	 */
	public DTNode getLeftNode() {
		return leftNode;
	}
	/**
	 * @param leftNode the leftNode to set
	 */
	public void setLeftNode(DTNode leftNode) {
		this.leftNode = leftNode;
	}
	/**
	 * @return the rightNode
	 */
	public DTNode getRightNode() {
		return rightNode;
	}
	/**
	 * @param rightNode the rightNode to set
	 */
	public void setRightNode(DTNode rightNode) {
		this.rightNode = rightNode;
	}
	/**
	 * @return the feature
	 */
	public Feature getFeature() {
		return feature;
	}
	/**
	 * @param feature the feature to set
	 */
	public void setFeature(Feature feature) {
		this.feature = feature;
	}
	/**
	 * @return the nodeType
	 */
	public DecisionTreeNodeType getNodeType() {
		return nodeType;
	}
	/**
	 * @param nodeType the nodeType to set
	 */
	public void setNodeType(DecisionTreeNodeType nodeType) {
		this.nodeType = nodeType;
	}
	/**
	 * @return the log
	 */
	public String getLog() {
		return log;
	}
	/**
	 * @param log the log to set
	 */
	public void setLog(String log) {
		this.log = log;
	}
	
	/**
	 * @return the outputLabel
	 */
	public String getOutputLabel() {
		return outputLabel;
	}
	/**
	 * @param outputLabel the outputLabel to set
	 */
	public void setOutputLabel(String outputLabel) {
		this.outputLabel = outputLabel;
	}
	public StringBuilder toString(StringBuilder prefix, boolean isTail, StringBuilder sb) {
	    if(getRightNode() != null) {
	    	getRightNode().toString(new StringBuilder().append(prefix).append(isTail ? "│   " : "    "), false, sb);
	    }
	    
	    String result = "";
	    if(getFeature() != null) {
	    	result = getFeature().toString();
	    }
	    sb.append(prefix).append(isTail ? "└── " : "┌── ").append(result+getLog()).append("\n");
	    if(getLeftNode() != null) {
	    	getLeftNode().toString(new StringBuilder().append(prefix).append(isTail ? "    " : "│   "), true, sb);
	    }
	    return sb;
	}

	@Override
	public String toString() {
	    return this.toString(new StringBuilder(), true, new StringBuilder()).toString();
	}
	
}
