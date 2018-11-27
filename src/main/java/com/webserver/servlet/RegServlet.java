package com.webserver.servlet;

import java.io.File;
import java.io.RandomAccessFile;
import java.security.Provider.Service;
import java.util.Arrays;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

/**
 * ����ע��ҵ��
 * 
 * @author lenovo
 *
 */
public class RegServlet extends HttpServlet{
	public void service(HttpRequest request, HttpResponse respone) {
		/**
		 * 1.��ȡ�û�ע����Ϣ , 2.��ע����Ϣд���ļ�, 3.���û���Ӧע��ɹ�ҳ��
		 */
		System.out.println("RegServlet����ʼ����ע��ҵ��...");
		String usetname = request.getParameter("username");
		String password = request.getParameter("password");
		String nickname = request.getParameter("nickname");
		int age = Integer.parseInt(request.getParameter("age"));
		System.out.println(usetname + ":" + password + ":" + nickname + ":" + age + ">>>>");

		/**
		 * ���û���Ϣд���ļ�user.dat��,ÿһ���û�ռ100���ֽڣ������û��������룬�ǳ�Ϊ �ַ�������ռ32�ֽڣ�����Ϊintֵռ�̶�4�ֽ�
		 */
		try (RandomAccessFile raf = new RandomAccessFile("user.dat", "rw");) {
			raf.seek(raf.length());
			//д����
			byte[] data=usetname.getBytes("utf-8");
			data=Arrays.copyOf(data, 32);
			raf.write(data);
			//д����
			data=password.getBytes("utf-8");
			data=Arrays.copyOf(data, 32);
			raf.write(data);
			//д�ǳ�
			data=nickname.getBytes("utf-8");
			data=Arrays.copyOf(data, 32);
			raf.write(data);
			//д����
			raf.writeInt(age);
			//ע�����
			//3.���û���Ӧע��ɹ�ҳ��
			forward("myweb/login.html", request, respone);
			//raf.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println("RegServlet������ע��ҵ�����!");
	}
}
