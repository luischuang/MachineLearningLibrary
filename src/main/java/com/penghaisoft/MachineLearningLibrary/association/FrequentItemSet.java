package com.penghaisoft.MachineLearningLibrary.association;
/**
 * 频繁项集
 * @author lc199
 *
 */
public class FrequentItemSet {

	/**
	 * 频繁项集
	 */
	private String[] itemSet;
	/**
	 * 支持度
	 */
	private float suportDegree;
	public FrequentItemSet(){
		
	}
	public FrequentItemSet(float suportDegree,String[] itemSet){
		this.itemSet=itemSet;
		this.suportDegree=suportDegree;
	}
	public String[] getItemSet() {
		return itemSet;
	}
	public void setItemSet(String[] itemSet) {
		this.itemSet = itemSet;
	}
	public float getSuportDegree() {
		return suportDegree;
	}
	public void setSuportDegree(float suportDegree) {
		this.suportDegree = suportDegree;
	}
	
}
