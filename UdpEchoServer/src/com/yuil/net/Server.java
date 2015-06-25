package com.yuil.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.crypto.Data;

import com.yuil.util.JavaDataConverter;

public class Server {
	public Map<String, Client> clientMap = new HashMap<String, Client>();
	private DatagramSocket serverSocket;

	public static void main(String args[]) {
		Server server = new Server();
		try {
			server.startService(9091);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class Servicer implements Runnable {
		//public volatile boolean started = false;
		int lastPackageId = 0;
		int port = 9091;
		long lastMessageTime=0;
		long currentTime;
		UdpMessage responds = new UdpMessage();

		public Servicer() throws SocketException {
			serverSocket = new DatagramSocket(port);
		}

		public Servicer(int port) throws SocketException {
			this.port = port;
			serverSocket = new DatagramSocket(port);
			
		}

		@Override
		public void run() {
			udpService();
			System.out.println("service over!");
		}

		private void udpService() {
			while (true) {
				try {
					byte[] recvBuf = new byte[65515];
					DatagramPacket recvPacket = new DatagramPacket(recvBuf,
							recvBuf.length);
					serverSocket.receive(recvPacket);
					String str = recvPacket.getSocketAddress().toString();
					UdpMessage message = new UdpMessage(recvPacket);
					Client client = clientMap.get(str);
					if (client == null) {
						client = new Client();
						client.setSocketAddress(recvPacket.getSocketAddress());
						clientMap.put(str, client);
					}
					if (message.getSequenceId() != client.getLastSequenceId() + 1) {
						System.err.println("sequenceId error!");
						System.out.println(message.getSequenceId());
					} else {
						currentTime=System.nanoTime();
						System.out.println("Delay:"+(currentTime-lastMessageTime));
						lastMessageTime=currentTime;
						System.out.println(client.getSocketAddress().toString());
						System.out.println(message.toString());
						client.messageArray.add(message);
						client.lastSequenceId++;
					}
					if (message.type == 0) {
						break;
					}
					responds.sequenceId = message.getSequenceId();
					responds.type = 2;
					responds.length = 4;
					responds.data = JavaDataConverter.intToBytes(client.getLastSequenceId());
					send(recvPacket.getSocketAddress(), responds);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			serverSocket.close();

		}

		public void setPort(int port) {
			this.port = port;
		}

		public void send(SocketAddress address, UdpMessage message) {
			DatagramPacket sendPacket = new DatagramPacket(message.toBytes(),
					message.toBytes().length, address);
			try {
				serverSocket.send(sendPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void startService(int port) throws Exception {

		Servicer s1 = new Servicer(port);

		System.out.println("startService");

		new Thread(s1).start();

	}

	public void SendUdpMessage(int src, Client client, UdpMessage message) {
		byte[] temp = message.toBytes();
		DatagramPacket sendPacket = new DatagramPacket(temp, temp.length,
				client.getSocketAddress());
		try {
			serverSocket = new DatagramSocket(src);
			serverSocket.send(sendPacket);
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void send(int src, int dst, UdpMessage message) {
		try {
			serverSocket = new DatagramSocket(src);
			DatagramPacket sendPacket = new DatagramPacket(message.toBytes(),
					message.toBytes().length, InetAddress.getLocalHost(), dst);
			serverSocket.send(sendPacket);
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
