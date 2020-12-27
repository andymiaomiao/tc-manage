package com.weiqisen.tc.utils;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

	private static final int BUFFER_SIZE = 2 * 1024;

	/**
     * @param srcDir 压缩文件夹路径
	 * @param outDir 压缩文件输出流
	 * @param KeepDirStructure 是否保留原来的目录结构,
	 * 			true:保留目录结构;
	 *			false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
	 * @throws RuntimeException 压缩失败会抛出运行时异常
	 */
	public static void toZip(String[] srcDir, String outDir,
			boolean KeepDirStructure) throws RuntimeException, Exception {

		OutputStream out = new FileOutputStream(new File(outDir));

		long start = System.currentTimeMillis();
		ZipOutputStream zos = null;
		try {
			zos = new ZipOutputStream(out);
			List<File> sourceFileList = new ArrayList<File>();
			for (String dir : srcDir) {
				File sourceFile = new File(dir);
				sourceFileList.add(sourceFile);
			}
			compress(sourceFileList, zos, KeepDirStructure);
			long end = System.currentTimeMillis();
			System.out.println("压缩完成，耗时：" + (end - start) + " ms");
		} catch (Exception e) {
			throw new RuntimeException("zip error from ZipUtils", e);
		} finally {
			if (zos != null) {
				try {
					zos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static void toZipOutStream(List<Map<String,Object>> fileList, String outDir,String zipFileName, boolean KeepDirStructure) throws FileNotFoundException {
		OutputStream out = new FileOutputStream(new File(outDir+zipFileName));
		long start = System.currentTimeMillis();
		ZipOutputStream zos = null;
		try {
			zos = new ZipOutputStream(out);
			List<File> sourceFileList = new ArrayList<File>();
			StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("文件清单对照\r\n");
			for (Map<String,Object> dir : fileList) {
				File sourceFile = new File(String.valueOf(dir.get("fileUrl")));
//				File newfile=new File(sourceFile.getParent()+File.separator+ dir.get("fileName"));//创建新名字的抽象文件
//				if(sourceFile.renameTo(newfile)) {
//					System.out.println("重命名成功！");
//				}
				stringBuffer.append(dir.get("realName")+" ----------> "+dir.get("fileName")+"\r\n");
				sourceFileList.add(sourceFile);
			}
			File fileLog = new File(outDir+"文件清单"+IdWorker.getIdStr()+".txt");
			FileWriter writer = null;
			if (!fileLog.exists()) {
				fileLog.createNewFile();// 创建目标文件
				writer = new FileWriter(fileLog, true);
				writer.append(stringBuffer.toString());
				writer.flush();
			}
			sourceFileList.add(fileLog);
			compress(sourceFileList, zos, KeepDirStructure);
			long end = System.currentTimeMillis();
			System.out.println("压缩完成，耗时：" + (end - start) + " ms");
			fileLog.delete();
		} catch (Exception e) {
			throw new RuntimeException("zip error from ZipUtils", e);
		} finally {
			if (zos != null) {
				try {
					zos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 递归压缩方法
	 * @param sourceFile 源文件
	 * @param zos zip输出流
	 * @param name 压缩后的名称
	 * @param KeepDirStructure 是否保留原来的目录结构,
	 * 			true:保留目录结构;
	 *			false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
	 * @throws Exception
	 */
	private static void compress(File sourceFile, ZipOutputStream zos,
			String name, boolean KeepDirStructure) throws Exception {
		byte[] buf = new byte[BUFFER_SIZE];
		if (sourceFile.isFile()) {
			zos.putNextEntry(new ZipEntry(name));
			int len;
			FileInputStream in = new FileInputStream(sourceFile);
			while ((len = in.read(buf)) != -1) {
				zos.write(buf, 0, len);
			}
			// Complete the entry
			zos.closeEntry();
			in.close();
		} else {
			File[] listFiles = sourceFile.listFiles();
			if (listFiles == null || listFiles.length == 0) {
				if (KeepDirStructure) {
					zos.putNextEntry(new ZipEntry(name + "/"));
					zos.closeEntry();
				}

			} else {
				for (File file : listFiles) {
					if (KeepDirStructure) {
						compress(file, zos, name + "/" + file.getName(),
								KeepDirStructure);
					} else {
						compress(file, zos, file.getName(), KeepDirStructure);
					}

				}
			}
		}
	}

	private static void compress(List<File> sourceFileList,
			ZipOutputStream zos, boolean KeepDirStructure) throws Exception {
		byte[] buf = new byte[BUFFER_SIZE];
		for (File sourceFile : sourceFileList) {
			String name = sourceFile.getName();
			if (sourceFile.isFile()) {
				zos.putNextEntry(new ZipEntry(name));
				int len;
				FileInputStream in = new FileInputStream(sourceFile);
				while ((len = in.read(buf)) != -1) {
					zos.write(buf, 0, len);
				}
				zos.closeEntry();
				in.close();
			} else {
				File[] listFiles = sourceFile.listFiles();
				if (listFiles == null || listFiles.length == 0) {
					if (KeepDirStructure) {
						zos.putNextEntry(new ZipEntry(name + "/"));
						zos.closeEntry();
					}

				} else {
					for (File file : listFiles) {
						if (KeepDirStructure) {
							compress(file, zos, name + "/" + file.getName(),
									KeepDirStructure);
						} else {
							compress(file, zos, file.getName(),
									KeepDirStructure);
						}

					}
				}
			}
		}
	}


	/**
	 * 把文件打成压缩包并输出到客户端浏览器中
	 */
	public static void downloadZipFiles(HttpServletResponse response, List<String> srcFiles, String zipFileName) {
		try {
			response.reset(); // 重点突出
			response.setCharacterEncoding("UTF-8"); // 重点突出
			response.setContentType("application/x-msdownload"); // 不同类型的文件对应不同的MIME类型 // 重点突出
			// 对文件名进行编码处理中文问题
			zipFileName = new String(zipFileName.getBytes(), StandardCharsets.UTF_8);
			// inline在浏览器中直接显示，不提示用户下载
			// attachment弹出对话框，提示用户进行下载保存本地
			// 默认为inline方式
			response.setHeader("Content-Disposition", "attachment;filename=" + zipFileName);

			// --设置成这样可以不用保存在本地，再输出， 通过response流输出,直接输出到客户端浏览器中。
			ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
			zipFile(srcFiles, zos);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 压缩文件
	 *
	 * @param filePaths 需要压缩的文件路径集合
	 * @throws IOException
	 */
	private static void zipFile(List<String> filePaths, ZipOutputStream zos) {
		//设置读取数据缓存大小
		byte[] buffer = new byte[4096];
		try {
			//循环读取文件路径集合，获取每一个文件的路径
			for (String filePath : filePaths) {
				File inputFile = new File(filePath);
				//判断文件是否存在
				if (inputFile.exists()) {
					//判断是否属于文件，还是文件夹
					if (inputFile.isFile()) {
						//创建输入流读取文件
						BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inputFile));
						//将文件写入zip内，即将文件进行打包
						zos.putNextEntry(new ZipEntry(inputFile.getName()));
						//写入文件的方法，同上
						int size = 0;
						//设置读取数据缓存大小
						while ((size = bis.read(buffer)) > 0) {
							zos.write(buffer, 0, size);
						}
						//关闭输入输出流
						zos.closeEntry();
						bis.close();
					} else {  //如果是文件夹，则使用穷举的方法获取文件，写入zip
						File[] files = inputFile.listFiles();
						List<String> filePathsTem = new ArrayList<String>();
						for (File fileTem : files) {
							filePathsTem.add(fileTem.toString());
						}
						zipFile(filePathsTem, zos);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != zos) {
				try {
					zos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {

		String[] srcDir = { "path\\Desktop\\java",
				"path\\Desktop\\java2",
				"path\\Desktop\\fortest.txt" };
		String outDir = "path\\Desktop\\aaa.zip";
		ZipUtil.toZip(srcDir, outDir, true);
	}
}
