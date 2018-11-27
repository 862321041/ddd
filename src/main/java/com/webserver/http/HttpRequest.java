package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * ������� �����ÿһ��ʵ�����ڱ�ʾ�ͻ��˷��͹�����һ��������������ݡ� �����������֣� �����С���Ϣͷ����Ϣ����
 * 
 * @author lenovo
 *
 */
public class HttpRequest {
	/**
	 * �����������Ϣ����
	 */
	// ����ʽ
	private String method;
	// ������Դ·��
	private String url;
	// ����ʹ�õ�HttpЭ��汾
	private String protocol;
	// url�е����󲿷�
	private String requestURL;
	// url�еĲ�������
	private String queryString;
	// ����ÿһ������Ĳ���
	private Map<String, String> parameters = new HashMap<>();

	/**
	 * ��Ϣͷ�����Ϣ����
	 */
	// key����Ϣͷ�����֣�value��Ϣͷ��Ӧ��ֵ
	private Map<String, String> headers = new HashMap<String, String>();
	/**
	 * ��Ϣ���������Ϣ����
	 */
	private Socket socket;
	private InputStream in;

	/**
	 * ���췽�����ڳ�ʼ��������� ��ʼ���������Ĺ��̣����Ƕ�ȡ�������ͻ��˷��͹���������Ĺ��̡�
	 * �Դ˹��췽��Ҫ��Socket���룬�Դ˻�ȡ��������ȡ�ͻ��˷��͵��������ݡ�
	 * 
	 * @throws EmptyRequestException
	 */
	public HttpRequest(Socket socket) throws EmptyRequestException {
		System.out.println("HttpRequest:��ʼ������...");
		/**
		 * ����һ�������Ϊ������ 1.���������� 2.������Ϣͷ 3.������Ϣ����
		 */
		try {
			this.socket = socket;
			// ͨ��Socket��ȡ����������ȡ�ͻ��˷��͵���������
			this.in = socket.getInputStream();
			// 1����������
			parseRequestLine();
			// 2������Ϣͷ
			parseHeaders();
			// 3������Ϣ����
			parseContent();
			// ��������parseRequestLine();�׳��쳣
		} catch (EmptyRequestException e) {
			// �����󲻴���ֱ���ܸ�ClientHandler
			throw e;

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("HttpRequest:��ʼ��������ϣ�");
	}

	/**
	 * ����������
	 * GET /index.html HTTP/1.1(CRLF)
	 * @throws EmptyRequestException
	 */
	private void parseRequestLine() throws EmptyRequestException {
		System.out.println("��ʼ����������...");
		String line = readLine();
		System.out.println("���������ݣ�" + line);
		if ("".equals(line)) {
			// �������׳��������쳣
			throw new EmptyRequestException();
		}

		/**
		 * ���������е�������Ϣ�ֱ�������������õ���Ӧ�����������ϡ� method��url��protocol ���������ݣ�GET /
		 * HTTP/1.1
		 */
		String[] data = line.split("\\s");
		this.method = data[0];
		this.url = data[1];
		this.protocol = data[2];
		System.out.println("method:" + method);
		System.out.println("url:" + url);
		System.out.println("protocol:" + protocol);
		// ��һ������url����
		parseUrl(url);
		System.out.println("������������ϣ�");
	}

	/**
	 * ��һ������url����
	 */
	private void parseUrl(String url) {
		/**
		 * ����url�п��ܺ����û����ݵĲ������֣��Դ�����Ҫ���url��һ������������
		 * 
		 * ����Ҫ�жϵ�ǰurl�Ƿ��в������ж����ݿ��Ը���url���Ƿ���"?"���������ʾ��url�в������֣���û����
		 * ��ʾ��ǰurl�����в�����
		 * 
		 * �������в�����ֱ�ӽ�url��ֵ��ֵ�����ԣ�requestURL���ɡ�
		 * 
		 * �����в����� 1.�Ƚ�url����"?"���Ϊ�����֣���"��"������ݸ�ֵ�����ԣ�requestURL
		 * ��"��"�Ҳ����ݸ�ֵ�����ԣ�queryString
		 * 
		 * 2.ϸ�ֲ������֣����������ְ���"&"��ֳ�ÿһ��������Ȼ��ÿһ�������ڰ���"="���
		 * Ϊ�����֣�����"="���Ϊ���������Ҳ�Ϊ����ֵ����ÿһ�������Ĳ�������Ϊkey������ֵ��Ϊ
		 * value��ӵ����ԣ�parameters���Map�У�����ɲ����Ľ����� ���������Ҫ����\\����ת��
		 */
		// url.matches("\\?");
		if (url.contains("?")) {
			String[] data = url.split("\\?");
			this.requestURL = data[0];
			if (data.length > 1) {
				this.queryString = data[1];
				// ���ÿһ������
				String[] date = queryString.split("&");
				for (String str : date) {
					// ����=���
					String[] paraArr = str.split("=");
					if (paraArr.length > 1) {// �ж���=�����Ƿ���ֵ
						this.parameters.put(paraArr[0], paraArr[1]);
					} else {
						this.parameters.put(paraArr[0], null);
					}
				}
			}
		} else if (!url.contains("?")) {
			requestURL = url;
		}
		System.out.println("��һ������url...");
		System.out.println("reqyestURL:" + requestURL);
		System.out.println("queryString:" + queryString);
		System.out.println("parameters:" + parameters);
		System.out.println("url������ϡ�");

	}

	/**
	 * ������Ϣͷ
	 */
	private void parseHeaders() {
		System.out.println("��ʼ������Ϣͷ...");
		
		/**
		 * Host: localhost:8088(CRLF)
         * Connection: keep-alive��CRLF��
         * ...
         * (CRLF)(CRLF)
		 * ѭ������readLine��������ȡ���ɵ���Ϣͷ����readLine��������ֵΪһ�����ַ���ʱ��
		 * ��Ӧ���ǵ�����ȡ����CRLF����ʱ�Ϳ���ֹͣ��ȡ�ˡ�
		 * 
		 * ����ȡ��һ����Ϣͷ�����ǿ��Խ��䰴��": "(ð�ſո�)���в�֣��������Բ�ֳ�����
		 * ��һ�������Ϣͷ�����֣��ڶ���Ϊ��Ϣͷ��ֵ�����ǽ�������ΪKey����ֵ��Ϊvalue��
		 * �浽����headers���map�У�������ɽ�����Ϣͷ�Ĺ���
		 */
		String len = null;
		// �жϵ�ǰ��һ���Ƿ�Ϊ��
		while (!(len = readLine()).equals("")) {
			System.out.println(len);
			String[] data = len.split(": ");
			headers.put(data[0], data[1]);
		}
		System.out.println("headrs" + headers);
		System.out.println("������Ϣͷ��ϣ�");
	}

	/**
	 * ������Ϣ����
	 */
	private void parseContent() {
		System.out.println("��ʼ������Ϣ����...");
		System.out.println("������Ϣ������ϣ�");
	}

	/**
	 * ͨ���ͻ��˶�Ӧ���������ж�ȡ�ͻ��˷��͹�����һ���ַ�������CRLF��Ϊһ�н����ı�־�� ���ص������ַ����в������к����CRLF��
	 * 
	 * @return
	 */
	private String readLine() {
		try {

			StringBuilder builder = new StringBuilder();
			int len = -1;
			// c1��ʾ�ϴζ�ȡ�����ַ���c2��ʾ���ζ�ȡ�����ַ�
			char c1 = 'a', c2 = 'a';
			while ((len = in.read()) != -1) {
				c2 = (char) len;
				// CR:13,LF:10,��������ǰһ��ΪCR����һ��ΪLF�ͱ�ʾһ�ж�ȡ��ɣ��˳���ǰѭ��
				if (c1 == 13 && c2 == 10) {
					break;
				}
				// ׷�ӵ��ɱ��ַ���
				builder.append(c2);
				c1 = c2;
			}
			// ȥ���ո�(CRLF)
			return builder.toString().trim();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return "";
	}

	public String getMethod() {
		return method;
	}

	public String getUrl() {
		return url;
	}

	public String getProtocol() {
		return protocol;
	}

	// �ṩһ������ȡֵ
	public String getHeaders(String name) {
		return headers.get(name);
	}

	public String getRequestURL() {
		return requestURL;
	}

	public String getQueryString() {
		return queryString;
	}

	/**
	 * ���ݸ����Ĳ�������ȡ��Ӧ�Ĳ���ֵ
	 * @param name
	 * @return ��ǰ���ֵ�ֵ
	 */
	public String getParameter(String name) {
		return this.parameters.get(name);
	}

}
