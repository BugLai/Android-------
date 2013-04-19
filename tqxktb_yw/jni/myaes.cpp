#include"javaAES.h"
#include<fstream>
using namespace std;

JNIEXPORT jstring JNICALL Java_com_tqxktbyw_videoplayer_AesVideoDec_getkey(JNIEnv *env, jobject obj) {
    const char key[17] = "0123456789abcdef";
    return env->NewStringUTF(key);
}

JNIEXPORT jstring JNICALL Java_com_tqxktbyw_videoplayer_AesVideoDec_getIv(JNIEnv *env, jobject obj) {
    const char iv[17] = "fedcba9876543210";
    return env->NewStringUTF(iv);
}

JNIEXPORT jbyteArray JNICALL Java_com_tqxktbyw_videoplayer_AesVideoDec_getData(JNIEnv * env, jobject obj, jstring PATH) {
    const char *path = (char*) env->GetStringUTFChars(PATH, 0);
    fstream file;
    file.open(path, ios_base::in | ios_base::out | ios_base::binary);
    if (false == file.good()) {
    }
    file.seekg(0, ios_base::end);
    unsigned char indata[1168]; // = ( char*)malloc(1168);
    file.seekg(0, ios_base::beg);
    file.read((char*) indata, 1168);
    file.close();
    jbyteArray data = NULL;
    data = env->NewByteArray(1168);
    env->SetByteArrayRegion(data, 0, 1168, (jbyte*) indata);
    return data;
}

JNIEXPORT void JNICALL Java_com_tqxktbyw_videoplayer_AesVideoDec_setData(JNIEnv *env, jobject obj, jstring SPATH, jstring DPATH, jbyteArray DATA)
{
    const char *spath = (char*) env->GetStringUTFChars(SPATH, 0);
    const char *dpath = (char*) env->GetStringUTFChars(DPATH, 0);
    jsize length = env->GetArrayLength(DATA);
    char *newdata = (char *)env->GetByteArrayElements(DATA, NULL);
    fstream file;
    file.open(spath, ios_base::in | ios_base::out | ios_base::binary);
    if (false == file.good()) {
    }
    file.seekg(0, ios_base::end);
    long lLen = file.tellg();
    char * redata = new char [lLen - 1168];
    char * fdata = new char [1168];
    file.seekg(0, ios_base::beg);
    file.read(fdata, 1168);
    file.read(redata, lLen - 1168);
    file.close();
    fstream fs; 
    fs.open(dpath, ios_base::out | ios_base::binary | ios::app);
    fs.write(newdata, length); 
    fs.write(redata, (lLen - 1168)); 
    fs.close(); 
}
