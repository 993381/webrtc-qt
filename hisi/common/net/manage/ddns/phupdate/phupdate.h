// phupdate.h: interface for the CBaseThread class.
//
//////////////////////////////////////////////////////////////////////
/* ! \file phupdate.h
  \author skyvense
  \date   2009-09-14
  \brief PHDDNS �ͻ���ʵ��
*/

#ifndef _PHUPDATE_H_
#define _PHUPDATE_H_

#include "../ddns.h"
#include "../phglobal/phglobal.h"
#include "../phsocket/phsocket.h"
#include "../../../../common_api.h"

// ! ������DDNS�ͻ���ʵ�ֻ���
/* !
*/
// ! �������ã����úò�������Ҫ��������˺��������������´���Ҫִ�б�������ʱ�䣨������
extern	int phddns_step(PHGlobal *phglobal);

// ! ֹͣ������DDNS���£��������ò�����ɽ�����һ��
extern	void phddns_stop(PHGlobal *phglobal);

// ! ��ʼ��socket
extern	unsigned int  InitializeSockets(PHGlobal *phglobal);
// ! �ر�����socket
extern	unsigned int DestroySockets(PHGlobal *phglobal);
// ! ��DDNS���������ӵ�TCP������
//extern	int ExecuteUpdate(PHGlobal *phglobal,int iSock);
// ! ����UDP�����ӡ�
//extern	unsigned int BeginKeepAlive(PHGlobal *phglobal,int iSock);
// ! ����һ��UDP������
//extern	unsigned int SendKeepAlive(PHGlobal *phglobal, int iSock,int opCode);
// ! ��������������
//extern	int RecvKeepaliveResponse(PHGlobal *phglobal,int iSock);

#endif
