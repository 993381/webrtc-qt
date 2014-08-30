#ifndef __STORE_DEFINE_H__
#define __STORE_DEFINE_H__

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>

#include "common_define.h"

typedef enum
{
	STORE_TYPE_SWITCH = 0,									//������
	STORE_TYPE_MV,											//�ƶ����
	STORE_TYPE_PLAN,										//�ƻ�¼��
	STORE_TYPE_MANUAL,										//�ֶ�¼��
	STORE_TYPE_JPEG,										//�ֶ�¼��
	STORE_TYPE_COUNT
}	e_store_type;

typedef struct store_list
{
	char filePath[48];										//�ļ�·�������ƣ��������������ʼʱ��
	int iFileSize;											//�ļ���С
	st_clock_t stEndTime;									//�ļ��Ľ���ʱ��
}	st_store_list_t;



#endif	//__STORE_DEFINE_H__
