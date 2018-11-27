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
 * httpЭ��,״̬����ʾ������ݶ���
 * 
 * @author lenovo
 *
 */
public class HttpContext {
	/**
	 * ״̬������״̬�����Ķ�Ӧ key��״̬���� value��״̬����
	 */
	private static Map<Integer, String> statusReasonMapping = new HashMap<Integer, String>();
	/**
	 * ��Դ�ļ���׺���Ӧ�Ľ������Ͷ���Ķ�Ӧ��ϵ�� key����Դ�ļ��ĺ�׺�� value��Content-type��Ӧ��ֵ
	 */
	private static Map<String, String> mimeMapping = new HashMap<String, String>();
	static {
		// ��ʼ��
		initStatusMapping();
		initMimeMapping();
	}

	/**
	 * ��ʼ����������ӳ��(������ļ���ʽ��Ӧ��Content-type)
	 */
	private static void initMimeMapping() {
		/**
		 * ������ĿĿ¼�е�confĿ¼���web.xml�ļ�������Ԫ���е�������Ԫ��<mime-mapping> �����������������е���Ԫ�أ�
		 * <extension>�е��ı���Ϊkey�� <mime-type>�е��ı���Ϊvalue��
		 * ���뵽mimeMapping���Map�У���ɳ�ʼ��������
		 */
		try {
			// 1.����SAXReader
			SAXReader reader = new SAXReader();
			// 2.ʹ��SAXReader��ȡҪ������xml�ĵ�������Document����
			Document doc = reader.read(new File("conf/web.xml"));
			// * 3.ͨ��Document�����ȡ��Ԫ�ء�
			Element root = doc.getRootElement();
			// * 4.ͨ����Ԫ���𼶻�ȡ��Ԫ���Դﵽ����xml�ĵ����ݵ�Ŀ��
			List<Element> emplist = root.elements("mime-mapping");
			for (Element empEle : emplist) {
				String key = empEle.elementText("extension");
				String value = empEle.elementText("mime-type");
				mimeMapping.put(key, value);

			}
			/*
			 * System.out.println("mim��" + mimeMapping.size());
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
	 * ��ʼ��״̬�������Ӧ��״̬��
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
	 * ���ݸ�����״̬�����ȡ��Ӧ��״̬����
	 * 
	 * @param code
	 * @return
	 */
	public static String getStatusReason(int code) {
		return statusReasonMapping.get(code);

	}

	/**
	 * ���ݸ�������Դ��׺����ȡ��Ӧ��Content-typeֵ
	 * 
	 * @param ext
	 *            ��Դ��׺��
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
