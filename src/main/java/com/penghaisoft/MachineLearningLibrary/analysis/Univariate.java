package com.penghaisoft.MachineLearningLibrary.analysis;

import java.util.HashMap;
import java.util.Map;

import com.penghaisoft.MachineLearningLibrary.util.DataConverter;
import com.penghaisoft.MachineLearningLibrary.util.DataSelect;

/**
 * Univariate算法类
 * @author Jackie Xu
 * @version 1.0
 */

public class Univariate {
	/*
	 * 导入C++的DLL文件
	 */
	static {
		System.load("C:/DLL_64/UnivariateDLL.dll");
	}
	/**
	 * 输入数据调用C++代码进行计算
	 * @param datafile 输入数据
	 * @param size 样本数据个数
	 * @param method 要调用的具体方法分类
	 * 
	*/
	
	private native static double calculate(double filedata[], int size, int method);
	
	public Map<String, Object> calculate(String[][] data, Map<String, Object>parameter) {
		//选择的列
		int []selectCols = (int [])parameter.get("trainCols");
		int method = (Integer)parameter.get("calculateMethod");
		//选择的列名
		String []selectColName = DataSelect.getSelectedColumnName(data, selectCols);
		//选择的数据
		String [][]selectData = DataSelect.ColumnSelect(data, selectCols);
		//转换后用于计算的数据
		//double [][]calculateData = DataConverter.String2DtoDouble2D(selectData);
		
		Map<String, Double>calculateResults = new HashMap<String, Double>();
		//System.out.println(calculateData.length);
		
		for(int i=0,j=0; i<selectCols.length; i++) {
			if(selectCols[i] == 1) {
				String []cData = new String[selectData.length];
				for(int k=0; k<selectData.length; k++) {
					cData[k] = selectData[k][j];
				}
				double[]calculateData = DataConverter.String1DtoDouble1D(cData);
				double tempResult = calculate(calculateData, calculateData.length, method);
				System.out.println("****tempResult = "+tempResult);
				System.out.println("selectColName[j] = "+selectColName[j]);
				calculateResults.put(selectColName[j], tempResult);
				System.out.println("****"+calculateResults.get(selectColName[j]));
				j++;
			}
		}
		
		Map<String, Object>result = new HashMap<String, Object>();
		result.put("calculateResults", calculateResults);
		result.put("calculateMethod", method);
		Map<String, Object>methodMap = new HashMap<String, Object>();
		methodMap.put("1", "平均数");
		methodMap.put("2", "方差");
		methodMap.put("3", "标准差");
		methodMap.put("4", "峰值");
		methodMap.put("5", "偏态");
		methodMap.put("6", "中位数");
		result.put("methodMap", methodMap);
		Map<String,Double>cResult = (Map<String, Double>) result.get("calculateResults");
		System.out.println(cResult.size());
		return result;
	}
	
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		String [][]data = {{"a","b","c","d"},{"1","1","1","1"},{"2","2","2","2"},{"3","3","3","3"},{"4","4","4","4"},{"5","5","5","5"},{"6","6","6","6"},{"7","7","7","7"},{"8","8","8","8"},{"9","9","9","9"},{"10","10","10","10"}};
		Map<String, Object>parameter = new HashMap<String, Object>();
		int []selectCols = {1,0,1,0};
		int calculate = 1;
		parameter.put("trainCols", selectCols);
		parameter.put("calculateMethod", calculate);

		//double[] Data= {1,2,3,4,5,6,7,8,9,10};
		Map<String,Object>result = new HashMap<String, Object>();
		Univariate un = new Univariate();
		result = un.calculate(data, parameter);
		Map<String,Double>cResult = (Map<String, Double>) result.get("calculateResults");
		 
		System.out.println(cResult.get("a")); 
		
		for (String key : cResult.keySet()) { 
			System.out.println("Key = " + key); 
		} 
		//遍历map中的值 
		for (Double val : cResult.values()) { 
		  System.out.println("Value = " + val); 
		}
		
		System.out.println("************");
		System.out.println("Skewness="+result);
	}

}
