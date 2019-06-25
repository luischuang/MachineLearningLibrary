package com.penghaisoft.MachineLearningLibrary.preprocess;

import java.util.HashMap;
import java.util.Map;

import com.penghaisoft.MachineLearningLibrary.util.DataConverter;
import com.penghaisoft.MachineLearningLibrary.util.DataSelect;

public class MissingValue {
	static{
//		System.load("C:/DLL_64/MissingValueDLL.dll");
		System.loadLibrary("MissingValueDLL");

	}
	private native double[][] chioce(double[][] a,int m,int n,int choice);
	
	public String[][] process(String[][] data, Map<String, Object>parameter){
		//System.out.println("**************");
		
		int selectCols[] = (int [])parameter.get("trainCols");
		//partitionData为需要缺失值处理的列数据。
		String [][]partitionData = DataSelect.ColumnSelect(data, selectCols);
		//取所有数据的列名
		String []allColsName = DataSelect.getAllColumnName(data);
		
		//需要进行处理的行数和列数
		int row = partitionData.length;
		int col = partitionData[0].length;
		//System.out.println(row+" "+col);
		//处理方法
		int method = (Integer) parameter.get("processMethod");
		if(method>3) {//超出3默认使用1
			method = 1;
		}
		//System.out.println("method="+method);
		
		MissingValue mv = new MissingValue();
		double [][] transedData = DataConverter.String2DtoDouble2D(partitionData);
		transedData = mv.chioce(transedData, row, col, method);
		
		partitionData = DataConverter.Double2DtoString2D(transedData);
//		System.out.println("******缺失值处理后转为string数据********");
//		for(int i=0; i<partitionData.length;i++) {
//			for(int j=0; j<partitionData[0].length;j++) {
//				System.out.print(partitionData[i][j]+"***");
//			}
//			System.out.println();
//		}
//		System.out.println("******缺失值处理后转为string数据输出结束********");

		//填回到原数据中。
		
		int tempCol = 0;
		for(int i=0; i<data[0].length; i++) {//列
			if(selectCols[i] == 1) {
				for(int j=0; j<data.length-1;j++) {//行
					data[j+1][i] = partitionData[tempCol][i];
					tempCol++;
				}
				tempCol = 0;
			}	
		}
		return data;
	}
	
	public static void main(String[] args){
		MissingValue s=new MissingValue();
		String[][] a={
				{"1", "2","3","4"},
				{"1.2","","23.2","232.3"},
				{"1.2","34","23.2",""},
				{"1.2","2.3","23.2","34"},
				{"1.2","2.3","23.2",""},
				{"1.2","2.3","23.2","38"},
				{"1.2","2.3","23.2","34"}
		};
		Map<String, Object>parameter = new HashMap<String, Object>();
		int selectCols[] = {1,1,1,1};
		parameter.put("processMethod", 1);
		parameter.put("trainCols", selectCols);
		String[][] result=s.process(a, parameter);
		for(int i=0;i<6;i++){
			for(int j=0;j<4;j++){
				System.out.print(result[i][j]+" ");
			}
			System.out.println();
		}
		//System.out.print(123);
	}
}
