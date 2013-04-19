#include "com_tqxktbyw_study_packer_ListFile.h"
#include <stdio.h>
#include<stdlib.h>
#include<malloc.h>
#include <android/log.h>
#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO, "System.out", __VA_ARGS__))
#define LOGW(...) ((void)__android_log_print(ANDROID_LOG_WARN, "System.out", __VA_ARGS__))

/*
 * 调用java中的方法更新进度
 */
void publishJavaProgress(JNIEnv * env, jobject obj, jint progress) {
	//1.找到jclass
	jclass jclazz = env->FindClass("com/tqxktbyw/study/packer/ListFile");
	if (jclazz == 0) {
		LOGI("找不到jclazz!");
	}
	//2.找到jmethodid
	jmethodID methodid = env->GetMethodID(jclazz, "setprogress", "(I)V");
	if (methodid == 0) {
		LOGI("找不到methodid!");
	}
	//3.调用方法
	env->CallVoidMethod(obj, methodid, progress);
}

JNIEXPORT jint JNICALL Java_com_tqxktbyw_study_packer_ListFile_depack(
		JNIEnv * env, jobject obj, jstring dpath, jstring spath) {

	//1.找到jclass
	jclass jclazz = env->FindClass("com/tqxktbyw/study/packer/ListFile");
	if (jclazz == 0) {
		LOGI("找不到jclazz!");
	}
	//2.找到jmethodid
	jmethodID methodid = env->GetMethodID(jclazz, "setprogress", "(I)V");
	if (methodid == 0) {
		LOGI("找不到methodid!");
	}
	const char * drcpath = (char*) env->GetStringUTFChars(dpath, 0);
	const char * srcpath = (char*) env->GetStringUTFChars(spath, 0);

	LOGI("drcpath = %s", drcpath);
	LOGI("srcpath = %s", srcpath);

	long length;
	FILE *minbag, *maxbag; //文件指针
	char filename[20], *temp;
	char a[256] = { 0 };
	int total = 0; //记录读取进度
	//char b[]="E:\\360data\\重要数据\\桌面\\压缩解压\\6077462packer\\packer\\";
	struct Fhead //用来记录文件的名字和长度
	{
		char calibration[17]; //校准
		char fname[20]; // 文件名
		long flength; //文件长度
	} FH;

	if ((maxbag = fopen(srcpath, "rb")) == NULL) //为输入打开一个文件
			{
		LOGI("打开文件失败");
		return 0;
	} else {
		LOGI("开辟内存空间");
		temp = (char *) malloc(10240); //在内存中申请一段存取空间
		fread(&FH, sizeof(struct Fhead), 1, maxbag); //读取小文件的结点信息

		while (!feof(maxbag)) //当文件未读取结束时循环
		{
			strcpy(a, drcpath);
			//校准文件
			if (strcmp(FH.calibration, "www.taiqiedu.com") == 0) {
				strcat(a, FH.fname);
				LOGI("小文件名 = %s", a);
				if ((minbag = fopen(a, "wb")) == NULL) //用结点中的名字做小文件的名字为输出打开一个文件
						{
					LOGI("打开文件minbag失败");
					return 0;
				} else {
					length = FH.flength;
					while (length) {

						if (length >= 10240) {
							fread(temp, 10240, 1, maxbag); //从打包文件中读取1024个字节到内存中
							fwrite(temp, 10240, 1, minbag); //把读取到内存中的信息输出到小文件中
							length = length - 10240; //每读完一段length减少1024
							total += 10240;
						} else {
							fread(temp, length, 1, maxbag); //当length小于1024时，直接读取length长度的信息到内存中
							fwrite(temp, length, 1, minbag); //把读取到内存中的信息输出到小文件中
							total += length;
							length = 0; //length置零，退出循环

						}

						env->CallVoidMethod(obj, methodid, total);
						//publishJavaProgress(env, obj, total);
					}
					fclose(minbag); //关闭文件

					fread(&FH, sizeof(struct Fhead), 1, maxbag); //读取小文件的结点信息

				}
			} else {
				LOGI("文件校准失败  %s", FH.calibration);
				return 0;
			}
		}
		fclose(maxbag); //关闭文件
		free(temp); //释放内存

		return 1;
	}
}
