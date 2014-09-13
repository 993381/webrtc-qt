#ifndef __STORE_API_H__
#define __STORE_API_H__

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include "store_define.h"

#ifdef __cplusplus
extern "C" {
#endif

int STORE_Initialize(void);								//��ʼ��ý�����
int STORE_Cleanup(void);								//����ʼ��ý�����
short STORE_Get_Lib_Version(void);

int STORE_SaveDataToFile(char * path_name,char *pBuf,int datalen);//�洢һ��ͼƬ��һ���ļ���������pBuf�У�path_nameΪ����·�������ļ���
int STORE_Get_Disk_Free(int ctrl, int drive);			//��ȡ����ʣ������������ֵ��λ��M��ctrl��drive����0
int STORE_Get_Disk_Capacity(int ctrl, int drive);		//��ȡ����������������ֵ��λ��M��ctrl��drive����0
int STORE_Get_Disk_Status(int ctrl, int drive);			//��ȡ��ǰ���̵�״̬
int STORE_Get_Store_Status(int iChn,int *plan,int *alarmIN,int *alarmMD,int *manual);//��ȡͨ���Ĵ洢״̬
int STORE_Set_Disk_Format(int ctrl, int drive);			//��ʽ��
														//��ȡ�ļ��б�startTime endTime�ǲ�ѯ��ֹ��ʱ��Σ�iChn��ͨ����0��ʼ��enType��ѯͼ������ͣ�iMaxListNum������ѯ��������stList���ļ��б��ռ��ɵ����߷���
int STORE_Get_File_List(st_clock_t * startTime,st_clock_t *endTime,int iChn,e_store_type enType,int iMaxListNum,st_store_list_t * stList);	//���ص�ֵΪʵ�ʲ�ѯ�����ļ��б������

void STORE_Stop_Manual(int chans);						//ֹͣ�ֶ�¼��chans��ʾ¼���ͨ��
void STORE_Start_Manual(int chans);						//��ʼ�ֶ�¼��chans��ʾ¼���ͨ��
void STORE_Start_Switch(int chans, int alarmInputNum);	//��ʼ����������¼��chans��ʾ¼���ͨ����alarmInputNum��ʾ��������ͨ����
void STORE_Stop_Switch(int chans,int alarmInputNum);	//ֹͣ����������¼��chans��ʾ¼���ͨ����alarmInputNum��ʾ��������ͨ����
void STORE_Start_Motion(int chans,int area);			//��ʼ�ƶ���ⱨ��¼��chans��ʾ�����ƶ�����ͨ����������Ҫ¼���ͨ����area��ʾ�����ƶ���ⱨ���������
void STORE_Stop_Motion(int chans);						//ֹͣ�ƶ���ⱨ��¼��chans¼���ͨ��
void STORE_Stop_Plan(int chans);						//��ʼ�ƻ�¼��chans��ʾ¼���ͨ��
void STORE_Start_Plan(int chans);						//ֹͣ�ƻ�¼��chans��ʾ¼���ͨ��

#ifdef __cplusplus
}
#endif

#endif	//__STORE_API_H__
