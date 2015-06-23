package com.yuil.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

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
		public volatile boolean started = false;
		int lastPackageId = 0;
		int port = 9091;

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
			started = false;
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
						clientMap.put(str, client);
					}
					if (message.getSequenceId() != client.getLastSequenceId() + 1) {
						System.err.println("sequenceId 错误!");
						System.out.println(message.getSequenceId());
						UdpMessage responds = new UdpMessage();
						responds.sequenceId = 0;
						responds.type = 3;
						responds.length = 4;
						responds.data = JavaDataConverter.intToBytes(message
								.getSequenceId());
						send(recvPacket.getSocketAddress(), responds);
					} else {

						System.out.println(message.toString());
						client.messageArray.add(message);
						client.lastSequenceId++;
					}
					if (message.type == 0) {
						break;
					}

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
		s1.started = true;
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