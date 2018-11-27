package com.webserver.core;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 服务端相关配置信息
 * 
 * @author lenovo
 *
 */
public class ServerContext {
	/**
	 * Servlet映射 key：请求路径 value：Servlet的完全限定名
	 */
	private static Map<String, String> servletMapping = new HashMap<>();
	static {
		initServletMapping();
	}

	/**
	 * 初始化Servlet映射,后期加入到xml配置文件中,在xml文件中进行配置
	 */
	private static void initServletMapping() {
		
	try {
		SAXReader reader = new SAXReader();
		// 2.
		Document doc = reader.read(new File("conf/servlets.xml"));
	
		Element root = doc.getRootElement();
		/**
		 * 根据根元素获取下面所有1<servlets>标签
		 */
		
		List<Element> emplist = root.elements("servlet");
		/**
		 * 遍历<emp>,获取每一个员工信息
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
	 * 根据请求路径获取对应的Servlet名字
	 * @param url
	 * @return
	 */
	public static String getServlet(String url) {
		return servletMapping.get(url);

	}

}
