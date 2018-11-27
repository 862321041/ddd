package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 请求对象 该类的每一个实例用于表示客户端发送过来的一个具体的请求类容。 请求有三部分： 请求行、消息头、消息正文
 * 
 * @author lenovo
 *
 */
public class HttpRequest {
	/**
	 * 请求行相关信息定义
	 */
	// 请求方式
	private String method;
	// 请求资源路径
	private String url;
	// 请求使用的Http协议版本
	private String protocol;
	// url中的请求部分
	private String requestURL;
	// url中的参数部分
	private String queryString;
	// 保存每一个具体的参数
	private Map<String, String> parameters = new HashMap<>();

	/**
	 * 消息头相关信息定义
	 */
	// key：消息头的名字，value消息头对应的值
	private Map<String, String> headers = new HashMap<String, String>();
	/**
	 * 消息正文相关信息定义
	 */
	private Socket socket;
	private InputStream in;

	/**
	 * 构造方法用于初始化请求对象 初始化请求对象的过程，就是读取并解析客户端发送过来的请求的过程。
	 * 对此构造方法要求将Socket传入，以此获取输入流读取客户端发送的请求内容。
	 * 
	 * @throws EmptyRequestException
	 */
	public HttpRequest(Socket socket) throws EmptyRequestException {
		System.out.println("HttpRequest:初始化请求...");
		/**
		 * 解析一个请求分为三步： 1.解析请求行 2.解析消息头 3.解析消息正文
		 */
		try {
			this.socket = socket;
			// 通过Socket获取输入流，读取客户端发送的请求内容
			this.in = socket.getInputStream();
			// 1解析请求行
			parseRequestLine();
			// 2解析消息头
			parseHeaders();
			// 3解析消息正文
			parseContent();
			// 单独捕获parseRequestLine();抛出异常
		} catch (EmptyRequestException e) {
			// 空请求不处理，直接跑给ClientHandler
			throw e;

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("HttpRequest:初始化请求完毕！");
	}

	/**
	 * 解析请求行
	 * GET /index.html HTTP/1.1(CRLF)
	 * @throws EmptyRequestException
	 */
	private void parseRequestLine() throws EmptyRequestException {
		System.out.println("开始解析请求行...");
		String line = readLine();
		System.out.println("请求行内容：" + line);
		if ("".equals(line)) {
			// 空请求，抛出空请求异常
			throw new EmptyRequestException();
		}

		/**
		 * 将请求行中的三个信息分别解析出来并设置到对应的三个属性上。 method、url、protocol 请求行内容：GET /
		 * HTTP/1.1
		 */
		String[] data = line.split("\\s");
		this.method = data[0];
		this.url = data[1];
		this.protocol = data[2];
		System.out.println("method:" + method);
		System.out.println("url:" + url);
		System.out.println("protocol:" + protocol);
		// 进一步解析url部分
		parseUrl(url);
		System.out.println("解析请求行完毕！");
	}

	/**
	 * 进一步解析url部分
	 */
	private void parseUrl(String url) {
		/**
		 * 由于url中可能含有用户传递的参数部分，对此我们要求对url进一步解析操作。
		 * 
		 * 首先要判断当前url是否含有参数，判断依据可以根据url中是否含有"?"。若有则表示该url有参数部分，若没有则
		 * 表示当前url不含有参数。
		 * 
		 * 若不含有参数则直接将url的值赋值给属性：requestURL即可。
		 * 
		 * 若含有参数： 1.先将url按照"?"拆分为两部分，将"？"左侧内容赋值给属性：requestURL
		 * 将"？"右侧内容赋值给属性：queryString
		 * 
		 * 2.细分参数部分，将参数部分按照"&"拆分出每一个参数，然后每一个参数在按照"="拆分
		 * 为两部分，其中"="左侧为参数名，右侧为参数值。将每一个参数的参数名作为key，参数值作为
		 * value添加到属性：parameters这个Map中，以完成参数的解析。 特殊符号需要两个\\进行转义
		 */
		// url.matches("\\?");
		if (url.contains("?")) {
			String[] data = url.split("\\?");
			this.requestURL = data[0];
			if (data.length > 1) {
				this.queryString = data[1];
				// 拆分每一个参数
				String[] date = queryString.split("&");
				for (String str : date) {
					// 按照=拆分
					String[] paraArr = str.split("=");
					if (paraArr.length > 1) {// 判断是=两边是否有值
						this.parameters.put(paraArr[0], paraArr[1]);
					} else {
						this.parameters.put(paraArr[0], null);
					}
				}
			}
		} else if (!url.contains("?")) {
			requestURL = url;
		}
		System.out.println("进一步解析url...");
		System.out.println("reqyestURL:" + requestURL);
		System.out.println("queryString:" + queryString);
		System.out.println("parameters:" + parameters);
		System.out.println("url解析完毕。");

	}

	/**
	 * 解析消息头
	 */
	private void parseHeaders() {
		System.out.println("开始解析消息头...");
		
		/**
		 * Host: localhost:8088(CRLF)
         * Connection: keep-alive（CRLF）
         * ...
         * (CRLF)(CRLF)
		 * 循环调用readLine方法，读取若干的消息头，当readLine方法返回值为一个空字符串时，
		 * 那应当是单独读取到了CRLF，这时就可以停止读取了。
		 * 
		 * 当读取到一个消息头后，我们可以将其按照": "(冒号空格)进行拆分，这样可以拆分出两项
		 * 第一项就是消息头的名字，第二项为消息头的值，我们将名字作为Key，将值作为value保
		 * 存到属性headers这个map中，最终完成解析消息头的工作
		 */
		String len = null;
		// 判断当前这一行是否为空
		while (!(len = readLine()).equals("")) {
			System.out.println(len);
			String[] data = len.split(": ");
			headers.put(data[0], data[1]);
		}
		System.out.println("headrs" + headers);
		System.out.println("解析消息头完毕！");
	}

	/**
	 * 解析消息正文
	 */
	private void parseContent() {
		System.out.println("开始解析消息正文...");
		System.out.println("解析消息正文完毕！");
	}

	/**
	 * 通过客户端对应的输入流中读取客户端发送过来的一行字符串，以CRLF作为一行结束的标志。 返回的这行字符串中不包含有后面的CRLF。
	 * 
	 * @return
	 */
	private String readLine() {
		try {

			StringBuilder builder = new StringBuilder();
			int len = -1;
			// c1表示上次读取到的字符，c2表示本次读取到的字符
			char c1 = 'a', c2 = 'a';
			while ((len = in.read()) != -1) {
				c2 = (char) len;
				// CR:13,LF:10,当读到的前一个为CR，后一个为LF就表示一行读取完成，退出当前循环
				if (c1 == 13 && c2 == 10) {
					break;
				}
				// 追加到可变字符串
				builder.append(c2);
				c1 = c2;
			}
			// 去除空格(CRLF)
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

	// 提供一个名字取值
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
	 * 根据给定的参数名获取对应的参数值
	 * @param name
	 * @return 当前名字的值
	 */
	public String getParameter(String name) {
		return this.parameters.get(name);
	}

}
