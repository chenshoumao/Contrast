package com.solar.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.solar.file.utils.CopyFileUtil;
import com.solar.file.utils.WebApp;

import sun.misc.BASE64Encoder;

/**
 * Servlet implementation class Listener
 */ 
 
public class Listener extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Listener() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		System.out.println(12345);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	//	response.getWriter().append("Served at1122: ").append(request.getContextPath());
		PrintWriter out = response.getWriter();
		message("http://localhost:8080/Contrast");
		boolean str =  reloadWebApp("Contrast");
		out.println(str);
		 //System.exit(0);
//		PrintWriter out = response.getWriter();
//		out.println(12);
//		try {
//            Process process = Runtime.getRuntime().exec("taskList");
//            Scanner in = new Scanner(process.getInputStream());
//            int count = 0;
//            while (in.hasNextLine()) {
//                count++;
//                String temp = in.nextLine(); 
//                if (temp.contains("Tomcat7")) {
//                    String[] t = temp.split(" ");
//                    // �жϸý�����ռ�ڴ��Ƿ����20M
//                    if (Integer.parseInt(t[t.length - 2].replace(",", "")) > 20000) {
//                        temp = temp.replaceAll(" ", "");
//                        // ���pid
//                        // String pid = temp.substring(9, temp.indexOf("Console"));
//                        String pid = "2972";
//                        Runtime.getRuntime().exec("D:\\��ͼ��Ŀ\\tomcat7\\bin\\startup.bat");
//                        Thread.sleep(12000);
//                        Runtime.getRuntime().exec("net start Tomcat7");
//                        out.println(112322);
//                        System.out.println(pid);
//
//                        // dos�¿�cmd���� ntsd -c q -p PID
//                        // Runtime.getRuntime().exec("ntsd -c q -p 1528");
//                    }
//                }
//                // System.out.println(count + ":" + temp);
//            }
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } 
		//stopWebApp("Contrast"); 
	}
	
	 public static String message(String operateURL) {

	        StringBuffer dataResult = new StringBuffer();
	        URL url = null;
	        try {
	            url = new URL(operateURL);

	            URLConnection conn = (URLConnection) url.openConnection();
	            //�������ǰ�admin��123456 ����û���Ϣ�ŵ���һ��json�ļ�����json��ʽ��ţ�Ȼ��ȡ������������������ַ�ʽ��ţ������ֱ������username = admin ��password =123456
	            

	            String username = "admin";
	            String password = "123456";

	            String configuration = username+":"+password; // manager��ɫ���û�
	            String encodedPassword = new BASE64Encoder().encode(configuration.getBytes());
	            conn.setRequestProperty("Authorization", "Basic " + encodedPassword);
	            // URL��Ȩ���� -- End

	            InputStream is = conn.getInputStream();
	            BufferedReader bufreader = new BufferedReader(new InputStreamReader(is));
	            String line = null;
	            while ((line = bufreader.readLine()) != null) {
	                dataResult.append(line);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return dataResult.toString();
	    }
	
	public ArrayList<WebApp> getTomcatWebAppData(){

        ArrayList<WebApp> webAppArrayList = new ArrayList<WebApp>();

        String data = Listener.message("http://localhost:8080/manager/text/list");

        String[] oldDataStrs = data.split("/");

        if(oldDataStrs[0].startsWith("OK")){
            for (int i = 0; i < oldDataStrs.length; i++) {
                String name = oldDataStrs[i].split(":")[0];
                if(name.startsWith("legacy-proxy")){
                    WebApp webApp = new WebApp();
                    webApp.setName(name);
                    if(oldDataStrs[i].split(":")[1].equals("running")){
                        if(oldDataStrs[i].split(":")[2].equals("0")){
                            webApp.setStatus("����");
                        }
                        else{
                            webApp.setStatus("�쳣");
                        }
                    }
                    else if(oldDataStrs[i].split(":")[1].equals("stopped")){
                        if(oldDataStrs[i].split(":")[2].equals("0")){
                            webApp.setStatus("ֹͣ");
                        }
                        else{
                            webApp.setStatus("�쳣");
                        }
                    }
                    else{
                        webApp.setStatus("�쳣");
                    }
                    webAppArrayList.add(webApp);
                }
            }
        }
        return webAppArrayList;
    }
	
	/**
     * ���²���һ����Ŀ
     * @param webAppName
     * @return
     */
    public boolean reloadWebApp(String webAppName){
        String data = Listener.message("http://localhost:8080/manager/text/reload?path=/"+webAppName);
        if(data.startsWith("OK")){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * ֹͣһ����Ŀ
     * @param webAppName
     * @return
     */
    public boolean stopWebApp(String webAppName){
        String data = Listener.message("http://localhost:8080/manager/text/stop?path=/"+webAppName);
        if(data.startsWith("OK")){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * ��ʼһ����Ŀ
     * @param webAppName
     * @return
     */
    public boolean startWebApp(String webAppName){
        String data = Listener.message("http://localhost:8080/manager/text/start?path=/"+webAppName);
        if(data.startsWith("OK")){
            return true;
        }
        else {
            return false;
        }
    }


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	
	public static void main(String[] args) {
		Runtime rt = Runtime.getRuntime(); //Runtime.getRuntime()���ص�ǰӦ�ó����Runtime����
        Process ps = null;  //Process���Կ��Ƹ��ӽ��̵�ִ�л��ȡ���ӽ��̵���Ϣ��
        try {
         //   ps = rt.exec("cmd /c start D:\\��ͼ��Ŀ\\tomcat7\\bin\\startup.bat");   //�ö����exec()����ָʾJava���������һ���ӽ���ִ��ָ���Ŀ�ִ�г��򣬲���������ӽ��̶�Ӧ��Process����ʵ����
        	ps = rt.exec("cmd /c net start Tomcat7"); 
            ps.waitFor();  //�ȴ��ӽ������������ִ�С�
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        int i = ps.exitValue();  //����ִ����ϵķ���ֵ
        if (i == 0) {
            System.out.println("ִ�����.");
        } else {
            System.out.println("ִ��ʧ��.");
        }

        ps.destroy();  //�����ӽ���
        ps = null;   
	}

}

/**
 * @ping 127.0.0.1 -n 6 >nul 
net start Tomcat7
pause
 */




















