//package com.penghaisoft.MachineLearningLibrary;
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import com.penghaisoft.MachineLearningLibrary.classify.MulClassLogisticRegression;
//import com.penghaisoft.MachineLearningLibrary.regression.*;
//
///**
// * Hello world!
// *
// */
//public class App 
//{
//    public static void main( String[] args )
//    {
//        System.out.println( "Hello World!" );
//        testMultipleLinearRegression();
////        testMultipleLogisticRegression();
////        testPolynomialRegression();
//    }
//
//    public static void testMultipleLinearRegression(){
//    	double data[][] = {
//				{ 2104, 3, 399900 },
//				{ 1600, 3, 329900 },
//				{ 2400, 3, 369000 },
//				{ 1416, 2, 232000 },
//				{ 3000, 4, 539900 },
//				{ 1985, 4, 299900 },
//				{ 1534, 3, 314900 },
//				{ 1427, 3, 198999 },
//				{ 1380, 3, 212000 },
//				{ 1494, 3, 242500 }
//			};
//    	ArrayList<ArrayList<Double>> Data=new ArrayList<ArrayList<Double>>();
//    	for(int i=0;i<10;i++){
//    		ArrayList<Double> d=new ArrayList<Double>();
//    		for(int j=0;j<3;j++){
//    			d.add(data[i][j]);
//    		}
//    		Data.add(d);
//    	}
//    	
//		MultipleLinearRegression regression=new MultipleLinearRegression();
//		
//		ArrayList<Double> factor=regression.Regression(Data, 10, 3,1);
//		System.out.println("factor:");
//		for (int i = 0; i < factor.size(); i++) {
//			System.out.println(factor.get(i));
//		}
//		Map<String, Double> indicators=regression.getIndicators();
//		System.out.println("indicator:");
//		for (String key : indicators.keySet()) {
//			System.out.println(key+":"+indicators.get(key));
//		}
//    }
//
////    public static void testMultipleLogisticRegression(){
////    	MultipleLogisticRegression regression=new MultipleLogisticRegression();
////		loadData("D:\\Data\\letter-recognition.data", 20000, 16, 26, "A");
////
////		List<List<Double>> xArrayLists=new ArrayList<List<Double>>();
////		List<Integer> yArrayList=new ArrayList<Integer>();
////		for(int i=0;i<20000;i++){
////			ArrayList<Double> xArrayList=new ArrayList<Double>();
////			for(int j=0;j<16;j++){
//////				System.out.print(xData[i][j]+" ");
////				xArrayList.add(xData[i][j]);
////			}
//////			System.out.println();
////			xArrayLists.add(xArrayList);
////		}
////		for(int i=0;i<20000;i++){
////			yArrayList.add(yData[i]);
////		}
//////		System.out.println("0".getBytes()[0]);
////			
////		
////		ArrayList<ArrayList<Double>> theta=regression.Regression(xArrayLists, yArrayList, 16, 20000, 26, 100, 0.1);;
////		
////		for (int i = 0; i<26; i++) {
////			for (int j = 0; j<16; j++) {
////				System.out.print(theta.get(i).get(j)+",");			
////			}
////			System.out.println();
////		}
////		double[][] testdata={
////
////				{ 5, 10, 6, 8, 4, 7, 7, 12, 2, 7, 9, 8, 9, 6, 0, 8 },//M
////
////				{ 6, 12, 7, 6, 5, 8, 8, 3, 3, 6, 9, 7, 10, 10, 3, 6 },//W
////
////				{ 3, 8, 4, 6, 4, 7, 7, 12, 1, 6, 6, 8, 5, 8, 0, 8 },//N
////
////				{ 1, 0, 1, 0, 0, 7, 8, 10, 1, 7, 5, 8, 2, 8, 0, 8 },//H
////
////				{ 3, 6, 5, 5, 6, 6, 8, 3, 3, 6, 5, 9, 6, 7, 5, 9 },//R
////
////				{ 7, 11, 11, 8, 7, 4, 8, 2, 9, 10, 11, 9, 5, 8, 5, 4 }, //X
////
////				{ 6, 9, 6, 4, 4, 8, 9, 5, 3, 10, 5, 5, 5, 10, 5, 6 },//P	
////
////				{ 4, 7, 6, 5, 5, 8, 5, 7, 4, 6, 7, 9, 3, 7, 6, 9 }//Q
////
////			};
////		for(int i=0;i<8;i++){
////			ArrayList<Double> testX=new ArrayList<Double>();
////			for(int j=0;j<16;j++){
////				testX.add(testdata[i][j]);
////			}
////			System.out.println((char)(regression.Predict(testX,theta,26,16)+"A".getBytes()[0]));
////		}
////
////		
////    }
//	private static double[][] xData;
//	private static int[] yData;
//    private static void loadData(String filepath,int dataSize,int xSize,int classSize,String firstClass){
//		xData=new double[dataSize][xSize];
//		yData=new int[dataSize];
//
//		File file = new File(filepath); 
//        InputStreamReader reader;
//		try {
//			reader = new InputStreamReader(  
//			        new FileInputStream(file));
//			BufferedReader br = new BufferedReader(reader); 
//	        String line = "";  
//	        int index=0;
//	        line = br.readLine();  
//	        while (line != null) {  
//	            String[] str=line.split(",");
//	            int y=str[0].getBytes()[0]-firstClass.getBytes()[0];
//	            yData[index]=y;
//	            for(int i=1;i<str.length;i++){
//	            	xData[index][i-1]=Double.valueOf(str[i]);
//	            	
//	            }
//	            line = br.readLine(); 
//	            index++;
//	            if(index>=dataSize) break;
//	        }  
//	        reader.close();
//	        br.close();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}   
//
//	}
//    public void testPolynomialRegression(){
//    	ArrayList<Double> x=new ArrayList<Double>();
//		ArrayList<Double> y=new ArrayList<Double>();
//		for(int i=0;i<10;i++){
//			x.add((double) i);
//			y.add(5.33+4.3*i+234*i*i+43.76*i*i*i);
//			System.out.println(5.33+4.3*i+234*i*i+43.76*i*i*i);
//		}
//		PolynomialRegression regression=new PolynomialRegression();
//		
//		System.out.println("factor:");
//		ArrayList<Double> factor=regression.Regression(x, y, 10, 3);
//		System.out.print("Y=");
//		for(int i=0;i<4;i++){
//			System.out.print(factor.get(i));
//			for(int j=0;j<i;j++){
//				System.out.print("*x");
//			}
//			if(i!=3){
//				System.out.print("+");
//			}
//		}
//		System.out.println();
//		System.out.println("fitedYs:");
//		ArrayList<Double> fitedYs=regression.getFitedYs(10);
//		
//		for(int i=0;i<10;i++)
//			System.out.println(fitedYs.get(i));
//    }
//}
