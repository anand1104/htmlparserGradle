package com.ahoy.parsetest;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import java.util.Map;

public class SRSGrocery {

	public static void main(String[] args) {
		
		try {
			Connection con = Jsoup.connect("http://www.srsgrocery.com/").timeout(25000);
			Response response = con.execute();
			
			Map<String, String> cookies = response.cookies();
			
			System.out.println("Cookie size: "+cookies.size());
			for(Map.Entry<String, String> cookie: cookies.entrySet()){
				System.out.println(cookie.getKey()+": "+cookie.getValue());
			}
			
			Map<String, String> headers = response.headers();
			System.out.println("Header Size: "+headers.size());
			
			for(Map.Entry<String, String> header: headers.entrySet()){
				System.out.println(header.getKey()+": "+header.getValue());
			}
			
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
