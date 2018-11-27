package com.webserver.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * ��Ӧ���� �����ÿһʵ�����ڱ�ʾҪ���ͻ��˷��͵�ʵ����Ӧ���ݡ�
 * 
 * @author lenovo
 *
 */
public class HttpResponse {
	/*
	 * ״̬�������Ϣ����
	 */
	// ״̬����
	private int statusCode = 200;
	/*
	 * ��Ӧͷ�����Ϣ����
	 */
	// key:��Ӧͷ���� value����Ӧͷ��ֵ
	private Map<String, String> headers = new HashMap<>();
	/*
	 * ��Ӧ���������Ϣ����
	 */
	// ��Ӧ��ʵ���ļ�
	private File entity;

	// �Ϳͻ��������������
	private Socket socket;
	private OutputStream out;

	// ��ʼ��
	public HttpResponse(Socket socket) {
		try {
			this.socket = socket;
			this.out = socket.getOutputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * ����ǰ��Ӧ�����������һ��HTTP��׼��Ӧ��ʽ���͸��ͻ���
	 */
	public void flush() {
		/*
		 * 1�� ����״̬�� 2��������Ӧͷ 3: ������Ӧ����
		 */
		System.out.println("Httpresponse:��ʼ������Ӧ...");
		sendStatusLine();
		sendHeaders();
		sendContent();
		System.out.println("Httpresponse:������Ӧ��ϣ�");
	}

	/*
	 * ����״̬��
	 */
	private void sendStatusLine() {
		try {
			System.out.println("��ʼ����״̬��...");
			// ����״̬��,statusCode:״̬�룬HttpContext.getStatusReason(statusCode)��״̬��Ϣ����
			String line = "HTTP/1.1" + " " + statusCode + " " + HttpContext.getStatusReason(statusCode);
			out.write(line.getBytes("ISO8859-1"));
			out.write(13);// CR
			out.write(10);// LF

			System.out.println("״̬�з������ !");
		} catch (Exception e) {

		}
	}

	/*
	 * ������Ӧͷ
	 */
	private void sendHeaders() {
		try {
			System.out.println("��ʼ������Ӧͷ...");
			// ������Ӧͷ,��������
			for (Entry<String, String> header : headers.entrySet()) {
				String line = header.getKey() + ": " + header.getValue();
				out.write(line.getBytes("ISO8859-1"));
				out.write(13);// CR
				out.write(10);// LF
			}
			// ��������CRLF��ʾ��Ӧͷ���ַ������
			out.write(13);// CR
			out.write(10);// LF
			System.out.println("��Ӧͷ������ϣ�");
		} catch (Exception e) {

		}

	}

	/*
	 * ������Ӧ����
	 */
	private void sendContent() {
		try (FileInputStream fis = new FileInputStream(entity);) {
			System.out.println("��ʼ������Ӧ����");

			// ������Ӧ����

			int len = -1;
			byte[] date = new byte[1024 * 10];
			while ((len = fis.read(date)) != -1) {
				out.write(date, 0, len);
			}
			System.out.println("������Ӧ�������");
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	// �õ���Ӧ��ʵ���ļ�
	public File getEntity() {
		return entity;
	}

	/**
	 * ������Ӧ���ĵ�ʵ���ļ��������õ�ͬʱ���Զ���Ӷ�Ӧ���ĵ�������Ӧͷ�� Content-type��Content-Lnegth
	 * 
	 * @param entity
	 */
	public void setEntity(File entity) {

		this.entity = entity;
		// ���ݸ�ʵ���ļ�������Ӧͷ
		//����ʵ���ļ��ĺ�׺��ȡ��Ӧ�Ľ�������
		String fileName=entity.getName();
		String ext=fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
		String type=HttpContext.getMimeType(ext);
		this.putHeaders("Content-Type", type);
		this.putHeaders("Content-Length", entity.length() + "");
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * ������Ӧͷ
	 * 
	 * @param name
	 *            ��Ӧͷ������
	 * @param value
	 *            ��Ӧͷ��Ӧ��ֵ
	 */
	public void putHeaders(String name, String value) {
		headers.put(name, value);
	}

	public String getHeaders(String name) {
		return headers.get(name);
	}

}
