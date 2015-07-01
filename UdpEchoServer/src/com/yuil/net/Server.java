package com.yuil.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonStreamParser;
import com.yuil.util.JavaDataConverter;

public class Server {
	public Map<String, Client> clientMap = new HashMap<String, Client>();
	public List<Client> clientList=new ArrayList<Client>();
	public Map<String, Client> userMap = new HashMap<String, Client>();

	private DatagramSocket serverSocket;
	
	public static void main(String args[]) {
		Server server = new Server();
		try {
			server.startService(9091);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class RecvServicer implements Runnable {
		//public volatile boolean started = false;

		int port = 9091;
		long lastMessageTime=0;
		long currentTime;
		UdpMessage responds = new UdpMessage();

		public RecvServicer() throws SocketException {
			serverSocket = new DatagramSocket(port);
		}

		public RecvServicer(int port) throws SocketException {
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
						clientList.add(client);
					}
					if (message.getSequenceId() != client.getLastSequenceId() + 1) {
						System.out.println(client.getSocketAddress().toString());
						System.err.println("sequenceId error!");
						System.out.println(message.getSequenceId());
						responds.sequenceId = client.lastSequenceId;
						responds.type = 3;
						responds.length = 4;
						responds.data = JavaDataConverter.intToBytes(client.getLastSequenceId());
					} else {
						currentTime=System.currentTimeMillis();
						System.out.println("Delay:"+(currentTime-lastMessageTime));
						lastMessageTime=currentTime;
						System.out.println(client.getSocketAddress().toString());
						System.out.println(message.toString());
						client.messageQueue.add(message);
						client.lastSequenceId++;
						
						responds.sequenceId = message.getSequenceId();
						responds.type = 2;
						responds.length = 4;
						responds.data = JavaDataConverter.intToBytes(client.getLastSequenceId());
					}
					if (message.type == 0) {
						break;
					}
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

		RecvServicer s1 = new RecvServicer(port);

		System.out.println("startService");

		new Thread(s1).start();


		while(true){
			for (Iterator iterator = clientList.iterator(); iterator.hasNext();) {
				Client  client = (Client) iterator.next();
				
				for (int i = 0; i < client.messageQueue.size(); i++) {
					UdpMessage message = client.messageQueue.poll();
					String str=new String(message.data);
					System.out.println(str);
					JsonStreamParser parser=new JsonStreamParser(str);
					JsonElement element=parser.next();
					if(element.getAsJsonObject().get("login").getAsJsonObject().get("userName").getAsString().equals("123")){
						System.out.println("userName right!");
					}
				}
			}
		}
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
