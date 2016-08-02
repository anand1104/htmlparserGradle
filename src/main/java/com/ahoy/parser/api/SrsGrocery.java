package com.ahoy.parser.api;

import com.ahoy.parser.dao.CityDao;
import com.ahoy.parser.dao.CityDaoImpl;
import com.ahoy.parser.dao.MerchantDaoImpl;
import com.ahoy.parser.domain.CityDo;
import com.ahoy.parser.domain.MerchantDo;
import com.ahoy.parser.util.DBUtil;
import com.ahoy.parser.util.GetStackElements;
import com.ahoy.parser.util.SrsVariant;
import com.ahoy.parser.util.UtilConstants;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
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
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

public class SrsGrocery extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2613422165734052354L;
	Logger logger = LoggerFactory.getLogger(SrsGrocery.class);
	protected void process(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		
		
		try{
			
			TreeMap<String, String> mapCities = this.citymap();
			
			if(mapCities!=null && mapCities.size()>0){
				for(Map.Entry<String, String> mapCity:mapCities.entrySet()){
					String cityKey = mapCity.getKey();
					String cityValue = mapCity.getValue();
					CityDao cityDao = new CityDaoImpl();
					CityDo cityDo = cityKey.equalsIgnoreCase("Kaushambi/Indirapuram") ?cityDao.getByName("ghaziabad"):cityDao.getByName(cityKey.trim());
					MerchantDo merchantDo = new MerchantDaoImpl().getByMerchantname("Srs Grocery");
					
					if(merchantDo!=null && cityDo!=null){
						String setCityUrl ="http://www.srsgrocery.com/SelectLocation.aspx?Location="+cityValue.trim()+"&RetUrl=http://www.srsgrocery.com/";
						Connection connection1=null;
						
						connection1 = this.getConnectionViaJsoup(setCityUrl, null, true);			
						
						Response res = connection1.execute();
						Map<String, String> mapResCookies = res.cookies();
						
						HashMap<String, HashMap<String,String>> mapGetCategories = this.getCategories(res.parse());
						if(mapGetCategories!=null && mapGetCategories.size()>0)	{
							
							
								for(Map.Entry<String,HashMap<String,String>> mapGetCategory:mapGetCategories.entrySet()){
									String catName = mapGetCategory.getKey();
									HashMap<String,String> mapSubCategories = mapGetCategory.getValue();
									for(Map.Entry<String, String> mapSubCategory:mapSubCategories.entrySet()){
										String subCatName =mapSubCategory.getKey();
										String subCatUrl=mapSubCategory.getValue();
										Document doc1=null;
										try{
											do {
												connection1 = this.getConnectionViaJsoup(mapSubCategory.getValue(), mapResCookies, false);
												doc1 = connection1.get();
											}while (doc1==null);
											
											if(doc1!=null){
												
												this.processDoc(doc1, mapResCookies, catName, subCatName,mapCity.getValue(),merchantDo,cityDo);
												logger.info("[SrsGrocery][process]["+mapCity.getValue()+"] categoryName:"+catName+" | subCatName: "+subCatName+" | subCatUrl:"+subCatUrl+" | process is complete");
											}
										}catch (Exception e) {
											logger.error("[SrsGrocery][process] subCatName:"+subCatName+" | subCatUrl:"+subCatUrl+" | Exception: "+e);
										}	
									}
								}
								
								String cityResp = "Process Complete";
								
								URL url = UtilConstants.class.getResource("/parser.properties");					
								Properties properties = new Properties();
								properties.load(url.openStream());
								
								String saveRes = UtilConstants.saveFileOnServer(properties,cityDo, merchantDo);
								if(saveRes.startsWith("Success")){	
									String onlineUrl = properties.getProperty("onlineurl");
									onlineUrl=onlineUrl.replace("<MERCHANT-NAME>", UtilConstants.pathVariable(merchantDo.getMerchantName(), (short)1))
											.replace("<CITY-NAME>", UtilConstants.pathVariable(cityDo.getCityName(), (short)1))+URLEncoder.encode(saveRes.split("\\|")[1].trim(), "UTF-8");
									
									cityResp +=" | saveRes: "+saveRes.split("\\|")[0]+" | onlineHitRes: "+UtilConstants.processURL(onlineUrl);
								}else{
									cityResp +=" | saveRes: "+saveRes;
								}
								
								
								logger.info("[SrsGrocery][process] city: "+cityKey+" | cityResp: "+cityResp);
							
						}else{
							logger.info("[SrsGrocery][process] category size is 0 or null");
						}
					}else{
						logger.info("[SrsGrocery][process] city: "+cityKey+" | "+(merchantDo==null?"merchantDo is null":"cityDo is null"));
					}					
				}
			}else{
				logger.error("[SrsGrocery][process] city map  is"+(citymap()!=null?citymap().size():null));
			}
			
		}catch (Exception e) {
			logger.error("[SrsGrocery][process] "+GetStackElements.getRootCause(e, getClass().getName()));
		}
	}
	
	
	private void processDoc(Document doc,Map<String, String> map,String catName,String subCatname,String cityName,MerchantDo merchantDo,CityDo cityDo){
		try{
			
			Elements elements3 = doc.getElementsByTag("script");
			if(!elements3.isEmpty()){
				DBUtil dbUtil = new DBUtil();
				for (Element element : elements3) {
										
					if(element.toString().contains("var objShowCaseInputs")){
						 String scriptString = element.toString();
						String inputPara ="";
						if(scriptString!=null){
							String[] splt = scriptString.split("\n");
							for (String string : splt) {
								if(string.contains("var objShowCaseInputs")){
									inputPara = string.substring(string.indexOf("{"), string.indexOf("}")+1);
								}
							}
						}
						String url = "http://www.srsgrocery.com/Handler/ProductShowcaseHandler.ashx?ProductShowcaseInput="+inputPara.replaceAll("\"", "%22");
						Connection connection =  getConnectionViaJsoup(url, map, false);
						Document doc2 = connection.get();
						Elements elementsPager = doc2.getElementsByClass("pagerdiv");
						int pages = 1;
						if(!elementsPager.isEmpty()){
							for (Element element2 : elementsPager) {
								pages = element2.getElementsByClass("pager").size();
								pages=pages>0?pages:1;
							}
						}						
						for(int i=0 ; i < pages ; i++){
							if(i!=0){
								inputPara = inputPara.replace("\"PageNo\":1", "\"PageNo\":"+(i+1));
								doc2 = null;
								url = "http://www.srsgrocery.com/Handler/ProductShowcaseHandler.ashx?ProductShowcaseInput="+inputPara.replaceAll("\"", "%22");
								connection =  getConnectionViaJsoup(url, map, false);
								doc2 = connection.get();
							}
							
							Element element3 = doc2.select("div.bucketgroup").first();
							Elements elements = element3.select("div.bucket");
							for (Element element4 : elements) {
								String productId = element4.attr("data-productid");
								String productName = element4.select("h4.mtb-title").first().text();
								String imageUrl = element4.select("img.mtb-img").first().attr("original");	
								String link = element4.select("a.mtb-more").first().attr("href");
								
								List<SrsVariant> srsVariants = getVariants(productId);
								
								for (SrsVariant srsVariant : srsVariants) {
									String respDB = dbUtil.saveOrUpdateByPIdAndVId(catName, subCatname, productName, srsVariant.getVariant(), srsVariant.getMrp(), srsVariant.getOffer(), srsVariant.getSellPrice(), imageUrl, cityDo, merchantDo, "",Long.valueOf(productId.trim()),srsVariant.getVariantId(),srsVariant.getSku(),(short)1,link) ;
									logger.info("[SrsGrocery][processDoc]["+cityName+"] catName: "+catName+" | subCatname: "+subCatname+" | productId: "+productId+" | productName"+productName+" | imageUrl: "+imageUrl+" | variantId: "+srsVariant.getVariantId()+" | variant: "+srsVariant.getVariant()+" | maxPrice: "+srsVariant.getMrp()+" | sellPrice: "+srsVariant.getSellPrice()+" | offer: "+srsVariant.getOffer()+" | link: "+link+" | respDB: "+respDB);
								}
							}
						}
					}
				}
			}else{
				logger.info("[SrsGrocery][processDoc]["+cityName+"] catName: "+catName+" | subCatname: "+subCatname+" | elements3 is empty");
			}
		}catch (Exception e) {
			logger.error("[SrsGrocery][processDoc]["+cityName+"] catName: "+catName+" | subCatname: "+subCatname+" | Exception: "+e);
		}
	}
	
	private List<SrsVariant> getVariants(String productId){	
		List<SrsVariant> srsVariants = new ArrayList<SrsVariant>();
		try{

			String url = "http://www.srsgrocery.com/Handler/ProductQuickViewHandler.ashx?ActionType=GetVariants&MID=600f0371-3cf1-478b-a67e-c33fe2b5523b&PIDS="+productId+"&LID=13080&SortVarientsBy=d&SortDirection=d";
			Document doc = getConnectionViaJsoup(url, null, true).get();
			String json = doc.body().text();
			if(json!=null&&!"".equals(json)){
				JSONArray jsonArray = new JSONArray(doc.body().text());
				JSONArray jsonArray2 = new JSONArray(jsonArray.get(0).toString());
				for(int i =0; i < jsonArray2.length() ; i++){
					JSONObject jsonObject = (JSONObject) jsonArray2.get(i);					
					JSONArray jsonArray3 = jsonObject.getJSONArray("lstProductVariantValueView");
					JSONObject jsoObject = (JSONObject)jsonArray3.get(0);
					long variantId = Long.valueOf(jsonObject.get("variantProductId").toString());
					String sku =  jsonObject.get("sku").toString().split("-")[0];
					String variant = jsoObject.get("variantValue").toString();
					String mrp = jsonObject.get("mrp").toString();
					String sellPrice = jsonObject.get("webPrice").toString();					
					String saveRs = jsonObject.get("SaveOnMRPAmount").toString();
					saveRs = (saveRs!=null&&saveRs.matches("[0-9]+")&&Integer.valueOf(saveRs.trim())>0)?("Save Rs."+saveRs):"";
					SrsVariant srsVariant = new SrsVariant(variantId, sku, variant, mrp, sellPrice, saveRs);
					srsVariants.add(srsVariant);
				}
			}
		}catch (Exception e) {
			logger.error("[SrsGrocery][getVariants]["+productId+"] Exception: "+GetStackElements.getRootCause(e, getClass().getName()));
		}
		return srsVariants;
	}
	
	
	private HashMap<String, HashMap<String,String>> getCategories(Document doc){
		HashMap<String, HashMap<String,String>> getCategories=null;
		try{
//			Connection connection1=this.getConnectionViaJsoup("http://www.srsgrocery.com", mapResCookies, false);				
//			Document doc = connection1.get();
			if(doc!=null){
				getCategories = new HashMap<String, HashMap<String,String>>();
				Element element = !doc.select("div.l2_wrapper.w190").isEmpty()?doc.select("div.l2_wrapper.w190").first().select("ul.l2_inner_ul").first():null;
				Elements elements = element.children();
				for (Element element2 : elements) {
					String categoryName = "";
//					String catUrl ="";
					Elements elements2 = element2.children();
					HashMap<String, String> mapSubCategories = new HashMap<String, String>();
					for (Element element3 : elements2) {
						
						if(element3.tagName().equals("a")){
							categoryName=element3.text();
//							catUrl = element3.attr("href");
						}
						if(element3.tagName().equals("div") && element3.attr("class").equals("l3_wrapper")){
							Elements elements3 = element3.select("div.cols");
							for (Element element4 : elements3) {
								Element element5 = element4.select("ul").first();
								
								Elements elements4 = element5.children();
								for (Element element6 : elements4) {
									
									if(elements4.size()>1){
										if(element6.tagName().equals("li")&& !element6.attr("class").equals("cat_title")){
											Element element7 = element6.select("a").first();
											mapSubCategories.put(element7.text(), element7.attr("href"));
										}
									}else{
										Element element7 = element6.select("a").first();
										mapSubCategories.put(element7.text(), element7.attr("href"));
									}								
								}
								
							}
						}						
					}
					getCategories.put(categoryName, mapSubCategories);
				}
				
				element = null;
				element = doc.select("div.offers-slots").first();
				String categoryName = element.select("span").first().text();				
				Elements elements2 = element.select("div.show-me").first().select("ul").first().select("li");
				HashMap<String, String> mapSubCategories = new HashMap<String, String>();
				for (Element element2 : elements2) {
					Element element3 = element2.getElementsByTag("a").first();
					String subCatName = element3.text();
					String subCatUrl = element3.attr("href");
					mapSubCategories.put(subCatName, subCatUrl);
				}
				getCategories.put(categoryName, mapSubCategories);
			}else{
				logger.info("[SrsGrocery][getCategories] doc is null");
			}			
		}catch (Exception e) {
			logger.info("[SrsGrocery][getCategories] Exception: "+e);
		}
		return getCategories;
	}
	
	
	private Connection getConnectionViaJsoup(String url,Map<String, String> map,boolean ignoreContentType) throws Exception{
		Connection connection = null;		
		try{
			
			connection = Jsoup.connect(url)
					.method(Method.GET)
					.timeout(25000)
					.header("Connection", "keep-alive")
					.ignoreContentType(ignoreContentType);
				if(map!=null && map.size()>0)
					connection = connection.cookies(map);
						
			
		}catch (Exception e) {
			logger.info("[SrsGrocery][hitUrlByJsoup] Exception: "+e);
			throw e;
		}
		return connection;				
	}
	private TreeMap<String, String> citymap() throws Exception{
		TreeMap<String, String> map = null;
		try{
			map = new TreeMap<String, String>();
			
			Connection con = this.getConnectionViaJsoup("http://www.srsgrocery.com/", null, false);
			Document doc = con.get();
			Elements cityElements = doc.getElementById("ddlLocationNames").children();
			for (Element element : cityElements) {
				String value = element.attr("value");
				String key= element.text();
				if(key.trim().equalsIgnoreCase("gurgaon")
						||key.trim().equalsIgnoreCase("noida")
						||key.trim().equalsIgnoreCase("Kaushambi/Indirapuram")
						||key.trim().equalsIgnoreCase("Faridabad")){
					map.put(key, value);
				}
			}
		}catch (Exception e) {
			throw e;
		}
		return map;
	}
			
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		  process(request,response);
	}
	  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  process(request,response);
	}
		
}
