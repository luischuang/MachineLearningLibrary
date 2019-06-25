package com.penghaisoft.MachineLearningLibrary.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 数据分类映射工具类
 * @author gl
 *
 */
public class ClassMap {
	public static Map<String, String> StringLtoNumberL(List<String> label){
		Map<String, String> mapResult;
		mapResult = new HashMap<String, String>(); 
		int number = mapResult.size();
		for(int i=0; i<label.size(); i++) {
			if(mapResult.containsKey(label.get(i))) {
				continue;
			}
			else {
				number = mapResult.size();
				mapResult.put(label.get(i), Integer.toString(number+1));
			}
		}
		return mapResult;
	}
}
