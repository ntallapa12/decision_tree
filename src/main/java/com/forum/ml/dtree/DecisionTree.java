/**
 * 
 */
package com.forum.ml.dtree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.forum.ml.dtree.data.Feature;
import com.forum.ml.dtree.data.Features;
import com.forum.ml.dtree.data.Instance;
import com.forum.ml.dtree.data.Instances;

/**
 * @author ntallapa
 *
 */
public class DecisionTree {
	private static final Logger log = LoggerFactory
			.getLogger(DecisionTree.class);
	// Enum to describe the kind of node in the decision tree
	public enum DecisionTreeNodeType {
		FEATURE_NODE, OUTPUT_LEAF_NODE
	}

	public DTNode buildDecisionTree(Features features, Instances trainExamples, 
			String[] outputLabels) {

		if(trainExamples.getExamples().size() ==0 || features.getFeatures().size() ==0) {
			return null;
		}

		Map<String, Integer> outputLabelCt = getOutputLabelDistribution(features.getFeatures(), trainExamples.getExamples(), outputLabels);
		int posCt = outputLabelCt.get(outputLabels[0]);
		int negCt = outputLabelCt.get(outputLabels[1]);
		String debug = "{ " + posCt + "(+)" + " / "
				+ negCt + "(-)" + " }";
		
		String outputLabel = outputLabels[1];
		if(posCt > negCt) {
			outputLabel = outputLabels[0];
		}

		if(posCt == 0) {
			DTNode node = new DTNode();
			node.setNodeType(DecisionTreeNodeType.OUTPUT_LEAF_NODE);
			node.setLog(outputLabel+"#ONLYPOS#"+debug);
			node.setOutputLabel(outputLabel);
			return node;
		}

		if(negCt == 0) {
			DTNode node = new DTNode();
			node.setNodeType(DecisionTreeNodeType.OUTPUT_LEAF_NODE);
			node.setLog(outputLabel+"#ONLYNEG#"+debug);
			node.setOutputLabel(outputLabel);
			return node;
		}
		
		Feature bestFeature = getBestFeature(features.getFeatures(), trainExamples.getExamples(), outputLabels, outputLabelCt);
		Features remainingFtrs = getRemainingFtrs(features.getFeatures(), bestFeature);
		//System.out.println("remainingFtrs size "+remainingFtrs.getFeatures().size());

		// build a node based on the best feature
		DTNode root = new DTNode();
		root.setNodeType(DecisionTreeNodeType.FEATURE_NODE);
		root.setLog("#Normal#"+debug);
		root.setFeature(bestFeature);

		// traverse left side and construct
		//System.out.println("traversing left");
		Instances filteredExOnBestFtr = getFilteredExamplesOnBestFtr(trainExamples.getExamples(), bestFeature, bestFeature.getfOptions()[0]);
		root.setLeftNode(buildDecisionTree(remainingFtrs, filteredExOnBestFtr, outputLabels));

		// traverse right side and construct
		//System.out.println("traversing right");
		filteredExOnBestFtr = getFilteredExamplesOnBestFtr(trainExamples.getExamples(), bestFeature, bestFeature.getfOptions()[1]);
		root.setRightNode(buildDecisionTree(remainingFtrs, filteredExOnBestFtr, outputLabels));

		//System.out.println("end");
		// return root of the tree
		return root;
	}

	private Instances getFilteredExamplesOnBestFtr(List<Instance> examples,
			Feature bestFeature, String ftrValue) {
		Instances ins = new Instances();
		List<Instance> newInstances = new ArrayList<Instance>();

		for(Instance i: examples) {
			String[] ftrValues = i.getUserFeatures();

			if(ftrValues[bestFeature.getIdx()].equalsIgnoreCase(ftrValue)) {
				newInstances.add(i);
			}

		}
		ins.setExamples(newInstances);
		return ins;
	}

	private Features getRemainingFtrs(List<Feature> features,
			Feature bestFeature) {
		List<Feature> remFtrs = new ArrayList<Feature>();

		for(Feature f: features) {
			if(!f.getfName().equalsIgnoreCase(bestFeature.getfName())) {
				remFtrs.add(f);
			}
		}
		Features fs = new Features();
		fs.setFeatures(remFtrs);
		return fs;
	}

