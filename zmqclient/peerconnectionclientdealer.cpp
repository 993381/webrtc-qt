#include "peerconnectionclientdealer.h"

#include "talk/base/logging.h"

// This is our magical hangup signal.
const char kByeMessage[] = "BYE";

//const std::string routerURL = "tcp://192.168.0.182:5555";

PeerConnectionClientDealer::PeerConnectionClientDealer()
{
}

bool PeerConnectionClientDealer::Connect(const std::string &router,
                                        const std::string &id)
{
    dealer_.reset(new AsynDealer());
    dealer_->SignalReadData.connect(
                this,&PeerConnectionClientDealer::OnMessageFromPeer);
    LOG(INFO)<<"connect to "<<router<<"  with id "<<id;
    return  dealer_->initialize(id,router);
}

void PeerConnectionClientDealer::Reconnect()
{
    LOG(INFO)<<"PeerConnectionClientDealer::Reconnect---";
    std::string oldAddr = dealer_->addr();
    std::string oldId = dealer_->id();
    dealer_->terminate();
    dealer_->initialize(oldId,oldAddr);
}


void PeerConnectionClientDealer::SendEcho(const std::string &data)
{
    dealer_->AsynSend(dealer_->id(),data);
}

bool PeerConnectionClientDealer::SendToPeer(const std::string &peer_id,
                                            const std::string &message)
{
    ASSERT(dealer_ != NULL);
    dealer_->AsynSend(peer_id,message);
    return true;
}


bool PeerConnectionClientDealer::IsSendingMessage()
{
    return false;
}

void PeerConnectionClientDealer::OnMessageFromPeer(const std::string &peer_id,
                                                   const std::string &message)
{
    SignalMessageFromPeer(peer_id,message);
}

//void PeerConnectionClientDealer::OnMessageSent()
//{
//    this->SignalMessageSent(0);
//}

