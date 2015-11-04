/**
 * 
 */
package com.forum.ml.dtree;

/**
 * @author ntallapa
 *
 */
public class DTUtility {

	public static double getEntropy(Integer positiveCt, Integer negativeCt) {
		// positive prob
		double totCt = positiveCt+negativeCt;
		double prob = (1.0 * positiveCt)/totCt;
		double entropy = -1 * (prob) * log2(prob);
		
		// negative prob
		prob = negativeCt/totCt;
		entropy += (-1 * (prob) * log2(prob));
		return entropy;
	}
	
	public static double getOutputEntropyOverFeature(int totOutputCt, int featurePosCt, int featureNegCt) {
		int totFeatureCt = featurePosCt+featureNegCt;
		double featureOption1Prob = 1.0 * totFeatureCt/totOutputCt;
		
		if(featureNegCt == 0 || featurePosCt == 0) {
			return 0;
		}
		
		double ftrOption1OutputLable1Prob = 1.0*featurePosCt/totFeatureCt;
		double ftrOption1OutputLable2Prob = 1.0*featureNegCt/totFeatureCt;
		double featureOption1Entropy = (-ftrOption1OutputLable1Prob * log2(ftrOption1OutputLable1Prob)) 
				- (ftrOption1OutputLable2Prob * log2(ftrOption1OutputLable2Prob) );
		
		return featureOption1Entropy * featureOption1Prob;
	}
	
	public static double log2(double arg) {
		return Math.log(arg)/Math.log(2);
	}
	
	public static void main(String[] args) {
		System.out.println(getEntropy(20, 10));
	}
}
