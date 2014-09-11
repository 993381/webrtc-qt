#ifndef __MY_CONVERT_TS_H__
#define __MY_CONVERT_TS_H__

#include "../fifo.h"
#include "../../common_define.h"

#define TS_PACKET_SIZE					188
#define TS_MAX_OUT_BUFF					200*1024
#define TS_SYNC_BYTE					0x47
#define TS_PAT_PID						0x00
#define TS_PMT_PID						0x81
#define TS_H264_PID						0x810
#define TS_AAC_PID						0x814
#define TS_H264_STREAM_ID				0xE0
#define TS_AAC_STREAM_ID				0xC0
#define	FPS								25

typedef struct Tag_PacketHeader
{
    unsigned sync_byte							: 8;
    unsigned transport_error_indicator			: 1;
    unsigned payload_unit_start_indicator		: 1;
    unsigned transport_priority					: 1;
    unsigned PID								: 13;
    unsigned transport_scrambling_control		: 2;
    unsigned adaption_field_control				: 2;
    unsigned continuity_counter					: 4;
}__attribute__((packed))TsPacketHeader;

typedef struct Tag_TsPat
{
    unsigned table_id							: 8;
    unsigned section_syntax_indicator			: 1;
    unsigned zero								: 1;
    unsigned reserved_1							: 2;
    unsigned section_length						: 12;
    unsigned transport_stream_id				: 16;
    unsigned reserved_2							: 2;
    unsigned version_number						: 5;
    unsigned current_next_indicator				: 1;
    unsigned section_number						: 8;
    unsigned last_section_number				: 8;
    	
	unsigned int program_number					: 16;			//��Ŀ��
	unsigned char reserved_3					: 3;			//����λ
	unsigned int program_map_PID				: 13;			//��Ŀӳ����PID����Ŀ�Ŵ���0ʱ��Ӧ��PID��ÿ����Ŀ��Ӧһ��
	unsigned long CRC_32						: 32;			//CRC32У����
} __attribute__((packed))TsPat;

typedef struct TS_PMT
{
    unsigned table_id							: 8;
    unsigned section_syntax_indicator			: 1;
    unsigned zero								: 1;
    unsigned reserved_1							: 2;
    unsigned section_length						: 12;
    unsigned program_number						: 16;
    unsigned reserved_2							: 2;
    unsigned version_number						: 5;
    unsigned current_next_indicator				: 1;
    unsigned section_number						: 8;
    unsigned last_section_number				: 8;
    unsigned reserved_3							: 3;
    unsigned PCR_PID							: 13;
    unsigned reserved_4							: 4;
    unsigned program_info_length				: 12;
   
	unsigned char stream_type_video				: 8;			//ָʾ�ض�PID�Ľ�ĿԪ�ذ������͡��ô�PID��elementary PIDָ��
	unsigned char reserved_5_video				: 3;			//0x07
	unsigned int elementary_PID_video			: 13;			//����ָʾTS����PIDֵ����ЩTS��������صĽ�ĿԪ��
	unsigned char reserved_6_video				: 4;			//0x0F
	unsigned int ES_info_length_video			: 12;			//ǰ��λbitΪ00������ָʾ��������������ؽ�ĿԪ�ص�byte��
	unsigned char stream_type_audio				: 8;			//ָʾ�ض�PID�Ľ�ĿԪ�ذ������͡��ô�PID��elementary PIDָ��
	unsigned char reserved_5_audio				: 3;			//0x07
	unsigned int elementary_PID_audio			: 13;			//����ָʾTS����PIDֵ����ЩTS��������صĽ�ĿԪ��
	unsigned char reserved_6_audio				: 4;			//0x0F
	unsigned int ES_info_length_audio			: 12;			//ǰ��λbitΪ00������ָʾ��������������ؽ�ĿԪ�ص�byte��
	unsigned long CRC_32						: 32;			//CRC32У����
} __attribute__((packed))TsPmt;

