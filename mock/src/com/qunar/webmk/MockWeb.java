package com.qunar.webmk;

public class MockWeb {
	public static void main(String[] args) {
		//解压服务
		MKUtils.initData();
		//启动服务 
		MKUtils.startServer();  //能判断当前服务是否是启动状态?
		
		//执行adb命令
		MKUtils.execCmd("adb reverse tcp:8080 tcp:8080");
		
		//打开浏览器
		MKUtils.startExplorer();
	}
}