package com.webserver.servlet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
/**
 * 读取用户信息，读取user.dat里面的内容
 * @author lenovo
 *
 */
public class ShowAllUserDemo {
	public static void main(String[] args) throws IOException {
		RandomAccessFile raf = new RandomAccessFile("user.dat", "rw");
		//读取若干个100字节（每一个用户信息）
		for (int i = 0; i < raf.length() / 100; i++) {
			byte[] data = new byte[32];
			raf.read(data);
			String name = new String(data, "utf-8").trim();

			raf.read(data);
			String pwd = new String(data, "utf-8").trim();

			raf.read(data);
			String nic = new String(data, "utf-8").trim();

			int age =raf.readInt();
			System.out.println("名字:"+name+"密码:"+pwd+"昵称:"+nic+"年龄:"+age);
			System.out.println("pos:"+raf.getFilePointer());//指针

		}
		raf.close();
	}
}
