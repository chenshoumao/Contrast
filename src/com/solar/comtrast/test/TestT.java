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

import com.solar.file.utils.CopyFileUtil;
import com.solar.file.utils.Zip;

 

/**
 * Servlet implementation class TestT
 */
@WebServlet("/TestT")
public class TestT extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TestT() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		 

	}

	public static void main(String[] args) {
		String path = "D:\\海图项目\\zip";
		String path2 = "D:\\海图项目\\zip2";
		// 获取path1,path2的所有文件夹路径,文件的md5值put map
		Map<String, FileMd5> path1Map;
		try {
			path1Map = listDir(path);

			Map<String, FileMd5> path2Map = listDir(path2);
			// compare path1 map to path2 map 得到path2没有的文件夹和文件及其md5值不同的文件
			//List<FileMd5> compareFile1 = compareFile(path1Map, path2Map);
			// compare path2 map to path1 map 得到path1没有的文件夹和文件及其md5值不同的文件
			List<FileMd5> compareFile = compareFile(path2Map, path1Map);
			// 过滤结果
		//	List<FileMd5> equalsFile = filterFile(compareFile1, compareFile2);
			// 输出最终结果
		//	printResult(equalsFile, compareFile1, compareFile2);
			printResult(compareFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 获取指定文件夹下的文件夹路径和文件md5值
	 */
	private static Map<String, FileMd5> listDir(String dir) throws IOException {
		Map<String, FileMd5> map = new HashMap<String, FileMd5>();
		File path = new File(dir);
		Object[] files = listPath(path).toArray();
		Arrays.sort(files);
		for (Object _file : files) {
			File file = (File) _file; 
			//String key = file.getAbsolutePath().replaceAll("\\\\", "/");
			String key = file.getAbsolutePath();
			key = key.replaceAll(dir, "");// 去掉根目录
			
			int index = key.indexOf(dir);
			if(index != -1){
				index = dir.length() + 1;
				key = key.substring(index, key.length());
			}
		//	path = path.replaceAll("\\\\", "\");
			
			
			String md5 = "";// 文件夹的md5默认为空,即不比较md5值
			if (file.isFile()) {
				// String text = FileUtils.readFileToString(file);
				md5 = MD5.getFileMD5(file);
				//System.out.println(md5);
			}
			FileMd5 fileMd5 = new FileMd5(file, md5);
			map.put(key, fileMd5);
		}
		return map;
	}

	/**
	 * 获取指定路径下的所有文件路径
	 */
	private static List<File> listPath(File path) {
		List<File> list = new ArrayList<File>();
		File[] files = path.listFiles();
		Arrays.sort(files);
		for (File file : files) {
			list.add(file);
			//System.out.println(file.getAbsolutePath());
			if (file.isDirectory()) {
				List<File> _list = listPath(file);
				list.addAll(_list);
			}
		}
		return list;
	}

	/**
	 * 比较两个文件夹的不同
	 */
	public static List<FileMd5> compareFile(Map<String, FileMd5> path1Map, Map<String, FileMd5> path2Map) {
		List<FileMd5> list = new ArrayList<FileMd5>();
		for (String key : path1Map.keySet()) {
			FileMd5 fileMd5 = path1Map.get(key);
			FileMd5 _fileMd5 = path2Map.get(key);

			// 不管文件夹还是文件，只要path2没有则add到比较结果集中
			if (_fileMd5 == null) {
				list.add(fileMd5);
				continue;
			}

			// 文件的md5值不同则add到比较结果集中
			if (fileMd5.getFile().isFile() && !fileMd5.getMd5().equals(_fileMd5.getMd5())) {
				list.add(fileMd5);
			}
		}
		return list;
	}

	/**
	 * 过滤相同
	 */
	public static List<FileMd5> filterFile(List<FileMd5> compareFile1, List<FileMd5> compareFile2) {
		List<FileMd5> list = new ArrayList<FileMd5>();
		for (FileMd5 fileMd5 : compareFile1) {
			if (compareFile2.contains(fileMd5)) {
				list.add(fileMd5);// 千万不要在此remove fileMd5
			}
		}
		// remove equals fileMd5
		for (FileMd5 fileMd5 : list) {
			compareFile1.remove(fileMd5);
			compareFile2.remove(fileMd5);
		}
		return list;
	}

//	/**
//	 * 打印结果
//	 */
//	public static void printResult(List<FileMd5> equalsFile, List<FileMd5> compareFile1, List<FileMd5> compareFile2) {
//		System.out.println("########################比较结果########################");
//		System.out.println("########################均不同的结果########################");
//		printFile(equalsFile);
//		System.out.println("########################均不同的结果########################");
//		System.out.println("########################path1多的结果########################");
//		printFile(compareFile1);
//		System.out.println("########################path1多的结果########################");
//		System.out.println("########################path2多的结果########################");
//		printFile(compareFile2);
//		System.out.println("########################path2多的结果########################");
//	}
	
	
	/**
	 * 打印结果
	 */
	public static void printResult(List<FileMd5> compareFile) {
		
		System.out.println("########################结果########################");
		printFile(compareFile);
		
	}

	/**
	 * 打印结果 + 复制文件
	 */
	public static void printFile(List<FileMd5> fileMd5s) {
		CopyFileUtil copyUtil = new CopyFileUtil();
		for (FileMd5 fileMd5 : fileMd5s) {
			System.out.println(fileMd5.getFile().getAbsolutePath() + " " + fileMd5.getMd5());
			
			String filePath = fileMd5.getFile().getAbsolutePath();
			String startTag = "D:\\海图项目\\zip2";
			int index = filePath.indexOf(startTag);
			if(index != -1){
				index = startTag.length() + 1;
				filePath = filePath.substring(index, filePath.length());
			}
			copyUtil.copyFile(fileMd5.getFile().getAbsolutePath(), "D:\\海图项目\\zip3"+File.separator + filePath, true);
			
		}
		
		
		//遍历目录获取文件小
		File preZip = new File("D:\\海图项目\\zip3");
		long size = 0;
		if(preZip.isDirectory()){
			File[] fileList = preZip.listFiles();
			for(File file:fileList)
				size += file.length(); 
		}
		
		
		//压缩文件目录
		Zip zip = new Zip();
		zip.zip("D:\\海图项目\\zip3", "D:\\海图项目\\zip4\\"+size+".zip");
		//File file = new File("D:\\海图项目\\zip4\\zipfil1e.zip");
	//	System.out.println(file.length());
		try {
			//file.renameTo(new File("D:\\海图项目\\zip4\\"+String.valueOf(file.length())+ ".zip"));
			
			File zipFile =new File("D:\\海图项目\\zip4");
			System.out.println(zipFile.getName() + "," + zipFile.length());
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
		
		
		try {
			Thread.sleep(5000);
			System.out.println(123);
			
			File preZip5 = new File("D:\\海图项目\\zip5");
			long size5 = 0;
			if(preZip5.isDirectory()){
				File[] fileList = preZip5.listFiles();
				for(File file:fileList)
					size5 += file.length(); 
			}
			
			boolean resultState = size == size5 ;
			if(resultState)
				System.out.println("解压数据成功");
			else
				System.out.println("解压数据损失");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

class FileMd5 {
	public File file;
	public String md5;

	public FileMd5(File file, String md5) {
		this.file = file;
		this.md5 = md5;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	@Override
	public String toString() {
		return file.getAbsolutePath();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		FileMd5 fileMd5 = (FileMd5) obj;
		return this.toString().equals(obj.toString())
				&& this.getMd5().equals(fileMd5.getMd5());
	}
}
