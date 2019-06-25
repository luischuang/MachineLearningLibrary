package com.penghaisoft.MachineLearningLibrary.classify;


import com.alibaba.fastjson.JSONObject;

/** 
* @author  作者:lc E-mail: lc19940628@outlook.com
* @date 创建时间：2019年5月16日 上午10:08:34 
* @version 1.0 
*/
public class SVMParameter {
	/**
	 * SVM算法类型
	 * 	0 -- C-SVC
	 *  1 -- nu-SVC
	 *  2 -- one-class SVM
	 *  3 -- epsilon-SVR
	 *  4 -- nu-SVR
	 */
	private int svm_type;
	/**
	 * 核函数类型
	 * 0——线性：U’*V
	 * 1——多项式：（gamma*u'*v+coef0）^degree
	 * 2——径向基函数：exp（-gamma*u-v_^2）
	 * 3——sigmoid：tanh（gamma*u'*v+coef0）
	 */
	private int kernel_type;
	/**
	 * 多项式核函数的degree值（默认为3）
	 */
	private int degree;	/* for poly */
	/**
	 * 多项式核函数/径向基核函数/sigmoid核函数的gamma值（默认为1/num_features）
	 */
	private double gamma;	/* for poly/rbf/sigmoid */
	/**
	 * 多项式核函数/sigmoid核函数的coef0值（默认为0）
	 */
	private double coef0;	/* for poly/sigmoid */

