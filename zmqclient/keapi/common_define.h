#ifndef COMMAND_DEFINE_H
#define COMMAND_DEFINE_H

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
/**********************************************************************/
//module clock
/**********************************************************************/
typedef enum
{
	CLOCK_TYPE_HIRTC = 0,										//��˼�ڲ�RTCʱ��
	CLOCK_TYPE_PCF8563,											//�ⲿӲ��ʱ��оƬ
	CLOCK_TYPE_NTP,												//����Уʱ
	CLOCK_TYPE_COUNT
}	e_clock_type;

typedef struct struct_clock_s
{
	int year;
	int month;
	int day;
	int hour;
	int minute;
	int second;
	int week;
}	st_clock_t;
/**********************************************************************/
//module config
/**********************************************************************/
#define MAXVIDEOCHNS				8					//ͨ����
#define MAX_ALARMINPUTS				16					//��������
#define MAX_ALARMOUTPUTS			16
typedef enum
{
	CONFIG_TYPE_DEV = 0,										//��˼�ڲ�RTCʱ��
	CONFIG_TYPE_NET,											//�ⲿӲ��ʱ��оƬ
	CONFIG_TYPE_MEDIA,
	CONFIG_TYPE_INPUT,
	CONFIG_TYPE_OUTPUT,
	CONFIG_TYPE_JPEG,
	CONFIG_TYPE_NAME,
	CONFIG_TYPE_SERIAL,
	CONFIG_TYPE_STORE,
	CONFIG_TYPE_OSDTIME,
	CONFIG_TYPE_OSDTITLE,
	CONFIG_TYPE_OSDPRIVACY,
	CONFIG_TYPE_SENSOR,
	CONFIG_TYPE_SENSE,
	CONFIG_TYPE_OVERLAY,
	CONFIG_TYPE_LOSTSINGLE,
	CONFIG_TYPE_WIRLESS_OLD,
	CONFIG_TYPE_WIRLESS_NEW,
	CONFIG_TYPE_VO,
	CONFIG_TYPE_COUNT
}	e_config_type;

