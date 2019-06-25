package com.penghaisoft.MachineLearningLibrary.util;

import java.util.HashMap;
import java.util.Map;
/**
 * 数据标签映射工具类
 * @author lc199
 *
 */
public class LabelMap {

	public static Map<String, Integer> getIntLabelMap(String[] data){
		Map<String, Integer> labelMap=new HashMap<String, Integer>();
		labelMap.put(data[0], 0);
		int number=0;
		for (String str : data) {
			boolean find=false;
			for(String key : labelMap.keySet()){
				if(key.equals(str)){
					find=true;
					break;
				}
			}
			if(!find){
				number++;
				labelMap.put(str, number);
			}
		}

		
		return labelMap;
	}
	public static Map<String, Double> getDoubleLabelMap(String[] data){
		Map<String, Double> labelMap=new HashMap<String, Double>();
		labelMap.put(data[0], 0d);
		double number=0d;
		for (String str : data) {
			boolean find=false;
			for(String key : labelMap.keySet()){
				if(key.equals(str)){
					find=true;
					break;
				}
			}
			if(!find){
				number+=1;
				labelMap.put(str, number);
			}
		}

		
		return labelMap;
	}
	public static int[] getIntLabelLists(String[] data,Map<String, Integer> labelMap){
		int labelLists[]=new int[data.length];
		int i=0;
		for (String str : data) {
			for(String key : labelMap.keySet()){
				if(key.equals(str)){
					labelLists[i]=labelMap.get(key);
					break;
				}
			}
			i++;
		}
		return labelLists;
	}
	public static double[] getDoubleLabelLists(String[] data,Map<String, Double> labelMap){
		double labelLists[]=new double[data.length];
		int i=0;
		for (String str : data) {
			for(String key : labelMap.keySet()){
				if(key.equals(str)){
					labelLists[i]=labelMap.get(key);
					break;
				}
			}
			i++;
		}
		return labelLists;
	}
	
	public static String getLabel(int data,Map<String, Integer> labelMap){
		String label="";
		for(String key : labelMap.keySet()){
			if(labelMap.get(key)==data){
				label=key;
				break;
			}
		}
		return label;
	}
	public static String[] getLabel(int[] data,Map<String, Integer> labelMap){
		String[] label=new String[data.length];
		for(int i=0;i<data.length;i++){
			for(String key : labelMap.keySet()){
				if(labelMap.get(key)==data[i]){
					label[i]=key;
					break;
				}
			}
		}

		return label;
	}
	
	public static String getLabel(double data,Map<String, Double> labelMap){
		String label="";
		for(String key : labelMap.keySet()){
			if(labelMap.get(key)==data){
				label=key;
				break;
			}
		}
		return label;
	}
	public static String[] getLabel(double[] data,Map<String, Double> labelMap){
		String[] label=new String[data.length];
		for(int i=0;i<data.length;i++){
			for(String key : labelMap.keySet()){
				if(labelMap.get(key)==data[i]){
					label[i]=key;
					break;
				}
			}
		}

		return label;
	}
	public static void main(String[] args) {
		String string[]={"e","b","c","a","d","c"};
		Map<String, Integer> test=new HashMap<String, Integer>();
		test=LabelMap.getIntLabelMap(string);

		for(String key : test.keySet()){
			System.out.println(key+":"+test.get(key));
		}
	}
}
