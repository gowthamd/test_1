package com.trippal.places;

import java.util.List;
import java.util.Map;

public class DayPlannerIter2 {
	
	int[] weightageMatrix;
	
	public DayPlannerIter2(){
		weightageMatrix = new int[5];
		weightageMatrix[0]=200;
		weightageMatrix[1]=160;
		weightageMatrix[2]=100;
		weightageMatrix[3]=30;
		weightageMatrix[4]=5;		
	}
	
	public Integer identifySuitablePathBasedOnWeightage(Map<Integer, List<Input>> totalDstToPathsMap){
		int maxWeightage =0;
		int identifiedPathTotalDst  =0;
		for(Map.Entry<Integer, List<Input>> entry :totalDstToPathsMap.entrySet()) {
			List<Input> value = entry.getValue();
			int totalWeightage =0;
			for(Input ipt :value) {
				int rank = ipt.getToPlace().getRank();
				totalWeightage += weightageMatrix[rank-1];
			}
			if(totalWeightage > maxWeightage) {
				maxWeightage =totalWeightage;
				identifiedPathTotalDst = entry.getKey();
			}
		}
		return identifiedPathTotalDst;
	}

}
