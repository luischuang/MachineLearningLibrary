package com.penghaisoft.MachineLearningLibrary.clustering;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.penghaisoft.MachineLearningLibrary.util.DataSelect;

/**
 * K-Medians聚类算法
 * @author lc199
 * @date 2018/11/7
 * @version 1.0
 */
public class KMedians {
	/*
	 * 导入C++的DLL文件
	 */
	static {
		System.load("C:/DLL_64/KMediansDLL.dll");
	}


	/**
	 * 输入数据调用C++代码进行计算
	 * @param data 输入数据
	 * @param row 单个样本数据个数
	 * @param col 样本个数
	 * @param classes 要区分的类的个数
	 * @param maxIteration 最大迭代次数
	 * @param initType 初始化类中心的方法
	 * @param distanceType 计算点与点集中心的距离的方法
	 * @param MINKOWSKI_POWER 闵可夫斯基指标,仅当distanceType=3时有意义
	 * @param threads 计算使用的线程数
	 * @param exitThreshold 退出阈值，范围0-1
	 * @return 二维数组矩阵，前row列为原始数据，最后一列为各个样本的分类结果
	 */
	private native double[][] ClusteringbyKMedians(double[][] data, int row, int col, int classes, int maxIteration,
			int initType, int distanceType, double MINKOWSKI_POWER, int threads, double exitThreshold);
	
	/**
	 * 从本地文件导入数据调用C++代码进行计算
	 * @param file 输入数据的文件路径
	 * @param row 单个样本数据个数
	 * @param col 样本个数
	 * @param classes 要区分的类的个数
	 * @param maxIteration 最大迭代次数
	 * @param initType 初始化类中心的方法
	 * @param distanceType 计算点与点集中心的距离的方法
	 * @param MINKOWSKI_POWER 闵可夫斯基指标,仅当distanceType=3时有意义
	 * @param threads 计算使用的线程数
	 * @param exitThreshold 退出阈值，范围0-1
	 * @return 计算结果保存的文件路径，内容为二维数组矩阵，前row列为原始数据，最后一列为各个样本的分类结果
	 */
	private native String ClusteringbyKMedians(String file, int row, int col, int classes, int maxIteration,
			int initType, int distanceType, double MINKOWSKI_POWER, int threads, double exitThreshold);
	/**
	 * 获取变量类型方法
	 * @param o 对象
	 * @return 对象类型
	 */
	private static String getType(Object o){ 
		return o.getClass().toString(); 
	} 
	/**
	 * K-Medians算法计算生成数据聚类
	 * @param Data 待计算数据
	 * @param parameter 算法参数。
	 * parameter包括：{
	 * trainCols：用户选择的数据列。（int[]）
	 * clusterNums：聚类数目。（int）
	 * initCenterMethod：初始中心点计算方法，1：观测到的前K个点；2：随机选择K个点；3：随机淘汰N-K个点；4：选距离最远的K个点。（int）
	 * distanceMethod：数据点与中心点间距离计算方法，1：曼哈顿距离；2：欧氏距离；3：闵可夫斯基距离；4：切比雪夫距离。（int）
	 * maxIteration：最大迭代次数。（double）
	 * exitThreshold：退出阈值。（double）
	 * }
	 * @return {
	 * kmediansResults：聚类所得结果。（String[][]）
	 * }
	 */
	public Map<String, Object> calculate(String[][] Data, Map<String, Object> parameter) {
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
		int Classes=Integer.parseInt(parameter.get("clusterNums").toString());
		int MaxIteration=Integer.parseInt(parameter.get("maxIteration").toString()); 
		int InitType=Integer.parseInt(parameter.get("initCenterMethod").toString());
		int DistanceType=Integer.parseInt(parameter.get("distanceMethod").toString());
		double ExitThreshold=Double.parseDouble(parameter.get("exitThreshold").toString());
		
		int row = selectedData.length;//行数
		int col = selectedData[0].length;//列数
		
		double[][] data = new double[row][col];//c++算法输入数据
		for(int i=0;i<row;i++){
			for(int j=0;j<col;j++){
				data[i][j]=Double.parseDouble(selectedData[i][j]);
			}
		}

		
		//实例化一个KMedians对象
		KMedians k = new KMedians();
		/*
		 * 调用c++算法
		 * 获取结果到result数组中
		 */
		double[][] resultTemp = k.ClusteringbyKMedians(data, row, col, Classes, MaxIteration, InitType, DistanceType,5, 1, ExitThreshold);
		
		/*
		 * 对计算结果进行封装
		 */
		String[][] resultStrings=new String[resultTemp.length+1][resultTemp[0].length];
		for(int i=0;i<resultTemp[0].length-1;i++){
			resultStrings[0][i]=selectedColumnName[i];
		}
		resultStrings[0][resultTemp[0].length-1]="cluster";
		for(int i=0;i<resultTemp.length;i++){
			int j=0;
			for(j=0;j<resultTemp[0].length-1;j++){
				resultStrings[i+1][j]=String.valueOf(resultTemp[i][j]);
			}
			resultStrings[i+1][j]=String.format("%.0f", resultTemp[i][j]);
		}

		result.put("kmediansResults", resultStrings);
		/*
		 * 返回java代码结果
		 */
		return result;

	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String[][] Data=new String[151][4];
		String filepath="D:/Data/MachineLearningData/KMeansTrainData.csv";
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
		parameter.put("clusterNums", 3);
		parameter.put("initCenterMethod", 1);
		parameter.put("distanceMethod", 1);
		parameter.put("maxIteration", 100);
		parameter.put("exitThreshold", 0.2);
		int selectedColumn[]={1,1,1,1};
		parameter.put("trainCols", selectedColumn);
		KMedians k = new KMedians();
		Map<String, Object> result=k.calculate(Data, parameter);
		String[][] resultStrings=(String[][])result.get("kmediansResults");
		for (int i = 0; i < resultStrings.length; i++) {
			for (int j = 0; j < resultStrings[0].length; j++) {
				
				System.out.print(resultStrings[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println(getType(123.0));
		
	}


}
