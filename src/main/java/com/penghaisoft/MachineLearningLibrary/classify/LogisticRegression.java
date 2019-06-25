package com.penghaisoft.MachineLearningLibrary.classify;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.penghaisoft.MachineLearningLibrary.util.DataConverter;
import com.penghaisoft.MachineLearningLibrary.util.DataSelect;
import com.penghaisoft.MachineLearningLibrary.util.LabelMap;
/**
 * 逻辑回归
 * @author lc
 * @date 2018/11/7
 * @version 1.0
 */
public class LogisticRegression {

	/*
	 * 导入C++的DLL文件
	 */
	static {
//		System.load("C:/DLL_64/LogisticRegressionDLL.dll");
		System.loadLibrary("LogisticRegressionDLL");
	}
	/**
	 * 逻辑回归，获取回归系数
	 * @param data 待计算数据
	 * @param label 标签
	 * @param dataNum 数据数量
	 * @param featureNum 属性数量
	 * @param method 计算方法，0表示梯度上升法；1表示随机梯度上升法。
	 * @param iteration 迭代次数
	 * @param stepSize 学习速率
	 * @return 回归系数
	 */
	private native double[] regression(double[][] data,int[] label,int dataNum,int featureNum,int method, int iteration,double stepSize);
	/**
	 * 根据回归系数对数据进行分类预测
	 * @param weight 回归系数
	 * @param data 待预测数据
	 * @param featureNum 属性个数
	 * @return 该数据所属类别
	 */
	private native int[] predict(double[] weight,double[][] data,int dataSize, int featureNum);

