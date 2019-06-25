package com.penghaisoft.MachineLearningLibrary.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

/**
 * 数据列选择类
 * @author lc199
 *
 */
public class DataSelect {

	public static String[][] ColumnSelect(String[][] Data,int[] selectedColumn) {
		int selectedNumber=0;
		for(int i=0;i<selectedColumn.length;i++){
			if(selectedColumn[i]==1){
				selectedNumber++;
			}
		}
		String[][] data=new String[Data.length-1][selectedNumber];
		int count=0;
		for(int i=1;i<Data.length;i++){
			int a=0;
			for(int j=0;j<Data[0].length;j++){
				if(selectedColumn[j]==1){
					data[count][a++]=Data[i][j];
				}
			}
			count++;
		}
		return data;
	}
	public static String[] ColumnSelect(String[] Data,int[] selectedColumn) {
		int selectedNumber=0;
		for(int i=0;i<selectedColumn.length;i++){
			if(selectedColumn[i]==1){
				selectedNumber++;
			}
		}
		String[] data=new String[selectedNumber];
		int count=0;
		for(int i=1;i<Data.length;i++){
			if(selectedColumn[i]==1){
				data[count]=Data[i];
			}
			count++;
		}
		return data;
	}
	public static String[] getSelectedColumnName(String[][] Data,int[] selectedColumn){
		int selectedNumber=0;
		for(int i=0;i<selectedColumn.length;i++){
			if(selectedColumn[i]==1){
				selectedNumber++;
			}
		}
		String[] selectedColumnNume=new String[selectedNumber];
		int a=0;
		for(int i=0;i<selectedColumn.length;i++){
			if(selectedColumn[i]==1){
				selectedColumnNume[a++]=Data[0][i];
			}
		}
		return selectedColumnNume;
	}
	public static String[] getAllColumnName(String[][] Data){
		
		String[] allColumnNume=new String[Data[0].length];
		int a=0;
		for(int i=0;i<Data[0].length;i++){
			
			allColumnNume[a++]=Data[0][i];
		}
		return allColumnNume;
	}

	public static boolean SplitDataSets(String filePath, double testRate) {
		Vector<Vector<String>> Data = new Vector<Vector<String>>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			String line = null;
			while ((line = reader.readLine()) != null) {
				String item[] = line.split(",");// CSV格式文件为逗号分隔符文件，这里根据逗号切分
				Vector<String> data=new Vector<String>();
				for (int j = 0; j < item.length; j++) {
					data.add(item[j]);
				}
				Data.add(data);
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String strs[]=filePath.split("\\.");
		String fileType=strs[strs.length-1];
		int fileNameLength=filePath.length()-fileType.length()-1;
		char filePathChars[]=filePath.toCharArray();
		String fileName="";
		for(int i=0;i<fileNameLength;i++){
			fileName+=filePathChars[i];
		}
		String trainPath=fileName+"_train."+fileType;
		String testPath=fileName+"_test."+fileType;
		HashMap<Integer, Integer> randomMaps=new HashMap<Integer, Integer>();
		int totalSize=Data.size()-1;
		int testSize=(int)(totalSize*testRate);
//		int trainSize=totalSize-testSize;
		Random random=new Random();
		random.setSeed(System.currentTimeMillis());
		while(randomMaps.size()<testSize){
			int r=random.nextInt(totalSize)+1;
			randomMaps.put(r, 1);
		}
		Vector<String> Label=Data.get(0);
		Vector<Vector<String>> TestData = new Vector<Vector<String>>();
		TestData.addElement(Label);
		Vector<Vector<String>> TrainData = new Vector<Vector<String>>();
		TrainData.addElement(Label);
		for(int i=1;i<=totalSize;i++){
			if(randomMaps.containsKey(i)){
				TestData.addElement(Data.get(i));
			}else{
				TrainData.addElement(Data.get(i));
			}
		}
		write2File(trainPath, TrainData);
		write2File(testPath, TestData);
		return true;
	}
	private static void write2File(String filename,Vector<Vector<String>> data){
		File file = new File(filename);  
        PrintStream ps;
		try {
			ps = new PrintStream(new FileOutputStream(file));
			for(int i=0;i<data.size();i++){
				int j=0;
				for(;j<data.get(i).size()-1;j++){
					ps.append(data.get(i).get(j)+",");// 在已有的基础上添加字符串  
				}
	            ps.append(data.get(i).get(j)+"\n");// 在已有的基础上添加字符串  
	        }
	        ps.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        
	}
}
