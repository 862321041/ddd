package com.webserver.servlet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
/**
 * ��ȡ�û���Ϣ����ȡuser.dat���������
 * @author lenovo
 *
 */
public class ShowAllUserDemo {
	public static void main(String[] args) throws IOException {
		RandomAccessFile raf = new RandomAccessFile("user.dat", "rw");
		//��ȡ���ɸ�100�ֽڣ�ÿһ���û���Ϣ��
		for (int i = 0; i < raf.length() / 100; i++) {
			byte[] data = new byte[32];
			raf.read(data);
			String name = new String(data, "utf-8").trim();

			raf.read(data);
			String pwd = new String(data, "utf-8").trim();

			raf.read(data);
			String nic = new String(data, "utf-8").trim();

			int age =raf.readInt();
			System.out.println("����:"+name+"����:"+pwd+"�ǳ�:"+nic+"����:"+age);
			System.out.println("pos:"+raf.getFilePointer());//ָ��

		}
		raf.close();
	}
}
