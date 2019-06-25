package com.penghaisoft.MachineLearningLibrary.util;
/**
 * 数据转换工具类
 * @author lc
 *
 */
public class DataConverter {

	/**
	 * String二维数组转化为double二维数组
	 * @param strings 待转化的String二维数组
	 * @return double二维数组
	 */
	public static double[][] String2DtoDouble2D(String[][] strings) {
		if(strings==null||(strings!=null && strings.length==0)){
			return null;
		}else if(strings[0]==null||(strings[0]!=null && strings[0].length==0)){
			return null;
		}
		
		double[][] datas=new double[strings.length][strings[0].length];
		for(int i=0;i<strings.length;i++){
			for(int j=0;j<strings[0].length;j++){
				if(strings[i][j] == "") {
					datas[i][j] = 3.4E38;
				}
				else {
					datas[i][j]=Double.parseDouble(strings[i][j]);
				}
				}
		}

		return datas;
	}
	/**
	 * String一维数组转化为double一维数组
	 * @param strings 待转化的String一维数组
	 * @return double一维数组
	 */
	public static double[] String1DtoDouble1D(String[] strings) {
		if(strings==null||(strings!=null && strings.length==0)){
			return null;
		}
		
		double[] datas=new double[strings.length];
		for(int i=0;i<strings.length;i++){
			if(strings[i] == "") {
				datas[i] = 3.4E38;
			}
			else {
				datas[i]=Double.parseDouble(strings[i]);
			}
			}

		return datas;
	}
	/**
	 * String二维数组转化为float二维数组
	 * @param strings 待转化的String二维数组
	 * @return float二维数组
	 */
	public static float[][] String2DtoFloat2D(String[][] strings) {
		if(strings==null||(strings!=null && strings.length==0)){
			return null;
		}else if(strings[0]==null||(strings[0]!=null && strings[0].length==0)){
			return null;
		}
		
		float[][] datas=new float[strings.length][strings[0].length];
		for(int i=0;i<strings.length;i++){
			for(int j=0;j<strings[0].length;j++){
				datas[i][j]=Float.parseFloat(strings[i][j]);
			}
		}

		return datas;
	}
	/**
	 * String一维数组转化为float一维数组
	 * @param strings 待转化的String一维数组
	 * @return float一维数组
	 */
	public static float[] String1DtoFloat1D(String[] strings) {
		if(strings==null||(strings!=null && strings.length==0)){
			return null;
		}
		
		float[] datas=new float[strings.length];
		for(int i=0;i<strings.length;i++){
			datas[i]=Float.parseFloat(strings[i]);
		}

		return datas;
	}
	/**
	 * String二维数组转化为int二维数组
	 * @param strings 待转化的String二维数组
	 * @return int二维数组
	 */
	public static int[][] String2DtoInt2D(String[][] strings) {
		if(strings==null||(strings!=null && strings.length==0)){
			return null;
		}else if(strings[0]==null||(strings[0]!=null && strings[0].length==0)){
			return null;
		}
		
		int[][] datas=new int[strings.length][strings[0].length];
		for(int i=0;i<strings.length;i++){
			for(int j=0;j<strings[0].length;j++){
				datas[i][j]=Integer.parseInt(strings[i][j]);
			}
		}

		return datas;
	}
	/**
	 * String一维数组转化为int一维数组
	 * @param strings 待转化的String一维数组
	 * @return int一维数组
	 */
	public static int[] String1DtoInt1D(String[] strings) {
		if(strings==null||(strings!=null && strings.length==0)){
			return null;
		}
		
		int[] datas=new int[strings.length];
		for(int i=0;i<strings.length;i++){
			datas[i]=Integer.parseInt(strings[i]);
		}

		return datas;
	}

	
	/**
	 * Double二维数组转为String二维数组
	 * @param datas 待转换的String二维数组
	 * @return String二维数组
	 */
	public static String[][] Double2DtoString2D(double[][] datas) {
		int row = datas.length;
		int col = datas[0].length;
		String[][]strings = new String[row][col];
		for(int i=0; i<row; i++) {
			for(int j=0; j<col; j++) {
				strings[i][j] = String.valueOf(datas[i][j]);
			}
			
		}
		
		return strings; 
	}
}