	/**
	 * 以MB为单位设置缓存内存大小（默认为100）
	 */
	private double cache_size; /* in MB */
	/**
	 * 终止标准的公差（默认为0.001）
	 */
	private double eps;	/* stopping criteria */
	/**
	 * C-SVC、epsilon-SVR、nu-SVR的参数C（默认1）
	 */
	private double C;	/* for C_SVC, EPSILON_SVR and NU_SVR */
	/**
	 * C-SVC的类别权重（默认为0）
	 */
	private int nr_weight;		/* for C_SVC */
	/**
	 * C-SVC的类别权重（默认为NULL）
	 */
	private int[] weight_label;	/* for C_SVC */
	/**
	 * C-SVC的样本权重（默认为NULL）
	 */
	private double[] weight;		/* for C_SVC */
	/**
	 * NU-SVC/ONE-CLASS/NU-SVR的nu值（默认值为0.5）
	 */
	private double nu;	/* for NU_SVC, ONE_CLASS, and NU_SVR */
	/**
	 * epsilon-SVR损失函数中的epsilon值（默认为0.1）
	 */
	private double p;	/* for EPSILON_SVR */
	/**
	 * 是否使用收缩试探法，0（否）或1（是）,（默认为1）
	 */
	private int shrinking;	/* use the shrinking heuristics */
	/**
	 * 是否训练SVC或SVR模型进行概率估计，0（否）或1（是）,（默认为0）
	 */
	private int probability; /* do probability estimates */
	public SVMParameter(){
		// 默认参数
		setSvm_type(1);        //算法类型
		setKernel_type(1);    //核函数类型
		setDegree(3);    //多项式核函数的参数degree
		setCoef0(0);    //多项式核函数的参数coef0
		setGamma(0.5);    //1/num_features，rbf核函数参数
		setNu(0.5);        //nu-svc的参数
		setC(1);        //正则项的惩罚系数
		setEps(1e-3);    //收敛精度
		setCache_size(100);    //求解的内存缓冲 100MB
		setP(0.1);   //epsilon-SVR损失函数中的epsilon值（默认为0.1）
		setShrinking(1);  //是否使用收缩试探法，0（否）或1（是）,（默认为1）
		setProbability(0);    //1表示训练时生成概率模型，0表示训练时不生成概率模型，用于预测样本的所属类别的概率
		setNr_weight(0);    //类别权重
		setWeight(null);    //样本权重
		setWeight_label(null);    //类别权重
	}
	public static String ParameterToJsonString(SVMParameter parameter){
		JSONObject object=new JSONObject();
		object.put("svm_type", parameter.getSvm_type());
		object.put("kernel_type", parameter.getKernel_type());
		object.put("degree", parameter.getDegree());
		object.put("gamma", parameter.getGamma());
		object.put("coef0", parameter.getCoef0());
		object.put("cache_size", parameter.getCache_size());
		object.put("eps", parameter.getEps());
		object.put("C", parameter.getC());
		object.put("nr_weight", parameter.getNr_weight());
		object.put("weight_label", parameter.getWeight_label());
	    object.put("weight",parameter.getWeight());
	    object.put("nu", parameter.getNu());
	    object.put("p", parameter.getP());
	    object.put("shrinking", parameter.getShrinking());
	    object.put("probability", parameter.getProbability());
		return object.toJSONString();
	}
	public static SVMParameter JsonStringToParameter(String paramStr){
		SVMParameter parameter=new SVMParameter();
		JSONObject object=JSONObject.parseObject(paramStr);
		parameter.setSvm_type(object.getIntValue("svm_type"));
		parameter.setKernel_type(object.getIntValue("kernel_type"));
		parameter.setDegree(object.getIntValue("degree"));
		parameter.setGamma(object.getDoubleValue("gamma"));
		parameter.setCoef0(object.getDoubleValue("coef0"));
		parameter.setCache_size(object.getDoubleValue("cache_size"));
		parameter.setEps(object.getDoubleValue("eps"));
		parameter.setC(object.getDoubleValue("C"));
		parameter.setNr_weight(object.getIntValue("nr_weight"));
		parameter.setWeight_label((int[])object.get("weight_label"));
		parameter.setWeight((double[])object.get("weight"));
		parameter.setNu(object.getDoubleValue("nu"));
		parameter.setP(object.getDoubleValue("p"));
		parameter.setShrinking(object.getIntValue("shrinking"));
		parameter.setProbability(object.getIntValue("probability"));
		return parameter;
	}
	public int getSvm_type() {
		return svm_type;
	}
	/**
	 * 设置SVM算法类型
	 * 0 -- C-SVC
	 *  1 -- nu-SVC
	 *  2 -- one-class SVM
	 *  3 -- epsilon-SVR
	 *  4 -- nu-SVR
	 */
	public void setSvm_type(int svm_type) {
		this.svm_type = svm_type;
	}
	public int getKernel_type() {
		return kernel_type;
	}
	/**
	 * 设置核函数类型
	 * 0——线性：U’*V
	 * 1——多项式：（gamma*u'*v+coef0）^degree
	 * 2——径向基函数：exp（-gamma*u-v_^2）
	 * 3——sigmoid：tanh（gamma*u'*v+coef0）
	 */
	public void setKernel_type(int kernel_type) {
		this.kernel_type = kernel_type;
	}
	public int getDegree() {
		return degree;
	}
	/**
	 * 多项式核函数的degree值（默认为3）
	 */
	public void setDegree(int degree) {
		this.degree = degree;
	}
	public double getGamma() {
		return gamma;
	}
	/**
	 * 多项式核函数/径向基核函数/sigmoid核函数的gamma值（默认为1/num_features）
	 */
	public void setGamma(double gamma) {
		this.gamma = gamma;
	}
	public double getCoef0() {
		return coef0;
	}
	/**
	 * 多项式核函数/sigmoid核函数的coef0值（默认为0）
	 */
	public void setCoef0(double coef0) {
		this.coef0 = coef0;
	}
	public double getCache_size() {
		return cache_size;
	}
	/**
	 * 以MB为单位设置缓存内存大小（默认为100）
	 */
	public void setCache_size(double cache_size) {
		this.cache_size = cache_size;
	}
	public double getEps() {
		return eps;
	}
	/**
	 * 终止标准的公差（默认为0.001）
	 */
	public void setEps(double eps) {
		this.eps = eps;
	}
	public double getC() {
		return C;
	}
	/**
	 * C-SVC、epsilon-SVR、nu-SVR的参数C（默认1）
	 */
	public void setC(double c) {
		C = c;
	}
	public int getNr_weight() {
		return nr_weight;
	}
	/**
	 * C-SVC的类别权重（默认为0）
	 */
	public void setNr_weight(int nr_weight) {
		this.nr_weight = nr_weight;
	}
	public int[] getWeight_label() {
		return weight_label;
	}
	/**
	 * C-SVC的类别权重（默认为NULL）
	 */
	public void setWeight_label(int[] weight_label) {
		this.weight_label = weight_label;
	}
	public double[] getWeight() {
		return weight;
	}
	/**
	 * C-SVC的样本权重（默认为NULL）
	 */
	public void setWeight(double[] weight) {
		this.weight = weight;
	}
	public double getNu() {
		return nu;
	}
	/**
	 * NU-SVC/ONE-CLASS/NU-SVR的nu值（默认值为0.5）
	 */
	public void setNu(double nu) {
		this.nu = nu;
	}
	public double getP() {
		return p;
	}
	/**
	 * epsilon-SVR损失函数中的epsilon值（默认为0.1）
	 */
	public void setP(double p) {
		this.p = p;
	}
	public int getShrinking() {
		return shrinking;
	}
	/**
	 * 是否使用收缩试探法，0（否）或1（是）,（默认为1）
	 */
	public void setShrinking(int shrinking) {
		this.shrinking = shrinking;
	}
	public int getProbability() {
		return probability;
	}
	/**
	 * 是否训练SVC模型进行概率估计，0（否）或1（是）,（默认为0）
	 */
	public void setProbability(int probability) {
		this.probability = probability;
	}
	
	
}
