package com.penghaisoft.MachineLearningLibrary.association;

import java.util.Vector;

/**
 * 关联规则
 * @author lc199
 *
 */
public class AssociationRule {
	/**
	 * 先导
	 */
	private String[] antecedent;
	/**
	 * 后继
	 */
	private String[] consequent;
	/**
	 * 置信度
	 */
	private float confidenceDegee;
	public AssociationRule(){
		
	}
	public AssociationRule(float confidenceDegee,String[] antecedent,String[] consequent){
		this.confidenceDegee=confidenceDegee;
		this.antecedent=antecedent;
		this.consequent=consequent;
	}

	public float getConfidenceDegee() {
		return confidenceDegee;
	}
	public void setConfidenceDegee(float confidenceDegee) {
		this.confidenceDegee = confidenceDegee;
	}
	public String[] getAntecedent() {
		return antecedent;
	}
	public void setAntecedent(String[] antecedent) {
		this.antecedent = antecedent;
	}
	public String[] getConsequent() {
		return consequent;
	}
	public void setConsequent(String[] consequent) {
		this.consequent = consequent;
	}
}
