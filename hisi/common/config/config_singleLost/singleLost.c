#include "singleLost.h"

int singleLostNum=MAXVIDEOCHNS;
static struct SINGLELOST singleLostParam[MAXVIDEOCHNS];
CONFIG_CALLBACK singleLostCallBack=0;

static void setDefaultSingleLost(struct SINGLELOST *this,struct DEVHARDPARAM * dev)
{
	int i = 0;
	for(i=0; i<dev->VideoChanNum; i++)
		memset(&this[i],0,sizeof(struct SINGLELOST));
}

int singleLostFileRead(struct DEVHARDPARAM * dev)
{
	singleLostNum = dev->VideoChanNum;
	
	FILE *fp = NULL;
	if((fp=fopen("/dvs/SingleLost.bin","r")) == NULL)
	{
		perror("���ڵ����������ļ� �� ʧ��\n");
		setDefaultSingleLost(singleLostParam,dev);
	}
	else
	{
		int i = 0;
		for(i=0; i<singleLostNum; i++)
			fread(&singleLostParam[i],1,sizeof(struct SINGLELOST),fp);
		fclose(fp);
	}
	return 0;
}

int singleLostFileGet(struct SINGLELOST *singleLost)
{
	int i;
	for (i=0; i<singleLostNum; i++,singleLost++)
		memcpy(singleLost,&singleLostParam[i],sizeof(struct SINGLELOST));
	return 0;
}

int singleLostFileSet(struct SINGLELOST *singleLost)
{
	int i;
	int isWrite = 0;
	for (i=0; i<singleLostNum; i++,singleLost++)
	{
		if (memcmp(singleLost,&singleLostParam[i],sizeof(struct SINGLELOST)))
		{	
			memcpy(&singleLostParam[i],singleLost,sizeof(struct SINGLELOST));
			isWrite = 1;
		}
	}
	
	if (isWrite == 1)
	{
		FILE *fp = NULL;
		if((fp=fopen("/dvs/SingleLost.bin","w+")) == NULL)
			perror("���ڵ����������ļ� д ʧ��\n");
		else
		{
			printf("%s:%d д�ļ�\n",__FUNCTION__,__LINE__);
			int i = 0;
			for(i=0; i<singleLostNum; i++)
				fwrite(&singleLostParam[i],1,sizeof(struct SINGLELOST),fp);
			fclose(fp);
		}
	}
	return 0;
}
