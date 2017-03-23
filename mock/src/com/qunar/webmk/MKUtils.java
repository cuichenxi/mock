package com.qunar.webmk;

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

public class MKUtils {
	public static String macName = "Mac OS X";

	public static boolean isMocSystem() {
		Properties props = System.getProperties(); // 获得系统属性集
		String osName = props.getProperty("os.name"); // <a
		return macName.equals(osName);
	}

	/**
	 * 初始化服务器和json数据
	 */
	public static void initData() {
		//创建一个demo json文件
		String filePath = getMockFile() +"/默认目录"+ "/f_demo.json";
		if (!(new File(filePath)).exists()) {
			InputStream in = MKUtils.class.getClass().getResourceAsStream("/resource/f_demo.json");
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
		
		//判断版本信息，判断是否更新服务
		if(Version.getNowVersion()>Version.getOldVersion()){
			Version.updateVersion();
		}
			
	}

	/**
	 * 获得mockdata目录
	 * 
	 * @return
	 */
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

	/**
	 * 解压
	 */
	public static void unzip() {
		if (isMocSystem()) {
			String rootfile = getMockFile().getAbsolutePath();
			String s1 = "unzip " + rootfile + "/temp.zip -d" + rootfile;
			exec(s1).toString();
			String s2 = "rm -f " + rootfile + "/temp.zip";
			exec(s2).toString();
			System.out.println("s1:" + s1);
		} else {
			String rootfile = getMockFile().getAbsolutePath();
			String s1 = "unzip " + rootfile + "/temp.zip -d" + rootfile;
			execWin(s1).toString();
			String s2 = "rm -f " + rootfile + "/temp.zip";
			execWin(s2).toString();
			System.out.println("s1:" + s1);
		}
	}

	/**
	 * 启动tomcat服务
	 * 
	 * @return
	 */
	public static boolean startServer() {
		if (isMocSystem()) {
			String rootfile = getMockFile().getAbsolutePath();
			String s2 = "chmod u+x " + rootfile + "/server/bin/*.sh";
			// System.out.println("获取权限: " + exec(s2).toString());

			// System.out.println("rootfile服务: " + rootfile);
			String s1 = "sh " + rootfile + "/server/bin/startup.sh";
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
						System.out.println("启动tomcat服务成功 ");
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
	 * 停止tomcat服务
	 * 
	 * @return
	 */
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
	 * 是否是windows 64位系统
	 * 
	 * @return
	 */
	public static boolean isSysArch64() {
		String sysArch = System.getProperty("os.arch");
		System.out.println("sys arch: " + sysArch);
		return sysArch.contains("64");
	}

	/**
	 * 启动浏览器
	 */
	public static void startExplorer() {
		String cmd;
		if (isMocSystem()) {
			cmd = "open http://localhost:8080/fm/";
			exec(cmd);
		} else {
			cmd = "start explorer http://localhost:8080/fm/";
			execWin(cmd);
		}
		System.out.println("启动浏览器：" + cmd);
	}

	/**
	 * 执行命令
	 * 
	 * @param cmd
	 */
	public static void execCmd(String cmd) {
		if (isMocSystem()) {
			exec(cmd);
		} else {
			execWin(cmd);
		}
		System.out.println("执行命令：" + cmd);
	}

	/**
	 * 执行命令（mac系统）
	 * 
	 * @param cmd
	 * @return
	 */
	public static Object exec(String cmd) {
		try {
			String[] cmdA = { "/bin/sh", "-c", cmd };
			Process process = Runtime.getRuntime().exec(cmdA);
			LineNumberReader br = new LineNumberReader(new InputStreamReader(process.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
//				System.out.println(line);
				sb.append(line).append("\n");
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * windows系统执行docs命令
	 * 
	 * @param cmd
	 * @return
	 */
	public static Object execWin(String cmd) {
		try {
			cmd = "cmd /c " + cmd;
			Process process = Runtime.getRuntime().exec(cmd);
			LineNumberReader br = new LineNumberReader(new InputStreamReader(process.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
//				System.out.println(line);
				sb.append(line).append("\n");
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
