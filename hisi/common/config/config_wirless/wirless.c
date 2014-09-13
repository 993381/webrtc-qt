#include "wirless.h"

static char wirlessParam[256];
CONFIG_CALLBACK wirlessCallBack=0;

static void setDefaultWirless(char * this)
{
	memset(this,0,256);
}

int wirlessFileRead(struct DEVHARDPARAM * dev)
{
	FILE *fp = NULL;

	if((fp=fopen("/dvs/Wirless.bin","r")) == NULL)
	{
		perror("�����߱������������ļ� �� ʧ��\n");
		setDefaultWirless(wirlessParam);
	}
	else
	{
		fread(wirlessParam,1,256,fp);
		fclose(fp);
	}
	return 0;
}

int wirlessFileGet(char *wirless)
{
	memcpy(wirless,wirlessParam,256);
	return 0;
}

int wirlessFileSet(char *wirless)
{
	if (memcmp(wirless,wirlessParam,256))
	{
		memcpy(wirlessParam,wirless,256);
		
		FILE *fp = NULL;
		if((fp=fopen("/dvs/Wirless.bin","w+")) == NULL)
			perror("�����߱������������ļ� д ʧ��\n");
		else
		{
		    printf("%s:%d д�ļ�\n",__FUNCTION__,__LINE__);
		    fwrite(wirlessParam,1,256,fp);
			fclose(fp);
		}
		
		if (wirlessCallBack != 0)
			wirlessCallBack((void *)&wirlessParam);
	}
	
	return 0;
}
