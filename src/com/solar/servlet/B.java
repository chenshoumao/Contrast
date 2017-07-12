package com.solar.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class B
 */
@WebServlet("/B")
public class B extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public B() {
        super();
        // TODO Auto-generated constructor stub
    }
    public static void main(String[] args) {
    	   try {
               Process process = Runtime.getRuntime().exec("taskList");
               Scanner in = new Scanner(process.getInputStream());
               int count = 0;
<<<<<<< HEAD
               Runtime.getRuntime().exec("cmd /c net start Tomcat7");
           } catch (Exception e) {
               // TODO Auto-generated catch block
        	   System.out.println(e);
=======
               Runtime.getRuntime().exec("net start Tomcat7");
           } catch (Exception e) {
               // TODO Auto-generated catch block
>>>>>>> master2
               e.printStackTrace();
           }
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
