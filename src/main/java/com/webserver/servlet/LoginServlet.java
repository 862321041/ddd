package com.webserver.servlet;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

/**
 * ��¼ҵ��
 * 
 * @author lenovo
 *
 */
public class LoginServlet extends HttpServlet{
	public void service(HttpRequest request, HttpResponse response) {
		System.out.println("��ʼ��¼ҵ��");
		String name = request.getParameter("username");
		String pwd = request.getParameter("password");
		// System.out.println(name+":"+pwd);
		/*
		 * 2.ͨ��RandomAccessFile��ȡuser.dat�ļ����Ա�ÿһ��ע���û������û�������û�����������
		 * ���ļ��е�ĳһ��ע���û���Ϣһ��ʱ����Ӧ�û���¼�ɹ�ҳ�档
		 * ���û���һ�µ����벻һ�»��߸��û�����user.dat�ļ��в�����ʱ����Ӧ�û���¼ʧ��ҳ�档
		 */
		boolean flag = false;
		RandomAccessFile raf = null;
		try {
			// ע��ѭ���жϵ�ǰ�˺ź������Ƿ���ȷ
			raf = new RandomAccessFile("user.dat", "rw");
			// ��ȡ���ɸ�100�ֽڣ�ÿһ���û���Ϣ��
			for (int i = 0; i < raf.length() / 100; i++) {
				//�Ƚ�ָ���ƶ���������¼�û�����λ��
				raf.seek(i * 100);
				byte[] data = new byte[32];
				raf.read(data);
				String names = new String(data, "utf-8").trim();
				raf.read(data);
				String pwds = new String(data, "utf-8").trim();
				if (names.equals(name)) {
					if (pwds.equals(pwd)) {
						// 3.���û���Ӧ��¼�ɹ�ҳ��
						forward("myweb/login_success.html", request, response);
						System.out.println("��¼ҵ����ɡ�");
						//��¼�ɹ�
						flag = true;
					}
					break;
				}

			}
			//�ж��ǵ�¼�Ƿ񲻳ɹ�
			if (!flag) {
				forward("myweb/login_fail.html", request, response);
				System.out.println("��¼ʧ�ܣ���¼ʧ��ҵ����ɡ�");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			try {
				raf.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
