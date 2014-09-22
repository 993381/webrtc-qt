#ifndef __MY_NTP_CLOCK_H__
#define __MY_NTP_CLOCK_H__

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <sys/ioctl.h>
#include <arpa/inet.h>
#include <net/if_arp.h>
#include <netinet/in.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <sys/time.h>
#include "../clock.h"

struct ntp_timestamp
{
	int sec;			//��������
	int sec_fraction;   //С������
};

typedef struct ntp_message
{
	char flags;       		//�汾��Ϣ
	char stratum;     		//�����������ļ���
	char poll;        		//������Ϣ��������
	char precision;   		//ʱ�Ӿ�ȷ��

	int root_delay;   		//�ο�Դ�����ӳ�
	int root_dispersion;	//��Ҫ�ο�Դ���������
	int reference_identifier;//ʶ������ο�Դ
	struct ntp_timestamp reference_timestamp;   //��ǰʱ�Ӳο�Դ����������һ�θ��µ�ʱ��
	struct ntp_timestamp originate_timestamp;   //�ͻ�����ʱ��
	struct ntp_timestamp receive_timestamp;     //����ʱ��
	struct ntp_timestamp transmit_timestamp;    //��������ͻ���ʱ�����ʱ��
}	st_ntp_request;

extern int open_ntp(st_clock_hanndle_t * p_st_handle);
extern int close_ntp(st_clock_hanndle_t * p_st_handle);
extern int read_ntp(st_clock_t * p_st_clock);
extern int write_ntp(st_clock_t * p_st_clock);
extern int write_ntp_server(int ip,short port,signed int zero);

#endif