	/**
	 * 根据回归系数,获取数据的分类概率
	 * @param weight 回归系数
	 * @param data 待预测数据
	 * @param featureNum 属性个数
	 * @return 该数据所属类别
	 */
	private native double[] getProbability(double[] weight,double[][] data,int dataSize, int featureNum);
	/**
	 * 逻辑回归训练，获取回归系数
	 * @param Data 待计算数据
	 * @param parameter 算法参数。
	 * parameter包括：{
	 * trainCols：用户选择的数据列。（int[]）
	 * iterCalMethod：计算方法，0表示梯度上升法；1表示随机梯度上升法。（int）
	 * iteration：迭代次数（int）
	 * learnRate：学习速率（double）
	 * }
	 * @return {
	 * lrResults：逻辑回归所得系数。（double[]）
	 * classMap：分类映射，分类映射为（0~1）的整数，在逻辑回归预测时会用到。（Map<String, Integer>）
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
		int method=(Integer)parameter.get("iterCalMethod");
		int iteration=(Integer)parameter.get("iteration");
		double trainingRate=(Double)parameter.get("learnRate");
		String[][] selectedData=DataSelect.ColumnSelect(Data, selectedColumn);
		String[] selectedColumnName=DataSelect.getSelectedColumnName(Data, selectedColumn);
		int dataNum=selectedData.length;
		int featureNum=selectedData[0].length-1;
		double data[][]=new double[dataNum][featureNum];
		String labelStrings[]=new String[dataNum];
		int label[]=new int[dataNum];
		for(int i=0;i<dataNum;i++){
			for(int j=0;j<featureNum;j++){
				data[i][j]=Double.parseDouble(selectedData[i][j]);
			}
			labelStrings[i]=selectedData[i][featureNum];
		}
		Map<String, Integer> labelMap=LabelMap.getIntLabelMap(labelStrings);
		label=LabelMap.getIntLabelLists(labelStrings, labelMap);
		LogisticRegression regression=new LogisticRegression();
		double[] coefficient=regression.regression(data, label, dataNum, featureNum, method, iteration, trainingRate);
		result.put("lrResults", coefficient);
		result.put("classMap", labelMap);
		result.put("trainCols", selectedColumn);
		result.put("trainColsName", selectedColumnName);
		return result;
	}

	/**
	 * 根据回归系数对多个样本数据进行分类预测
	 * @param Data 待预测数据
	 * @param parameter 预测所需参数
	 * parameter包括：{
	 * lrResults：逻辑回归所得系数。（double[]）
	 * classMap：分类映射。（Map<String, Integer>）
	 * trainCols：用户选择的数据列。（int[]）
	 * trainColsName：用户选择的数据列名。（String[]）
	 * }
	 * @return {
	 * predictResult：预测得出各个数据样本的所属分类。（String[]）
	 * }
	 */
	public Map<String, Object> predict(String[][] Data, Map<String, Object> parameter) {
		//最终预测结果
		Map<String, Object> result=new HashMap<String, Object>();
		//逻辑回归系数
		double[] weight=(double[]) parameter.get("lrResults");
		//类别预测
		Map<String, Integer> labelMap=(Map<String, Integer>) parameter.get("classMap");
		//训练数据的列选择
		int[] trainCols=(int[]) parameter.get("trainCols");
		int[] selectedColumn=new int[trainCols.length-1];//预测数据的列选择比训练数据的列选择少最后一列
		for(int i=0;i<selectedColumn.length;i++){
			selectedColumn[i]=trainCols[i];
		}
		String[] trainColsName=(String[]) parameter.get("trainColsName");//用户训练选择的列名
		String[][] selectedData=DataSelect.ColumnSelect(Data, selectedColumn);//预测数据
		double data[][] = DataConverter.String2DtoDouble2D(selectedData);//预测数据转换为double

		LogisticRegression regression=new LogisticRegression();
		//预测返回结果，包含列名和最后一列分类
		String[][] predictResult=new String[data.length+1][trainColsName.length];
		//第一行位列名，最后一个为分类
		for(int i=0;i<trainColsName.length;i++){
			predictResult[0][i]=trainColsName[i];
		}
		
		//预测结果的最初值
		int predictResultInt[]=new int[data.length];
		double predictProbability[] = new double[data.length];
		//进行预测
		predictResultInt=regression.predict(weight, data,data.length, data[0].length);
		predictProbability=regression.getProbability(weight, data, data.length, data[0].length);
		//预测结果的分类，中间值
		String predictResultTemp[]=LabelMap.getLabel(predictResultInt, labelMap);
		for(int i=0;i<predictResultTemp.length;i++){
			int j=0;
			//将预测数据赋给predictResult。
			for(j=0;j<selectedData[0].length;j++){
				predictResult[i+1][j]=selectedData[i][j];
			}
			//最后一列结果赋给predictResult。
			predictResult[i+1][j]=predictResultTemp[i];
		}
		
		result.put("predictResult", predictResult);
		result.put("predictProbability", predictProbability);
		return result;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		LogisticRegression regression=new LogisticRegression();
		LogisticRegression regression1=new LogisticRegression();
		String filepath="D:/Data/MachineLearningData/LogisticRegressionTrainData.csv";
		String[][] data=new String[101][3];
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
	            	data[index][i]=str[i];
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
		String testData[][]={{"L","W"},{"1.7","0.5"},{"4.8","5.8"}};
		
			Map<String, Object> parameter=new HashMap<String, Object>();
			parameter.put("iterCalMethod", 1);
			parameter.put("iteration", 500);
			parameter.put("learnRate", 0.01);
			int selectedColumn[]={1,1,1};
			parameter.put("trainCols", selectedColumn);
			Map<String, Object> result=regression.train(data, parameter);
			Map<String, Object> result1=regression1.train(data, parameter);
			String[] trainColsName=(String[]) result.get("trainColsName");
			for(int i=0;i<trainColsName.length;i++){
				System.out.print(trainColsName[i]+" ");
			}
			System.out.println();
			double[] coefficient=(double[]) result.get("lrResults");
			for(int i=0;i<coefficient.length;i++){
				System.out.print(coefficient[i]+" ");
			}
			System.out.println();
			Map<String, Integer> labelMap=(Map<String, Integer>) result.get("classMap");
		while(true){
			System.out.println("-----------LogisticRegression1------------------");
			Map<String, Object> predict=regression.predict(testData, result);
			String[][] predictResult=(String[][]) predict.get("predictResult");
			double[] predictProbability=(double[])predict.get("predictProbability");
			for(int j=0;j<predictResult[0].length;j++){
				System.out.print(predictResult[0][j]+",");
			}
			System.out.println();
			for(int i=1;i<predictResult.length;i++){
				for(int j=0;j<predictResult[0].length;j++){
					System.out.print(predictResult[i][j]+",");
				}
				System.out.print(predictProbability[i-1]);
				System.out.println();
			}
			System.out.println("-----------LogisticRegression2------------------");
			Map<String, Object> predict1=regression1.predict(testData, result1);
			String[][] predictResult1=(String[][]) predict1.get("predictResult");
			double[] predictProbability1=(double[])predict1.get("predictProbability");
			for(int j=0;j<predictResult1[0].length;j++){
				System.out.print(predictResult1[0][j]+",");
			}
			System.out.println();
			for(int i=1;i<predictResult1.length;i++){
				for(int j=0;j<predictResult1[0].length;j++){
					System.out.print(predictResult1[i][j]+",");
				}
				System.out.print(predictProbability1[i-1]);
				System.out.println();
			}
		}

	}
}
