package com.yuil.net;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;



public class Client {
	int lastSequenceId=0;
	SocketAddress socketAddress;
	public List<UdpMessage> messageArray=new ArrayList<UdpMessage>();
	public int getLastSequenceId() {
		return lastSequenceId;
	}
	public void setLastSequenceId(int lastSequenceId) {
		this.lastSequenceId = lastSequenceId;
	}
	public SocketAddress getSocketAddress() {
		return socketAddress;
	}
	public void setSocketAddress(SocketAddress socketAddress) {
		this.socketAddress = socketAddress;
	}
	public List<UdpMessage> getMessageArray() {
		return messageArray;
	}
	public void setMessageArray(List<UdpMessage> messageArray) {
		this.messageArray = messageArray;
	}
	
	
}
