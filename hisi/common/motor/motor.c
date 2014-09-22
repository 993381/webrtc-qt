#include "motor.h"

/*********************************************************/
//�������ģ��
/*********************************************************/
int MOTOR_Send(char * pData,int iLen)
{
	int fd = open("/dev/ttyTimer", O_RDWR);
	if(fd < 0)
	{
		perror("open");
		return -1;
	}
	int fw = write(fd,pData,iLen);
	if(fw < 0)
	{
		perror("write");
		return -1;
	}
	close(fd);
    return 0;
}

unsigned char command_d[34][4] =
{
    {0x00,0x00,0x00,0x00},		//ֹͣ0
    {0x00,0x08,0x00,0x10},		//��1
    {0x00,0x10,0x00,0x10},		//��2
    {0x00,0x04,0x10,0x00},		//��3
    {0x00,0x02,0x10,0x00},		//��4
    {0x00,0x0C,0x10,0x10},		//����5
    {0x00,0x14,0x10,0x10}, 		//����6
    {0x00,0x0A,0x10,0x10},		//����7
    {0x00,0x12,0x10,0x10},		//����8
    {0x00,0x2D,0x00,0x00},		//��Ȧ�Զ�9
    {0x02,0x00,0x00,0x00},		//��Ȧ�� +10 open
    {0x04,0x00,0x00,0x00},		//��Ȧ�� -11 close
    {0x00,0x00,0x00,0x00},		//��Ȧ�仯ֹͣ12
    {0x01,0x00,0x00,0x00},		//�����13 near
    {0x00,0x80,0x00,0x00},		//����Զ14 far
    {0x00,0x00,0x00,0x00},		//����仯ֹͣ15
    {0x00,0x20,0x00,0x00},		//�䱶С+16 IN
    {0x00,0x40,0x00,0x00},		//�䱶��-17 OUT
    {0x00,0x00,0x00,0x00},		//�䱶�仯ֹͣ18
    {0x90,0x00,0x08,0x08},		//�Զ���ʼ19
    {0x00,0x00,0x00,0x00},		//�Զ�ֹͣ20
    {0x00,0x09,0x00,0x01},		//��ˢ��21
    {0x00,0x0b,0x00,0x01},		//��ˢ��22
    {0x00,0x09,0x00,0x02},		//�ƹ⿪23
    {0x00,0x0b,0x00,0x02},		//�ƹ��24
    {0x00,0x07,0x00,0x00},		//����Ԥ�õ�25
    {0x00,0x03,0x00,0x00},		//����Ԥ�õ�26
    {0x00,0x05,0x00,0x00},		//���Ԥ�õ�27
    {0x00,0x1f,0x00,0x00},		//ģʽ��28
    {0x00,0x21,0x00,0x00},		//ģʽ��29
    {0x00,0x23,0x00,0x00},		//����ģʽ30
    {0x00,0x07,0x00,0x21},      //180�ȷ�ת31
    {0x00,0x07,0x00,0x77},      //�������Ԥ��λ
    {0x99,0x51,0x00,0x00},      //�������Ԥ��λ
};

/*********************************************************/
//PELCO-P
unsigned char command_p[40][4] =
{
    {0x00,0x00,0x00,0x00},		//ֹͣ0
    {0x00,0x08,0x00,0x08},		//��1
    {0x00,0x10,0x00,0x08},		//��2
    {0x00,0x04,0x08,0x00},		//��3
    {0x00,0x02,0x08,0x00},		//��4
    {0x00,0x0C,0x08,0x08},		//����5
    {0x00,0x14,0x08,0x08}, 		//����6
    {0x00,0x0A,0x08,0x08},		//����7
    {0x00,0x12,0x08,0x08},		//����8
    {0x00,0x2D,0x00,0x00},		//��Ȧ�Զ�9
    {0x04,0x00,0x00,0x00},		//��Ȧ�� +10 op
    {0x08,0x00,0x00,0x00},		//��Ȧ�� -11 cl
    {0x00,0x00,0x00,0x00},		//��Ȧ�仯ֹͣ1
    {0x01,0x00,0x00,0x00},		//�����13 near
    {0x02,0x00,0x00,0x00},		//����Զ14 far
    {0x00,0x00,0x00,0x00},		//����仯ֹͣ1
    {0x00,0x20,0x00,0x00},		//�䱶С+16 IN
    {0x00,0x40,0x00,0x00},		//�䱶��-17 OUT
    {0x00,0x00,0x00,0x00},		//�䱶�仯ֹͣ1
    {0x00,0x19,0x08,0x08},		//�Զ���ʼ19
    {0x00,0x00,0x00,0x00},		//�Զ�ֹͣ20
    {0x00,0x09,0x00,0x01},		//��ˢ��21
    {0x00,0x0b,0x00,0x01},		//��ˢ��22
    {0x00,0x09,0x00,0x02},		//�ƹ⿪23
    {0x00,0x0b,0x00,0x03},		//�ƹ��24
    {0x00,0x07,0x00,0x00},		//����Ԥ�õ�25
    {0x00,0x03,0x00,0x00},		//����Ԥ�õ�26
    {0x00,0x05,0x00,0x00},		//���Ԥ�õ�27
    {0x00,0x1f,0x00,0x00},		//ģʽ��28
    {0x00,0x21,0x00,0x00},		//ģʽ��29
    {0x00,0x23,0x00,0x00},		//����ģʽ30
    {0x00,0x07,0x00,0x21},      //180�ȷ�ת31
    {0x00,0x07,0x00,0x77},      //�������Ԥ��λ
    {0x99,0x51,0x00,0x00},      //�������Ԥ��λ

};

