package com.penghaisoft.MachineLearningLibrary.classify;

import java.util.ArrayList;
import javafx.util.Pair;

public class ID3Model {
	private int index;//当前节点样本最大增益对应第index个属性，根据这个进行分类的  
	private int type;//当前节点的类型  
	private int nextSize;//当前节点的后继节点集合
	private ArrayList<ArrayList<Pair<Integer,Integer>>> sample;//未分类的样本集合  
	private int sampleSize;
	public ID3Model(){
		
	}
	public ID3Model(int index, int type, int next, ArrayList<ArrayList<Pair<Integer,Integer>>> sample,int sampleSize){
		this.setIndex(index);
		this.setType(type);
		this.setNextSize(next);
		this.setSample(sample);
		this.setSampleSize(sampleSize);
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getNextSize() {
		return nextSize;
	}
	public void setNextSize(int nextSize) {
		this.nextSize = nextSize;
	}
	public ArrayList<ArrayList<Pair<Integer,Integer>>> getSample() {
		return sample;
	}
	public void setSample(ArrayList<ArrayList<Pair<Integer,Integer>>> sample) {
		this.sample = sample;
	}
	public int getSampleSize() {
		return sampleSize;
	}
	public void setSampleSize(int sampleSize) {
		this.sampleSize = sampleSize;
	}
}