/*************************�豸����*************************/
struct DEVHARDPARAM
{
	unsigned int  InputAlarmNum;
	unsigned int  InputAlarmPort[MAX_ALARMINPUTS];		//16�����뱨����gpio�˿�
	unsigned int  InputAlarmBit[MAX_ALARMINPUTS];		//16�����뱨����gpio bit
	unsigned int  OutputAlarmNum;						//��������ĸ���
	unsigned int  OutputAarmPort[MAX_ALARMOUTPUTS];		//16�����������gpio�˿�
	unsigned int  OutputAlarmBit[MAX_ALARMOUTPUTS];		//16�����������gpio bit
	unsigned int  VideoAdType;							//��Ƶad���ͺ�      С��10�Ǳ���С��20��720PС��30��960PС��40��1080PС��50��300wС��60��500W
	unsigned int  AudioAiAdType;						//��Ƶai ad���ͺ�   0=tlv320aic23 1=tw2815 2=aic320aic31
	unsigned int  AudioAoAdType;						//��Ƶao ad���ͺ�   0=tlv320aic21 1=tw2815 2=aic320aic31
	unsigned int  AudioInputMode;						//��Ƶ������ģʽ    0=mic         1=line
	unsigned int  VideoChanNum;							//��Ƶͨ����
	unsigned int  RecordDeviceType;						//¼���豸�����ͣ�
	unsigned int  SD_enable;							//sd��ʹ�ܱ�־
	unsigned int  USB_enable;							//usb�豸ʹ�ܱ�־;
	unsigned int  SATA_enable;							//SATA�豸ʹ�ܱ�־
	unsigned int  hardver;								//Ӳ���汾
	unsigned int  softver;								//����汾
	unsigned int  encodever;							//�������汾
	unsigned int  LampBit;                              //����ָʾ�Ƶ�λ
	unsigned int  LampPort;                             //����ָʾ�ƵĶ˿�
	unsigned int  IrCardPort;                           //ircard���ƿڵ���
	unsigned int  IrCardBit;                            //ircard���ƿڵ�bit
	unsigned int  LightSensorPort;                      //����������ڵ���
	unsigned int  LightSensorBit;                       //����������ڵ�bit
	char          DeviceType[20];						//�豸����
	char          OEM_Type[20];                         //OEM�ͺ�
	unsigned int  NetWork;
	unsigned int  ModulePowerPort[4];		            //4��ģ���Դ��gpio�˿�
	unsigned int  ModulePowerbit[4];		            //4��ģ���Դ��gpio bit
	unsigned int  ModuleResetPort[4];		            //4��ģ�鸴λ��gpio�˿�
	unsigned int  ModuleResetbit[4];		            //4��ģ�鸴λ��gpio bit
	unsigned int  ModuleClosePort[4];		            //4��ģ��ػ���gpio�˿�
	unsigned int  ModuleCLosebit[4];		            //4��ģ��ػ���gpio bit
	unsigned int  ResetKeyPort;                         //�ָ��������õİ�ť��gpio�˿�
	unsigned int  ResetKeyBit;                          //�ָ��������õİ�ť��gpiobit
	unsigned int  usbControlPort;						//usb���Ƶ�Դ�Ķ˿� port
	unsigned int  usbControlBit;						//usb���Ƶ�Դ�Ķ˿� bit
	unsigned int  wirelessAlarm;
	char          wifiEth[16];                          //wifiģ��ʹ�õ��豸��
	unsigned int  voltageDetect;                        //�Ƿ�������������ʵ��ص�ѹ�Ĺ���
	int           vencNumber;                           //�ֻ������������ͨ����
	unsigned int  talkCtrl;                             //ǰ�˶Խ��ı�������ͨ�������ó�100��������
	char          infoDevice_1[16];                     //���Ų�ѯ�Ŀ�1�豸��
	char          infoDevice_2[16];                     //���Ų�ѯ�Ŀ�2�豸��
	unsigned int  uartFlag;								//ʹ�ô��ڵı�־��0��485��1��232������������Ҫ������̨��Ҫͨ��232��������
	unsigned int  rtpMode;                              //vlc��������ģʽ��һ��0��rtsp����1��ֱ��rtp����
	unsigned int  singleton;                            //���� ��������
	unsigned int  tvp5150Reset;							//tvp5150��λ����
	char          doubleModule[4];                      //�ĸ�������������1��ֱ��ʾ������(ģ������ͺ�)��������(ģ�������netWork��Ӧ)
	unsigned int  GpsFromModem;                         //˫����ʱһ��ģ�鴫��Ƶһ��ģ�鴫GPS��Ϣ
	unsigned int  NetcardType;							//����ʹ�������ͺ�0=rtl8201  1=ksz8041
	unsigned int  P2P_Enable;
}__attribute__((packed));
/*************************�������*************************/
struct modem_t
{
    unsigned char   jrh[32];
    unsigned char   usr[32];
    unsigned char   pwd[32];
    unsigned char   vpn[32];
}__attribute__((packed));
struct ctrl_t
{
	char cardEnable;
	char cardNetType;
}__attribute__((packed));
struct DDNSInfo
{
	char user[32];
	char pass[32];
	char server[32];
	unsigned short port;
	char enable;
	char serverType;        							//0-ÿ����1-������
}__attribute__((packed));
struct NETPARAM											//�������
{
    unsigned char   localIP[4];             			//IP��ַ
    unsigned char   gateIP[4];							//����
    unsigned char   netMask[4];             			//��������
    unsigned short  dnsPort;                			//DNS�˿�
    unsigned short	webMediaPort;
    unsigned char   dnsIP[4];               			//DNS������IP
    unsigned char   domainName[32];         			//����(���������)����
    unsigned char   hostName[32];           			//�豸������
    unsigned char   username[8];            			//�����������½�û���
    unsigned char   password[8];            			//�����������½����
    unsigned char   pppoeEnable;
    unsigned char   pppoeIPType;
    unsigned char   pppoeUser[30];
    unsigned char   pppoePass[30];
    unsigned char   dhcpEnable;
    unsigned char   wifiEnable;
    unsigned char   wifiNum;
    unsigned char   wifiMode;
    unsigned char   wifiASC;                			//ʮ������(0)ASCII(1) TKIP(0) AES(1)
    unsigned char   wifiIP[4];
    unsigned char   wifimask[4];
    unsigned char   wifigateway[4];
    unsigned char   wifiSSID[33];
    unsigned char   wifiCode[33];
    struct modem_t  modem[4];
    unsigned char   autoChange;
    unsigned char   changeTime;
    struct ctrl_t   card_ctrl[2];
    unsigned char   macAddress[32];
	unsigned short  webServicePort;						//web Service�˿�
	unsigned char   ntpIP[4];
	unsigned short  ntpPort;
	unsigned char	ntpZone[4];
	unsigned int    ntpCycle;
	unsigned char   netTranMode;
	unsigned char   wirelessEnable;
	unsigned short  netSleep;
	unsigned char   telephoneInfo[128];
	unsigned char   bEnablePass; 	  					//dvr ��������ʹ��

