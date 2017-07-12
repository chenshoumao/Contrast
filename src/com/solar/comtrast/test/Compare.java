package com.solar.comtrast.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.solar.file.utils.CopyFileUtil;
import com.solar.file.utils.FileMd5;
import com.solar.file.utils.FileSize;
import com.solar.file.utils.Version;
import com.solar.file.utils.Zip;

public class Compare{
	
	private static String sourcePath = "";
	
	public static void main(String[] args){
		List<Version> list = new ArrayList<Version>();
		String repositoriesUrl = "D:\\海图项目\\repositories";
	
		File file = new File(repositoriesUrl);
		if(file.exists()){
			File[] fileList = file.listFiles();
			for(File fileIt:fileList){
				String name = fileIt.getName();
				String[] validateName = name.split("_");
				if(validateName.length == 3)
					list.add(new Version(name));
				else
					System.out.println("命名非法！:  " + name);
			}
		}
		
		Collections.sort(list);
		
		System.out.println("打印所有的版本的信息");
		for(Version verion:list){
			System.out.println(verion.toString());
		}
		
		System.out.println("打印 最新的版本");
		System.out.println(list.get(0).toString());
		
 
		//最新的版本
		sourcePath = "D:\\海图项目\\repositories\\"+list.get(0).toString();
		// 获取path1,path2的所有文件夹路径,文件的md5值put map
		Map<String, FileMd5> path1Map;
		try {
			String path = "D:\\海图项目\\repositories\\1.0.2.2_release_20170203";
			File sff = new File(path);
			String sourcePathMD5 = MD5.getFileMD5(new File(path));
			String destPathMD5 = MD5.getFileMD5(new File(sourcePath));
			
			System.out.println(sourcePathMD5 + "," + destPathMD5);
			
			
			path1Map = listDir(path);

			Map<String, FileMd5> path2Map = listDir(sourcePath);
		 
			List<FileMd5> compareFile = compareFile(path2Map, path1Map);
			 
			printResult(compareFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	/**
	 * 打印结果 + 复制文件
	 */
	public static void printFile(List<FileMd5> fileMd5s) {
		CopyFileUtil copyUtil = new CopyFileUtil();

		boolean stateCopyResult = false;
		for (FileMd5 fileMd5 : fileMd5s) {
			System.out.println(fileMd5.getFile().getAbsolutePath() + " " + fileMd5.getMd5());

			String filePath = fileMd5.getFile().getAbsolutePath();
			String startTag = sourcePath;
			int index = filePath.indexOf(startTag);
			if (index != -1) {
				index = startTag.length() + 1;
				filePath = filePath.substring(index, filePath.length());
			}
			stateCopyResult = copyUtil.copyFile(fileMd5.getFile().getAbsolutePath(),
					"D:\\海图项目\\repositories\\Temp" + File.separator + filePath, true);
		}

//		if (stateCopyResult) {
//			// 遍历目录获取文件小
//			File preZip = new File("D:\\海图项目\\zip3");
//			FileSize fileSize = new FileSize();
//			long size = fileSize.getFileSize(preZip); 
//			// 压缩文件目录
//			boolean stateResult = zipFile(size); 
//		}

	} 
	
	/**
	 * 打印结果
	 */
	public static void printResult(List<FileMd5> compareFile) {

		System.out.println("########################结果########################");
		printFile(compareFile);

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
	
	
}
