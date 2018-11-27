package com.webserver.servlet;

import java.io.File;
import java.io.RandomAccessFile;
import java.security.Provider.Service;
import java.util.Arrays;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

/**
 * 处理注册业务
 * 
 * @author lenovo
 *
 */
public class RegServlet extends HttpServlet{
	public void service(HttpRequest request, HttpResponse respone) {
		/**
		 * 1.获取用户注册信息 , 2.将注册信息写入文件, 3.给用户响应注册成功页面
		 */
		System.out.println("RegServlet：开始处理注册业务...");
		String usetname = request.getParameter("username");
		String password = request.getParameter("password");
		String nickname = request.getParameter("nickname");
		int age = Integer.parseInt(request.getParameter("age"));
		System.out.println(usetname + ":" + password + ":" + nickname + ":" + age + ">>>>");

		/**
		 * 将用户信息写入文件user.dat中,每一个用户占100个字节，其中用户名，密码，昵称为 字符串，各占32字节，年龄为int值占固定4字节
		 */
		try (RandomAccessFile raf = new RandomAccessFile("user.dat", "rw");) {
			raf.seek(raf.length());
			//写姓名
			byte[] data=usetname.getBytes("utf-8");
			data=Arrays.copyOf(data, 32);
			raf.write(data);
			//写密码
			data=password.getBytes("utf-8");
			data=Arrays.copyOf(data, 32);
			raf.write(data);
			//写昵称
			data=nickname.getBytes("utf-8");
			data=Arrays.copyOf(data, 32);
			raf.write(data);
			//写年龄
			raf.writeInt(age);
			//注册完毕
			//3.给用户响应注册成功页面
			forward("myweb/login.html", request, respone);
			//raf.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println("RegServlet：处理注册业务完毕!");
	}
}
