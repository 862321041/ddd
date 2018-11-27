package com.webserver.core;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * ��������������Ϣ
 * 
 * @author lenovo
 *
 */
public class ServerContext {
	/**
	 * Servletӳ�� key������·�� value��Servlet����ȫ�޶���
	 */
	private static Map<String, String> servletMapping = new HashMap<>();
	static {
		initServletMapping();
	}

	/**
	 * ��ʼ��Servletӳ��,���ڼ��뵽xml�����ļ���,��xml�ļ��н�������
	 */
	private static void initServletMapping() {
		
	try {
		SAXReader reader = new SAXReader();
		// 2.
		Document doc = reader.read(new File("conf/servlets.xml"));
	
		Element root = doc.getRootElement();
		/**
		 * ���ݸ�Ԫ�ػ�ȡ��������1<servlets>��ǩ
		 */
		
		List<Element> emplist = root.elements("servlet");
		/**
		 * ����<emp>,��ȡÿһ��Ա����Ϣ
		 */
		for (Element empEle : emplist) {
			String url=empEle.attributeValue("url");
			String className=empEle.attributeValue("className");
			servletMapping.put(url, className);
		}
		
	} catch (Exception e) {
		
	}
		
		//servletMapping.put("/myweb/reg", "com.webserver.servlet.RegServlet");
		//servletMapping.put("/myweb/login", "com.webserver.servlet.LoginServlet");
		
	}
	/**
	 * ��������·����ȡ��Ӧ��Servlet����
	 * @param url
	 * @return
	 */
	public static String getServlet(String url) {
		return servletMapping.get(url);

	}

}