	struct DDNSInfo ddns;								//ddns
	unsigned char   wifiDhcp;                           //wifi dhcp�����ÿ���
    unsigned int    routePort;
}__attribute__((packed));
/*************************�������*************************/
#define RESO_QCIF 1
#define RESO_CIF 2
#define RESO_HD1 3
#define RESO_D1 4
#define RESO_QVGA 5
#define RESO_VGA 6
#define RESO_720P 7
#define RESO_960P 8
#define RESO_1080P 9
#define RC_VBR 0
#define RC_CBRP 1
struct VENC
{
	char			enable;								//ͨ���Ƿ���
	unsigned char	resolution;							//ͼ��ֱ���:
														//1-QCIF,2-CIF,3-HD1,4-D1,//5-QVGA,6-VGA,//7-720P,8-960P,9-1080P
	unsigned int	frame_rate;							//֡��  1~60
	unsigned int	idr_interval;						//�ؼ�֡���
	char			rate_ctrl_mode;						//���ʿ��Ʒ�ʽ:0-VBR,1-CBR,2-ABR,3-FIXQP
	unsigned int	bitrate;
                                            			//CBR/ABR ģʽ,��ʾƽ�����ʡ�//VBR ģʽ,��ʾ������ʡ�//FIXQP ģʽ,��ֵ��Ч��//ȡֵ��Χ:[1, 20000],��λ Kbps��
    char			piclevel;							//ͼ��ȼ�,�� VBR/CBR ģʽ����Ч��
                                            			//VBR ģʽ��,��ʾͼ��������ȼ���ȡֵ��Χ:[0, 5],ֵԽС,ͼ������Խ�á�
                                            			//CBR ģʽ��,��ʾ���ʲ�����Χ��ȡֵ��Χ:[0, 5]��
                                            			//0:�Զ���������,�Ƽ�ʹ�á�1~5:��Ӧ���ʲ�����Χ�ֱ�Ϊ��10%~��50%��
	char			qp_i;								//I ֡ QP��FIXQP ģʽ����Ч��ȡֵ��Χ:[10, 50]��
	char			qp_p;								//P ֡ QP��FIXQP ģʽ����Ч��ȡֵ��Χ:[10, 50]��

	int				rate_statistic;						//����ͳ��ʱ�Ρ�ABR ģʽ����Ч��ABR,�����ʶ�ʱ�䲨��,��ʱ��ƽ�ȡ���ʱ�����ʵ�ͳ��,�Դ�ʱ��Ϊ׼��

