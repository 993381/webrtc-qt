#include "serial.h"

static struct SERIAL serialParam;
CONFIG_CALLBACK serialCallBack=0;

static void setDefaultSerial(struct SERIAL *this)
{
	memset(this,0,sizeof(struct SERIAL));
	this->baudRate = 3;
	this->udata = 8;
	this->ustop = 1;
	this->ucheck = 0;
}

int serialFileRead(struct DEVHARDPARAM * dev)
{
	FILE *fp = NULL;

	if((fp=fopen("/dvs/Serial.bin","r")) == NULL)
	{
		perror("��485���������ļ� �� ʧ��\n");
		setDefaultSerial(&serialParam);
	}
	else
	{
		fread(&serialParam,1,sizeof(struct SERIAL),fp);
		fclose(fp);
	}
	return 0;
}

int serialFileGet(struct SERIAL *serial)
{
	memcpy(serial,&serialParam,sizeof(struct SERIAL));
	return 0;
}

int serialFileSet(struct SERIAL *serial)
{
	if (memcmp(serial,&serialParam,sizeof(struct SERIAL)))
	{
		memcpy(&serialParam,serial,sizeof(struct SERIAL));
		
		FILE *fp = NULL;	
		if((fp=fopen("/dvs/Serial.bin","w+")) == NULL)
			perror("��485���������ļ� д ʧ��\n");
		else
		{
			printf("%s:%d д�ļ�\n",__FUNCTION__,__LINE__);
			fwrite(&serialParam,1,sizeof(struct SERIAL),fp);
			fclose(fp);
		}		
		
		if (serialCallBack != 0)
			serialCallBack((void *)&serialParam);
	}
	return 0;
}
