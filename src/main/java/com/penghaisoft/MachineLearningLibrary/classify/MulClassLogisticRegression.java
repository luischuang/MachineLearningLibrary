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
import java.util.concurrent.atomic.LongAdder;

import com.penghaisoft.MachineLearningLibrary.util.DataConverter;
import com.penghaisoft.MachineLearningLibrary.util.DataSelect;
import com.penghaisoft.MachineLearningLibrary.util.LabelMap;
/**
 * 多分类逻辑回归
 * @author lc
 * @date 2018/11/7
 * @version 1.0
 */
public class MulClassLogisticRegression {
	private double[][] xData;
	private double[][] yData;

	/*
	 * 导入C++的DLL文件
	 */
	static {
//		System.load("C:/DLL_64/MulClassLogisticRegressionDLL.dll");
		System.loadLibrary("MulClassLogisticRegressionDLL");
	}
	/**
	 * 多分类逻辑回归运算
	 * @param xData 属性数据，若共有n组数据，m个属性，则数据为（n x m ）的矩阵
	 * @param yData 标签数据，若一共有p种标签，则数据大小为（n x p ）的矩阵，每一行只有一个1，（p-1）个0，为1表示为属于该标签下。
	 * @param featureNumber 属性个数m
	 * @param dataNumber 数据个数n
	 * @param classes 分类个数，即标签个数p
	 * @param iterations 迭代次数
	 * @param learningRate 迭代学习率
	 * @return 返回theta系数矩阵，大小为（ p x m ）的矩阵
	 */
	private native double[][] LogisticRegression(double[][] xData,double[][] yData,int featureNumber,int dataNumber,
			int classes,int iterations,double learningRate);