/*********************************************************/
//YAAN
unsigned char command_y[34][4] =
{
    {0x01,0x00,0x00,0x00},		//ֹͣ0
    {0x01,0x04,0x00,0x20},		//��1
    {0x01,0x08,0x00,0x20},		//��2
    {0x01,0x02,0x00,0x20},		//��3
    {0x01,0x01,0x00,0x20},		//��4
    {0x01,0x06,0x00,0x20},		//����5
    {0x01,0x0A,0x00,0x20}, 		//����6
    {0x01,0x05,0x00,0x20},		//����7
    {0x01,0x09,0x00,0x20},		//����8
    {0x01,0x00,0x00,0x00},		//��Ȧ�Զ�9
    {0x01,0x00,0x01,0x20},		//��Ȧ�� +10 op
    {0x01,0x00,0x02,0x20},		//��Ȧ�� -11 cl
    {0x01,0x00,0x00,0x00},		//��Ȧ�仯ֹͣ1
    {0x01,0x40,0x00,0x20},		//�����13 near
    {0x01,0x80,0x00,0x20},		//����Զ14 far
    {0x01,0x00,0x00,0x00},		//����仯ֹͣ1
    {0x01,0x20,0x00,0x20},		//�䱶С+16 IN
    {0x01,0x10,0x00,0x20},		//�䱶��-17 OUT
    {0x01,0x00,0x00,0x00},		//�䱶�仯ֹͣ1
    {0x0F,0x01,0x20,0x00},		//�Զ���ʼ19
    {0x01,0x00,0x00,0x00},		//�Զ�ֹͣ20
    {0x01,0x00,0x00,0x00},		//��ˢ��21
    {0x01,0x00,0x00,0x00},		//��ˢ��22
    {0x01,0x00,0x00,0x00},		//�ƹ⿪23
    {0x01,0x00,0x00,0x00},		//�ƹ��24
    {0x02,0x01,0x00,0x00},		//����Ԥ�õ�25
    {0x05,0x01,0x00,0x00},		//����Ԥ�õ�26
    {0x08,0x00,0x00,0x00},		//���Ԥ�õ�27
    {0x06,0x01,0x00,0x00},		//ģʽ��28
    {0x08,0x00,0x00,0x00},		//ģʽ��29
    {0x03,0x01,0x00,0x00},		//����ģʽ �Զ�Ѳ��30
    {0x01,0x00,0x00,0x00},		//180�ȷ�ת31
    {0x08,0x00,0x00,0x00},      //�������Ԥ��λ
    {0x99,0x51,0x00,0x00},      //�������Ԥ��λ
};

