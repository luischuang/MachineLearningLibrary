//package com.penghaisoft.MachineLearningLibrary.preprocess;
//import com.penghaisoft.MachineLearningLibrary.preprocess.SamplingData;
//public class Sampling {
//	static{
//		System.loadLibrary("Samplingdll");
//	}
//	private native String[][] method(SamplingData z, int dig, int m, int n, int num,int column);
//	public static void main(String[] args)
//	{
//		String[][] data={{"1","2","ab","aa"},
//				{"1","2","ad","aa"},
//				{"1","2","ae","ab"}
//				};
//		SamplingData Data=new SamplingData();
//		Data.setData(data);
//		Sampling s=new Sampling();
//		int dig=1;//用户输入
//		String [][] result=s.method(Data, 1, 3, 4, 1,3);
//		
//		System.out.println("123");
//		for(int i=0;i<result.length;i++)
//		{
//			for (int j=0;j<4;j++)
//			{
//				System.out.print(result[i][j]+",");
//			}
//			System.out.println();
//		}
//		
//		System.out.println("123");
//		
//	}
//}

package com.penghaisoft.MachineLearningLibrary.preprocess;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.penghaisoft.MachineLearningLibrary.preprocess.SamplingData;
import com.penghaisoft.MachineLearningLibrary.util.DataConverter;
import com.penghaisoft.MachineLearningLibrary.util.DataSelect;
import com.penghaisoft.MachineLearningLibrary.util.Load;
public class Sampling {
	static{
		
		System.loadLibrary("Samplingdll");
//		try {
//			Load.loadLib("/Samplingdll");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	private native String[][] method(SamplingData z, int dig, int m, int n, int num,int column);
	
	public String[][] process(String [][]data, Map<String, Object>parameter){
		int dig;
		int num;
		int column;
		dig = Integer.parseInt(parameter.get("Dig").toString());
		num = Integer.parseInt(parameter.get("Num").toString());
		column=Integer.parseInt(parameter.get("Column").toString());
		int [] selectCols = (int[]) parameter.get("trainCols");
		
		String colsName[] = DataSelect.getSelectedColumnName(data, selectCols);
		String [][] trainData = DataSelect.ColumnSelect(data, selectCols);
		for(int i=0; i<trainData.length; i++) {
			for(int j=0; j<trainData[0].length; j++) {
				System.out.print(trainData[i][j]+" ");
			}
			System.out.println();
		}
		
		int row = trainData.length;
		int col = trainData[0].length;
		SamplingData Data=new SamplingData();
		Data.setData(trainData);
		String[][] result=new String[row][col];
		result = method(Data, dig, row, col,num,column);
		
		String[][]pResult = new String[row+1][col];
		for(int i=0; i<col; i++) {
			pResult[0][i] = colsName[i];
		}
		for(int i=1; i<row+1; i++) {
			for(int j=0; j<col; j++) {
			  pResult[i][j] = String.valueOf(result[i-1][j]);
			}
		}
		return pResult;
		
	}
	public static void main(String[] args)
	{
		String[][] data={
				{"No","year","month","day","hour","pm2.5","DEWP","TEMP","PRES","cbwd","Iws","Is","Ir"},
				{"1","2010","1","1","0","NA","-21","-11","1021","NW","1.79","0","0"},
				{"2","2010","1","1","1","NA","-21","-12","1020","NW","4.92","0","0"},
				{"3","2010","1","1","2","NA","-21","-11","1019","NW","6.71","0","0"},
				{"4","2010","1","1","3","NA","-21","-14","1019","NW","9.84","0","0"},
				{"5","2010","1","1","4","NA","-20","-12","1018","NW","12.97","0","0"},
				{"6","2010","1","1","5","NA","-19","-10","1017","NW","16.1","0","0"},
				{"7","2010","1","1","6","NA","-19","-9","1017","NW","19.23","0","0"},
				{"8","2010","1","1","7","NA","-19","-9","1017","NW","21.02","0","0"},
				{"9","2010","1","1","8","NA","-19","-9","1017","NW","24.15","0","0"},
				{"10","2010","1","1","9","NA","-20","-8","1017","NW","27.28","0","0"},
				{"11","2010","1","1","10","NA","-19","-7","1017","NW","31.3","0","0"},
				{"12","2010","1","1","11","NA","-18","-5","1017","NW","34.43","0","0"},
				{"13","2010","1","1","12","NA","-19","-5","1015","NW","37.56","0","0"},
				{"14","2010","1","1","13","NA","-18","-3","1015","NW","40.69","0","0"},
				{"15","2010","1","1","14","NA","-18","-2","1014","NW","43.82","0","0"},
				{"16","2010","1","1","15","NA","-18","-1","1014","cv","0.89","0","0"},
				{"17","2010","1","1","16","NA","-19","-2","1015","NW","1.79","0","0"},
				{"18","2010","1","1","17","NA","-18","-3","1015","NW","2.68","0","0"},
				{"19","2010","1","1","18","NA","-18","-5","1016","NE","1.79","0","0"},
				{"20","2010","1","1","19","NA","-17","-4","1017","NW","1.79","0","0"}
				
		};
//		System.out.println("123");
//		System.out.println("123");
		Sampling s = new Sampling();
		int []selectCols = {1,1,1,1,1,1,1,1,1,1,1,1,1};//设置需要选中的列数
		Map<String, Object>Parameter = new HashMap<String, Object>();
		Parameter.put("trainCols", selectCols);
		Parameter.put("Dig", 21);
		Parameter.put("Num", 0);
		Parameter.put("Column", 3);
	    String[][] result = s.process(data, Parameter);
		for(int i=0; i<result.length; i++) {
			for(int j=0; j<result[0].length; j++) {
				System.out.print(result[i][j]+" ");
			}
			System.out.println();
		}
		
  } 
}
