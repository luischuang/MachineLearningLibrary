/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_penghaisoft_MachineLearningLibrary_classify_SupportVectorMachineClassification */

#ifndef _Included_com_penghaisoft_MachineLearningLibrary_classify_SupportVectorMachineClassification
#define _Included_com_penghaisoft_MachineLearningLibrary_classify_SupportVectorMachineClassification
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_penghaisoft_MachineLearningLibrary_classify_SupportVectorMachineClassification
 * Method:    SVCTrain
 * Signature: ([[D[DIILjava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_penghaisoft_MachineLearningLibrary_classify_SupportVectorMachineClassification_SVCTrain
  (JNIEnv *, jclass, jobjectArray, jdoubleArray, jint, jint, jstring);

/*
 * Class:     com_penghaisoft_MachineLearningLibrary_classify_SupportVectorMachineClassification
 * Method:    SVCPredict
 * Signature: ([[DIILjava/lang/String;)[[D
 */
JNIEXPORT jobjectArray JNICALL Java_com_penghaisoft_MachineLearningLibrary_classify_SupportVectorMachineClassification_SVCPredict
  (JNIEnv *, jclass, jobjectArray, jint, jint, jstring);

#ifdef __cplusplus
}
#endif
#endif
