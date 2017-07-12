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
		
//		File file = new File("D:\\��ͼ��Ŀ\\zip2\\Struts2+Spring3+Hibernate��ܼ������������ϰ������ҵĹ���\\��3ƪ STRUTS2���ƪ\\��9�� ����\\CH09_02_REGISTER\\WEBROOT\\WEB-INF\\CLASSES\\COM");
//		System.out.println(file.isDirectory());
		
		String path = "D:\\��ͼ��Ŀ\\zip";
		String path2 = "D:\\��ͼ��Ŀ\\zip2";
		// ��ȡpath1,path2�������ļ���·��,�ļ���md5ֵput map
		Map<String, FileMd5> path1Map;
		try {
			path1Map = listDir(path);

			Map<String, FileMd5> path2Map = listDir(path2);
			// compare path1 map to path2 map �õ�path2û�е��ļ��к��ļ�����md5ֵ��ͬ���ļ�
			// List<FileMd5> compareFile1 = compareFile(path1Map, path2Map);
			// compare path2 map to path1 map �õ�path1û�е��ļ��к��ļ�����md5ֵ��ͬ���ļ�
			List<FileMd5> compareFile = compareFile(path2Map, path1Map);
			// ���˽��
			// List<FileMd5> equalsFile = filterFile(compareFile1,
			// compareFile2);
			// ������ս��
			// printResult(equalsFile, compareFile1, compareFile2);
			printResult(compareFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * ��ȡָ���ļ����µ��ļ���·�����ļ�md5ֵ
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
			key = key.replaceAll(dir, "");// ȥ����Ŀ¼

			int index = key.indexOf(dir);
			if (index != -1) {
				index = dir.length() + 1;
				key = key.substring(index, key.length());
			}
			// path = path.replaceAll("\\\\", "\");

			String md5 = "";// �ļ��е�md5Ĭ��Ϊ��,�����Ƚ�md5ֵ
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
	 * ��ȡָ��·���µ������ļ�·��
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
	 * �Ƚ������ļ��еĲ�ͬ
	 */
	public static List<FileMd5> compareFile(Map<String, FileMd5> path1Map, Map<String, FileMd5> path2Map) {
		List<FileMd5> list = new ArrayList<FileMd5>();
		for (String key : path1Map.keySet()) {
			FileMd5 fileMd5 = path1Map.get(key);
			FileMd5 _fileMd5 = path2Map.get(key);

			// �����ļ��л����ļ���ֻҪpath2û����add���ȽϽ������
			if (_fileMd5 == null) {
				list.add(fileMd5);
				continue;
			}

			// �ļ���md5ֵ��ͬ��add���ȽϽ������
			if (fileMd5.getFile().isFile() && !fileMd5.getMd5().equals(_fileMd5.getMd5())) {
				list.add(fileMd5);
			}
		}
		return list;
	}

	/**
	 * ������ͬ
	 */
	public static List<FileMd5> filterFile(List<FileMd5> compareFile1, List<FileMd5> compareFile2) {
		List<FileMd5> list = new ArrayList<FileMd5>();
		for (FileMd5 fileMd5 : compareFile1) {
			if (compareFile2.contains(fileMd5)) {
				list.add(fileMd5);// ǧ��Ҫ�ڴ�remove fileMd5
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
	// * ��ӡ���
	// */
	// public static void printResult(List<FileMd5> equalsFile, List<FileMd5>
	// compareFile1, List<FileMd5> compareFile2) {
	// System.out.println("########################�ȽϽ��########################");
	// System.out.println("########################����ͬ�Ľ��########################");
	// printFile(equalsFile);
	// System.out.println("########################����ͬ�Ľ��########################");
	// System.out.println("########################path1��Ľ��########################");
	// printFile(compareFile1);
	// System.out.println("########################path1��Ľ��########################");
	// System.out.println("########################path2��Ľ��########################");
	// printFile(compareFile2);
	// System.out.println("########################path2��Ľ��########################");
	// }

	/**
	 * ��ӡ���
	 */
	public static void printResult(List<FileMd5> compareFile) {

		System.out.println("########################���########################");
		printFile(compareFile);

	}

	/**
	 * ��ӡ��� + �����ļ�
	 */
	public static void printFile(List<FileMd5> fileMd5s) {
		CopyFileUtil copyUtil = new CopyFileUtil();

		boolean stateCopyResult = false;
		for (FileMd5 fileMd5 : fileMd5s) {
			System.out.println(fileMd5.getFile().getAbsolutePath() + " " + fileMd5.getMd5());

			String filePath = fileMd5.getFile().getAbsolutePath();
			String startTag = "D:\\��ͼ��Ŀ\\zip2";
			int index = filePath.indexOf(startTag);
			if (index != -1) {
				index = startTag.length() + 1;
				filePath = filePath.substring(index, filePath.length());
			}
			stateCopyResult = copyUtil.copyFile(fileMd5.getFile().getAbsolutePath(),
					"D:\\��ͼ��Ŀ\\zip3" + File.separator + filePath, true);
		}

		if (stateCopyResult) {
			// ����Ŀ¼��ȡ�ļ�С
			File preZip = new File("D:\\��ͼ��Ŀ\\zip3");
			FileSize fileSize = new FileSize();
			long size = fileSize.getFileSize(preZip);
			 

			// ѹ���ļ�Ŀ¼
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
		// ѹ���ļ�Ŀ¼
		Zip zip = new Zip();
		
		 
		try {
		 
			stateResult = zip.zip("D:\\��ͼ��Ŀ\\zip3", "D:\\��ͼ��Ŀ\\zip4\\" + size + ".zip");
			File zipFile = new File("D:\\��ͼ��Ŀ\\zip4\\" + size + ".zip");
			long zipSize = zipFile.length();
			zipFile.renameTo(new File("D:\\��ͼ��Ŀ\\zip4\\" + zipSize + "_" + size + ".zip"));
			//System.out.println(zipFile.getName() + "," + zipFile.length());
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
			stateResult = false;
		}
		return stateResult;
	}

}


