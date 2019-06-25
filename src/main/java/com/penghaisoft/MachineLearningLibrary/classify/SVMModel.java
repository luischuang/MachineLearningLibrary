package com.penghaisoft.MachineLearningLibrary.classify;
import com.penghaisoft.MachineLearningLibrary.classify.SVMParameter;

import java.util.Iterator;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
/** 
* @author  作者:lc E-mail: lc19940628@outlook.com
* @date 创建时间：2019年5月16日 上午10:16:46 
* @version 1.0 
*/
public class SVMModel {
	
	private SVMParameter param;	/* parameter */
	private int nr_class;		/* number of classes, = 2 in regression/one class svm */
	private int l;			/* total #SV */
	private SVMNode[][] SV;		/* SVs (SV[l]) */
	private double[][] sv_coef;	/* coefficients for SVs in decision functions (sv_coef[k-1][l]) */
	private double[] rho;		/* constants in decision functions (rho[k*(k-1)/2]) */
	private double[] probA;		/* pariwise probability information */
	private double[] probB;
	private int[] sv_indices;        /* sv_indices[0,...,nSV-1] are values in [1,...,num_traning_data] to indicate SVs in the training set */

	/* for classification only */

	private int[] label;		/* label of each class (label[k]) */
	private int[] nSV;		/* number of SVs for each class (nSV[k]) */
				/* nSV[0] + nSV[1] + ... + nSV[k-1] = l */
	/* XXX */
	private int free_sv;		/* 1 if svm_model is created by svm_load_model*/
				/* 0 if svm_model is created by svm_train */
	public SVMModel(){
		
	}
	private static SVMNode[][] JsonStringToSV(String SVMNodeStr){
		JSONArray SVStrArray = JSONArray.parseArray(SVMNodeStr);
		int SVnum=SVStrArray.size();
		int NodeNum=JSONObject.parseObject(SVStrArray.get(0).toString()).keySet().size();
		SVMNode[][] SVs=new SVMNode[SVnum][];
		for(int i=0;i<SVnum;i++){
			JSONObject o=JSONObject.parseObject(SVStrArray.get(0).toString());
			Set<String>keys=o.keySet();
			Iterator<String>iterator = keys.iterator();
			int j=0;
			SVMNode[] SV=new SVMNode[NodeNum];
			while(iterator.hasNext()){
				String indexStr=iterator.next();
				SV[j]=new SVMNode();
				SV[j].setIndex(Integer.parseInt(indexStr));
				SV[j].setValue(o.getDoubleValue(indexStr));
				j++;
			}
			SVs[i]=SV;
		}
		return SVs;
	}
	private static double[][] JsonStringToSVcoef(String SVcoefStr){
		JSONArray SVStrArray = JSONArray.parseArray(SVcoefStr);
		int SVnum=SVStrArray.size();
		int coefNum=JSONObject.parseObject(SVStrArray.get(0).toString()).keySet().size();
		double[][] SVcoefs=new double[SVnum][];
		for(int i=0;i<SVnum;i++){
			JSONObject o=JSONObject.parseObject(SVStrArray.get(0).toString());
			Set<String>keys=o.keySet();
			Iterator<String>iterator = keys.iterator();
			int j=0;
			double[] SVcoef=new double[coefNum];
			while(iterator.hasNext()){
				SVcoef[j]=Double.parseDouble(iterator.next());
				j++;
			}
			SVcoefs[i]=SVcoef;
		}
		return SVcoefs;
	}
	private static String ModelToJsonString(SVMModel model){
		JSONObject object=new JSONObject();
		object.put("param", model.getParam());
		object.put("nr_class", model.getNr_class());
		object.put("l", model.getL());
		object.put("SV", model.getSV());
		object.put("sv_coef", model.getSv_coef());
		object.put("rho", model.getRho());
		object.put("probA", model.getProbA());
		object.put("probB", model.getProbB());
		object.put("sv_indices", model.getSv_indices());
		object.put("label", model.getLabel());
		object.put("nSV", model.getnSV());
		object.put("free_sv", model.getFree_sv());
		return object.toJSONString();
	}
	private static SVMModel JsonStringToModel(String modelStr) {
		SVMModel model=new SVMModel();
		
		JSONObject object=JSONObject.parseObject(modelStr);
		SVMParameter parameter=SVMParameter.JsonStringToParameter(object.get("param").toString());
		model.setParam(parameter);
		
		model.setNr_class(object.getIntValue("nr_class"));
		
		model.setL(object.getIntValue("l"));
		
		model.setSV(JsonStringToSV(object.getJSONArray("SV").toJSONString()));
		
		model.setSv_coef(JsonStringToSVcoef(object.get("sv_coef").toString()));
		
		JSONArray RhoJsonArray=object.getJSONArray("rho");
		double[] Rho=new double[RhoJsonArray.size()];
		for(int i=0;i<Rho.length;i++){
			Rho[i]=RhoJsonArray.getDoubleValue(i);
		}
		model.setRho(Rho);
		
		JSONArray ProbAJsonArray=object.getJSONArray("probA");
		double[] ProbA=new double[ProbAJsonArray.size()];
		for(int i=0;i<ProbA.length;i++){
			ProbA[i]=ProbAJsonArray.getDoubleValue(i);
		}
		model.setProbA(ProbA);
		
		JSONArray ProbBJsonArray=object.getJSONArray("probB");
		double[] ProbB=new double[ProbBJsonArray.size()];
		for(int i=0;i<ProbB.length;i++){
			ProbB[i]=ProbBJsonArray.getDoubleValue(i);
		}
		model.setProbB(ProbB);
		
		JSONArray SvIndicesJsonArray=object.getJSONArray("sv_indices");
		int[] sv_indices=new int[SvIndicesJsonArray.size()];
		for(int i=0;i<sv_indices.length;i++){
			sv_indices[i]=SvIndicesJsonArray.getIntValue(i);
		}
		model.setSv_indices(sv_indices);
		
		JSONArray LabelJsonArray=object.getJSONArray("label");
		int[] label=new int[LabelJsonArray.size()];
		for(int i=0;i<label.length;i++){
			label[i]=LabelJsonArray.getIntValue(i);
		}
		model.setLabel(label);
		
		JSONArray nSVJsonArray=object.getJSONArray("nSV");
		int[] nSV=new int[nSVJsonArray.size()];
		for(int i=0;i<nSV.length;i++){
			nSV[i]=nSVJsonArray.getIntValue(i);
		}
		model.setnSV(nSV);
		
		model.setFree_sv(object.getIntValue("free_sv"));
		return model;
	}
	public SVMParameter getParam() {
		return param;
	}
	public void setParam(SVMParameter param) {
		this.param = param;
	}
	public int getNr_class() {
		return nr_class;
	}
	public void setNr_class(int nr_class) {
		this.nr_class = nr_class;
	}
	public int getL() {
		return l;
	}
	public void setL(int l) {
		this.l = l;
	}
	public SVMNode[][] getSV() {
		return SV;
	}
	public void setSV(SVMNode[][] sV) {
		SV = sV;
	}
	public double[][] getSv_coef() {
		return sv_coef;
	}
	public void setSv_coef(double[][] sv_coef) {
		this.sv_coef = sv_coef;
	}
	public double[] getRho() {
		return rho;
	}
	public void setRho(double[] rho) {
		this.rho = rho;
	}
	public double[] getProbA() {
		return probA;
	}
	public void setProbA(double[] probA) {
		this.probA = probA;
	}
	public double[] getProbB() {
		return probB;
	}
	public void setProbB(double[] probB) {
		this.probB = probB;
	}
	public int[] getSv_indices() {
		return sv_indices;
	}
	public void setSv_indices(int[] sv_indices) {
		this.sv_indices = sv_indices;
	}
	public int[] getLabel() {
		return label;
	}
	public void setLabel(int[] label) {
		this.label = label;
	}
	public int[] getnSV() {
		return nSV;
	}
	public void setnSV(int[] nSV) {
		this.nSV = nSV;
	}
	public int getFree_sv() {
		return free_sv;
	}
	public void setFree_sv(int free_sv) {
		this.free_sv = free_sv;
	}
	
}