//PTS_DTS�ṹ�壺���������ö��� ��11��
typedef struct Tag_TsPtsDts
{
	unsigned char reserved_1 : 4;
	unsigned char pts_32_30  : 3;                //��ʾʱ���
	unsigned char marker_bit1: 1;
	unsigned int  pts_29_15 : 15;
	unsigned char marker_bit2 : 1;
	unsigned int  pts_14_0 : 15;
	unsigned char marker_bit3 :1 ;
	unsigned char reserved_2 : 4;
	unsigned char dts_32_30: 3;                  //����ʱ���
	unsigned char marker_bit4 :1;
	unsigned int  dts_29_15 :15;
	unsigned char marker_bit5: 1;
	unsigned int  dts_14_0 :15;
	unsigned char marker_bit6 :1 ;
}__attribute__((packed))TsPtsDts;

//PES���ṹ�壬����PES��ͷ��ES���� ,ͷ 19 ���ֽ�
typedef struct Tag_TsPes
{
	unsigned int   packet_start_code_prefix : 24;//��ʼ��0x000001
	unsigned char  stream_id : 8;                //�����������ͺͱ��
	unsigned int   PES_packet_length : 16;       //������,����֡���ݵĳ��ȣ�����Ϊ0,Ҫ�Լ���
	unsigned char  marker_bit:2;                 //�����ǣ�'10'
	unsigned char  PES_scrambling_control:2;     //pes����Ч�غɵļ��ŷ�ʽ
	unsigned char  PES_priority:1;               //��Ч���ص����ȼ�
	unsigned char  data_alignment_indicator:1;   //�������Ϊ1����PES����ͷ�����������Ƶ����Ƶsyncword��ʼ�Ĵ��롣
	unsigned char  copyright:1;                  //1:����Ȩ������0������
	unsigned char  original_or_copy:1;           //1;��Ч������ԭʼ�ģ�0����Ч����ʱ������
	unsigned char  PTS_DTS_flags:2;              //'10'��PTS�ֶδ��ڣ���11����PTD��DTS�����ڣ���00������û�У���01�������á�
	unsigned char  ESCR_flag:1;                  //1:escr��׼�ֶ� �� escr��չ�ֶξ����ڣ�0�����κ�escr�ֶδ���
	unsigned char  ES_rate_flag:1;               //1:es_rate�ֶδ��ڣ�0 ��������
	unsigned char  DSM_trick_mode_flag:1;        //1;8�����ؽӷ�ʽ�ֶδ��ڣ�0 ��������
	unsigned char  additional_copy_info_flag:1;  //1:additional_copy_info���ڣ�0: ������
	unsigned char  PES_CRC_flag:1;               //1:crc�ֶδ��ڣ�0��������
	unsigned char  PES_extension_flag:1;         //1����չ�ֶδ��ڣ�0��������
	unsigned char  PES_header_data_length :8;    //�������ݵĳ��ȣ�
	TsPtsDts       tsptsdts;                     //ptsdts�ṹ�����10���ֽ�
	unsigned char  *Es;							 //һ֡ ԭʼ����
	unsigned int   Pes_Packet_Length_Beyond;
}__attribute__((packed))TsPes;

//����Ӧ�α�־
typedef struct Tag_Ts_Adaptation_field
{
	unsigned char discontinuty_indicator:1;                //1������ǰ����������Ĳ�����״̬Ϊ��
	unsigned char random_access_indicator:1;               //������һ������ͬPID��PES����Ӧ�ú���PTS�ֶκ�һ��ԭʼ�����ʵ�
	unsigned char elementary_stream_priority_indicator:1;  //���ȼ�
	unsigned char PCR_flag:1;                              //����pcr�ֶ�
	unsigned char OPCR_flag:1;                             //����opcr�ֶ�
	unsigned char splicing_point_flag:1;                   //ƴ�ӵ��־       
	unsigned char transport_private_data_flag:1;           //˽���ֽ�
	unsigned char adaptation_field_extension_flag:1;       //�����ֶ�����չ

	unsigned char adaptation_field_length;                 //����Ӧ�γ���
	unsigned long long  pcr;                               //����Ӧ�����õ��ĵ�pcr
	unsigned long long  opcr;                              //����Ӧ�����õ��ĵ�opcr
	unsigned char splice_countdown;
	unsigned char private_data_len;
	unsigned char private_data [256];
}Ts_Adaptation_field;


extern void *convert_ts_stream(void * cvt);

#endif	//__MY_CONVERT_TS_H__
