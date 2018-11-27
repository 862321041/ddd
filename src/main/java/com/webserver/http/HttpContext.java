package com.webserver.http;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * http协议,状态码显示相关内容定义
 * 
 * @author lenovo
 *
 */
public class HttpContext {
	/**
	 * 状态代码与状态描述的对应 key：状态代码 value：状态描述
	 */
	private static Map<Integer, String> statusReasonMapping = new HashMap<Integer, String>();
	/**
	 * 资源文件后缀与对应的介质类型定义的对应关系。 key：资源文件的后缀名 value：Content-type对应的值
	 */
	private static Map<String, String> mimeMapping = new HashMap<String, String>();
	static {
		// 初始化
		initStatusMapping();
		initMimeMapping();
	}

	/**
	 * 初始化介质类型映射(请求的文件格式对应的Content-type)
	 */
	private static void initMimeMapping() {
		/**
		 * 解析项目目录中的conf目录里的web.xml文件，将跟元素中的所有子元素<mime-mapping> 解析出来，并将其中的子元素：
		 * <extension>中的文本作为key， <mime-type>中的文本作为value。
		 * 存入到mimeMapping这个Map中，完成初始化操作。
		 */
		try {
			// 1.创建SAXReader
			SAXReader reader = new SAXReader();
			// 2.使用SAXReader读取要解析的xml文档并生成Document对象。
			Document doc = reader.read(new File("conf/web.xml"));
			// * 3.通过Document对象获取根元素。
			Element root = doc.getRootElement();
			// * 4.通过根元素逐级获取子元素以达到遍历xml文档数据的目的
			List<Element> emplist = root.elements("mime-mapping");
			for (Element empEle : emplist) {
				String key = empEle.elementText("extension");
				String value = empEle.elementText("mime-type");
				mimeMapping.put(key, value);

			}
			/*
			 * System.out.println("mim：" + mimeMapping.size());
			 * for(Entry<String, String> s:mimeMapping.entrySet()){
			 * System.out.println(s.getKey()+">>:"+s.getValue()); }
			 */
		} catch (Exception e) {
			// TODO: handle exception
		}

		// mimeMapping.put("html", "text/html");
		// mimeMapping.put("png", "image/png");
		// mimeMapping.put("gif", "image/gif");
		// mimeMapping.put("jpg", "image/jpeg");
		// mimeMapping.put("css", "text/css");
		// mimeMapping.put("js", "application/javascript");
		// mimeMapping.put("zip", "application/x-zip-compressed");

	}

	/**
	 * 初始化状态代码与对应的状态码
	 */
	private static void initStatusMapping() {
		statusReasonMapping.put(200, "OK");
		statusReasonMapping.put(201, "Created");
		statusReasonMapping.put(202, "Accepted");
		statusReasonMapping.put(204, "No Content");
		statusReasonMapping.put(301, "Moved Permanently");
		statusReasonMapping.put(302, "Moved Temporarily");
		statusReasonMapping.put(304, "Not Modified");
		statusReasonMapping.put(400, "Bad Request");
		statusReasonMapping.put(401, "Unauthorized");
		statusReasonMapping.put(403, "Forbidden");
		statusReasonMapping.put(404, "Not Found");
		statusReasonMapping.put(500, "Internal Server Error");
		statusReasonMapping.put(501, "Not Implemented");
		statusReasonMapping.put(502, "Bad Gateway");
		statusReasonMapping.put(503, "Service Unavailable");

	}

	/**
	 * 根据给定的状态代码获取对应的状态描述
	 * 
	 * @param code
	 * @return
	 */
	public static String getStatusReason(int code) {
		return statusReasonMapping.get(code);

	}

	/**
	 * 根据给定的资源后缀名获取对应的Content-type值
	 * 
	 * @param ext
	 *            资源后缀名
	 * @return
	 */
	public static String getMimeType(String ext) {
		return mimeMapping.get(ext);

	}

//	public static void main(String[] args) {
//		String fileName = "sda.sad.logo.zip";
//		fileName = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
//		// String[]
//		// name=fileName.split("//.");
//		// System.out.println(fileName)
//		String type = getMimeType(fileName);
//		System.out.println(type);
//	}

}
