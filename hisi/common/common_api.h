#ifndef COMMON_API_H
#define COMMON_API_H

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include "common_define.h"

#ifdef __cplusplus
extern "C" {
#endif

/**********************************************************************/
//module config
/**********************************************************************/
int CONFIG_Initialize(void);															//��ʼ����������ģ��
int CONFIG_Cleanup(void);																//�رղ�������ģ��
int CONFIG_Register_Callback(e_config_type enAttrId,CONFIG_CALLBACK config_callback);	//ע�����ò����Ļص�����
int CONFIG_Get(e_config_type enAttrId,void * pData);									//��ȡĳ�����
int CONFIG_Set(e_config_type enAttrId,void * pData);									//����ĳ�����
/**********************************************************************/
//module clock
/**********************************************************************/
int CLOCK_Open(e_clock_type enType);													//����ʱ�ӹ���
int CLOCK_Close(int handle);															//�ر�ʱ�ӹ���
int CLOCK_Get(st_clock_t *);															//��ȡ��ǰʱ��
int CLOCK_Set(st_clock_t *);															//����ϵͳʱ��
int CLOCK_Get_Error(int handle);
int CLOCK_Version(void);																//1.00.00=0x10000  0.00.01=0x000001
int CLOCK_Set_NTP(int ip,short port,signed int zero);									//�������NTPУʱ��������ȵ��ô˽ӿ�����NTP�ķ�������ʱ��
/**********************************************************************/
//module gpio
/**********************************************************************/
int GPIO_Open(char *devName);															//��gpio�豸
int GPIO_Close(int handle);																//�ر�gpio�豸
int GPIO_Set_Dir(int gpio_group,int gpio_bit,int value);								//����ĳ��gpio�������������0-input 1-output
int GPIO_Set_Value(int gpio_group,int gpio_bit,int value);								//�������ģʽ��gpio���value״̬
int GPIO_Get_Value(int gpio_group,int gpio_bit);										//��ȡ����ģʽ��gpio�ڵĵ�ǰ״̬
int GPIO_Control(int gpio_group,int gpio_bit,e_gpio_control enCtrl,int ms_value,int isHigh);//�������ģʽ��gpio���ղ����������enCtrl��Ϊ�����Ժ�һ�������֣�
																						//������ms_value��ʾ�ߵͱ仯��ͣ��ʱ��
																						//һ���Ե�ms_valueΪ0ʱ���ʾһֱ���ó�isHigh״̬��ms_value��Ϊ0���ʾ��ms_valueʱ���ڱ���isHigh״̬������ʱ��״̬ͣ���ڷ�ת��״̬
/**********************************************************************/
//module system
/**********************************************************************/
int SYSTEM_Initialize(void);															//��ʼ��ϵͳ
int SYSTEM_Cleanup(void);																//ϵͳģ���˳�
int SYSTEM_Get_Mem(int *total,int *free);												//��ȡ�ڴ���������ʣ��������ռ����
int SYSTEM_Get_Cpu(void);																//��ȡcpuռ����
int SYSTEM_Set_Reboot(void);															//��������ϵͳ
int SYSTEM_Set_RestartApp(void);														//����Ӧ�ó���
int SYSTEM_Get_Run(void);																//��ȡ��ǰ�Ƿ�Ҫreboot�ı�־
int SYSTEM_Command(char *command);														//ϵͳ������ã���װ��system��ִ��ʱ����
int SYSTEM_Register_Callback(SYSTEM_CALLBACK system_reboot_callback);					//ϵͳ������ʱ��ص���zmqclient

/**********************************************************************/
//module fifo
/**********************************************************************/
int FIFO_Initialize(void);																//��ʼ��FIFO����ģ��
int FIFO_Create(st_fifo_t stFifo,int iCapacity);										//����һ��FIFO
int FIFO_Write(int iHandle,char *pData,int iLen);										//��FIFO�Ļ���������д������
int FIFO_Destory(int iHandle);															//���ٴ�����FIFO
int FIFO_Request(st_fifo_t stFifo,int * pHandle,int second);							//����һ��FIFO�Ķ�ָ��,�˽ӿڻ᷵���������FIFO�ľ��
int FIFO_Get_MediaInfo(st_fifo_t stFifo,char * sps,int * sps_len,char *pps,int * pps_len);//��ȡH264��sps��pps
int FIFO_Read(int iHandle,int rId,char *pData);											//��ȡFIFO������
int FIFO_Release(int iHandle,int rId);													//�ͷ������FIFO��ָ��
int FIFO_Cleanup(void);																	//FIFOģ��ر�

int FIFO_Register_Callback(e_fifo_h264 enStreamChn,FIFO_CALLBACK fifo_callback);		//ע�ᴿH264�����Ļص�����

int FIFO_Stream_Open(e_fifo_stream enStream,int iGroup,int iChn,int second);			//��ĳ������������ת���̣߳�����Ǵ�H264�����ӻص������������ߣ�ÿ�η���һ֡��iGroup��iChn��0��ʼ
int FIFO_Stream_Set_(int iHandle,char *filename,int speed,int pulltime);				//����һЩ����Ĳ�����ת���߳�
int FIFO_Stream_RequestID(int iHandle);													//����һ����ָ��
//int FIFO_Stream_Write(int iHandle,int iID,char *pData,int iLen);
int FIFO_Stream_Read(int iHandle,int iID,char *pData);									//��ȡת�����������
int FIFO_Stream_ReleaseID(int iHandle,int iID);											//�ͷŶ�ָ��
int FIFO_Stream_Close(int iHandle);														//ֹͣ������ת��

void FIFO_Stream_Get_AviHead(char *head_buf,int *ptr,int reso,int frame);
void FIFO_Stream_Get_HeadIndex(char *index_buf,int *index_len,int len);
void FIFO_Stream_Get_DataIndex(int isAudio,char idrFlag,char *index_buf,int *index_len,int len);

int FIFO_Alarm_Write(st_alarm_upload_t *cInfo);											//д��һ��������Ϣ
int FIFO_Alarm_Get_Wpoint(e_fifo_read enRead);														//��ȡ��ǰ�ı���дָ��
int FIFO_Alarm_Read(int iReader,st_alarm_upload_t *cInfo);								//
/**********************************************************************/
//module net
/**********************************************************************/
int TCP_Server_Create(short sPort,int iMaxConn,int iTimeout);							//����һ��tcp��server
int TCP_Server_Wait(int handle,int *pSock,int iRcvSize,NET_RECEIVE_CALLBACK,NET_PROTOCOL_CALLBACK);//tcp server�ȴ�һ������ ���ݽ��պ�Э�鴦���ڻص������
int TCP_Server_Send(int handle,int link,char *pData,int iLen);							//��ĳ�������Ϸ�������
int TCP_Server_GetLinkIP(int handle,int link,int ip);									//��ȡ���ӹ�����client��ip
int TCP_Server_Finish(int handle,int link);												//�Ͽ�ĳ������
int TCP_Server_Destory(int handle);														//����tcp server��

int TCP_Client_Create(int iIp,short sPort,int iTimeout,int iRcvSize,NET_RECEIVE_CALLBACK,NET_PROTOCOL_CALLBACK);//����һ��tcp client ���ݽ��պ�Э�鴦���ڻص������
int TCP_Client_BindPHY(int iSock,char * phy);											//tcp client�˰�ĳ����������ҪΪ�����豸׼���Ľӿ�
int TCP_Client_Send(int iSock,char *pData,int iLen);									//�ڴ������Ϸ�������
int TCP_Client_Destory(int iSock);														//�Ͽ�tcp���ӣ�����tcp client

int UDP_Server_Create(short sPort,int iRcvSize,UDP_PROTOCOL_CALLBACK);					//����һ��UDP server
int UDP_Server_Send(int iSock,int iIP,short sPort,char *pData,int iLen);				//����udp����
int UDP_Server_Destory(int iSock);														//����udp server

int NET_Initialize(void);																//��ʼ��ϵͳ����
int NET_Get_WanStatus(void);															//�ɴ˽ӿ����ж��豸�Ƿ��ܹ����ӵ�����
int NET_Get_RouteIP(char * cIP);														//��ȡ��ǰ·��ʹ�õ�������ip ����ֵint���͵�ip��cIP���ַ������͵�ip��"192.168.0.230"
int NET_Get_Status(st_net_status_t * status);											//��ȡ����������״̬
int NET_Get_WifiList(int iMaxList,st_wifi_list_t *);									//��ȡwifi�����б� ����ѯiMaxList������wifi�б����棬����ʵ�ʲ�ѯ����������
int NET_Set_Wifi(st_wifi_list_t *);														//��������wifi����
int Net_Get_WifiLink_Status(void);														//����wifi���ӵ�״̬��0-δ��ʼ+δʹ��(��������ͬʱwifiδ����)��1-δ��ʼ+��ʹ��(wifi�����Ѿ����õ���������)��2-��ʼ+δʹ��(���߰ε��˵�wifiδ����)��3-��ʼ���ӻ����������ӣ�4-���ӳɹ�
int NET_Cleanup(void);																	//����ʼ��ϵͳ����

int RTSP_Options(int iSock,char *pData,int iLen,char *pOut);							//
int RTSP_Describe(int iSock,char *pData,int iLen,e_rtsp_type enRtsp,void *pAttr,char *pOut);
int RTSP_Setup(int iSock,char *pData,int iLen,e_rtsp_type enRtsp,void *pAttr,char *pOut);
int RTSP_Play(int iSock,char *pData,int iLen,e_rtsp_type enRtsp,void *pAttr,char *pOut);
int RTSP_Pause(int iSock,char *pData,int iLen,char *pOut);
int RTSP_Teardown(int iSock,char *pData,int iLen,char *pOut);
int RTSP_SetParameter(int iSock,char *pData,int iLen,char *pOut);
int RTSP_SetError(int iSock,char *pData,int iLen,int error,char *pOut);

int gethostbyname_my(char * host,char * ip);											//��ȡ������ip
int searchStr(char *src,char *dst,char *content,int add);								//��ȡ�ı��ַ���
/**********************************************************************/
//module Uart
/**********************************************************************/
int UART_Open(char * pName,UART_RECEIVE_CALLBACK receive,UART_PROTOCOL_CALLBACK protocol);//�򿪴���
int UART_Set_Attr(int handle,st_uart_attr_t * attr,st_485_ctrl_t *ctrl);				//���ô�������
int UART_Send(int handle,char * pData,int iLen);										//�Ӵ��ڷ�������
int UART_Close(int handle);																//�رմ���
/**********************************************************************/
//module Motor
/**********************************************************************/
int MOTOR_Send(char * pData,int iLen);
//iCmdȡֵ��ֹͣ0,��1,��2,��3,��4,����5,����6,����7,����8,//��Ȧ�Զ�9,��Ȧ��10 open,��Ȧ��11 close,��Ȧ�仯ֹͣ12,
//�����13 near,����Զ14 far,����仯ֹͣ15,�䱶С16 IN,�䱶��17 OUT,�䱶�仯ֹͣ18,//�Զ���ʼ19,�Զ�ֹͣ20,
//��ˢ��21,��ˢ��22,�ƹ⿪23,�ƹ��24,����Ԥ�õ�25,����Ԥ�õ�26,���Ԥ�õ�27,
//ģʽ��28,ģʽ��29,����ģʽ30,180�ȷ�ת31,�������Ԥ��λ32,�������Ԥ��λ33
//iSpeed��̨ת���ٶȣ�Ĭ��63
//iParamԤ��λ
int MOTOR_Control(int iChn, int iCmd,int iSpeed,int iParam);
/**********************************************************************/
//module Utility
/**********************************************************************/
int UTILITY_MD5(unsigned char *Src, unsigned char *dst, int len);						//MD5�����㷨
int UTILITY_HMAC1_IOV(st_iov_t* iov,int iov_num,unsigned char* key,int key_len,unsigned char* dst);//��ϣ�����㷨
int UTILITY_BASE64_Encode(unsigned char *src,int len,char *dst);						//base64�����㷨
int UTILITY_MEDIA_Convert_Size(int resolution,int *width,int *high);					//�ֱ���ת���ɿ��	
int UTILITY_MEDIA_Convert_Format(int reso);												//��������ķֱ��ʣ���С�����ʾ�ֱ�����С����),ת���ɿ���Э��ʹ�õķֱ��ʵĶ������
int UTILITY_MEDIA_Convert_Resolution(int format);										//����Э�鴫�����Ĳ���ת���ɳ�����ʵ��Ӧ�õķֱ��ʣ���С�����ʾ�ֱ�����С����)
int UTILITY_B64_ntop(unsigned char const *,size_t, char *,size_t);
int UTILITY_B64_pton(char const *,unsigned char *,size_t);
int UTILITY_String_IP(char *src,char *cip,int *iip);									//�ַ���ipת����4�ֽ�ʮ������ip��int����ip
int UTILITY_String_MAC(char *src,char *char_mac,char *hex_mac);							//�ַ���macת����Сдmac�ַ�������ʮ������mac

#ifdef __cplusplus
}
#endif

#endif		//COMMON_API_H
