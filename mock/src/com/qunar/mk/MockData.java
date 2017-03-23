package com.qunar.mk;

import java.io.File;
import java.util.ArrayList;

import javax.swing.DefaultListModel;

public class MockData {
	public static File mockDataFile = MKUitls.getMockFile();

	public static ArrayList<File> getData() {
		mockDataFile = MKUitls.getMockFile();
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
	 
}
