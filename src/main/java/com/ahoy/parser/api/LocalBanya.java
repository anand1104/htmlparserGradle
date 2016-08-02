package com.ahoy.parser.api;

import com.ahoy.parser.dao.MerchantDaoImpl;
import com.ahoy.parser.domain.MerchantDo;
import com.ahoy.parser.util.DBUtil;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class LocalBanya extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	Logger logger = LoggerFactory.getLogger(LocalBanya.class);
	
	protected void process(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		
		PrintWriter out = null;
		String resp="";
		try{
			
			out = response.getWriter();
			
			HashMap<String, HashMap<String, String>> mapCategories = this.getAllCat();
			
			if(mapCategories!=null && mapCategories.size()>0){
				DBUtil dbUtil = new DBUtil();
				MerchantDo merchantDo = new MerchantDaoImpl().getByMerchantname("localbanya");
				
				if(merchantDo!=null){
					for (Map.Entry<String, HashMap<String, String>> mapCategory : mapCategories.entrySet()) {
						
						String category =mapCategory.getKey();					
						HashMap<String, String> mapSubCategories = mapCategory.getValue();
						
						for(Map.Entry<String, String> mapSubCategory:mapSubCategories.entrySet()){
							
							String subCat = mapSubCategory.getKey();
							String subCatUrl = mapSubCategory.getValue();
							Document doc = this.hitUrlByJsoup(subCatUrl);
							
							if(doc!=null){
								
								Elements elementsscroll1 = doc!=null?doc.getElementsByClass("scroll1"):null;
								int total = 0;
								
								if(elementsscroll1!=null && !elementsscroll1.isEmpty()){
									for (Element element : elementsscroll1) {
										String totalProd = element.getElementsByTag("span").text();
										totalProd = totalProd!=null&&!"".equals(totalProd)?totalProd.substring(1,totalProd.indexOf(")")):"";
										totalProd = totalProd.replaceAll("Products", "").trim();
										total = totalProd.matches("[0-9]+")?Integer.valueOf(totalProd.trim()):total;
									}
								}							
								
								int page = total>20?total/20+(total%20!=0?1:0):1;
								Elements elements = null;
								logger.info("[LocalBanya][process] catName: "+category+" | subCat: "+subCat+" | page: "+page+" | total: "+total);
								String pageUrl ="";
								for(int i =0 ; i < page ; i++){
									if(i==0){
										elements = doc!=null?doc.getElementsByClass("product-inner"):null;
										if(i !=page-1){
											pageUrl = doc!=null?doc.getElementsByClass("jscroll-next").attr("href"):"";
										}
									}else{
										doc = null;
										doc = pageUrl!=null && !"".equals(pageUrl)?this.hitUrlByHttpUrlConnection(pageUrl):null;
										elements = doc!=null?doc.getElementsByClass("product-inner"):null;
										pageUrl = "";
										if(i !=page-1){
											pageUrl = doc!=null?doc.getElementsByClass("jscroll-next").attr("href"):"";
										}
									}
									
									if(elements!=null && !elements.isEmpty()){
										
										for (Element element : elements) {
	
											String imgUrl = "";
											String description = "";
											String newPrice = "";
											String oldPrice = "";
											String discount = "";
											String weight = "";
											
											discount = element.getElementsByClass("discount-dis").text();
											
											Elements elements1 = element.getElementsByClass("productImg");
											for (Element element1 : elements1) {
												Elements elements2 = element1.getElementsByTag("img");
												for (Element element2 : elements2) {
													imgUrl = element2.attr("data-original");
												}
											}
											
											description = element.getElementsByClass("prName").text();
											
											Elements elements2 = element.getElementsByClass("skuPrice");
											for (Element element2 : elements2) {
												weight = element2.getElementsByClass("sku_weight").text();
												newPrice = element2.getElementsByClass("new-price").text();
												oldPrice = element2.getElementsByClass("old-price").text();
												
											}
//											String respDB = dbUtil.saveOrUpdateDB(category, subCat, description, weight, oldPrice, discount, newPrice, imgUrl, "", "", merchantDo, "");
//											logger.info("[LocalBanya][process] catName: "+category+" | subCat: "+subCat+" | pageNo: "+(i+1)+" | product: "+description+" | weight: "+weight+" | oldPrice: "+oldPrice+" | discount: "+discount+" | newPrice: "+newPrice+" | imgUrl: "+imgUrl+" | respDB: "+respDB);
										}
									}
								}	
							}else{
								logger.info("[LocalBanya][process] catName: "+category+" | subCat: "+subCat+" | subCatUrl: "+subCatUrl+" | doc is null !!");
							}
						}
					}
				}else{
					logger.info("[LocalBanya][process]merchant detail not found \"localbanya\"");
				}
			}else{
				logger.info("[LocalBanya][process] mapCategories: "+(mapCategories!=null?mapCategories.size():0));
			}
			

			resp="process complete";
		}catch (Exception e) {
			resp="Internal Server Error";
			logger.error("[LocalBanya][process] Exception: "+e);
		}finally{
			out.println(resp);
			logger.info("[LocalBanya][process] resp: "+resp);
		}
		
	}
	
	private HashMap<String, HashMap<String, String>>  getAllCat() {
		
		HashMap<String, HashMap<String, String>> multiHashMap = new HashMap<String, HashMap<String,String>>();
		try{

			
			Document doc = Jsoup.connect("http://www.localbanya.com/").timeout(25000).get();
			
			Elements elements1 = doc.getElementsByTag("aside");
			
			for (Element element1 : elements1) {
				
				Element element2 = element1.getElementById("cat");
				Elements elements2 =  element2.getElementsByClass("left_nav_cat");
				
				for (Element element3 : elements2) {
					
					Elements elements3 = element3.getElementsByClass("sub_cat");
					HashMap<String, String> hashMap = new HashMap<String, String>();
					for (Element element4 : elements3) {
						hashMap.put(element4.text(), element4.attr("href"));
					}
					
					multiHashMap.put(element3.getElementById("cat-name").text(), hashMap);
				}
			}
		}catch (Exception e) {
			logger.error("[LocalBanya][getAllCat] Exception: "+e);
		}
		
		return multiHashMap;
	}
	
	private Document hitUrlByJsoup(String url){
		
		Document document = null;		
		try{
			Connection connection = Jsoup.connect(url).timeout(25000);
			Response response = connection.execute();
			document = response.parse();	
			
		}catch (Exception e) {
			logger.error("[LocalBanya][hitUrlByJsoup]["+url+"] document:"+document+" Exception: "+e);
		}
		return document;				
	}
	
	private Document hitUrlByHttpUrlConnection(String url){
		Document document=null;
		try{
			URL url2 = new URL(url);
			HttpURLConnection connection = (HttpURLConnection)url2.openConnection();
						
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = "";
			StringBuilder builder = new StringBuilder();
			while((line=br.readLine())!=null){
				builder.append(line);
			}
			
			JSONObject jsonObject = new JSONObject(builder.toString());
			String status = jsonObject.getString("status");
//			System.out.println("status: "+status);
			if(status!=null && status.equalsIgnoreCase("success")){
				document = Jsoup.parse(jsonObject.getString("response"));
			}
			
		}catch (Exception e) {
			logger.error("[LocalBanya][hitUrlByHttpUrlConnection] Exception: "+e);
		}
		
		return document;
		
	}
		
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		  process(request,response);
	}
	  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  process(request,response);
	}
	
	public static void main(String[] args){
		
		try{
			Connection connection = Jsoup.connect("http://www.localbanya.com/").timeout(25000);
			Response response = connection.execute();
//			
			
			System.out.println("------------COOKIES------------------");
			Map<String, String> cookies = response.cookies();
			for(Map.Entry<String, String> cookie: cookies.entrySet()){
				if(cookie.getKey().trim().equals("lb_session") || cookie.getKey().trim().equals("L_Cookie_PARENT_CITY")){
					System.out.println(cookie.getKey()+" | "+URLDecoder.decode(cookie.getValue(),"UTF-8"));
				}else{
					System.out.println(cookie.getKey()+" | "+cookie.getValue());
				}
			}
			System.out.println("--------------HEADERS----------------");
			Map<String, String> resHeader = response.headers();
//			
			for(Map.Entry<String, String> header: resHeader.entrySet()){
					
					System.out.println(header.getKey()+" | "+header.getValue());
			}
//			
//			System.out.println(URLDecoder.decode("a%3A5%3A%7Bs%3A10%3A%22session_id%22%3Bs%3A32%3A%2235cf4fe50f9cc955d25183663b5df6ef%22%3Bs%3A10%3A%22ip_address%22%3Bs%3A11%3A%2252.74.90.73%22%3Bs%3A10%3A%22user_agent%22%3Bs%3A102%3A%22Mozilla%2F5.0+%28Windows+NT+6.1%29+AppleWebKit%2F537.36+%28KHTML%2C+like+Gecko%29+Chrome%2F44.0.2403.155+Safari%2F537.36%22%3Bs%3A13%3A%22last_activity%22%3Bi%3A1439805958%3Bs%3A9%3A%22user_data%22%3Bs%3A0%3A%22%22%3B%7Deeac45eb51cecf941a90002acb8e55f3;", "UTF-8"));
//			Document document = response.parse();
//			System.out.println(URLDecoder.decode("Cookie_Supported=1; __lc.visitor_id.4488361=S1417163423.a0f85c1115; L_Cookie_Campaign=true; __utmt=1; WZRK_G=989b7ce88c0e4e52bed2f9f033509bd8; WZRK_S_KZR-7R7-49KZ=%7B%22p%22%3A1%2C%22s%22%3A1430890050%2C%22t%22%3A1430890048%7D; __utma=206849294.1461157570.1417163406.1426503356.1430890047.7; __utmb=206849294.2.10.1430890047; __utmc=206849294; __utmz=206849294.1418907965.3.3.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=(not%20provided); WZRK_P=http%3A%2F%2Fwww.localbanya.com%2F; lb_session=a%3A6%3A%7Bs%3A10%3A%22session_id%22%3Bs%3A32%3A%2239b1d1131319e5ed34978cda909df92e%22%3Bs%3A10%3A%22ip_address%22%3Bs%3A11%3A%2252.74.90.73%22%3Bs%3A10%3A%22user_agent%22%3Bs%3A102%3A%22Mozilla%2F5.0+%28Windows+NT+6.1%29+AppleWebKit%2F537.36+%28KHTML%2C+like+Gecko%29+Chrome%2F38.0.2125.111+Safari%2F537.36%22%3Bs%3A13%3A%22last_activity%22%3Bi%3A1430890274%3Bs%3A9%3A%22user_data%22%3Bs%3A0%3A%22%22%3Bs%3A14%3A%22SESS_CITY_DATA%22%3Ba%3A4%3A%7Bs%3A4%3A%22MQ%3D%3D%22%3Bs%3A6%3A%22Mumbai%22%3Bs%3A4%3A%22Mg%3D%3D%22%3Bs%3A4%3A%22Pune%22%3Bs%3A4%3A%22NA%3D%3D%22%3Bs%3A9%3A%22Hyderabad%22%3Bs%3A4%3A%22OA%3D%3D%22%3Bs%3A5%3A%22Delhi%22%3B%7D%7D2a0b499059dca26da2060fbae105f89a; lc_window_state=minimized; L_Cookie_CITY=MQ==", "UTF-8"));
//			
//			
			Connection con = Jsoup.connect("http://www.localbanya.com/").timeout(25000)
					.cookies(cookies);
			for(Map.Entry<String, String> header: resHeader.entrySet()){
				
				System.out.println(header.getKey()+" | "+header.getValue());
				con.header(header.getKey().trim(), header.getValue().trim());
		}
			Response res = con.execute();
			System.out.println(res.body());
					
			
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
}
