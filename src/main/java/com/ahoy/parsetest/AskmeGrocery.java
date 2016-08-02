package com.ahoy.parsetest;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Map;

public class AskmeGrocery {
	public static void main(String [] args){
		
		try {
			Connection con = Jsoup.connect("http://www.askmegrocery.com/")
					.timeout(25000);
			
			Response res = con.execute();
			
			Map<String, String> responseCookies = res.cookies();
			System.out.println(responseCookies.size());
			for(Map.Entry<String, String> responseCookie:responseCookies.entrySet()){
				System.out.println(responseCookie.getKey()+": "+responseCookie.getValue());
			}
			
			con = Jsoup.connect("http://www.askmegrocery.com/shared/setArea")
					.method(Method.POST)
					.cookies(responseCookies)
					.ignoreContentType(true)
					.header("Accept","application/json")
					.data("StateID","34")
					.data("CityID","2")
					.data("AreaID","1146")
					.data("Area","Sector - 64")
					.data("ZonalCode","INUPNOID001")
					.data("StateName","Uttar Pradesh")
					.data("CityName","Noida")
					.timeout(25000);
			
			Response res1 = con.execute();
			Map<String, String> responseCookies1 = res1.cookies();
			System.out.println(responseCookies1.size());
			for(Map.Entry<String, String> responseCookie:responseCookies1.entrySet()){
				System.out.println(responseCookie.getKey()+": "+responseCookie.getValue());
			}
			
			
			Document doc = Jsoup.connect("http://www.askmegrocery.com")
					.cookies(responseCookies1)
					.timeout(25000)
					.get();
			System.out.println(doc.getElementById("spnAreaName"));
			
			
			System.out.println("====Done====");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
