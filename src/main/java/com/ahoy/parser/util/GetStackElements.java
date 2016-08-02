package com.ahoy.parser.util;

public class GetStackElements {
	public static String getDetail(StackTraceElement[] stackTraceElements){
		StringBuilder builder = new StringBuilder();
		try{
			for (StackTraceElement stackTraceElement : stackTraceElements) {
				builder.append("class Name: "+stackTraceElement.getClassName()+" | File Name: "+stackTraceElement.getFileName()+" | Line No.: "+stackTraceElement.getLineNumber()+" | Method Name: "+stackTraceElement.getMethodName()+"\n");
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return builder.toString();
	}
	
	public static String getRootCause(Exception e, String className) {

        for (StackTraceElement element : e.getStackTrace()) {
            if (element.getClassName().equals(className)) {
                return "Exception : " + e + " at " + element;
            }
        }
        return null;
    }
}
