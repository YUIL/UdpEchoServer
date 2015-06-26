package com.yuil.net;

import java.net.SocketAddress;
import java.util.LinkedList;
import java.util.Queue;



public class Client {
	int lastSequenceId=0;
	SocketAddress socketAddress;
	long lastSendTime;
	int timeOut=1000;
	UdpMessage currentSendMessage;
	UdpMessage lastSendMessage;
	
	boolean currentSendMessageLocked=false;

	public Queue<UdpMessage> messageQueue=new LinkedList<UdpMessage>();
	public int getTimeOut() {
		return timeOut;
	}
	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}
	public long getLastSendTime() {
		return lastSendTime;
	}
	public void setLastSendTime(long lastSendTime) {
		this.lastSendTime = lastSendTime;
	}
	public int getLastSequenceId() {
		return lastSequenceId;
	}
	public void setLastSequenceId(int lastSequenceId) {
		this.lastSequenceId = lastSequenceId;
	}
	public  UdpMessage getCurrentSendMessage() {
		//currentSendMessageLocked=true;
		return currentSendMessage;
	}
	public boolean isCurrentSendMessageLocked() {
		return currentSendMessageLocked;
	}
	public void setCurrentSendMessageLocked(boolean currentSendMessageLocked) {
		this.currentSendMessageLocked = currentSendMessageLocked;
	}
	public  void setCurrentSendMessage(UdpMessage currentSendMessage) {
		//if (!currentSendMessageLocked)
			this.currentSendMessage = currentSendMessage;
	}
	public  UdpMessage getLastSendMessage() {
		return lastSendMessage;
	}
	public  void setLastSendMessage(UdpMessage lastSendMessage) {
		this.lastSendMessage = lastSendMessage;
	}
	public SocketAddress getSocketAddress() {
		return socketAddress;
	}
	public void setSocketAddress(SocketAddress socketAddress) {
		this.socketAddress = socketAddress;
	}
	public Queue<UdpMessage> getMessageQueue() {
		return messageQueue;
	}
	public void setMessageQueue(Queue<UdpMessage> messageQueue) {
		this.messageQueue = messageQueue;
	}
	
	
}