int MOTOR_Control(int iChn, int iCmd,int iSpeed,int iParam)
{
    printf("cmd:%d\n",iCmd);
    char * pProtocol=NULL;
    char cComBuf[16];
    int j,iComLen =0;
    struct SERIAL motorSerial;
    char iMOTORID;
	CONFIG_Get(CONFIG_TYPE_SERIAL,(void *)&motorSerial);
	iMOTORID = motorSerial.ptz[iChn].yuntaiID;
    if (iCmd < 34)
    {
        switch (motorSerial.ptz[iChn].Protocol)
        {
        case 0://PELCO_D
            pProtocol = (char *)command_d;
            cComBuf[0] = 0xff;
            cComBuf[1] = iMOTORID;
            cComBuf[2] = pProtocol[iCmd*4+0];
            cComBuf[3] = pProtocol[iCmd*4+1];
            cComBuf[4] = 0;
            cComBuf[5] = 0;
            if ((iCmd==1)||(iCmd==2))
                cComBuf[5] = iSpeed;
            else if ((iCmd==3)||(iCmd==4)||(iCmd==33))
                cComBuf[4] = iSpeed;
            else if ((iCmd==5)||(iCmd==6)||(iCmd==7)||(iCmd==8))
            {
                cComBuf[4] = iSpeed;
                cComBuf[5] = iSpeed;
            }
            else if ((iCmd==25)||(iCmd==26)||(iCmd==27))
                cComBuf[5] = iParam;
            else if  (iCmd == 28 || iCmd == 29  || iCmd == 30)
            {
                cComBuf[4] = 0x00;
                cComBuf[5] = 0x3f;
            }
            else if (iCmd >= 31)
            {
                cComBuf[4] = pProtocol[iCmd*4+2];
                cComBuf[5] = pProtocol[iCmd*4+3];
            }
            cComBuf[6] = 0;
            for(j=1; j<6; j++)
                cComBuf[6] += cComBuf[j];
            iComLen = 7;
            break;
        case 1://PELCO_P
            pProtocol = (char *)command_p;
            cComBuf[0] = 0xa0;
            cComBuf[1] = iMOTORID - 1;
            cComBuf[2] = pProtocol[iCmd*4+0];
            cComBuf[3] = pProtocol[iCmd*4+1];
            cComBuf[4] = 0;
            cComBuf[5] = 0;
            if ((iCmd==1)||(iCmd==2))
                cComBuf[5] = iSpeed;
            else if ((iCmd==3)||(iCmd==4)||(iCmd==33))
                cComBuf[4] = iSpeed;
            else if ((iCmd==5)||(iCmd==6)||(iCmd==7)||(iCmd==8))
            {
                cComBuf[4] = iSpeed;
                cComBuf[5] = iSpeed;
            }
            else if ((iCmd==25)||(iCmd==26)||(iCmd==27))
                cComBuf[5] = iParam;
            else if (iCmd >= 31 && iCmd <= 33)
            {
                cComBuf[4] = pProtocol[iCmd*4+2];
                cComBuf[5] = pProtocol[iCmd*4+3];
            }
            else if (iCmd == 34 || iCmd == 35)
            {
                cComBuf[4] = pProtocol[iCmd*4+2];
                cComBuf[5] = pProtocol[iCmd*4+3];
            }
            cComBuf[6] = 0xaf;
            cComBuf[7] = cComBuf[0];
            for (j=1; j<7; j++)
                cComBuf[7] ^= cComBuf[j];
            iComLen = 8;
            break;
        case 2://PELCO_Y
            pProtocol = (char *)command_y;
            cComBuf[0] = 0x02;
            cComBuf[1] = iMOTORID - 1;
            cComBuf[2] = pProtocol[iCmd*4+0];
            cComBuf[3] = pProtocol[iCmd*4+1];
            cComBuf[4] = pProtocol[iCmd*4+2];
            cComBuf[5] = cComBuf[0]+cComBuf[1]+cComBuf[2]+cComBuf[3]+cComBuf[4];
            if (cComBuf[2] == 0x01)
            {
                cComBuf[6] = 0x02;
                cComBuf[7] = iMOTORID - 1;
                cComBuf[8] = 0x0e;
                cComBuf[9] = iSpeed;
                cComBuf[10] = iSpeed;
                cComBuf[11] = cComBuf[6]+cComBuf[7]+cComBuf[8]+cComBuf[9]+cComBuf[1];
                iComLen = 12;
            }
            else if (cComBuf[2] == 0x0f)
            {
                cComBuf[4] = iSpeed;
                cComBuf[5] = cComBuf[0]+cComBuf[1]+cComBuf[2]+cComBuf[3]+cComBuf[4];
                iComLen = 6;
            }
            else if (cComBuf[2] == 0x03 || cComBuf[2] == 0x08)
            {
                cComBuf[3] = 0x00;
                cComBuf[4] = cComBuf[0]+cComBuf[1]+cComBuf[2]+cComBuf[3];
                iComLen = 5;
            }
            else
            {
                cComBuf[3] = iParam;
                cComBuf[4] = cComBuf[0]+cComBuf[1]+cComBuf[2]+cComBuf[3];
                iComLen = 5;
            }

            break;
        default:
            break;
        }
    }
    if (iComLen != 0)
    {
//        DBG_PRINT_MEM(cComBuf,iComLen);
        MOTOR_Send(cComBuf, iComLen);
    }

    return 0;
}
