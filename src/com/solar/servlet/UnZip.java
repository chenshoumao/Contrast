package com.solar.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.LinkedList;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServlet;

import com.solar.file.utils.FileSize;

public class UnZip extends HttpServlet implements Runnable {

	private static String updatePath = "D:/��ͼ��Ŀ/zip4";
	private static String sourceUpdatePath = "";

	static FileSize fileSize = new FileSize();

	public static void monitor() {

		// ����ļ�·��
		String outPath = "D:/��ͼ��Ŀ/zip5";
		String filePath = ("D:\\��ͼ��Ŀ\\zip4");

		try {

			// ��ȡ�ļ�ϵͳ��WatchService����
			WatchService watchService = FileSystems.getDefault().newWatchService();

			Paths.get(filePath).register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
					StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);

			// ��Ҫ������ļ�
			File file = new File(filePath);
			LinkedList<File> fList = new LinkedList<File>();
			fList.addLast(file);
			while (fList.size() > 0) {
				File f = fList.removeFirst();
				if (f.listFiles() == null)
					continue;
				for (File file2 : f.listFiles()) {
					if (file2.isDirectory()) {// ��һ��Ŀ¼
						fList.addLast(file2);
						// ����ע����Ŀ¼
						Paths.get(file2.getAbsolutePath()).register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
								StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
					}
				}
			}

			while (true) {
				// ��ȡ��һ���ļ��Ķ��¼�
				WatchKey key = watchService.take();
				for (WatchEvent<?> event : key.pollEvents()) {
					if ((event.kind().toString()).equals("ENTRY_CREATE")) {
						System.out.println(event.context() + " --> " + event.kind());
						String zipPath = traverseFolder2(filePath);
						Thread.sleep(3000);
						unzip(zipPath, outPath);
						// updateFile("","");
					}
					// System.out.println(event.kind() +"," + ((
					// event.kind().toString()).equals("ENTRY_CREATE")));

				}
				// ����WatchKey
				boolean valid = key.reset();
				// �������ʧ�ܣ��˳�����
				if (!valid) {
					break;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e);
			e.printStackTrace();
		}
	}

	public static String traverseFolder2(String path) {

		File file = new File(path);
		if (file.exists()) {
			File[] files = file.listFiles();
			if (files.length == 0) {
				System.out.println("�ļ����ǿյ�!");
				return "";
			} else {
				for (File file2 : files) {
					if (file2.isDirectory()) {
						System.out.println("�ļ���:" + file2.getAbsolutePath());
						return file2.getAbsolutePath();
						// traverseFolder2(file2.getAbsolutePath());
					} else {
						System.out.println("�ļ�:" + file2.getAbsolutePath());
						return file2.getAbsolutePath();
					}
				}
			}
		} else {
			System.out.println("�ļ�������!");
			return "";
		}
		return "";
	}

	public static boolean unzip(String sourcePath, String outPath) {
		boolean stateResult = false;
		
		long zipFileSize = 0;
		try {
			
			

			// �ļ�������
			FileInputStream fin = null;

			
			boolean runState = false;
			while (!runState) {
				
				
				try { 
					fin = new FileInputStream(sourcePath);
				File fileJudge = new File(sourcePath);
				String name = "";
				name = fileJudge.getName(); 
				long zipSize = Long.valueOf(name.substring(0, name.indexOf("_")));
				zipFileSize = Long.valueOf(name.substring(name.indexOf("_") + 1, name.indexOf(".")));
				long itSize = fileJudge.length();
				System.out.println(itSize + ",,," + zipSize);
				if (itSize == zipSize)
					break;

				System.out.println("................");
				 
				runState = true;
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(e);
					runState = false;
					Thread.sleep(4000);
				}
			}
			
			
			// ��Ҫά������ȡ����У��͵���������У��Ϳ�������֤�������ݵ�������
			CheckedInputStream checkIn = new CheckedInputStream(fin, new CRC32());
			// ָ������ �������������ļ���ѹ����
			Charset gbk = Charset.forName("GBK");
			// zip��ʽ��������
			ZipInputStream zin = new ZipInputStream(checkIn, gbk);

			// ����ѹ���ļ��е�����ѹ����Ŀ
			ZipEntry zinEntry;

			while ((zinEntry = zin.getNextEntry()) != null) {
				System.out.println(zinEntry);
				File targetFile = new File(outPath + File.separator + zinEntry.getName());

				System.out.println("..." + targetFile + "   " + targetFile.getParentFile());

				sourceUpdatePath = targetFile.toString();
				if (!targetFile.getParentFile().exists()) {
					System.out.println("..." + targetFile + "   " + targetFile.getParentFile());
					targetFile.getParentFile().mkdirs();
				}
				if (zinEntry.isDirectory()) {
					targetFile.mkdirs();
				} else {
					FileOutputStream fout = new FileOutputStream(targetFile);
					byte[] buff = new byte[1024];
					int length;
					while ((length = zin.read(buff)) > 0) {
						fout.write(buff, 0, length);
					}
					fout.close();
				}
			}

			zin.close();
			fin.close();
			System.out.println(checkIn.getChecksum().getValue());
			checkIn.close();

			Thread.sleep(4000);
			File file = new File(sourcePath);

			if (file.exists())
				file.delete();
			File afterUnZip5 = new File("D:\\��ͼ��Ŀ\\zip5");

			long afterUnZip5Size = fileSize.getFileSize(afterUnZip5);
			long preUnZip5Size = zipFileSize;

			boolean resultState = preUnZip5Size == afterUnZip5Size;
			if (resultState){
				System.out.println("��ѹ���ݳɹ�");
				stateResult = true;
			}
			else{
				System.out.println("��ѹ������ʧ");
				stateResult = false;
			}

		} catch (Exception e) {
			// TODO: handle exception
			stateResult = false;
			System.out.println(e);
		}
		return stateResult;
		// System.exit(0);
	}

	public static void updateFile(String resourcePath, String desPath) {
		File file = new File(resourcePath);
		if (file.exists()) {
			File[] files = file.listFiles();
			if (files.length == 0) {
				System.out.println("�ļ����ǿյ�!");

			} else {
				for (File file2 : files) {
					if (file2.isDirectory()) {
						System.out.println("�ļ���:" + file2.getAbsolutePath());

						// traverseFolder2(file2.getAbsolutePath());
					} else {
						System.out.println("�ļ�:" + file2.getAbsolutePath());

					}
				}
			}
		} else {
			System.out.println("�ļ�������!");

		}
	}

	public void init() {
		UnZip unzip = new UnZip();
		Thread thread = new Thread(unzip);
		thread.start();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println(12223);
		monitor();

	}

	public static void main(String[] args) {
		monitor();
	}

}