	char		    wm_enable;
	char			wm_key[8];
	char			wm_userword[16];
}__attribute__((packed));
struct MEDIAPARAM
{
	unsigned char vFormat;								//��Ƶ��ʽ0:P�� 1:N��
	unsigned char aFormat;								//��Ƶ��ʽ0:G711 1:G726 2:AAC
	struct VENC	main[MAXVIDEOCHNS];
	struct VENC	minor[MAXVIDEOCHNS];
}__attribute__((packed));
/*************************ͼƬ����*************************/
struct JPEGPARAM
{
	unsigned char resolution;							//ͼ��ߴ��Сcif,d1,qcif
    unsigned char picLevel;								//����ϵ��
}__attribute__((packed));
/**********************�������������**********************/
struct DEFTIME
{
    unsigned short begin[3];                 			//��������ʼʱ��
    unsigned short end[3];                   			//������ֹͣʱ��
}__attribute__((packed));
struct PTZACTION
{
    unsigned char enable[16];
	unsigned char pztPosition[16];
}__attribute__((packed));
struct ALMINPUT											//�����������
{
    unsigned char	TypeAlarmor;						//����������(����=0�򳣱�=1)
    unsigned char	CallCenter;                         //�Ƿ��ϱ����ľ���ʽ
    unsigned char	Soundor;                            //����������ʽ
    unsigned char	TypeOutBurst;                       //�ⲿ����������ʽ
    unsigned char	OutChan;							//������ͨ��
    unsigned char	JpegSheet;							//ץ������
    unsigned char	JpegIntv;			                //ץ�ļ��
    unsigned int	JpegChn;							//����ץ��ͨ��ѡ��
    unsigned int	RecChn;				               	//¼��ͨ��
    unsigned char	RecDelay;							//¼����ʱʱ��
    unsigned char	PreRecord;				            //Ԥ¼��ʱ��
    unsigned char	Enable;								//�Ƿ�����1Ϊ����Ĭ��0������
    unsigned int	AlmTime;							//���뱨��ʱ��
	struct DEFTIME  strategy[7];
	struct PTZACTION ptz;
}__attribute__((packed));
/**********************�������������**********************/
struct ALMOUTPUT										//�����������
{
	unsigned short AlarmLong;							//����ʱ��
	unsigned char  Type;								//���������ʽ
	unsigned char  Enable;                              //�Ƿ�����1Ϊ����Ĭ��0������
	struct DEFTIME strategy[7];
}__attribute__((packed));
/*************************���Ʋ���*************************/
struct NAMEINFO
{
    char devName[40];
    char chnName[MAXVIDEOCHNS][40];
	char alarmIn[MAX_ALARMINPUTS][40];
	char alarmOut[MAX_ALARMOUTPUTS][40];
}__attribute__((packed));
struct WEB_USER
{
    char cUser[9];
    char cPass[9];
	char bPTZ;											//������̨
	char bRecord;										//¼��Ȩ��
	char bSetParam;										//���ò���
	char bPlayRecord;									//����¼��Ȩ��
	char bTools;										//�޸Ĺ���Ȩ��
	char bUser;
}__attribute__((packed));
struct NAMEPARAM										//���Ʋ���
{
	struct NAMEINFO nameInfo;							//�豸��ͨ����������������

	struct WEB_USER user[16];							//web�û�����
}__attribute__((packed));
/*************************���ڲ���*************************/
struct PTZ
{
	char Protocol;
	char yuntaiID;
}__attribute__((packed));
struct SERIAL
{
	unsigned short baudRate;							//������̨������
	unsigned char  udata;								//���ڲ���
	unsigned char  ustop;								// ֹͣλ
	unsigned char  ucheck;								//У��λ
	struct PTZ ptz[MAXVIDEOCHNS];
}__attribute__((packed));
/*************************�洢����*************************/
struct STORPARAM
{
    unsigned char   Enable;                 			//�洢���� 1:��0:��
    unsigned char   overwrite;              			//ѭ�����Ǵ洢����
    unsigned short  SpaceAlarm;             			//�ռ䲻�㱨����ֵ
    unsigned int    channel;                			//¼��ͨ��ѡ��

