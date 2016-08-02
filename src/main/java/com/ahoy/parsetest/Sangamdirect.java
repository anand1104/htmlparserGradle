package com.ahoy.parsetest;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import java.util.Map;

public class Sangamdirect {

	public static void main(String[] args) {
		
		try{
			
			Connection cn = Jsoup.connect("http://www.sangamdirect.com/")
					.timeout(25000);
			Response res = cn.execute();
			Map<String, String> responseCookies = res.cookies();
			
			for(Map.Entry<String, String> responseCookie:responseCookies.entrySet()){
				System.out.println(responseCookie.getKey()+": "+responseCookie.getValue());
			}
			
			Map<String, String> responseHeaders = res.headers();
			
			for(Map.Entry<String, String> header:responseHeaders.entrySet()){
				System.out.println(header.getKey()+": "+header.getValue());
			}
			
			Connection cn1 = Jsoup.connect("http://www.sangamdirect.com/city")
					.method(Method.GET)
					.cookies(responseCookies)
					.userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36")
					.data("city", "DELHI")
					.data("redirect", "ref")
//					
//					.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
//					.header("Accept-Encoding", "gzip, deflate, sdch")
//					.header("Accept-Language", "en-GB,en-US;q=0.8,en;q=0.6")
//					.header("Connection", "keep-alive")
//					.header("Host", "www.sangamdirect.com")
//					
//					.header("User-Agent","Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36")
//					.referrer( "http://www.sangamdirect.com/")
					.timeout(25000);
			
			System.out.println(cn1.execute().statusCode());
//			
//			Response res1 = cn1.execute();
//			Map<String, String> responseCookies1 = res1.cookies();
//			
//			for(Map.Entry<String, String> responseCookie:responseCookies1.entrySet()){
//				System.out.println(responseCookie.getKey()+": "+responseCookie.getValue());
//			}
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