	private Feature getBestFeature(List<Feature> features,
			List<Instance> examples, String[] outputLabels,
			Map<String, Integer> outputLabelCt) {
		// entropy of output label
		double outputLabelEntropy = DTUtility.getEntropy(outputLabelCt.get(outputLabels[0]), outputLabelCt.get(outputLabels[1]));
		log.debug("outputLabelEntropy is "+outputLabelEntropy);
		int totOutputCt = outputLabelCt.get(outputLabels[0])+outputLabelCt.get(outputLabels[1]);

		double maxInfoGain = 0;
		Feature bestFeature = null;

		//for(int ftrIdx=0; ftrIdx<features.size(); ftrIdx++) {
		for(Feature currentFeature: features) {
			Map<String, Integer> featureOption1ValueDist = getFeatureValueDistribution(currentFeature,  
					currentFeature.getfOptions()[0], currentFeature.getIdx(), examples, outputLabels);

			double featureOption1Entropy = DTUtility.getOutputEntropyOverFeature(totOutputCt, featureOption1ValueDist.get(outputLabels[0]),
					featureOption1ValueDist.get(outputLabels[1]));
			log.debug("featureOption1Entropy is "+featureOption1Entropy);

			Map<String, Integer> featureOption2ValueDist = getFeatureValueDistribution(currentFeature,  
					currentFeature.getfOptions()[1], currentFeature.getIdx(), examples, outputLabels);
			double featureOption2Entropy = DTUtility.getOutputEntropyOverFeature(totOutputCt, featureOption2ValueDist.get(outputLabels[0]),
					featureOption2ValueDist.get(outputLabels[1]));
			log.debug("featureOption2Entropy is "+featureOption2Entropy);

			double featureToOutputEntropy = featureOption1Entropy + featureOption2Entropy;
			log.debug("currentFeature is "+currentFeature+", featureToOutputEntropy is "+featureToOutputEntropy);

			double infoGain = outputLabelEntropy-featureToOutputEntropy;
			log.debug("infoGain is "+infoGain);

			if(infoGain > maxInfoGain) {
				maxInfoGain = infoGain;
				bestFeature = currentFeature;
			}
		}
		return bestFeature;
	}

	private Map<String, Integer> getFeatureValueDistribution(Feature feature, String featureOptionValue, 
			int ftrIdx, List<Instance> examples, String[] outputLabels) {
		Map<String, Integer> outputLabelCt = new HashMap<String, Integer>();
		outputLabelCt.put(outputLabels[0], 0);
		outputLabelCt.put(outputLabels[1], 0);

		for(Instance example: examples) {
			String[] userFeaturesArr = example.getUserFeatures();
			String fatureValue = userFeaturesArr[ftrIdx];
			if(featureOptionValue.equalsIgnoreCase(fatureValue)) {
				if(example.getOutputIndicator().equalsIgnoreCase(outputLabels[0])) {
					int ct = outputLabelCt.get(outputLabels[0])+1;
					outputLabelCt.put(outputLabels[0], ct);
				} else {
					int ct = outputLabelCt.get(outputLabels[1])+1;
					outputLabelCt.put(outputLabels[1], ct);
				}
			}
		}
		log.debug(feature.getfName()+" - "+outputLabels[0]+"="+outputLabelCt.get(outputLabels[0])+"; "+outputLabels[1]+"="+outputLabelCt.get(outputLabels[1]));
		return outputLabelCt;
	}

	public Instances testDecisionTree(DTNode root, Instances testData) {
		List<Instance> testExamples = testData.getExamples();
		List<Instance> failedExamples = new ArrayList<Instance>();
		for(Instance example: testExamples) {
			String currentOutputLabel = example.getOutputIndicator();
			String dtOutputLabel = validateExample(example, root);
			
			if(!dtOutputLabel.equalsIgnoreCase(currentOutputLabel)) {
				log.info("dtOutputLabel is "+dtOutputLabel+", currentOutputLabel is "+currentOutputLabel);
				log.info("Failed example is "+example);
				failedExamples.add(example);
			}
		}
		log.info("Number of failed examples: "+failedExamples.size()+", out of total test examples: "+testExamples.size());
		int successfulExamples = testExamples.size()-failedExamples.size();
		double accuracy = (1.0*successfulExamples)/testExamples.size();
		log.info("accuracy achieved is "+accuracy*100+"%");
		Instances is = new Instances();
		is.setExamples(failedExamples);
		// return test examples which we failed to evaluate correct output label
		return is;
	}

	public String validateExample(Instance example, DTNode root) {
		String dtOutputLabel = "";
		while(root != null) {
			String[] exFtrValues = example.getUserFeatures();
			Feature dtFtr = root.getFeature();
			
			if(dtFtr != null) {
				String[] dtFtrValues = dtFtr.getfOptions();
				String exFtrValue = exFtrValues[dtFtr.getIdx()];
				
				if(exFtrValue.equals(dtFtrValues[0])) {
					root = root.getLeftNode();
				} else {
					root = root.getRightNode();
				}
				
				if(root != null) {
					dtOutputLabel = root.getOutputLabel();
				}
			} else {
				root = root.getLeftNode();
			}
		}
		return dtOutputLabel;
	}

	public void printDecisionTree(DTNode root, int level) {
		System.out.println(root);
	}

	public Map<String, Integer> getOutputLabelDistribution(List<Feature> features, List<Instance> instances, String[] outputLabels) {

		Map<String, Integer> outputLabelCt = new HashMap<String, Integer>();
		outputLabelCt.put(outputLabels[0], 0);
		outputLabelCt.put(outputLabels[1], 0);

		for(Instance inst: instances) {
			if(inst.getOutputIndicator().equalsIgnoreCase(outputLabels[0])) {
				int ct = outputLabelCt.get(outputLabels[0])+1;
				outputLabelCt.put(outputLabels[0], ct);
			} else {
				int ct = outputLabelCt.get(outputLabels[1])+1;
				outputLabelCt.put(outputLabels[1], ct);
			}
		}

		log.debug(outputLabels[0]+"="+outputLabelCt.get(outputLabels[0]));
		log.debug(outputLabels[1]+"="+outputLabelCt.get(outputLabels[1]));

		return outputLabelCt;
	}
}
