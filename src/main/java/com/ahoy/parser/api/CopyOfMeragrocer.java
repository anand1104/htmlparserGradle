package com.ahoy.parser.api;

import com.ahoy.parser.dao.MerchantDaoImpl;
import com.ahoy.parser.domain.MerchantDo;
import com.ahoy.parser.util.DBUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class CopyOfMeragrocer extends HttpServlet{
	
	private static final long serialVersionUID = -6849180361152628937L;
	Logger logger = LoggerFactory.getLogger(CopyOfMeragrocer.class);
	
	protected void process(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		String resp = null;
		PrintWriter out = null;
		try{
			
			out = response.getWriter();
			HashMap<String, HashMap<String, String>> mapCategories = this.getCategories();
			
			if(mapCategories!=null && mapCategories.size()>0){
				MerchantDo merchantDo = new MerchantDaoImpl().getByMerchantname("meragrocer");
				
				if(merchantDo!=null){
				
					for(Map.Entry<String, HashMap<String, String>> mapCategory:mapCategories.entrySet()){
						String catName = mapCategory.getKey();
						HashMap<String, String> mapSubCategories = mapCategory.getValue();
						for(Map.Entry<String, String> mapSubCategory:mapSubCategories.entrySet()){
							String subCatName = mapSubCategory.getKey();
							String subCatUrl = mapSubCategory.getValue();
							logger.info("[Meragrocer][process] catName:"+catName+" | subCatName: "+subCatName+" | subCatUrl: "+subCatUrl);
							Document doc1 = this.getConnectionViaJsoup(subCatUrl, null, false).get();
	//						logger.info("doc1: "+doc1);
							this.processDoc(doc1, catName, subCatName,merchantDo);
						}
					}
				}else{
					logger.info("[Meragrocer][process] Merchant detail not found for \"meragrocer\"");
				}
				resp = "process complete";
			}else{
				resp = "category size is 0";
				logger.info("[Meragrocer][process] mapCategories size is 0");
			}
			
			
		}catch (Exception e) {
			resp = "Internal Server Error";
			logger.error("[MeraGrocer][process] Exception: "+e);
		}finally{
			out.println(resp);
		}		
	}
	
	private HashMap<String, HashMap<String, String>> getCategories(){
		HashMap<String, HashMap<String, String>> getCategories=null;
		try{
			getCategories = new HashMap<String, HashMap<String,String>>();
			Document doc = this.getConnectionViaJsoup("http://www.meragrocer.com/", null, false).get();
			if(doc!=null){
				Elements elements = doc.select("li.current");
				
				if(!elements.isEmpty()){
					for (Element element : elements) {

						String categoryName = "";
						HashMap<String, String> mapSubCategories = new HashMap<String, String>();
						Elements elements2 = element.children();

						for (Element element2 : elements2) {
							
							if(element2.tagName().equals("a")){
								categoryName = element2.select("span.main_navbtn_m").first().text();
							}
							
							if(element2.tagName().equals("ul")){
								
								Element element3 = element2.select("table.sub-menu-wrapper").first();
								Elements elements3 = element3.select("dl.section");
								
								for (Element element4 : elements3) {
									
									Element element5 = element4.getElementsByTag("dt").first().getElementsByTag("a").first();
									String subCatName = element5.text();
									String subCatUrl = element5.attr("href");
									Element element6 = element4.getElementsByTag("dd").first();									
									Elements elements4 = element6.select("ol.items");
									
									if(elements4!=null && !elements4.isEmpty()){
										for (Element element7 : elements4) {
											
											Elements elements5 = element7.getElementsByTag("a");
											
											for (Element element9 : elements5) {
												
												String subSubCatName = subCatName+" - "+element9.text();
												String subSubCatUrl = element9.attr("href");
												mapSubCategories.put(subSubCatName, subSubCatUrl);
											}											
											
										}
									}else{
										mapSubCategories.put(subCatName, subCatUrl);
									}									
								}
							}							
						}						
						getCategories.put(categoryName, mapSubCategories);
					}
				}else{
					logger.info("[Meragrocer][getCategories] elements is empty or null");
				}
			}else{
				logger.info("[Meragrocer][getCategories] doc is null");
			}
		}catch (Exception e) {
			logger.error("[Meragrocer][getCategories] Exception: "+e);
		}
		return getCategories;
	}
	
	private void processDoc(Document doc,String catName,String subCatname,MerchantDo merchantDo){
		
		try{
			
			if(doc!=null){
				DBUtil dbUtil = new DBUtil();
				Elements elements = doc.getElementsByTag("script");
				
				for (Element element : elements) {
					
					if(element.toString().contains("#ctl00_ContentPlaceHolder1_ctl00_ctl04_Showcase")){
						
						String jsonString= element.toString().substring(element.toString().indexOf("{"), element.toString().indexOf("}")).replace("{ ShowCaseInputs: {", "");
						jsonString=jsonString.trim().replaceAll(" ", "").trim();
						String[] splt = jsonString.split("\n");
						String inputPara ="";
						
						for (String string : splt) {
							inputPara+=string.trim();
						}
						inputPara = inputPara.substring(inputPara.indexOf("\""),inputPara.lastIndexOf("\"")+1);
						String url1 = "http://www.meragrocer.com/Handler/ProductShowcaseHandler.ashx?ProductShowcaseInput={"+inputPara.replaceAll("\"", "%22").replaceAll("\n", "")+"}";
						Document document = this.getConnectionViaJsoup(url1, null, false).get();
						
						if(document!=null){
							
							Element element2 = document.select("div.pagercontrol").first();
							int pages = element2!=null?(!element2.getElementsByClass("pager").isEmpty()?element2.getElementsByClass("pager").size():1):1;
							
							for(int i=0 ; i<pages ; i++){
								
								if(i!=0){
									jsonString = jsonString.replace("\"PageNo\":1", "\"PageNo\":"+(i+1));
									inputPara ="";
									splt = null;
									url1=null;
									splt = jsonString.split("\n");
									for (String string : splt) {
										inputPara+=string.trim();
									}
									inputPara = inputPara.substring(inputPara.indexOf("\""),inputPara.lastIndexOf("\"")+1);
									url1 = "http://www.meragrocer.com/Handler/ProductShowcaseHandler.ashx?ProductShowcaseInput={"+inputPara.replaceAll("\"", "%22").replaceAll("\n", "")+"}";
									document = null;
									document = this.getConnectionViaJsoup(url1, null, false).get();									
								}
								
								Elements elements5 =document!=null?document.select("div.bucket"):null;
								if(elements5!=null && !elements5.isEmpty()){
									for (Element element3 : elements5) {
										
										String productName = element3.select("h4.mtb-title").first().text();
										String imageUrl = element3.select("img.mtb-img").first().attr("original");								
										Element element5 = element3.select("span.mtb-price").first();
										String maxPrice = !element5.select("label.mtb-mrp").isEmpty()?element5.select("label.mtb-mrp").first().select("span.sp_currencysyb.WebRupee").first().text()+element5.select("label.mtb-mrp").first().select("span.sp_amt").first().text():"";
										String sellPrice = !element5.select("label.mtb-ofr").isEmpty()?element5.select("label.mtb-ofr").first().select("span.sp_currencysyb.WebRupee").first().text()+element5.select("label.mtb-ofr").first().select("span.sp_amt").first().text():"";
										String productId = element3.attr("data-productid");
										String weight = productId!=null && !"".equals(productId.trim())?this.getWeight(productId):"";
										String offer = !element3.select("span.offer_block").isEmpty()?element3.select("span.offer_block").first().text():"";
										
//										String respDB = dbUtil.saveOrUpdateDB(catName, subCatname, productName, weight, maxPrice, offer, sellPrice, imageUrl, "", "", merchantDo, "");
//										logger.info("[Meragrocer][processDoc] catName: "+catName+" | subCatname: "+subCatname+" | productName: "+productName+" | imageUrl: "+imageUrl+" | sellPrice: "+sellPrice+" | maxPrice: "+maxPrice+" | weight: "+weight+" | offer: "+offer+" | respDB: "+respDB);
										
									}									
								}else{
									logger.info("[Meragrocer][processDoc] catName: "+catName+" | subCatname: "+subCatname+" | elements5 is null");
								}								
							}
						}else{
							logger.info("[Meragrocer][getDetail] catName: "+catName+" | subCatname: "+subCatname+" | productShowCase is null");
						}
					}
				}
			}else{
				logger.info("[Meragrocer][getDetail] catName: "+catName+" | subCatname: "+subCatname+" | doc: "+doc);
			}
			
		}catch (Exception e) {
			logger.error("[Meragrocer][processDoc] catName: "+catName+" | subCatname: "+subCatname+" | Exception: "+e);
		}
		
	}
	
	private String getWeight(String productId){	
		String weight = "";
		String json="";
		String url="";
		try{
			url ="http://www.meragrocer.com/Handler/ProductQuickViewHandler.ashx?ActionType=GetVariants&MID=0147a52a-3272-4f4f-8fce-241dbbfb3ed5&PIDS="+productId;
			Document doc = this.getConnectionViaJsoup(url, null, true).get();
			json = doc.body().text();
			if(json!=null&&!"".equals(json.trim())){
				JSONArray jsonArray = new JSONArray(json);
				if(jsonArray!=null && jsonArray.length()>0){
					JSONArray jsonArray2 = new JSONArray(jsonArray.get(0).toString());
					JSONObject jsonObject = (JSONObject) jsonArray2.get(0);
					JSONArray jsonArray3 = jsonObject.getJSONArray("lstProductVariantValueView");
					JSONObject jsoObject = (JSONObject)jsonArray3.get(0);
					weight = jsoObject.get("variantValue").toString();
				}
			}
		}catch (Exception e) {
			logger.info("[Meragrocer][getWeight]["+productId+"]["+url+"] json: "+json+" | Exception: "+e);
		}
		return weight;
	}
	
	public Connection getConnectionViaJsoup(String url,Map<String, String> map,boolean ignoreContentType){
		Connection connection = null;		
		try{
			
			connection = Jsoup.connect(url)
					.method(Method.GET)
					.timeout(25000)
					.ignoreContentType(ignoreContentType);
				if(map!=null && map.size()>0)
					connection = connection.cookies(map);
						
			
		}catch (Exception e) {
			logger.info("[Meragrocer][getConnectionViaJsoup] Exception: "+e);
		}
		return connection;				
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		  process(request,response);
	}
	  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  process(request,response);
	}
	
	public static void main(String[] args){
		try{
			Connection con = new CopyOfMeragrocer().getConnectionViaJsoup("http://www.meragrocer.com/", null, false);
			Document doc = con.get();
			System.out.println(doc);
			Map<String, String> cokies = con.response().cookies();
			for(Map.Entry<String, String> mapcokie: cokies.entrySet()){
				System.out.println(mapcokie.getKey()+" | "+mapcokie.getValue());
			}
		}catch(Exception e){
			System.out.println(e);
		}
	}
}
