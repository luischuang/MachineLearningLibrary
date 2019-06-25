package com.penghaisoft.MachineLearningLibrary.classify;
import java.util.ArrayList;

//import org.apache.commons.lang3.tuple.Pair;
import javafx.util.Pair;
import com.alibaba.fastjson.JSON;
import com.penghaisoft.MachineLearningLibrary.classify.ID3Model;
public class ID3 {

	static{
		System.loadLibrary("ID3DLL");
	}
	private native String train(int[][] xData,int[] yData,int row,int col);
	private native int getTreeNodesNumber(String tree);
	private native int[] predict(int[][] xData,int row,int col,String Model,int modelSize);

	static String modelString="1#-1#0#3#@3#-1#0#2#@-1#1#4#0#2,0;3,0;4,0;0,1;!2,2;3,1;4,1;0,1;!2,1;3,0;4,1;0,1;!2,0;3,1;4,0;0,1;!@4#-1#0#2#@-1#0#3#0#2,0;4,0;0,0;!2,0;4,1;0,0;!2,1;4,0;0,0;!@-1#1#2#0#2,2;4,0;0,1;!2,1;4,1;0,1;!@-1#1#3#0#2,1;3,0;0,1;!2,2;3,1;0,1;!2,1;3,1;0,1;!@-1#0#2#0#2,2;3,1;0,0;!2,1;3,0;0,0;!";


	static ID3Model[] changeStr2model(String str){
		
		String modelsStr[]=str.split("@");
		int modelSize=modelsStr.length;
		ID3Model model[] =new ID3Model[modelSize];
		for(int i=0;i<modelsStr.length;i++){
			System.out.println(modelsStr[i]);
			model[i]=new ID3Model();
			String paramsStr[]=modelsStr[i].split("#");
			model[i].setIndex(Integer.parseInt(paramsStr[0]));
			model[i].setType(Integer.parseInt(paramsStr[1]));
			model[i].setSampleSize(Integer.parseInt(paramsStr[2]));
			model[i].setNextSize(Integer.parseInt(paramsStr[3]));
			if(paramsStr.length<=4){
				
			}else{
				String samplesStrs[]=paramsStr[4].split("!");
				ArrayList<ArrayList<Pair<Integer,Integer>>> sample=new ArrayList<ArrayList<Pair<Integer,Integer>>>();
				for(int j=0;j<samplesStrs.length;j++){
					String dataStrs[]=samplesStrs[j].split(";");
					ArrayList<Pair<Integer,Integer>> dataList=new ArrayList<Pair<Integer,Integer>>();
					for(int k=0;k<dataStrs.length;k++){
						String valueStr[]=dataStrs[k].split(",");
						int first=Integer.parseInt(valueStr[0]);
						int second=Integer.parseInt(valueStr[1]);
						Pair<Integer,Integer> data=new Pair<Integer,Integer>(first,second);
						dataList.add(data);
					}
					sample.add(dataList);
				}
				model[i].setSample(sample);
			}
		}
		return model;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int xData[][] =
			{
				{ 0, 0, 0, 0 },
				{ 0, 0, 0, 1 },
				{ 1, 0, 0, 0 },
				{ 2, 1, 0, 0 },
				{ 2, 2, 1, 0 },
				{ 2, 2, 1, 1 },
				{ 1, 2, 1, 1 },
				{ 0, 1, 0, 0 },
				{ 0, 2, 1, 0 },
				{ 2, 1, 1, 0 },
				{ 0, 1, 1, 1 },
				{ 1, 1, 0, 1 },
				{ 1, 0, 1, 0 },
				{ 2, 1, 0, 1 }

			};
		int yData[]={0,0,1,1,1,0,1,0,1,1,1,1,1,0};
		ID3 id3=new ID3();
		String result=id3.train(xData , yData, 14, 4);
		System.out.println(result);
		ID3Model model[]=ID3.changeStr2model(result);
		String json =JSON.toJSON(model).toString();
		System.out.println(json);
		int[] predictResult=id3.predict(xData, 14, 4, result, model.length);
		for (int i = 0; i < predictResult.length; i++) {
			System.out.println(predictResult[i]);
		}
		
	}

}
