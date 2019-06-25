package com.penghaisoft.MachineLearningLibrary.regression;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.penghaisoft.MachineLearningLibrary.util.DataConverter;
import com.penghaisoft.MachineLearningLibrary.util.DataSelect;
/**
 * 指数回归
 * @author lc
 * @date 2018/11/7
 * @version 1.0
 */
public class ExponentialRegression {

	/*
	 * 导入C++的DLL文件
	 */
	static {
//		System.load("C:/DLL_64/ExponentialRegressionDLL.dll");
		System.loadLibrary("ExponentialRegressionDLL");
	}
	private native void Regression(double[][] data,int row, int col);
	private native double[] GetFactor();
	private native double[] GetIndicator();
	private native double Predict(double[] data,double coefficient);
	/**
	 * 指数回归训练，获取回归系数
	 * @param Data 待计算数据
	 * @param parameter 算法参数。
	 * parameter包括：{
	 * trainCols：用户选择的数据列。（int[]）
	 * }
	 * @return {
	 * erResults：指数回归所得系数。（double[]）
	 * modelAssess：拟合评价指标，包括："回归平方和"，"残差平方和"，"回归平方差"，"剩余平方差"。（Map<String, Double>）
	 * trainCols：用户选择的数据列。（int[]）
	 * trainColsName：用户选择的数据列名。（String[]）
	 * }
	 */
	public Map<String, Object> train(String[][] Data,Map<String, Object> parameter){
		Map<String, Object> result=new HashMap<String, Object>();
		if(Data==null||(Data!=null && Data.length==0)){
			return null;
		}else {
			for(int i=0;i<Data.length;i++){
				if(Data[i]==null||(Data[i]!=null && Data[i].length==0)){
					return null;
				}
			}
		}
		int[] selectedColumn=(int[]) parameter.get("trainCols");
		String[][] selectedData=DataSelect.ColumnSelect(Data, selectedColumn);
		String[] selectedColumnName=DataSelect.getSelectedColumnName(Data, selectedColumn);
		int Row=selectedData.length;
		int Col=selectedData[0].length;
		double[][] data=DataConverter.String2DtoDouble2D(selectedData);
		ExponentialRegression regression=new ExponentialRegression();
		regression.Regression(data, Row, Col);
		double[] factor=regression.GetFactor();
		double[] indicator=regression.GetIndicator();
		Map<String, Double> indicators=new HashMap<String, Double>();
		indicators.put("回归平方和", indicator[0]);//回归平方和 ESS (explained sum of squares)
		indicators.put("残差平方和", indicator[1]);//残差平方和SSR（residual sum of squares），也称剩余平方和
		indicators.put("回归平方差", indicator[2]);//回归平方差 Regression variance
		indicators.put("剩余平方差", indicator[3]);//剩余平方差 Residual mean square
		result.put("erResults", factor);
		result.put("modelAssess", indicators);
		result.put("trainCols", selectedColumn);
		result.put("trainColsName", selectedColumnName);
		return result;
	}
	/**
	 * 根据指数回归系数进行预测
	 * @param Data 待预测数据。
	 * @param parameter 预测所需参数。
	 * parameter包括：{
	 * erResults：指数回归所得系数。（double[]）
	 * }
	 * @return {
	 * predictResult：预测得出数据结果。（double[]）
	 * }
	 */
	public Map<String, Object> predict(String[][] Data, Map<String, Object> parameter) {
		//最终预测结果
		Map<String, Object> result=new HashMap<String, Object>();
		
		int[] trainCols = (int[]) parameter.get("trainCols");
		int[] selectedColumn=new int[trainCols.length-1];//预测数据的列选择比训练数据的列选择少最后一列
		for(int i=0;i<selectedColumn.length;i++){
			selectedColumn[i]=trainCols[i];
		}
		String[][] selectedData = DataSelect.ColumnSelect(Data, selectedColumn);//用户选择的数据
		String[] trainColsName=(String[]) parameter.get("trainColsName");//用户训练选择的列名
		
		double[] factor=(double[]) parameter.get("erResults");//指数回归系数
		double[][] data=DataConverter.String2DtoDouble2D(selectedData);//预测数据
		
		//预测返回结果，包含列名和最后一列分类
		String[][] predictResult=new String[data.length+1][trainColsName.length];
		//第一行位列名，最后一个为分类
		for(int i=0;i<trainColsName.length;i++){
			predictResult[0][i]=trainColsName[i];
		}
		
		//预测结果的最初值
		double predictResultDouble[]=new double[data.length];
		for(int i=0;i<data.length;i++){
			predictResultDouble[i]=Predict(data[i], factor);
		}
		
		for(int i=0;i<data.length;i++){
			int j=0;
			//将预测数据赋给predictResult。
			for(j=0;j<selectedData[0].length;j++){
				predictResult[i+1][j]=selectedData[i][j];
			}
			//最后一列结果赋给predictResult。
			predictResult[i+1][j]=String.valueOf(predictResultDouble[i]);
		}
		
		result.put("predictResult", predictResult);
		return result;
	}
	private double Predict(double[] data,double[] factor){
		double result = factor[0];
		double mi = 0;
		for (int i = 0; i < data.length; i++){
			mi += data[i] * factor[i + 1];
		}
		result = result*Math.exp(mi);
		return result;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[][] Data=new String[11][3];
		String filepath="D:/Data/MachineLearningData/ExponentialRegressionTrainData.csv";
		File file = new File(filepath); 
        InputStreamReader reader;
		try {
			reader = new InputStreamReader(  
			        new FileInputStream(file));
			BufferedReader br = new BufferedReader(reader); 
	        String line = "";  
	        int index=0;
	        line = br.readLine();  
	        while (line != null) {  
	            String[] str=line.split(",");
	            for(int i=0;i<str.length;i++){
	            	Data[index][i]=str[i];
	            }
	            line = br.readLine(); 
	            index++;
	        }  
	        reader.close();
	        br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
		Map<String, Object> parameter=new HashMap<String, Object>();
		int selectedColumn[]={1,1,1};
		parameter.put("trainCols", selectedColumn);
		ExponentialRegression regression=new ExponentialRegression();
		Map<String, Object> result=regression.train(Data, parameter);
		
		String[][] testData={{"built-up area","number of bathroom"},{"3000","4"},{"2400","3"}};

		Map<String, Object> predict = regression.predict(testData, result);
		String[][] predictResult = (String[][]) predict.get("predictResult");
		for (int i = 0; i < predictResult.length; i++) {
			for(int j=0;j<predictResult[0].length;j++){
				System.out.print(predictResult[i][j]+",");
			}
			System.out.println();
		}


	}

}
