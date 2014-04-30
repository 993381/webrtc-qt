/*
 * KeJniTunnelClient.cpp
 *
 *  Created on: 2014-4-1
 *      Author: lht
 */

#include "KeJniTunnelClient.h"
#include "talk/base/bind.h"
#include "JniUtil.h"
KeJniTunnelClient::KeJniTunnelClient() {
}

void KeJniTunnelClient::OnTunnelOpened(PeerTerminalInterface * t,const std::string & peer_id){
	KeTunnelClient::OnTunnelOpened(t,peer_id);
	JniUtil::GetInstance()->JniTunnelMethodCallback( "TunnelOpened" , peer_id.c_str() );
}
void KeJniTunnelClient::OnRecvAudioData(const std::string& peer_id,
		const char* data, int len) {
	 JniUtil::GetInstance()->JniTunnelMethodCallback( "RecvAudioData" , peer_id.c_str() , data , len);

}

void KeJniTunnelClient::OnRecvVideoData(const std::string& peer_id,
		const char* data, int len) {
	 JniUtil::GetInstance()->JniTunnelMethodCallback( "RecvVideoData" , peer_id.c_str() , data , len);

}

void KeJniTunnelClient::OnTunnelClosed(PeerTerminalInterface* t,
		const std::string& peer_id) {
	KeTunnelClient::OnTunnelClosed(t,peer_id);
	JniUtil::GetInstance()->JniTunnelMethodCallback("TunnelClosed",peer_id.c_str());
}