    unsigned char   packinterval;						//�ļ�������
    unsigned char   snapeswitch;						//ץ�Ĵ洢����
    unsigned char   alarmswitch;						//����������¼�񿪹�
    unsigned char   senseswitch;						//�ƶ����¼�񿪹�
    unsigned int    diskspace;							//Ӳ������
    unsigned char   StoreCycle[16];         			//�洢����
    unsigned short  valveAlarm;
    unsigned short  saveWithSubChannel;     			//ʹ����ͨ�����д洢ÿһλ����һ· 0-��ʾ������ͨ�� 1-��ʾʹ����ͨ��

    struct DEFTIME  strategy[MAXVIDEOCHNS][7];
}__attribute__((packed));
/**********************OSD�������*************************/
struct TITLEOSD											//����OSD��ʾ����
{
	unsigned short X;
	unsigned short Y;
	unsigned char  Width;
	unsigned char  Height;
	unsigned char  Trans;
	unsigned char  Layer;
	unsigned char  Color[3];
	unsigned char  Enable;
	unsigned short Len;
	unsigned char  Contert[32];
}__attribute__((packed));
/**********************OSDʱ�����*************************/
struct TIMEOSD
{
	unsigned char  Switch;								//38-38 ʱ����ʾ����
	unsigned short X;									// x������
	unsigned short Y;									// y������
	unsigned char  Trans;								//͸����
	unsigned char  Layer;								//ͼ��
	unsigned char  Color[3];							//��ʾ��ɫ
	unsigned char  Format;								//��ʾ��ʽ
	unsigned char  Reso;								//10-10 ͼ��ߴ��Сcif,d1,qcif
}__attribute__((packed));
/********************OSD��˽��������***********************/
struct PRITOSD
{
	unsigned short X;			  //x������
	unsigned short Y;			  // y������
	unsigned short Width;		  //���
	unsigned short Height; 	  //�߶�
	unsigned char  Type;		  //�ڵ���������
	unsigned char  Color[3];	  //�ڵ��������ɫ
}__attribute__((packed));
/*********************ͼ���ڵ�����*************************/
struct OVALM
{
	unsigned char   Level;                                 //���������ȵȼ�0~9   [0=���ܹ�]
	unsigned char   Speed;                                 //��������������ٶ�
	unsigned char   Enable;                                //�Ƿ�����
 	unsigned char   CallCenter; 	                       //�Ƿ��ϱ����ľ���ʽ
	unsigned char   Soundor;    	                       //����������ʽ
	unsigned char   OutChan;		                       //���������ͨ��
	struct DEFTIME  strategy[7];
}__attribute__((packed));
/*********************�ƶ�������*************************/
struct SENSE												//�ƶ�������
{
    unsigned short  RangeStart[2];							//��⿪ʼ����
	unsigned short  RangeStop[2];							//���ֹͣ����
	unsigned char   Level;									//���������ȵȼ�0~9
	unsigned char   Speed;									//��������������ٶ�
	unsigned char   Enable;									//�Ƿ�����
	unsigned char   CallCenter;								//�Ƿ��ϱ����ľ���ʽ
    unsigned char   Soundor;								//����������ʽ
    unsigned char   OutChan;								//���������ͨ��
    unsigned char   PreRecord;								//Ԥ¼��ʱ��
    unsigned char 	JpegSheet;								//ץ������
    unsigned char 	JpegIntv;								//ץ�ļ��
    unsigned int	JpegChn;								//����ץ��ͨ��ѡ��
    unsigned int	RecChn;									//8·��������¼��ͨ��ѡ��
    unsigned char 	RecDelay;								//����������¼���ӳ�ʱ�䣨�룩
    unsigned char   DrawLine;								//�ƶ�����ʱ���߱�־
    unsigned char   NUL[5];
	struct DEFTIME  strategy[7];								//�ƶ���Ⲽ��ʱ��
}__attribute__((packed));
/*********************�Ŷ�ʧ������*************************/
struct SINGLELOST											//��Ƶ�źŶ�ʧ��������
{
	unsigned char  Enable;
	unsigned char  CallCenter;
	unsigned char  Soundor;
	unsigned char  OutChan;
	struct DEFTIME strategy[7];
}__attribute__((packed));
/***********************ģ�����***************************/
#define EXPOSURE_COMP		128
struct SENSORPARAM											//ģ�����
{
	char resolution;                                        //0-1080P;1-960P;2-720P
    char whiteBalance;                                      //0-�Զ�;1-����;2-����;3-Ԥ��;4-ATW;5-�ֶ�
    char redGain;                                           //0-��λ;1-"-";2-"+";3-���ֲ���
    char blueGain;                                          //0-��λ;1-"-";2-"+";3-���ֲ���
    char focusAuto;                                         //0-�Զ�;1-�ֶ�                     �۽�
    char focusChange;                                       //0-ֹͣ;1-��;2-Զ;3-���ֲ���
    char zoomChange;                                        //0-ֹͣ;1-С;2-��;3-���ֲ���       �䱶
    char exposure;                                          //0-�Զ�;1-�ֶ�;2-��������;3-��Ȧ����;
    char shutterSpeed;                                      //0-ֹͣ;1-��;2-��;3-���ֲ���
    char IRISGain;                                          //0-ֹͣ;1-С;2-��;3-���ֲ���
    char exposureGain;                                      //0-ֹͣ;1-"-";2-"+";3-���ֲ���     ����
    char exposureComp;                                      //0-��;1-��
    char exposureCompChange;                                //0-ֹͣ;1-"-";2-"+";3-���ֲ���     ���ⲹ��
    char turn_h;                                            //ˮƽ��ת 0-��;1-��
    char turn_v;                                            //��ֱ��ת 0-��;1-��
    char dayMode;                                           //�Զ��л� 0-��;1-��
    char dayOrNight;                                        //0-ҹģʽ;1-��ģʽ;3-��Ч
    char apertureChange;                                    //0-ֹͣ;1-"-";2-"+";3-���ֲ���     ���
    char cameraMode;                                        //0-ɾ��;1-����Ԥ�õ�;2-Ԥ�õ����;3��Ч
    unsigned char cameraNumber;                             //1-128;Ԥ�õ��
    char zoomSpeed;