	/**
	 * 预测结果
	 * @param xData 待预测数据
	 * @return 返回的值表示该组数据属于的标签分组。
	 */
	private native int[] predict(double[][] data, double[][] theta,int dataSize, int classSize,int featureSize);
	/**
	 * 预测结果
	 * @param xData 待预测数据
	 * @return 返回的值表示该组数据属于的标签分组。
	 */
	private native double[][] getProbability(double[][] data, double[][] theta,int dataSize, int classSize,int featureSize);
	/**
	 * 获取变量类型方法
	 * @param o 对象
	 * @return 对象类型
	 */
	private static String getType(Object o){ 
		return o.getClass().toString(); 
	} 
	/**
	 * 多分类逻辑回归训练，获取回归系数
	 * @param Data 待计算数据
	 * @param parameter 算法参数。
	 * parameter包括：{
	 * trainCols：用户选择的数据列。
	 * iteration：迭代次数
	 * learnRate：学习速率
	 * }
	 * @return {
	 * mclrResults：多分类逻辑回归所得系数。
	 * classMap：分类映射。
	 * trainCols：用户选择的数据列。
	 * trainColsName：用户选择的数据列名。
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
		int Iterations=Integer.parseInt(parameter.get("iteration").toString());
		double LearningRate=Double.parseDouble(parameter.get("learnRate").toString());
		String[][] selectedData=DataSelect.ColumnSelect(Data, selectedColumn);
		String[] selectedColumnName=DataSelect.getSelectedColumnName(Data, selectedColumn);
		int DataNumber=selectedData.length;
		int FeatureNumber=selectedData[0].length-1;

		double[][] xData=new double[DataNumber][FeatureNumber];
		String labelStrings[]=new String[DataNumber];
		for(int i=0;i<DataNumber;i++){
			for(int j=0;j<FeatureNumber;j++){
				xData[i][j]=Double.parseDouble(selectedData[i][j]);
			}
			labelStrings[i]=selectedData[i][FeatureNumber];
		}
		
		Map<String, Integer> labelMap=LabelMap.getIntLabelMap(labelStrings);
		int ClassNumber=labelMap.size();
		double[][] yData=new double[DataNumber][ClassNumber];
		for(int i=0;i<DataNumber;i++){
			for(int j=0;j<ClassNumber;j++){
				yData[i][j]=0;
			}
		}
		int label[]=new int[DataNumber];
		label=LabelMap.getIntLabelLists(labelStrings, labelMap);
		for(int i=0;i<DataNumber;i++){
			int y=label[i];
            yData[i][y]=1;
		}
		
		MulClassLogisticRegression regression=new MulClassLogisticRegression();
		double[][] theta=regression.LogisticRegression(xData, yData, FeatureNumber, DataNumber, ClassNumber, Iterations, LearningRate);
		result.put("mclrResults", theta);
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
	 * mclrResults：多分类逻辑回归所得系数。
	 * classMap：分类映射，分类映射为（0~m）的整数，在逻辑回归预测时会用到。
	 * trainCols：用户选择的数据列。
	 * trainColsName：用户选择的数据列名。
	 * }
	 * @return {
	 * predictResult：预测得出各个数据样本的所属分类。
	 * }
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> predict(String[][] Data, Map<String, Object> parameter) {
		//最终预测结果
		Map<String, Object> result=new HashMap<String, Object>();
		
		Map<String, Integer> labelMap=(Map<String, Integer>) parameter.get("classMap");
		double[][] theta=(double[][]) parameter.get("mclrResults");
		int[] trainCols=(int[]) parameter.get("trainCols");
		int[] selectedColumn=new int[trainCols.length-1];//预测数据的列选择比训练数据的列选择少最后一列
		for(int i=0;i<selectedColumn.length;i++){
			selectedColumn[i]=trainCols[i];
		}
		String[] trainColsName=(String[]) parameter.get("trainColsName");//用户训练选择的列名
		String[][] selectedData=DataSelect.ColumnSelect(Data, selectedColumn);//预测数据
		int classSize=labelMap.size();
		int featureSize=selectedData[0].length;
		MulClassLogisticRegression regression=new MulClassLogisticRegression();
		double data[][] = DataConverter.String2DtoDouble2D(selectedData);
		//预测返回结果，包含列名和最后一列分类
		String[][] predictResult=new String[data.length+1][trainColsName.length];
		//第一行位列名，最后一个为分类
		for(int i=0;i<trainColsName.length;i++){
			predictResult[0][i]=trainColsName[i];
		}
		
		//预测结果的最初值
		int predictResultInt[]=new int[data.length];
		double predictProbability[][] = new double[data.length][classSize];
		//进行预测
		predictResultInt=regression.predict(data, theta,data.length,classSize, featureSize);
		predictProbability=regression.getProbability(data, theta, data.length, classSize, featureSize);
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
		MulClassLogisticRegression regression=new MulClassLogisticRegression();
		String[][] Data=new String[20001][17];
		String filepath="D:/Data/MachineLearningData/MulClassLogisticRegressionTrainData.csv";
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
//		String testData[][]={{ "1", "0", "1", "0", "0", "2", "2", "5", "4", "1", "2", "6", "0", "8", "0", "8" },
//				{"4","5","5","4","4","8","4","4","4","9","4","10","3","6","6","6"}
//				};
		Map<String, Object> parameter=new HashMap<String, Object>();
		parameter.put("iteration", 100);
		parameter.put("learnRate", 0.01);
		int selectedColumn[]={1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
		parameter.put("trainCols", selectedColumn);
		Map<String, Object> result=regression.train(Data, parameter);	
		double[][] coefficient=(double[][]) result.get("mclrResults");
		for (int i = 0; i<26; i++) {
			for (int j = 0; j<16; j++) {
				System.out.print(coefficient[i][j]+",");			
			}
			System.out.println();
		}
		Map<String, Integer> labelMap=(Map<String, Integer>) result.get("classMap");
		
		
		String[][] testData = new String[201][16];
		String testfilepath="D:/Data/MachineLearningData/MulClassLogisticRegressionTestData.csv";
		File testfile = new File(testfilepath); 
        InputStreamReader testreader;
		try {
			testreader = new InputStreamReader( new FileInputStream(testfile));
			BufferedReader br = new BufferedReader(testreader); 
	        String line = "";  
	        int index=0;
	        line = br.readLine();  
	        while (line != null) {  
	            String[] str=line.split(",");
	            for(int i=0;i<str.length;i++){
	            	testData[index][i]=str[i];
	            }
	            line = br.readLine(); 
	            index++;
	        }  
	        testreader.close();
	        br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
		Map<String, Object> predict = regression.predict(testData, result);
		String[][] predictResult = (String[][]) predict.get("predictResult");
		for (int i = 0; i < predictResult.length; i++) {
			for(int j=0;j<predictResult[0].length;j++){
				System.out.print(predictResult[i][j]+",");
			}
			System.out.println();
		}
		double[][] predictProbability = (double[][]) predict.get("predictProbability");
		for (int i = 0; i < predictProbability.length; i++) {
			for(int j=0;j<predictProbability[0].length;j++){
				System.out.print(predictProbability[i][j]+",");
			}
			System.out.println();
		}
	}

}
