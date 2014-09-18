#ifndef __MEDIA_API_H__
#define __MEDIA_API_H__

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include "media_define.h"

#ifdef __cplusplus
extern "C" {
#endif

int MEDIA_Initialize(void);								//��ʼ��ý�����
int MEDIA_Cleanup(void);								//����ʼ��ý�����

int MEDIA_Get_IDR(int iGrp,int iSubChn);				//��ȡidr֡
unsigned long long MEDIA_Get_Stamp(int iGrp,int iSubChn);//��ȡ��ǰ���������ʱ�����

int MEDIA_Audio_isTalking(void);						//�����Ƿ����ڶԽ���״̬��
int MEDIA_Audio_Talk(char *pAudioData,int iLen);		//�Խ���Ƶ���ýӿڣ�pAudioData�Ǵ���Ƶ���ݣ�iLen�����ݵĳ��ȣ��������ݲ��ֲ���Ҫ�κδ���G.711 160�ֽڵĴ����ݣ�������0 0 0 1
int MEDIA_Audio_SoundFromFile(char *pFileName);			//��������ļ���pFileNameΪ�����ļ��ľ���·��
int MEDIA_Jpeg_Generate(int iChn,int iJpegNum,int iJpegGop,MEDIA_JPEG_CALLBACK jpeg_callback);//����iJpegNum��ͼƬ�����iJpegGop֡����һ�ţ����ҽ����ɵ�ͼƬͬ��jpeg_callback�ص���������

int MEDIA_VDA_MD_Start(int iGrp,int iArea);				//��ʼ����ƶ����
int MEDIA_VDA_MD_Stop(int iGrp,int iArea);				//ֹͣ����ƶ����

int MEDIA_VDA_OD_Start(int iChn);						//��ʼ����ڵ�����
int MEDIA_VDA_OD_Stop(int iChn);						//ֹͣ����ڵ�����

int MEDIA_VDA_SL_Start(int iChn);						//��ʼ����źŶ�ʧ����
int MEDIA_VDA_SL_Stop(int iChn);						//ֹͣ����źŶ�ʧ����


#ifdef __cplusplus
}
#endif

#endif	//__MEDIA_API_H__