	unsigned short luma;									//����
	unsigned short contrast;								//�Աȶ�
	unsigned short saturation;								//���Ͷ�
	unsigned short chroma;									//ɫ�Ȼ�Ҷ�
	unsigned short sharpen;
	unsigned char  resetValue;                              //1-�ظ�Ĭ��ֵʹ�� 0-�ظ�Ĭ��ֵ�ر�
	unsigned char  drcEnable;                               //��̬����0-�� 1-��
	unsigned short drcStrength;                             //��̬ǿ��
	unsigned short denoise2D;                               //2Dȥ��ǿ��
	unsigned char  dn3DEnable;                              //3Dȥ��ʹ��
	unsigned short sfStrength;                              //����ȥ��ǿ��
	unsigned short tfStrength;                              //ʱ��ȥ��ǿ��
	unsigned char  antifogEnable;                           //ȥ��
	unsigned short antifogStrength;                         //ȥ��ǿ��
	unsigned short rGain;                                   //red gain
	unsigned short gGain;                                   //green gain
	unsigned short bGain; 	                                //blue gain
	unsigned char  exposureTime;
    unsigned int zoomPosition;
}__attribute__((packed));
/***********************��Ƶ�������***********************/
struct VODEV
{
	unsigned char mode;
	unsigned char serial;
	unsigned char disp_size;
	unsigned char scroll_time;
}__attribute__((packed));
struct VOPARAM
{
	struct VODEV cvbs;
	struct VODEV vga;
}__attribute__((packed));
typedef int (*CONFIG_CALLBACK)(void * pData);

