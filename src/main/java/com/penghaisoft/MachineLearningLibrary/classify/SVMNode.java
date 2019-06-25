package com.penghaisoft.MachineLearningLibrary.classify;
/** 
* @author  作者:lc E-mail: lc19940628@outlook.com
* @date 创建时间：2019年6月10日 下午3:15:06 
* @version 1.0 
*/
public class SVMNode{
	private int index;
	private double value;
	public SVMNode(){
		setIndex(0);
		setValue(0);
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
}
