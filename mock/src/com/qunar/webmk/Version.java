package com.qunar.webmk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;


public class Version {
	
	/**
	 * jar包安装后目录
	 */
	public static File mockDataFile = MKUtils.getMockFile();
	
	/**
	 * 获取原有的版本
	 * @return
	 */
	public static float getOldVersion(){
		String versionFile = MKUtils.getMockFile() + "/version.md";
		float oldVersion=0;
		if((new File(versionFile)).exists()){
			oldVersion = readVersionFromFile(new File(versionFile));
		}
		return oldVersion;
	}
	
	/**
	 * 获取当前最新版本
	 * @return
	 */
	public static float getNowVersion(){
		InputStream in = MKUtils.class.getClass().getResourceAsStream("/resource/version.md");
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		float nowVersion = readVersionFromFile(reader);
	    return nowVersion;
	}
	
	/**
	 * 升级，更新version.md文件，server文件
	 */
	public static void updateVersion(){
		//更新version.md文件
		String versionFile = MKUtils.getMockFile() + "/version.md";
		if((new File(versionFile)).exists()){
			boolean isDelete = (new File(versionFile)).delete();
			if(isDelete){
				writeVersionFile();
			}else{
				System.out.println("更新版本-删除旧版本version.md失败");
			}
		}else{
			writeVersionFile();
		}
		
		
		//更新server文件
		String fileWebPath = MKUtils.getMockFile() + "/server";
		if ((new File(fileWebPath)).exists()) {
			boolean isDelete = deleteDir(new File(fileWebPath));
			if(isDelete){
				writeServerFile();
			}else{
				System.out.println("更新版本-删除旧版本server失败");
			}
		}else{
			writeServerFile();
		}
		
	}
	
	/**
	 * 从文件中读取版本号
	 * @param file
	 * @return
	 */
	private static float readVersionFromFile(File file){
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return readVersionFromFile(reader);
	}
	
	/**
	 * 读取版本号
	 * @param reader
	 * @return
	 */
	private static float readVersionFromFile(BufferedReader reader){
		String versionStr="0";
		try {
			String tempString = "";
			// 一次读入一行，查找version
			while ((tempString = reader.readLine()) != null) {
				if(tempString.contains("version")){
					String str[] = tempString.split(":");
					if(str!=null && str.length>=2){
						versionStr = str[1];
					}
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}

		return Float.valueOf(versionStr);
	}
	
	/**
	 * 将jar中的server资源写入到mockdata文件夹中
	 */
	public static void writeServerFile(){
		InputStream in = MKUtils.class.getClass().getResourceAsStream("/resource/server.zip");
		OutputStream out;
		try {
			out = new FileOutputStream(MKUtils.getMockFile() + "/temp.zip");
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
		MKUtils.unzip();
	}
	
	/**
	 * 将jar中的version文件，写入到mockdata文件夹中
	 */
	public static void writeVersionFile(){
		InputStream in = MKUtils.class.getClass().getResourceAsStream("/resource/version.md");
		OutputStream out;
		try {
			out = new FileOutputStream(MKUtils.getMockFile() + "/version.md");
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
	
	 /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
}
