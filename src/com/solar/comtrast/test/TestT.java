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
import com.solar.file.utils.FileMd5;
import com.solar.file.utils.FileSize;
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
		
//		File file = new File("D:\\海图项目\\zip2\\Struts2+Spring3+Hibernate框架技术精讲与整合案例：我的光盘\\第3篇 STRUTS2框架篇\\第9章 代码\\CH09_02_REGISTER\\WEBROOT\\WEB-INF\\CLASSES\\COM");
//		System.out.println(file.isDirectory());
		
		String path = "D:\\海图项目\\zip";
		String path2 = "D:\\海图项目\\zip2";
		// 获取path1,path2的所有文件夹路径,文件的md5值put map
		Map<String, FileMd5> path1Map;
		try {
			path1Map = listDir(path);

			Map<String, FileMd5> path2Map = listDir(path2);
			// compare path1 map to path2 map 得到path2没有的文件夹和文件及其md5值不同的文件
			// List<FileMd5> compareFile1 = compareFile(path1Map, path2Map);
			// compare path2 map to path1 map 得到path1没有的文件夹和文件及其md5值不同的文件
			List<FileMd5> compareFile = compareFile(path2Map, path1Map);
			// 过滤结果
			// List<FileMd5> equalsFile = filterFile(compareFile1,
			// compareFile2);
			// 输出最终结果
			// printResult(equalsFile, compareFile1, compareFile2);
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
			// String key = file.getAbsolutePath().replaceAll("\\\\", "/");
			String key = file.getAbsolutePath();
			key = key.replaceAll(dir, "");// 去掉根目录

			int index = key.indexOf(dir);
			if (index != -1) {
				index = dir.length() + 1;
				key = key.substring(index, key.length());
			}
			// path = path.replaceAll("\\\\", "\");

			String md5 = "";// 文件夹的md5默认为空,即不比较md5值
			if (file.isFile()) {
				// String text = FileUtils.readFileToString(file);
				md5 = MD5.getFileMD5(file);
				// System.out.println(md5);
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
			
			
			if (file.isDirectory()) {
				List<File> _list = listPath(file);
				list.addAll(_list);
			}
			else
				list.add(file);
			System.out.println(file.getAbsolutePath());
			
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

	// /**
	// * 打印结果
	// */
	// public static void printResult(List<FileMd5> equalsFile, List<FileMd5>
	// compareFile1, List<FileMd5> compareFile2) {
	// System.out.println("########################比较结果########################");
	// System.out.println("########################均不同的结果########################");
	// printFile(equalsFile);
	// System.out.println("########################均不同的结果########################");
	// System.out.println("########################path1多的结果########################");
	// printFile(compareFile1);
	// System.out.println("########################path1多的结果########################");
	// System.out.println("########################path2多的结果########################");
	// printFile(compareFile2);
	// System.out.println("########################path2多的结果########################");
	// }

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

		boolean stateCopyResult = false;
		for (FileMd5 fileMd5 : fileMd5s) {
			System.out.println(fileMd5.getFile().getAbsolutePath() + " " + fileMd5.getMd5());

			String filePath = fileMd5.getFile().getAbsolutePath();
			String startTag = "D:\\海图项目\\zip2";
			int index = filePath.indexOf(startTag);
			if (index != -1) {
				index = startTag.length() + 1;
				filePath = filePath.substring(index, filePath.length());
			}
			stateCopyResult = copyUtil.copyFile(fileMd5.getFile().getAbsolutePath(),
					"D:\\海图项目\\zip3" + File.separator + filePath, true);
		}

		if (stateCopyResult) {
			// 遍历目录获取文件小
			File preZip = new File("D:\\海图项目\\zip3");
			FileSize fileSize = new FileSize();
			long size = fileSize.getFileSize(preZip);
			 

			// 压缩文件目录
			boolean stateResult = zipFile(size);
			
			
			 

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
	
	
	public static boolean zipFile(long size) {
		boolean stateResult = false;
		// 压缩文件目录
		Zip zip = new Zip();
		
		 
		try {
		 
			stateResult = zip.zip("D:\\海图项目\\zip3", "D:\\海图项目\\zip4\\" + size + ".zip");
			File zipFile = new File("D:\\海图项目\\zip4\\" + size + ".zip");
			long zipSize = zipFile.length();
			zipFile.renameTo(new File("D:\\海图项目\\zip4\\" + zipSize + "_" + size + ".zip"));
			//System.out.println(zipFile.getName() + "," + zipFile.length());
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
			stateResult = false;
		}
		return stateResult;
	}

}


