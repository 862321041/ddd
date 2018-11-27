package com.webserver.servlet;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

/**
 * 登录业务
 * 
 * @author lenovo
 *
 */
public class LoginServlet extends HttpServlet{
	public void service(HttpRequest request, HttpResponse response) {
		System.out.println("开始登录业务");
		String name = request.getParameter("username");
		String pwd = request.getParameter("password");
		// System.out.println(name+":"+pwd);
		/*
		 * 2.通过RandomAccessFile读取user.dat文件，对比每一个注册用户，当用户输入的用户名与密码与
		 * 该文件中的某一个注册用户信息一致时，响应用户登录成功页面。
		 * 若用户名一致但密码不一致或者改用户名在user.dat文件中不存在时都响应用户登录失败页面。
		 */
		boolean flag = false;
		RandomAccessFile raf = null;
		try {
			// 注册循环判断当前账号和密码是否正确
			raf = new RandomAccessFile("user.dat", "rw");
			// 读取若干个100字节（每一个用户信息）
			for (int i = 0; i < raf.length() / 100; i++) {
				//先将指针移动到该条记录用户名的位置
				raf.seek(i * 100);
				byte[] data = new byte[32];
				raf.read(data);
				String names = new String(data, "utf-8").trim();
				raf.read(data);
				String pwds = new String(data, "utf-8").trim();
				if (names.equals(name)) {
					if (pwds.equals(pwd)) {
						// 3.给用户响应登录成功页面
						forward("myweb/login_success.html", request, response);
						System.out.println("登录业务完成。");
						//登录成功
						flag = true;
					}
					break;
				}

			}
			//判断是登录是否不成功
			if (!flag) {
				forward("myweb/login_fail.html", request, response);
				System.out.println("登录失败：登录失败业务完成。");
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
