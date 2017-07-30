package com.trippal.places;

import java.util.ArrayList;
import java.util.List;

public class CalculatePossiblePathCombos {
	
	
	/**
	 * given a n*m matrix,
	 * 
	 * it will return all possible combo's
	 * 
	 * 
	 * for eaxmple of 2*2 matix
	 * [{1,2},{3,4}]
	 * combo's will be {1,3},{1,4},{2,3},{2,4}
	 * 
	 * @param elements
	 * @return
	 */
	  public static List<Input[]> generate(Input[][] elements) {
		  List<Input[]> combos = new ArrayList<Input[]>();
	      int rows = elements.length;
	      int[] elementsIndex = new int[rows];
	      int[] elementsTotals = new int[rows];
	      java.util.Arrays.fill(elementsTotals, elements[0].length);
	      int curIdx = 0;
	      Input[] combo = new Input[rows];
	      while(true){
	          while(curIdx >= 0){
	              if(curIdx == rows) {
	                  addCombo(combo,combos);
	                  curIdx--;
	              }
	              if(elementsIndex[curIdx] == elementsTotals[curIdx]){
	                  elementsIndex[curIdx] = 0;
	                  curIdx--;
	              } else break;
	          }
	          if(curIdx < 0) {break;
	          }
	          combo[curIdx] = elements[curIdx][elementsIndex[curIdx]++];
	          curIdx++;
	      }
	      return combos;
	  }
	  private static void addCombo(Input[] combo,List<Input[]> combos){
		  Input[] comboList = new Input[combo.length];
	      System.arraycopy(combo, 0, comboList, 0, combo.length);
	      combos.add(comboList);
	  }
}
