package com.ahoy.parsetest;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Map;

public class LocalBanya {
	public static void main(String[] args){
		try{
			
			Connection con = getConnectionViaJsoup("http://www.localbanya.com/", null, false);
			Response response = con.execute();
			Map<String, String> mapCookies = response.cookies();
			
			System.out.println("=================================================================================");
			for(Map.Entry<String, String> map : mapCookies.entrySet()){
				System.out.println(map.getKey()+": "+map.getValue());
			}
			System.out.println("=================================================================================");
			Map<String, String> mapHeader = response.headers();
			
			for(Map.Entry<String, String> map : mapHeader.entrySet()){
				System.out.println(map.getKey()+": "+map.getValue());
			}
			
			System.out.println("=================================================================================");
			Document doc = response.parse();
			Element element = doc.getElementById("multicity");
			element = element.select("div.city-container").first();
			element = element.select("div.cities.mltctydrpdn").first();
			element = element.select("select.cityname").first();
			Elements elements = element.getElementsByTag("option");
			
			for (Element element2 : elements) {
				String cityName = element2.text();
				String cityValue = element2.attr("value");
				System.out.println(cityName+": "+cityValue);
			}
			
			
			
		}catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public static Connection getConnectionViaJsoup(String url,Map<String, String> map,boolean ignoreContentType){
		Connection connection = null;		
		try{
			
			connection = Jsoup.connect(url)
					.method(Method.GET)
					.timeout(25000)
					.ignoreContentType(ignoreContentType);
				if(map!=null && map.size()>0)
					connection = connection.cookies(map);
						
			
		}catch (Exception e) {
			System.out.println(e);
		}
		return connection;				
	}
}
