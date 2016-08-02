package com.ahoy.parsetest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Pepper {
	public static void main(String []args){
		try {
			
			DateFormat format = new SimpleDateFormat("dd-MMM-yyyy_hh:mm");
			System.out.println(format.format(new Date()));
			
			File f = new File("f:\\"+format.format(new Date())+".csv");
			System.out.println(f.getName());
//			String recievers = "vivek@ahoy.co.in|";
//			String spltR[] =recievers.split("\\|");
//			System.out.println(spltR.length+"| "+spltR[0]);
//			if(spltR!=null&&spltR.length==2){
//				
//			}
			
//			String str = "13";
//			
//			System.out.println(str!=null&&str.trim().matches("[0-9]+(?:\\.[0-9]+)?")?Float.valueOf(str.trim()):0);
			
//			downloadFile("http://grocermax.com/productcsv/inventory.csv", "/home/applogs/ShopEZZY/shopezzylive/files/downloadfile/grocermax");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		Connection connection = Jsoup.connect("http://shop.peppertap.com/welcome");
//		
//		Response response;
//		try {
//			System.out.println(connection.request().hasCookie("Peppertap.session_id"));
////			response = connection.execute();
//			Map<String, String> cookies = connection.request().cookies();;
//			System.out.println(cookies.size());
//			for(Map.Entry<String, String> map : cookies.entrySet()){
//				System.out.println(map.getKey()+" | "+map.getValue());
//			}
//			System.out.println(connection.response().header("Peppertap.session_id"));
//			cookies = connection.response().cookies();
//			System.out.println(cookies.size());
//			for(Map.Entry<String, String> map : cookies.entrySet()){
//				System.out.println(map.getKey()+" | "+map.getValue());
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		
	}
	
	public static void downloadFile(String fileURL, String saveDir)
            throws IOException {
        URL url = new URL(fileURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();
 
        // always check HTTP response code first
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String fileName = "";
            String disposition = httpConn.getHeaderField("Content-Disposition");
            String contentType = httpConn.getContentType();
            int contentLength = httpConn.getContentLength();
 
            if (disposition != null) {
                // extracts file name from header field
                int index = disposition.indexOf("filename=");
                if (index > 0) {
                    fileName = disposition.substring(index + 10,
                            disposition.length() - 1);
                }
            } else {
                // extracts file name from URL
                fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
                        fileURL.length());
            }
 
            System.out.println("Content-Type = " + contentType);
            System.out.println("Content-Disposition = " + disposition);
            System.out.println("Content-Length = " + contentLength);
            System.out.println("fileName = " + fileName);
 
            // opens input stream from the HTTP connection
            InputStream inputStream = httpConn.getInputStream();
            
            
            
            String saveFilePath = saveDir + File.separator + fileName;            
             
            
            File f = new File(saveFilePath);
            if(!new File(saveDir).mkdirs()){
        		if(f.exists()){
	            	f.delete();
	            }
        	}
            
            // opens an output stream to save into file
            FileOutputStream outputStream = new FileOutputStream(saveFilePath);
 
            int bytesRead = -1;
            byte[] buffer = new byte[4096];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
 
            outputStream.close();
            inputStream.close();
 
            System.out.println("File downloaded");
        } else {
            System.out.println("No file to download. Server replied HTTP code: " + responseCode);
        }
        httpConn.disconnect();
    }
}
