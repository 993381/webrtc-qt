#include "system.h"
#include "../common_api.h"

int g_iWtdgFd=-1;
int g_iThreadStatus_system = 0;
pthread_mutex_t mutex_clientcom;
SYSTEM_CALLBACK system_reboot_callback=0;

pthread_t g_thread_system;
void * system_thread(void * this)
{
    printf("system start pid %d thread_id=%d\n",getpid(),(int)pthread_self());
	int iCnt=0;
	int iWatchTime=50;

	g_iWtdgFd = open("/dev/watchdog", O_RDWR);
    if (g_iWtdgFd >= 0)
    	ioctl(g_iWtdgFd,WDIOC_SETTIMEOUT,&iWatchTime);

    while (g_iThreadStatus_system == 1)
    {
        iCnt++;
        if (iCnt >= 10)
        {
            if (g_iWtdgFd >= 0)
                ioctl(g_iWtdgFd,WDIOC_KEEPALIVE,0);
            iCnt = 0;
        }
        cal_mem_occupy();
        cal_cpu_occupy();
        sleep(1);
    }
    close(g_iWtdgFd);								//��ֹĳЩģ���쳣��ʱ�߳������������豸��������
    g_iWtdgFd = -1;
    printf("%s:%d\n",__FUNCTION__,__LINE__);
    return this;
}

int SYSTEM_Initialize(void)
{
	pthread_mutex_init(&mutex_clientcom,NULL);
	g_iThreadStatus_system = 1;
	pthread_create(&g_thread_system,NULL,system_thread,NULL);
	return 0;
}

int SYSTEM_Cleanup(void)
{
	g_iThreadStatus_system = 0;
	pthread_join(g_thread_system,NULL);
	pthread_mutex_destroy(&mutex_clientcom);
	return 0;
}

int SYSTEM_Register_Callback(SYSTEM_CALLBACK system_reboot_callback)
{
	system_reboot_callback=system_reboot_callback;
	return 0;
}

/**********************************************************/
//����:					system reboot
//����:ϵͳ��������
//����:	input	����ģ����
//		output	0-�ɹ���-1-ʧ��
//����:	������
//ʱ��:	2014-03-07
/**********************************************************/
int iSystemReboot = 0;
int SYSTEM_Set_Reboot(void)
{
	iSystemReboot = 1;
	if (system_reboot_callback != 0)
		system_reboot_callback();
	return 0;
}

/**********************************************************/
//����:					system reboot app
//����:Ӧ�ó�����������
//����:	input	����ģ����
//		output	0-�ɹ���-1-ʧ��
//����:	������
//ʱ��:	2014-03-07
/**********************************************************/
int SYSTEM_Set_RestartApp(void)
{
	iSystemReboot = 2;
	if (system_reboot_callback != 0)
		system_reboot_callback();
	return 0;
}

int SYSTEM_Get_Run(void)
{
    return iSystemReboot;
}

/**********************************************************/
//����:					system state
//����:��ѯϵͳ״̬
//����:	������
//ʱ��:	2014-03-7
/**********************************************************/
int SYSTEM_Get_Mem(int *total,int *free)
{
	* total = mem.total;
    * free  = mem.free;
    return memUsageRate;
}

int SYSTEM_Get_Cpu(void)
{
	return cpuUsageRate;
}

int SYSTEM_Command(char *command)
{
	int iRet;
	pthread_mutex_lock(&mutex_clientcom);
	iRet = system(command);
	pthread_mutex_unlock(&mutex_clientcom);
	if (iRet < 0)
        printf("ִ��%sʧ��\n",command);
    else
        printf("ִ��%s���\n",command);
	return iRet;
}


