package com.qunar.mk;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FormatJson {

//	public static void main(String[] args) throws Exception {
//		String s = readFile("D:\\dev\\project\\JavaTest\\src\\com\\qunar\\test\\a.json");
//		s = formatJson(s);
//		System.out.println(s);
//	}

	public static String readFile(String filePath) throws Exception {
		StringBuilder resultData = new StringBuilder();
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(filePath));
			String line = null;
			while ((line = in.readLine()) != null) {
				resultData.append(line);
			}
			return resultData.toString();
		} catch (IOException e) {
		} finally {
			in.close();
		}
		return null;
	}

	private static String SPACE = "   ";

	public static String formatJson(String json) {
		
		StringBuffer result = new StringBuffer();

		int length = json.length();
		int number = 0;
		char key = 0;
		char key2 = 0;
		for (int i = 0; i < length; i++) {
			key = json.charAt(i);
			if(i<length-1){
				key2 = json.charAt(i+1);
			}

			if ((key == '[') || (key == '{')&&key2!='\\') {
				if ((i - 1 > 0) && (json.charAt(i - 1) == ':')) {
					result.append('\n');
					result.append(indent(number));
				}

				result.append(key);

				result.append('\n');

				number++;
				result.append(indent(number));

				continue;
			}

			if ((key == ']') || (key == '}')&&key2!='\\') {
				result.append('\n');

				number--;
				result.append(indent(number));

				result.append(key);

				if (((i + 1) < length) && (json.charAt(i + 1) != ',')) {
					result.append('\n');
				}
				continue;
			}

			if ((key == ',')&&key2!='\\') {
				result.append(key);
				result.append('\n');
				result.append(indent(number));
				continue;
			}

			result.append(key);
		}

		return result.toString();
	}

	private static String indent(int number) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < number; i++) {
			result.append(SPACE);
		}
		return result.toString();
	}

//	public static String format(String jsonStr) {
//		int level = 0;
//		StringBuffer jsonForMatStr = new StringBuffer();
//		for (int i = 0; i < jsonStr.length(); i++) {
//			char c = jsonStr.charAt(i);
//			if (level > 0
//					&& '\n' == jsonForMatStr.charAt(jsonForMatStr.length() - 1)) {
//				jsonForMatStr.append(getLevelStr(level));
//			}
//			switch (c) {
//			case '{':
//			case '[':
//				jsonForMatStr.append(c + "\n");
//				level++;
//				break;
//			case ',':
//				jsonForMatStr.append(c + "\n");
//				break;
//			case '}':
//			case ']':
//				jsonForMatStr.append("\n");
//				level--;
//				jsonForMatStr.append(getLevelStr(level));
//				jsonForMatStr.append(c);
//				break;
//			default:
//				jsonForMatStr.append(c);
//				break;
//			}
//		}
//
//		return jsonForMatStr.toString();
//
//	}
//
//	private static String getLevelStr(int level) {
//		StringBuffer levelStr = new StringBuffer();
//		for (int levelI = 0; levelI < level; levelI++) {
//			levelStr.append("\t");
//		}
//		return levelStr.toString();
//	}

}