struct WIRELESS_ALARM
{
	char wireless_name[64];
	char wireless_uuid[16];
	char notifyCenter;										//�ϱ�����
	char enable;
	char triggerBuzzer;										//����������
	char triggerSwitch;										//�������������
	char triggerJpeg;										//����ץ�Ŀ���
	char jpegChn;											//����ץ�ĵ�ͨ��
	char jpegNum;											//����ץ������
	char jpegGop;											//����ץ�ļ��

	char triggerRecord;										//����¼�񿪹�
	char recordChn;											//����¼��ͨ��
	char recordDelay;										//����¼��ʱ��
	char recordPreTime;										//����¼��Ԥ¼ʱ��
	struct DEFTIME  strategy[7];
}__attribute__((packed));

/**********************************************************************/
//module gpio
/**********************************************************************/
typedef enum
{
	GPIO_TYPE_ONCE = 0,									//һ���Կ���
	GPIO_TYPE_CYCLE,									//�����Կ���
	GPIO_TYPE_COUNT
}	e_gpio_control;

/**********************************************************************/
//module system
/**********************************************************************/
typedef int (*SYSTEM_CALLBACK)(void);

/**********************************************************************/
//module fifo
/**********************************************************************/
typedef enum
{
	FIFO_START_BASE = 0,
	FIFO_START_NEW,
	FIFO_START_SECOND,
	FIFO_START_COUNT
}	e_fifo_start;
typedef enum
{
	FIFO_TYPE_MEDIA = 0,
	FIFO_TYPE_JPEG,
	FIFO_TYPE_COUNT
}	e_fifo_type;
typedef struct st_fifo
{
	e_fifo_type enType;
	int iGrp;
	int iChn;

}	st_fifo_t;

typedef enum
{
	FIFO_H264_MAIN = 0,										//������
	FIFO_H264_SUB,
	FIFO_H264_EXT,
	FIFO_H264_AUDIO,
	FIFO_H264_COUNT
}	e_fifo_h264;
typedef int (*FIFO_CALLBACK)(char * pFrameData,int iFrameLen);

//==========================================================
#define FIFO_ALARM_MAX_SHEET	16
typedef enum
{
	FIFO_ALARM_SWITCH = 0,									//������
	FIFO_ALARM_FAULT,										//����
	FIFO_ALARM_CAPACITY,									//����
	FIFO_ALARM_MV,											//�ƶ����
	FIFO_ALARM_OD,											//�ڵ�����
	FIFO_ALARM_SL,											//�źŶ�ʧ
	FIFO_BELL_PRESS,										//���尴��
	FIFO_ALARM_COUNT
}	e_fifo_alarm;

typedef enum
{
	FIFO_ALARM_READ_CURRENT = 0,							//���µı�����ָ��
	FIFO_ALARM_READ_EARLY,									//�����ڵı�����ָ��
	FIFO_ALARM_READ_COUNT
}	e_fifo_read;

typedef struct upload_alarm_info
{
	e_fifo_alarm enAlarm;									//����������
	int iChn;												//ͨ����
	int iArea;												//��ͨ�������������
	int iStatus;											//0-ֹͣ 1��ʼ
	char cInfo[256];										//����˵�������߿��Դ��һЩ�Զ�������
}	st_alarm_upload_t;

/***********************��Ƶ�����ʽ***********************/
typedef enum e_stream_type
{											//����  0-web stream 1-jpeg stream 2-rtp stream 3-avi stream 4-ts stream 5-ps stream 6-h264 stream
	FIFO_STREAM_RTP = 0,
	FIFO_STREAM_AVI,
	FIFO_STREAM_TS,
	FIFO_STREAM_PS,
	FIFO_FILE_WEB2RTP,
	FIFO_FILE_AVI2RTP,
	FIFO_STREAM_H264,
	FIFO_STREAM_AUDIO,
	FIFO_STREAM_COUNT
}	e_fifo_stream;

