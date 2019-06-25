package com.penghaisoft.MachineLearningLibrary.classify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.penghaisoft.MachineLearningLibrary.util.ClassMap;
import com.penghaisoft.MachineLearningLibrary.util.DataConverter;
import com.penghaisoft.MachineLearningLibrary.util.DataSelect;

public class KNN {
	
	/*
	 * 导入C++的DLL文件
	 */
	static {
		System.load("C:/DLL_64/KNNDLL.dll");
	}
	private native double[][] ClassbyKNN(int k, int row, int col, int type, int trainDataAtriNum, double [][] trainData,
			double [][] testData, int testDataNum);
	@SuppressWarnings("null")
	public Map<String, Object> train(String [][]trData, Map<String, Object>Parameter) {
		
		int [] selectCols = (int []) Parameter.get("trainCols");
		String [][] trainData = DataSelect.ColumnSelect(trData, selectCols);
		int row = trainData.length; 
		int col = trainData[0].length;
		List<String> label=new ArrayList<String>();
		for(int i=0; i<row; i++) {
			label.add(trainData[i][col-1]);
		}
		Map<String, String> labelMap = new HashMap<String, String>();
		labelMap = ClassMap.StringLtoNumberL(label);
		//int type = labelMap.size();  //数据的分类类型种类
		for(int i=0; i<row; i++) {
			trainData[i][col-1] = labelMap.get(trainData[i][col-1]);
		}
		double transTrainData[][] = DataConverter.String2DtoDouble2D(trainData);
		Map<String, Object> trainResult;
		int nearestK = (Integer) Parameter.get("nearestK");
		trainResult = new HashMap<String, Object>();
		trainResult.put("transTrainData", transTrainData);
		trainResult.put("nearestK", nearestK);
	    trainResult.put("labelMap", labelMap);
	    trainResult.put("trainCols", selectCols);
	    
		return trainResult;
	}
	
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> predict(String [][] teData, Map<String, Object>modelAttribute) {
		//predictResult为预测结果的Map映射，为测试数据的分类结果
		//int [] selectCols = (int []) modelAttribute.get("trainCols");
		String [][] testData = teData;
		
		Map<String, Object> predictResult;
		predictResult = new HashMap<String, Object>();
		
		//测试数据转换，将String的测试数据转换为Double类型
		int rowt = testData.length;
		int colt = testData[0].length;
		
		double transTestData[][] = DataConverter.String2DtoDouble2D(testData); //最后一列为标签列
		//属性1-k
		int nearestK = (Integer) modelAttribute.get("nearestK");
		//属性2-trainData
		//String trainData[][] = (String[][])modelAttribute.get("trainData");
		double transTrainData[][] = (double[][])modelAttribute.get("transTrainData"); 
		int rowtr = transTrainData.length;
		int coltr = transTrainData[0].length;
		
		//方法需要的参数
		int testDataNum = testData.length;
		int trainDataAtriNum = coltr-1;          
		Map<String, String> label = new HashMap<String, String>();
		label = (Map<String, String>) modelAttribute.get("labelMap");
		int type = label.size();
		KNN knn = new KNN();
		//System.out.println("******下面为输出的算法调用参数*******");
		
//		System.out.println(nearestK);
//		System.out.println(rowtr);
//		System.out.println(coltr);
//		System.out.println(type);
//		System.out.println(trainDataAtriNum);
//		for(int i=0; i<rowtr; i++) {
//			
//			for(int j=0; j<coltr; j++) {
//				System.out.print(transTrainData[i][j]+" ");
//			}
//			System.out.println();
//		}
//		for(int i=0; i<rowt; i++) {
//			
//			for(int j=0; j<colt; j++) {
//				System.out.print(transTestData[i][j]+" ");
//			}
//			System.out.println();
//		}
//		System.out.println(testDataNum); 
//		System.out.println("******参数输出结束*******");

		double [][] result = knn.ClassbyKNN(nearestK, rowtr, coltr, type, trainDataAtriNum, transTrainData, transTestData, testDataNum);
		predictResult.put("predictResult", result);
		
		return predictResult;
		
	}
	
	
	private static String getType(Object o){ 
		return o.getClass().toString(); 
	}
	static double[][] data = { {5.1,3.5,1.4,0.2,1 }, {4.9,3.0,1.4,0.2,1}, {5.5,2.4,3.8,1.1,2} ,{6.1,2.6,5.6,1.4,3}};
	static double[][] testData = {{6.3,2.8,5.1,1.5},{4.9,2.4,3.3,1.0},{6.1,2.6,5.6,1.4}};
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Map<String, Object>num;
		num = new HashMap<String, Object>();
		double[][]data = {{ 5.1,3.5,1.4,0.2,1 }, { 4.9,3.0,1.4,0.2,1 }, { 5.5,2.4,3.8,1.1,2 } ,{6.1,2.6,5.6,1.4,3}};
		num.put("data", data);
		
		//System.out.println(getType(123.0));
		KNN knn = new KNN();
		String[][] sData = {{"1", "2", "3","4","5"},{"5.1", "3.5", "1.4", "0.2", "1"},{"4.9", "3.0","1.4","0.2","1"}, {"5.5","2.4","3.8","1.1","2"} ,{"6.1","2.6","5.6","1.4","3"}};
		String[][] sTestData = {{"6.3","2.8","5.1","1.5"},{"4.9","2.4","3.3","1.0"},{"6.1","2.6","5.6","1.4"}};
		Map<String, Object> parameter = new HashMap<String, Object>();
		int [] selectCols = {1,1,1,1,1};
		parameter.put("trainCols", selectCols);	
		parameter.put("nearestK", 1);
		Map<String, Object> trainResult = new HashMap<String, Object>();
		trainResult = knn.train(sData, parameter);
		double[][] transTrainData = (double[][]) trainResult.get("transTrainData");
		Map<String, Object>result = new HashMap<String, Object>();
		result = knn.predict(sTestData, trainResult);
		
		double[][] result1 = (double[][]) result.get("predictResult");
		int row = result1.length;
		int col = result1[0].length;
		
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				System.out.print(result1[i][j]+" ");
			}
			System.out.println();
		}
		
		trainResult.clear();
		result.clear();
		//System.out.println(getType(123.0));

		
		
	}
}
