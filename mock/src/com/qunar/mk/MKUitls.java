package com.qunar.mk;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.filechooser.FileSystemView;

public class MKUitls {
	public static String macName = "Mac OS X";

	public static boolean isMocSystem() {
		Properties props = System.getProperties(); // 获得系统属性集
		String osName = props.getProperty("os.name"); // <a
		return macName.equals(osName);
	}

	public static void initData(){
		String filePath = getMockFile() + "/f_c2b_flightlist.json";
		if (!(new File(filePath)).exists()) {
			InputStream in = MKUitls.class.getClass().getResourceAsStream("/resource/f_c2b_flightlist.json");
			OutputStream out;
			try {
				out = new FileOutputStream(filePath);
				byte[] buffer = new byte[1024];
				int length;
				while ((length = in.read(buffer)) > 0) {
					out.write(buffer, 0, length);
				}
				in.close();
				out.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		String fileWebPath = getMockFile() + "/server";
		if (!(new File(fileWebPath)).exists()) {
			InputStream in = MKUitls.class.getClass().getResourceAsStream("/resource/server.zip");
			OutputStream out;
			try {
				out = new FileOutputStream(MKUitls.getMockFile() + "/temp.zip");
				byte[] buffer = new byte[1024];
				int length;
				while ((length = in.read(buffer)) > 0) {
					out.write(buffer, 0, length);
				}
				in.close();
				out.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		unzip();
	}
	
	public static void unzip() {
		if (isMocSystem()) {
			String rootfile = getMockFile().getAbsolutePath();
			String s1 = "unzip " + rootfile + "/temp.zip -d" + rootfile;
			String ss = exec(s1).toString();
			String s2 = "rm -f " + rootfile + "/temp.zip";
			exec(s2).toString();
		}else {
			  String rootfile = getMockFile().getAbsolutePath();
	            String s1 = "unzip " + rootfile + "/temp.zip -d" + rootfile;
	            execWin(s1).toString();
	            String s2 = "rm -f " + rootfile + "/temp.zip";
	            execWin(s2).toString();
	            System.out.println("s1:" + s1);
	            System.out.println("s2:"+s2);
			
		}
	}
	 public static Object execWin(String cmd) {
	        try {
	            cmd = "cmd /c "+cmd;
	            Process process = Runtime.getRuntime().exec(cmd);
	            LineNumberReader br = new LineNumberReader(new InputStreamReader(process.getInputStream()));
	            StringBuffer sb = new StringBuffer();
	            String line;
	            while ((line = br.readLine()) != null) {
	                System.out.println(line);
	                sb.append(line).append("\n");
	            }
	            return sb.toString();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return null;
	    }


	public static boolean isSysArch64() {
		String sysArch = System.getProperty("os.arch");
		System.out.println("sys arch: " + sysArch);
		return sysArch.contains("64");
	}

	/// Users/dzc/code/workspece/MK/server/bin/startup.sh
	// sh ~/apache-tomcat-6.0.44/bin/startup.sh
	public static boolean startServer() {
		if (isMocSystem()) {
			// sh
			// /Users/dzc/mockdata/server/bin chmod u+x *.sh
			// /Users/dzc/code/workspece/MK/server/bin/startup.sh
			String rootfile = getMockFile().getAbsolutePath();
			String s2 = "chmod u+x " + rootfile + "/server/bin/*.sh";
			System.out.println("获取权限: " + exec(s2).toString());
			// String c = "chmod u+x *.sh";
			// exec(c).toString();

			System.out.println("rootfile服务: " + rootfile);
			String s1 = "sh " + rootfile + "/server/bin/startup.sh";
			// exec(s1);
			String ss = exec(s1).toString();
			if (ss != null && ss.length() > 0) {
				System.out.println("启动服务成功");
				return true;
			} else {
				System.out.println("启动服务失败");
			}
		} else {
			try {

				String rootfile = getMockFile().getAbsolutePath();
				String line;
				Runtime rt = Runtime.getRuntime();
				String cmd;
				if (isSysArch64()) {
					cmd = "cmd /c " + rootfile + "/server/bin/startup.bat";
				} else {
					cmd = "cmd /c " + rootfile + "/server/bin/startup.bat";
				}
				Process ps = rt.exec(cmd);
				BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
				while (null != (line = br.readLine())) {
					System.out.println(line);
					if (line.contains("exec success")) {
						System.out.println("启动服务: " + cmd);
						return true;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return false;
	}

	public static boolean stopServer() {
		if (isMocSystem()) {
			String rootfile = getMockFile().getAbsolutePath();
			String s1 = "sh " + rootfile + "/server/bin/shutdown.sh";
			String ss = exec(s1).toString();
			if (ss != null && ss.length() > 0) {
				return true;
			}
		} else {
			try {
				String rootfile = getMockFile().getAbsolutePath();
				String line;
				Runtime rt = Runtime.getRuntime();
				String cmd;
				if (isSysArch64()) {
					cmd = "cmd /c " + rootfile + "/server/bin/shutdown.bat";
				} else {
					cmd = "cmd /c " + rootfile + "/server/bin/shutdown.bat";
				}
				Process ps = rt.exec(cmd);
				BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
				while (null != (line = br.readLine())) {
					System.out.println(line);
					if (line.contains("exec success")) {
						System.out.println("停止服务: " + cmd);
						return true;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return false;
	}

	/**
	 * 运行shell
	 * 
	 * @param shStr
	 *            需要执行的shell
	 * @return
	 * @throws IOException
	 */
	public static List runShell(String shStr) throws Exception {
		List<String> strList = new ArrayList();

		Process process;
		process = Runtime.getRuntime().exec(new String[] { "/bin/sh", "-c", shStr }, null, null);
		// process = Runtime.getRuntime().exec(shStr);
		InputStreamReader ir = new InputStreamReader(process.getInputStream());
		LineNumberReader input = new LineNumberReader(ir);
		String line;
		process.waitFor();
		while ((line = input.readLine()) != null) {
			System.out.println(line);
			strList.add(line);
		}

		return strList;
	}

	public static Object exec(String cmd) {
		try {
			String[] cmdA = { "/bin/sh", "-c", cmd };
			Process process = Runtime.getRuntime().exec(cmdA);
			LineNumberReader br = new LineNumberReader(new InputStreamReader(process.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
				sb.append(line).append("\n");
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getIP() {
		if (isMocSystem()) {
			return getMOC();

		} else {
			return getWin();
		}
	}

	/**
	 * @see 获取windows系统上的ip(单网卡)
	 * @author Herman.Xiong
	 * @date 2014年5月16日 09:36:29
	 */
	public static String getMOC() {
		try {
			InetAddress addr = InetAddress.getLocalHost();
			String ip = addr.getHostAddress().toString();// 获得本机IP
			String address = addr.getHostName().toString();// 获得本机名称
			System.out.println("获得本机IP:" + ip);
			System.out.println("获得本机名称:" + address);
			return ip;
		} catch (Exception e) {

			e.printStackTrace();
			return "获取失败...";
		}
	}

	public static String getWin() {
        // 获取id
        InetAddress inet;
        try {
            inet = InetAddress.getLocalHost();
            String localIp = inet.getHostAddress().toString();// 获得本机IP
            String hostName = inet.getHostName().toString();
            System.out.println("本机名称:" + hostName);
            
            InetAddress[] inetAddresses = InetAddress.getAllByName(hostName);  
            for(InetAddress addr : inetAddresses){  
                String ip = addr.getHostAddress().toString();
                if(!ip.equals(null)&&!ip.equals(localIp)&&!ip.equals("127.0.0.1")&&ip.length()<16){
                    System.out.println("本机IP:" + ip);
                    return ip;
                }
            }  
        } catch (java.net.UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "点击重新获取ip";

    }

	static String getLocalMachineInfo(String str) {
		String line = "";
		int n;
		try {
			Process ps = Runtime.getRuntime().exec("cmd /c ipconfig /all");
			BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
			while (null != (line = br.readLine())) {
				if (line.indexOf(str) != -1) {
					n = line.indexOf(":");
					line = line.substring(n + 2);
					break;
				}
			}
			ps.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
			line = "获取失败...";
		} catch (Exception e) {
			e.printStackTrace();
			line = "获取失败...";
		}
		return line;
	}

	public static String getRootFile() {
		File directory = new File("");// 参数为空
		String courseFile = null;
		try {
			courseFile = directory.getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return courseFile;
	}

	public static File getMockFile() {
		FileSystemView fsv = FileSystemView.getFileSystemView();
		File rootfile = fsv.getHomeDirectory();
		String dataFile = rootfile.getPath() + "/mockdata";
		File parent = new File(dataFile);
		if (parent != null && !parent.exists()) {
			parent.mkdirs();
		}

		return parent;
	}

	public static String loadAFileToStringDE2(File f) throws IOException {
		long beginTime = System.currentTimeMillis();
		InputStream is = null;
		String ret = null;
		try {
			is = new FileInputStream(f);
			long contentLength = f.length();
			byte[] ba = new byte[(int) contentLength];
			is.read(ba);
			ret = new String(ba);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
				}
			}
		}
		long endTime = System.currentTimeMillis();
		System.out.println("方法2用时" + (endTime - beginTime) + "ms");
		return ret;
	}

	public static boolean deleFile(File file) {
		// TODO Auto-generated method stub
		return file.delete();

	}

	public static void copyFile(File file, String fileName, String fileContent) {
		if (file == null) {
			return;
		}

		String oldFileName = file.getName();

		if (oldFileName.equals(fileName)) {
			String fliePath = file.getPath();
			file.delete();
			WriteStringToFile2(fliePath, fileContent);
			// String oldPath
			// File tempFile = getTempFile();
			// copyFile(file.getAbsolutePath(), tempFile.getAbsolutePath());
		} else {
			File mockfile = new File(MKUitls.getMockFile() + "/" + fileName);
			try {
				if (!mockfile.exists()) {
					mockfile.createNewFile();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			WriteStringToFile2(mockfile.getAbsolutePath(), fileContent);
			file.delete();
		}
	}

	public static File getTempFile() {
		String tempFile = getMockFile() + "/temFile";
		File parent = new File(tempFile);
		if (parent != null && !parent.exists()) {
			parent.mkdirs();
		}
		return parent;
	}

	public static void WriteStringToFile2(String filePath, String content) {
		try {
			FileWriter fw = new FileWriter(filePath, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.append(content);
			bw.close();
			fw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 复制一个目录及其子目录、文件到另外一个目录
	 * 
	 * @param src
	 * @param dest
	 * @throws IOException
	 */
	public static void copyFolder(File src, File dest) throws IOException {
		if (src.isDirectory()) {
			if (!dest.exists()) {
				dest.mkdir();
			}
			String files[] = src.list();
			for (String file : files) {
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);
				// 递归复制
				copyFolder(srcFile, destFile);
			}
		} else {
			InputStream in = new FileInputStream(src);
			OutputStream out = new FileOutputStream(dest);

			byte[] buffer = new byte[1024];

			int length;

			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}
			in.close();
			out.close();
		}
	}

	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf.txt
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf.txt
	 * @return boolean
	 */
	public static void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();
		}
	}

	public static void createFile(String fileName) {
		File mockfile = new File(MKUitls.getMockFile() + "/" + fileName);
		try {
			if (!mockfile.exists()) {
				mockfile.createNewFile();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteStringToFile2(mockfile.getAbsolutePath(), "请填写json数据!");
	}
}
