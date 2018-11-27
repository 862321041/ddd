package com.webserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * �����
 * 
 * @author lenovo
 *176.53.7.62
 */
public class WebServer {
	private ServerSocket server;
private ExecutorService threadPool;
	public WebServer() {
		try {
			//���÷���˿ں�
			server = new ServerSocket(8088);
			//����һ������Ϊ30���̳߳�
			threadPool=Executors.newFixedThreadPool(30);
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void start() {
		try {
			/**
			 * ��ʱ���ṩ�ͻ��˵Ķ�����ӣ��Ƚ�һ�������������Ϻ���֧�ָù���
			 */
			while (true) {
				System.out.println("�ȴ�һ���ͻ�������...");
				Socket socket = server.accept();
				System.out.println("һ���ͻ���������");
				// �����̴߳���ͻ�������
				ClientHandler handler = new ClientHandler(socket);
				//���߳���ӵ��̳߳�
				threadPool.execute(handler);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		WebServer server = new WebServer();
		server.start();
	}
}