/**********************************************************************/
//module net
/**********************************************************************/
typedef int (*NET_RECEIVE_CALLBACK)(int iSock,char *pData,int sock_status);
typedef int (*NET_PROTOCOL_CALLBACK)(int iSock,char *pData,int iLen);
typedef int (*UDP_PROTOCOL_CALLBACK)(int iIp,short sPort,int iSock,char *pData,int iLen);
typedef struct net_card
{
	int sDevice;                            //����״̬�������������
	int iIP;
	char devName[16];
}	NET_CARD;

typedef struct net_statue
{
	NET_CARD lan;					        //sDevice ��������״̬0-δ������;1-δ�ҵ������豸;0x10-10M;0x11-100M;
	NET_CARD adsl;
	NET_CARD wifi;
}	st_net_status_t;

typedef enum e_net_rtsp
{
	NET_RTSP_MEDIA = 0,
	NET_RTSP_FILE,
	NET_RTSP_SETUP,
	NET_RTSP_PLAY,
	NET_RTSP_COUNT
}	e_rtsp_type;

typedef struct st_rtsp_real
{
	char level[128];
	char param[128];
	int  width;
	int  high;
	int  hasAudio;
}	st_rtsp_real_t;

typedef struct st_rtsp_history
{
	char level[128];
	char param[128];
	int  width;
	int  high;
	int  interval;
	int  hasAudio;
}	st_rtsp_history_t;

typedef struct st_rtsp_steup
{
	int  isTcp;
	char cTarget[128];
	int  iClientPort1;
	int  iClientPort2;
	char cSource[128];
	int  iServerPort1;
	int  iServerPort2;

}	st_rtsp_setup_t;

typedef struct st_rtsp_play
{
	int  isTcp;

	int  iSpeed;
	int  iPullTime;
	char cFileName[128];
	int  v_rtptime;
	int  v_rtpseq;
	int  a_rtptime;
	int  a_rtpseq;

}	st_rtsp_play_t;

typedef enum e_encrypt_mode_
{
	NONE = 0,
	WEP,
	WPA,
	WPA2,
	MODE_COUNT
}	e_encrypt_mode;

typedef enum e_encrypt_format_
{
	ASCII = 0,
	HEX,
	TKIP,
	AES,
	FORMAT_COUNT
}	e_encrypt_format;

typedef struct st_wifi_list
{
	char ssid[32];
	char key[32];
	int  enable;							//����ʹ�õ�
	e_encrypt_mode encryptMode;				//���ܷ�ʽ
	e_encrypt_format encryptFormat;			//���ܸ�ʽ
	int	 wepPosition;						//wep��ʽ�µ�����λ��
	int  signalStrength;					//�ź�ǿ��

}	st_wifi_list_t;
typedef int (*SOUND_CALLBACK)(char * pFileName);

/**********************************************************************/
//module Uart
/**********************************************************************/
typedef int (*UART_RECEIVE_CALLBACK)(int handle,char *pData,int iMaxSize);
typedef int (*UART_PROTOCOL_CALLBACK)(int handle,char *pData,int iLen);
typedef struct uart_attr
{
	char ubaud;		//0-9  1200-...
	char udata;
	char ucheck;
	char ustop;
}	st_uart_attr_t;
typedef struct attr_485
{
	char enable;
	char gpio_group;
	char gpio_bit;
}	st_485_ctrl_t;

/**********************************************************************/
//module Utility
/**********************************************************************/
typedef struct ST_IOV
{
	int iov_len;						/**< Size of data */
	void * iov_base;					/**< Pointer on data */
}__attribute__((packed))st_iov_t;

#endif		//COMMAND_DEFINE_H
