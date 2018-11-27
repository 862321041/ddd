package com.webserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 服务端
 * 
 * @author lenovo
 *176.53.7.62
 */
public class WebServer {
	private ServerSocket server;
private ExecutorService threadPool;
	public WebServer() {
		try {
			//设置服务端口号
			server = new ServerSocket(8088);
			//创建一个容量为30的线程池
			threadPool=Executors.newFixedThreadPool(30);
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void start() {
		try {
			/**
			 * 暂时不提供客户端的多次连接，先将一次请求处理测试完毕后在支持该功能
			 */
			while (true) {
				System.out.println("等待一个客户端连接...");
				Socket socket = server.accept();
				System.out.println("一个客户端连接了");
				// 启动线程处理客户端请求
				ClientHandler handler = new ClientHandler(socket);
				//将线程添加到线程池
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
