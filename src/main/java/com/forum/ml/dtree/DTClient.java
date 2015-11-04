/**
 * 
 */
package com.forum.ml.dtree;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.forum.ml.dtree.common.DTConstants;
import com.forum.ml.dtree.data.DataLoader;
import com.forum.ml.dtree.data.Features;
import com.forum.ml.dtree.data.Instances;

/**
 * Entry point to the decision tree project
 * 
 * @author ntallapa
 *
 */
public class DTClient implements DTConstants {
	private static final Logger log = LoggerFactory
			.getLogger(DataLoader.class);
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		String homeDir = System.getProperty("user.dir");
		System.out.println("dir: "+homeDir);
		
		// features
		String featuresStr = homeDir+"/config/features";
		File featuresFile = new File(featuresStr);
		if(!featuresFile.exists()) {
			System.out.println("Features file does not exist, exiting");
			System.exit(-1);
		}
		
		// features
		String trainingSetStr = homeDir+"/config/training_set";
		File trainingSetFile = new File(trainingSetStr);
		if(!trainingSetFile.exists()) {
			System.out.println("Training file does not exist, exiting");
			System.exit(-1);
		}
		
		// features
		String testingSetStr = homeDir+"/config/testing_set";
		File testingSetFile = new File(testingSetStr);
		if(!testingSetFile.exists()) {
			System.out.println("Testing file does not exist, exiting");
			System.exit(-1);
		}
		
		// load data
		log.info("Loading the input data");
		DataLoader dl = new DataLoader();
		Features features = dl.loadFeatures(featuresFile);
		Instances trainExamples = dl.loadTrainData(trainingSetFile, features.getFeatures());
		Instances testExamples = dl.loadTestData(testingSetFile, features.getFeatures());
		String[] outputLabels = dl.loadOutputLabels(featuresFile);
		
		DecisionTree dt = new DecisionTree();
		// construct the tree
		log.info("Constructing the decision tree");
		DTNode root = dt.buildDecisionTree(features, trainExamples, outputLabels);
		
		// print the tree
		dt.printDecisionTree(root, 0);
				
		// test the decision tree
		dt.testDecisionTree(root, testExamples);
		
		
	}

	
}
