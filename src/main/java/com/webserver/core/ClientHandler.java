package com.webserver.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.webserver.http.EmptyRequestException;
import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
import com.webserver.servlet.HttpServlet;
import com.webserver.servlet.LoginServlet;
import com.webserver.servlet.RegServlet;

/**
 * �ͻ��˴�����
 * 
 * @author lenovo
 *
 */
public class ClientHandler implements Runnable {
	private Socket socket;

	public ClientHandler(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			// 1׼������
			System.out.println("ClientHandler:��ʼ��������...");
			// ����
			HttpRequest request = new HttpRequest(socket);
			System.out.println("ClientHandler:����������ϣ�");
			// ��Ӧ
			HttpResponse response = new HttpResponse(socket);
			// 2��������
			System.out.println("ClientHandler:��ʼ��������...");
			// �Ȼ�ȡ�û��������Դ·��
			String url = request.getRequestURL();
			//�����ж��Ƿ�Ϊ����ҵ��
			//���ȸ��ݵ�ǰ�����ȡ��Ӧ��Servlet�����֣����������ַ��ȡ��
			String servletName=ServerContext.getServlet(url);
			//�����ֲ�Ϊnull��˵��������ȷʵ��Ӧһ��ҵ��
			if(servletName!=null){
				//���÷�����ز�ʵ������Ӧ��Servlet
				Class cls=Class.forName(servletName);
				//��������
				HttpServlet servlet = (HttpServlet)cls.newInstance();
				//������service���������ҵ��
				servlet.service(request, response);
			}else{
			File file = new File("webapps" + url);
			if (file.exists()) {
				System.out.println("��Դ���ҵ�");
				// ������Դ���õ�response�У�ͬʱ������Ӧͷ
				response.setEntity(file);

			} else {
				System.out.println("��Դδ�ҵ�");
				// ��Ӧ404
				// ����response�е�״̬����Ϊ404
				response.setStatusCode(404);
				// ����404ҳ��
				File notFoundFile = new File("webapps/root/404.html");
				// ������Դ���õ�response�У�������Ӧͷ,Content-Type��Content-Type
				response.setEntity(notFoundFile);
			}
			}

			System.out.println("ClientHandler:����������ϣ�");
			// 3������Ӧ
			System.out.println("ClientHandler:��ʼ������Ӧ...");
			// ����ǰ��Ӧ�����������һ��HTTP��׼��Ӧ��ʽ���͸��ͻ���
			response.flush();
			System.out.println("ClientHandler:��Ӧ�������!");

		} catch(EmptyRequestException e){
			//���������κδ���
			System.out.println("������");
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			// ������ͻ��˶Ͽ����ӵĲ���
			try {
				socket.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

	}

}
