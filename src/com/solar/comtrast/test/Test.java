package com.solar.comtrast.test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

 
public class Test  {
	public static void main(String[] args) {
		String path = "D:\\��ͼ��Ŀ\\zip";
		//String path2 = "D:\\��ͼ��Ŀ\\zip2";
		File file =  new File(path);
		 
		System.out.println(""); 
		pringName(file,path);
	}
	 
	
	public static void pringName(File file,String path){
		//D:\��ͼ��Ŀ\zip\EXECL\2017_05_14.xlsx D:\��ͼ��Ŀ\zip
		if(file.isDirectory()){
			File[] fileList = file.listFiles();
			for(File ff:fileList){
				if(ff.isFile()){
					String abPath = ff.getAbsolutePath();
					int index = abPath.indexOf(path);
					if(index != -1)
						index = path.length() + 1;
				//	path = path.replaceAll("\\\\", "\");
					abPath = abPath.substring(index, abPath.length());
					System.out.println(ff.getName()  + "," +abPath);
					
				}
				else
					pringName(ff,path);
			}
		}
	}
}


