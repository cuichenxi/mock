package com.qunar.webmk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.DefaultListModel;

public class MockData {
	public static File mockDataFile = MKUtils.getMockFile();

	public static ArrayList<File> getData() {
		mockDataFile = MKUtils.getMockFile();
		ArrayList<File> filelist = new ArrayList<File>();
		if (mockDataFile.exists() && mockDataFile.isDirectory()) {
			File[] files = mockDataFile.listFiles(); // 该文件目录下文件全部放入数组
			if (files != null && files.length > 0) {
				for (int i = 0; i < files.length; i++) {
					if (!files[i].isDirectory()&&files[i].getName().endsWith(".json")) { // 判断是文件还是文件夹
						String strFileName = files[i].getName();
						System.out.println(strFileName);
						filelist.add(files[i]);
					}
				}
			}
		}
		return filelist;
	}

	public static DefaultListModel<String>  getDataModle(ArrayList<File> data) {
		DefaultListModel<String>  fileNames =null;
		if(data!=null&&data.size()>0){
			fileNames = new DefaultListModel<String>();
			for (File file : data) {
				fileNames.addElement(file.getName());
			}
		}
		return fileNames;
	}
	public static String []  getDataNames(DefaultListModel<String> data) {
		String [] fileNames =null;
		if(data!=null&&data.size()>0){
			fileNames = new String [data.size()] ;
			for(int i =0 ;i<data.size() ;i++){
				fileNames[i] =data.get(i);
			}
			 
		}
		return fileNames;
	}

	public static File getFileByName(String requestTag) {
		ArrayList<File> data = getData();
		if (data == null || data.size() == 0) {
			return null;
		}
		for (File file : data) {
			if (file.exists() && file.getName().equals(requestTag+".json")) {
				return file;
			}
		}
		return null;
	}

    public static File getFileByFileName(String fileName) {
        ArrayList<File> data = getData();
        if (data == null || data.size() == 0) {
            return null;
        }
        for (File file : data) {
            if (file.exists() && file.getName().equals(fileName)) {
                return file;
            }
        }
        return null;
    }
    
    /**
	 * 以行为单位读取文件，常用于读面向行的格式化文件
	 */
	public static String readFileByLines(File file) {

		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"));
			String tempString = "";
			String resultString = "";
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				resultString = resultString + tempString;
			}
			reader.close();

			return resultString;
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

		return null;
	}
	 
}
