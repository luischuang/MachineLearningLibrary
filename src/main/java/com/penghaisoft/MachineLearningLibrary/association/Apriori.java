package com.penghaisoft.MachineLearningLibrary.association;


public class Apriori {

	static{
		System.loadLibrary("AprioriDLL");
	}
//	private static native void test1(String[][] str);
//	private static native FrequentItemSet[] test2(String[][] str);
//	private static native AssociationRule[] test3(String[][] str);
	/**
	 * 运行Apriori的c++算法
	 * @param data 数据
	 * @param minSupport 最小支持度
	 * @param minConfidence 最小置信度
	 */
	private static native void run(String[][] data,float minSupport,float minConfidence);
	/**
	 * 获取频繁项集
	 * @return
	 */
	private static native FrequentItemSet[] getFrequentItemSets();
	/**
	 * 获取关联规则
	 * @return
	 */
	private static native AssociationRule[] getAssociationRules();
//	Map<Vector<String>, Float>
//	Map<Vector<Vector<String> >, Float>
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Apriori apriori=new Apriori();

		String[][] strings={{"1","2"},{"1","2"}};
		float minSupport=0;
		float minConfidence=0;
		Apriori.run(strings, minSupport, minConfidence);
		FrequentItemSet[] frequentItemSets=getFrequentItemSets();
		AssociationRule[] associationRules=getAssociationRules();

		
		
		System.out.println(123);
	}

}
