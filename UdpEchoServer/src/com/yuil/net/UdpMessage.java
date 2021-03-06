package com.yuil.net;

import java.net.DatagramPacket;
import java.util.Arrays;

import com.yuil.util.JavaDataConverter;

public class UdpMessage {
	public int sequenceId;
	public int type;
	public int length;
	public byte[] data;

	public UdpMessage(){
		
	}
	public UdpMessage(DatagramPacket recvPacket) {
		initUdpMessageByDatagramPacket(this, recvPacket);
	}

	public void initUdpMessageByDatagramPacket(UdpMessage message,
			DatagramPacket recvPacket) {
		message.setSequenceId(JavaDataConverter.bytesToInt(JavaDataConverter
				.subByte(recvPacket.getData(), 4, 0)));
		message.setType(JavaDataConverter.bytesToInt(JavaDataConverter.subByte(
				recvPacket.getData(), 4, 4)));
		message.setLength(JavaDataConverter.bytesToInt(JavaDataConverter
				.subByte(recvPacket.getData(), 4, 8)));
		message.initDateFromUdpbytes(recvPacket.getData());
	}

	public void initUdpMessageByDatagramPacket(
			DatagramPacket recvPacket) {
		setSequenceId(JavaDataConverter.bytesToInt(JavaDataConverter
				.subByte(recvPacket.getData(), 4, 0)));
		setType(JavaDataConverter.bytesToInt(JavaDataConverter.subByte(
				recvPacket.getData(), 4, 4)));
		setLength(JavaDataConverter.bytesToInt(JavaDataConverter
				.subByte(recvPacket.getData(), 4, 8)));
		initDateFromUdpbytes(recvPacket.getData());
	}
	public int getSequenceId() {
		return sequenceId;
	}

	public void setSequenceId(int sequenceId) {
		this.sequenceId = sequenceId;
	}

	public int getType() {
		return type;
	}

	@Override
	public String toString() {
		return "UdpMessage [sequenceId=" + sequenceId + ", type=" + type
				+ ", lenth=" + length + ", data=" + Arrays.toString(data) + "]";
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int lenth) {
		this.length = lenth;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public void initDateFromUdpbytes(byte[] data) {
		this.data = new byte[this.length];
		System.arraycopy(data, 12, this.data, 0, this.length);
	}

	public byte[] toBytes() {
		byte[] dest = new byte[12 + data.length];
		System.arraycopy(JavaDataConverter.intToBytes(sequenceId), 0, dest, 0,4);
		System.arraycopy(JavaDataConverter.intToBytes(type), 0, dest, 4, 4);
		System.arraycopy(JavaDataConverter.intToBytes(length), 0, dest, 8, 4);
		System.arraycopy(data, 0, dest, 12, data.length);
		return dest;
	}
}
