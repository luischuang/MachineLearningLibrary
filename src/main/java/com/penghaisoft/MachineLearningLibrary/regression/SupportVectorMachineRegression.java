package com.penghaisoft.MachineLearningLibrary.regression;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.penghaisoft.MachineLearningLibrary.classify.SVMParameter;
import com.penghaisoft.MachineLearningLibrary.util.DataConverter;
import com.penghaisoft.MachineLearningLibrary.util.DataSelect;

/** 
* @author  作者:lc E-mail: lc19940628@outlook.com
* @date 创建时间：2019年6月5日 下午4:45:06 
* @version 1.0 
*/
public class SupportVectorMachineRegression {
	static{
		System.loadLibrary("SVMDLL");
	}
	/**
	 * 支持向量回归训练
	 * @param x 
	 * @param y
	 * @param dataSize 样本个数
	 * @param featureNumber 变量个数
	 * @param parameters 参数的Json字符串
	 * @return 模型的Json字符串
	 */
	private native static String SVRTrain(double[][] x,double[] y,int dataSize,int featureNumber,String parameters);
	/**
	 * 支持向量回归预测
	 * @param x
	 * @param dataSize 样本个数
	 * @param featureNumber 变量个数
	 * @param modelStr 模型的Json字符串
	 * @return 预测结果
	 */
	private native static double[] SVRPredict(double[][] x,int dataSize,int featureNumber,String modelStr);
	/**
	 * 支持向量回归训练，获取SVM模型
	 * @param Data 待计算数据
	 * @param parameter1 算法参数
	 * parameter包括：{
	 * trainCols：用户选择的数据列。（int[]）
	 * svm_type	SVM算法类型  0 -- C-SVC  1 -- nu-SVC  2 -- one-class SVM  3 -- epsilon-SVR  4 -- nu-SVR
	 * kernel_type 核函数类型  0——线性：U’*V  1——多项式：（gamma*u'*v+coef0）^degree  2——径向基函数：exp（-gamma*u-v_^2）  3——sigmoid：tanh（gamma*u'*v+coef0）
	 * gamma 多项式核函数/径向基核函数/sigmoid核函数的gamma值（默认为1/num_features）
	 * degree 多项式核函数的degree值（默认为3）
	 * coef0 多项式核函数/sigmoid核函数的coef0值（默认为0）
	 * cache_size 以MB为单位设置缓存内存大小（默认为100）
	 * eps 终止标准的公差（默认为0.001）
	 * C C-SVC、epsilon-SVR、nu-SVR的参数C（默认1）
	 * nu NU-SVC/ONE-CLASS/NU-SVR的nu值（默认值为0.5）
	 * p epsilon-SVR损失函数中的epsilon值（默认为0.1）
	 * shrinking 是否使用收缩试探法，0（否）或1（是）,（默认为1）
	 * }
	 * @return {
	 * SVRModel：支持向量回归所得模型。（String）
	 * trainCols：用户选择的数据列。（int[]）
	 * trainColsName：用户选择的数据列名。（String[]）
	 * }
	 * @return
	 */
	public Map<String, Object> train(String[][] Data,Map<String, Object> parameter1){
		Map<String, Object>trainResult=new HashMap<String, Object>();
		if(Data==null||(Data!=null && Data.length==0)){
			return null;
		}else {
			for(int i=0;i<Data.length;i++){
				if(Data[i]==null||(Data[i]!=null && Data[i].length==0)){
					return null;
				}
			}
		}
		//准备选择的数据和数据映射
		int[] selectedColumn=(int[]) parameter1.get("trainCols");
		String[][] selectedData=DataSelect.ColumnSelect(Data, selectedColumn);
		String[] selectedColumnName=DataSelect.getSelectedColumnName(Data, selectedColumn);
		int row=selectedData.length;
		int col=selectedData[0].length-1;
		//System.out.println(col);
		double xdata[][]=new double[row][col];
		double ydata[]=new double[row];
		for(int i=0;i<row;i++){
			for(int j=0;j<col;j++){
				xdata[i][j]=Double.parseDouble(selectedData[i][j]);
			}
			ydata[i]=Double.parseDouble(selectedData[i][col]);
		}

		//设置算法参数
		SVMParameter parameter=new SVMParameter();
		if(parameter1.get("svm_type")!=null){
			int svm_type= (Integer) parameter1.get("svm_type");
			parameter.setSvm_type(svm_type);
		}
		if(parameter1.get("kernel_type")!=null){
			int kernel_type= (Integer)parameter1.get("kernel_type");
			parameter.setKernel_type(kernel_type);
		}
		if(parameter1.get("gamma")!=null){
			double gamma=(Double)parameter1.get("gamma");
			parameter.setGamma(gamma);
		}
		if(parameter1.get("degree")!=null){
			int degree= (Integer)parameter1.get("degree");
			parameter.setDegree(degree);
		}
		if(parameter1.get("coef0")!=null){
			double coef0= (Double)parameter1.get("coef0");
			parameter.setCoef0(coef0);
		}
		if(parameter1.get("cache_size")!=null){
			double cache_size= (Double)parameter1.get("cache_size");
			parameter.setCache_size(cache_size);
		}
		if(parameter1.get("eps")!=null){
			double eps= (Double)parameter1.get("eps");
			parameter.setEps(eps);
		}
		if(parameter1.get("C")!=null){
			double c= (Double)parameter1.get("C");
			parameter.setC(c);
		}
		if(parameter1.get("nu")!=null){
			double nu= (Double)parameter1.get("nu");
			parameter.setNu(nu);
		}
		if(parameter1.get("p")!=null){
			double p= (Double)parameter1.get("p");
			parameter.setP(p);
		}
		if(parameter1.get("shrinking")!=null){
			int shrinking= (Integer)parameter1.get("shrinking");
			parameter.setShrinking(shrinking);
		}
		//因算法参数太多，将算法参数转为json字符串进行传参
		String paramString=SVMParameter.ParameterToJsonString(parameter);
		String model=SVRTrain(xdata,ydata,row,col,paramString);
		trainResult.put("trainColsName", selectedColumnName);
		trainResult.put("trainCols",selectedColumn);
		trainResult.put("SVRModel", model);
		return trainResult;	
	}
	/**
	 * 根据支持向量回归模型进行预测
	 * @param Data 待预测数据。
	 * @param parameter 预测所需参数。
	 * parameter包括：{
	 * SVRModel：支持向量回归所得模型。（String）
	 * trainCols：用户选择的数据列。（int[]）
	 * }
	 * @return {
	 * predictResult：预测得出数据结果。（double[]）
	 * }
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> predict(String[][] Data, Map<String, Object> parameter) {
		//最终预测结果
		Map<String, Object> Result=new HashMap<String, Object>();
		
		//训练数据的列选择
		int[] selectedColumn1=(int[]) parameter.get("trainCols");
		int[]selectedCols = new int[selectedColumn1.length-1];
		for(int i=0; i<selectedCols.length;i++) {
			selectedCols[i] = selectedColumn1[i];//预测数据的列选择比训练数据的列选择少最后一列
		}
		String[][] selectedData1=DataSelect.ColumnSelect(Data, selectedCols);
		double data[][] = DataConverter.String2DtoDouble2D(selectedData1);//预测数据转换为int
		String model = (String) parameter.get("SVRModel");
		//进行预测
		double[] predictResult=SVRPredict(data, data.length, data[0].length, model);
		Result.put("predictResult", predictResult);
		return Result;
	}	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int trainSize=2401;
		int testSize=301;
		int featureSize=8;
		String [][] Data = new String [trainSize][featureSize]; 
		String[][] TestData=new String[testSize][featureSize-1];
		try {
			BufferedReader reader = new BufferedReader(new FileReader("PRSA_data_2010.1.1-2014.12.31_train.csv"));// 换成你的文件名
			String line = null;
			int i = 0;
			while ((line = reader.readLine()) != null&&i<trainSize) {
				String item[] = line.split(",");// CSV格式文件为逗号分隔符文件，这里根据逗号切分
				// System.out.println("第"+i+"行");
				for (int j = 0; j < featureSize; j++) {
					Data[i][j] = item[j];// 这就是你要的数据了
				}
				i++;
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		try {
			BufferedReader reader = new BufferedReader(new FileReader("PRSA_data_2010.1.1-2014.12.31_test.csv"));// 换成你的文件名
			String line = null;
			int i = 0;
			while ((line = reader.readLine()) != null&&i<testSize) {
				String item[] = line.split(",");// CSV格式文件为逗号分隔符文件，这里根据逗号切分
				// System.out.println("第"+i+"行");
				for (int j = 0; j < featureSize-1; j++) {
					TestData[i][j] = item[j];// 这就是你要的数据了
				}
				i++;
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	       int svm_type = 3;
			int kernel_type = 2;
			double gamma = 0.001;
			int degree = 1;
			double coef0 = 0.1;
			double cache_size = 200;
			double eps = 0.01;
			double c = 2;
			double nu = 0.02;
			double p = 0.2;
			int shrinking = 1;
			int []selectCols = {1,1,1,1,1,1,1,1};
			Map<String, Object>Parameter1 = new HashMap<String, Object>();
			Parameter1.put("svm_type",svm_type);
			Parameter1.put("kernel_type",kernel_type);
			Parameter1.put("gamma",gamma);
			Parameter1.put("degree",degree);
			Parameter1.put("coef0",coef0);
			Parameter1.put("cache_size",cache_size);
			Parameter1.put("eps",eps);
			Parameter1.put("C",c);
			Parameter1.put("nu",nu);
			Parameter1.put("p",p);
			Parameter1.put("shrinking",shrinking);
			Parameter1.put("trainCols",selectCols);
			SupportVectorMachineRegression SVM = new SupportVectorMachineRegression();
			Map<String,Object> result=SVM.train(Data ,Parameter1);
			System.out.println(result);
			Map<String,Object> predictResult=SVM.predict(TestData,result);
			double[] cr2 = (double[])predictResult.get("predictResult");
			System.out.println("输出测试数据的预测结果：");
			for (int i = 0; i < cr2.length; i++) {
				System.out.println(cr2[i]);
			}
	}
	
	public static void mmain(String[] args) {
		// TODO Auto-generated method stub
		DataSelect.SplitDataSets("D:\\Data\\MachineLearningData\\PRSA_data_2010.1.1-2014.12.31.csv", 0.3);
		System.out.println(123);
		
//		double Data[][]=readData("D:\\JavaProjects\\workspace\\MachineLearningLibrary\\iris.csv");
//		int dataSize=Data.length;
//		int FeatureNum=Data[0].length-1;
//		double x[][]=new double[dataSize][FeatureNum];
//		double y[]=new double[dataSize];
//		for(int i=0;i<dataSize;i++){
//			y[i]=Data[i][0];
//			for(int j=0;j<FeatureNum;j++){
//				x[i][j]=Data[i][j+1];
//			}
//		}
//		double test[][]={{5.1,3.5,1.4,0.3},{6.1,2.8,4.7,1.2},{7.7,2.8,6.7,2}};
////		double x[][]={{1,1,1},{1,1,1},{4,8,4},{4,8,4},{1,1,1},{1,1,1},{4,8,4},{4,8,4}};
////		double y[]={1,1,2,2,1,1,2,2};
////		double test[][]={{4,8,4},{1,1,1},{108,108,112}};
//		SVMParameter parameter=new SVMParameter();
//		parameter.setSvm_type(3);
//		parameter.setKernel_type(2);
//		parameter.setGamma(0.0001);
//		parameter.setDegree(3);
//		parameter.setCoef0(0.1);
//		parameter.setCache_size(200);
//		parameter.setC(2);
//		parameter.setEps(0.00001);
//		parameter.setNu(0.02);
//		parameter.setNr_weight(3);
//		int label[]={1,2,3};
//		parameter.setWeight_label(label);
//		double weight[]={1,2,3};
//		parameter.setWeight(weight);
//		parameter.setP(0.2);
//		parameter.setShrinking(1);
//		parameter.setProbability(0);
//		String paramString=SVMParameter.ParameterToJsonString(parameter);
//		String str=SVRTrain(x,y,dataSize,FeatureNum,paramString);
//		System.out.println(str);
//		double[] result=SVRPredict(test, 3, FeatureNum, str);
//		for(int i=0;i<result.length;i++){
//			System.out.println(result[i]);
//		}
	}
	/**
	 * 从文件读数
	 * @param filepath
	 * @return
	 */
	private static double[][] readData(String filepath){
		Vector<Vector<Double>> input=new Vector<Vector<Double>>();
		File file = new File(filepath); 
        InputStreamReader reader;
		try {
			reader = new InputStreamReader(  
			        new FileInputStream(file));
			BufferedReader br = new BufferedReader(reader); 
	        String line = "";  
	        line = br.readLine();  

	        while (line != null) {  
		        Vector<Double> lineVector=new Vector<Double>();
	            String[] str=line.split(",");
	            for(int i=0;i<str.length;i++)
	            	lineVector.add(Double.valueOf(str[i]));
	            input.add(lineVector);
	            line = br.readLine(); 
	            
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
		double[][] data=new double[input.size()][input.get(0).size()];
		for(int i=0;i<input.size();i++){
			for(int j=0;j<input.get(i).size();j++){
				data[i][j]=input.get(i).get(j);
			}
		}
        return data;
	}

}
