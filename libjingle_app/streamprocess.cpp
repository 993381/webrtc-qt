#include "streamprocess.h"
#include <iostream>
#include "talk/base/timeutils.h"
#include "talk/base/bind.h"
namespace kaerp2p {


const int kWriteBufferSize = 512*1024;
const int kReadBufferSize = 128*1024;
StreamProcess::StreamProcess(talk_base::Thread * stream_thread):
    write_buf_(kWriteBufferSize,stream_thread),
    writeBufferFull(false),stream_thread_(stream_thread),stream_(NULL)
{
    totalread = 0;
}

StreamProcess::~StreamProcess()
{
    LOG_T_F(INFO)<<"delete stream process";
    if(stream_){
        delete stream_;
    }
}

bool StreamProcess::ProcessStream(talk_base::StreamInterface *stream)
{
    ASSERT(stream_thread_->IsCurrent());
    stream->SignalEvent.connect(this, &StreamProcess::OnStreamEvent);
    if (stream->GetState() == talk_base::SS_CLOSED) {
        LOG(LS_ERROR) << "Failed to establish P2P tunnel";
        return false;
    }
    stream_ = stream;
    if (stream->GetState() == talk_base::SS_OPEN) {
        OnStreamEvent(stream,
                      talk_base::SE_OPEN | talk_base::SE_READ | talk_base::SE_WRITE, 0);
    }
    return true;
}

void StreamProcess::OnStreamEvent(talk_base::StreamInterface *stream,
                                  int events, int error) {
    ASSERT(stream==stream_);
    ASSERT(stream_thread_->IsCurrent());

    if (events & talk_base::SE_CLOSE) {
        if (error == 0) {
            LOG(LS_INFO) << "Tunnel closed normally";
        } else {
            LOG(LS_WARNING) << "Tunnel closed with error: " << error << std::endl;
        }
        Cleanup(stream);

        return;
    }
    if (events & talk_base::SE_OPEN) {
        LOG(INFO)<<"StreamProcess::OnStreamEvent"<<"Tunnel Connected";
        SignalOpened();
    }
    size_t count;
    if (events & talk_base::SE_WRITE) {
        WriteStreamInternel();
    }
    if ( events & talk_base::SE_READ) {
        //LOG(LS_VERBOSE) << "Tunnel SE_READ";
        this->ReadStreamInternel();
    }
}

void StreamProcess::ReadStreamInternel()
{
    size_t toRead = kReadBufferSize;
    char readBuffer[kReadBufferSize];
    size_t read = 0;
    int error;
    talk_base::StreamResult result = stream_->Read(readBuffer,toRead,&read,&error);
    if(result == talk_base::SR_SUCCESS){
       // LOG_F(INFO)<<"read "<<read;
        talk_base::Buffer sigBuffer(readBuffer,read);
        this->SignalReadData(this,sigBuffer);
    }else if(result == talk_base::SR_BLOCK){
        //LOG_F(INFO)<<"read block";
    }else{
        LOG_T_F(WARNING)<<" read error "<<result;
    }
//    void * buffer = this->read_buf_.GetWriteBuffer(&toRead);
//    talk_base::Buffer & buffer;
//    if(buffer != NULL && toRead > 0){
//        size_t read = 0;
//        int error;
//        if(read > 0){
//            this->read_buf_.ConsumeWriteBuffer(read);
//            size_t allRead = 0;
//            read_buf_.GetBuffered(&allRead);
//            this->SignalReadData(this,allRead);
//        }
//    }

}

void StreamProcess::WriteStreamInternel()
{
    //const int oneWrite = 1400;
    size_t toWrite = 0;
    size_t written = 0;
    talk_base::StreamResult result;
    const void * buffer = this->write_buf_.GetReadData(&toWrite);
    if(buffer != NULL && toWrite > 0){
        int error;
//        if(toWrite > oneWrite){
//            toWrite = oneWrite;
//        }
        result = stream_->WriteAll(buffer,toWrite,&written,&error);
        if(written > 0){
            this->write_buf_.ConsumeReadData(written);
        }
        if(result == talk_base::SR_SUCCESS ){
            //still have something to write
            size_t leftWrite =0 ;
            write_buf_.GetBuffered(&leftWrite);
            if( leftWrite > 0 ){
                stream_->PostEvent(talk_base::SE_WRITE, 0);
            }else{
            }
        }else{
            //LOG_T_F(INFO)<<"write false "<<result;
        }
    }
}

void StreamProcess::Cleanup(talk_base::StreamInterface *stream, bool delay) {
    LOG(INFO) << "StreamProcess::Cleanup";
    stream->Close();
    SignalClosed(this);
}

void StreamProcess::Close()
{
    if(stream_){
        stream_thread_->Invoke<void>(
                    talk_base::Bind(&StreamProcess::Cleanup,this,stream_,false));
        //Cleanup(stream_);
        delete stream_;
        stream_ = NULL;
    }
}

bool StreamProcess::WriteStream(const char *data, int len)
{
    size_t wlen =0 ;
    write_buf_.GetWriteRemaining(&wlen);
    //if write buffer is full ,wait until the buffer has half space to write
    if(writeBufferFull){
        if(wlen > kWriteBufferSize/2){
            LOG(INFO) << "StreamProcess::WriteStream---"<<
                            "write buffer is now able to use";
            writeBufferFull = false;
        } else {
            return false;
        }
    }
    if(len > wlen){
        LOG(WARNING) << "StreamProcess::WriteStream---"<<
                        "not enough space to write, write len" << len<<
                        " ,remainning "<<wlen<<".wait the buffer clear";
        writeBufferFull = true;
        return false;
    }
    int error;
    talk_base::StreamResult result = write_buf_.Write(data,len,&wlen,&error);
    if(result != talk_base::SR_SUCCESS){
        LOG(INFO) << "StreamProcess::WriteStream---"<<
                     "write_buf_ write buf error ";
        return false;
    }
    stream_->PostEvent(stream_thread_,talk_base::SE_WRITE, 0);
    return true;
}

//bool StreamProcess::ReadStream(void *buffer, size_t bytes, size_t *bytes_read)
//{
//    int error;
//    talk_base::StreamResult result =  read_buf_.Read(buffer,bytes,bytes_read ,&error);
//    if(result != talk_base::SR_SUCCESS)
//    {
//        return false;
//    }
//    return true;
//}


}
