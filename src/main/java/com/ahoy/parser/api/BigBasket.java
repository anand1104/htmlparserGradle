package com.ahoy.parser.api;

import com.ahoy.parser.dao.CityDaoImpl;
import com.ahoy.parser.dao.MerchantDaoImpl;
import com.ahoy.parser.domain.CityDo;
import com.ahoy.parser.domain.MerchantDo;
import com.ahoy.parser.util.DBUtil;
import com.ahoy.parser.util.GetStackElements;
import com.ahoy.parser.util.UtilConstants;
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
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

public class BigBasket extends HttpServlet {

    private static final long serialVersionUID = 1L;
    Logger logger = LoggerFactory.getLogger(BigBasket.class);

    protected void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = null;
        String resp = "";
        try {
            out = response.getWriter();
            Map<String, String> map = this.cityMap();
            for (Map.Entry<String, String> mp : map.entrySet()) {
                String key = mp.getKey();
                String cityId = key.split("\\|")[0];
                String cityName = key.split("\\|")[1];
                String saveOnLoc = key.split("\\|")[2];
                String cityJson = mp.getValue();
                String cityResp = null;
                Connection connection1 = null;
                MerchantDo merchantDo = null;
                CityDo cityDo = null;
                try {
                    //################ First Hit and get response cookies #####################
                    connection1 = Jsoup.connect("http://www.bigbasket.com/choose-city/?next=/")
                            .followRedirects(true)
                            .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36")
                            .method(Method.GET)
                            .header("Connection", "keep-alive")
                            .timeout(25000);

                    Response res = connection1.execute();
                    logger.info("[BigBasket][process][" + cityName + "] status Code: " + res.statusCode() + " | status Message: " + res.statusMessage());


//################ Set Locality ###########################################				
                    Response response2 = this.setLocality(res.cookies(), cityId, cityJson);

                    if (response2 != null) {
                        Document docLoc = response2.parse();

                        if (docLoc != null) {
                            logger.info("[BigBasket][process][" + cityName + "] set Locality: " + docLoc.getElementById("uiv2-selection").ownText());
//################ Get Categories #########################################							
                            Map<String, Map<String, String>> mapCategories = this.getCategories(docLoc, response2.cookies());

                            if (mapCategories != null && mapCategories.size() > 0) {
                                DBUtil dbUtil = new DBUtil();
                                merchantDo = new MerchantDaoImpl().getByMerchantname("Big Basket");
                                cityDo = new CityDaoImpl().getByName(cityName);
                                if (merchantDo != null && cityDo != null) {
                                    for (Map.Entry<String, Map<String, String>> map1 : mapCategories.entrySet()) {
                                        String catName = map1.getKey();
                                        Map<String, String> subCatMap = map1.getValue();
                                        String catRes = this.parseCatItems(dbUtil, merchantDo, cityDo, catName, response2, subCatMap);
                                        logger.info("[BigBasket][process] cityName: " + cityName + " | catName: " + catName + " | res: " + catRes);
                                        Thread.sleep(100);
                                    }
                                    cityResp = "Process complete";
                                    if (saveOnLoc.trim().equals("1")) {
                                        URL url = UtilConstants.class.getResource("/parser.properties");
                                        Properties properties = new Properties();
                                        properties.load(url.openStream());

                                        String saveRes = UtilConstants.saveFileOnServer(properties, cityDo, merchantDo);
                                        if (saveRes.startsWith("Success")) {
                                            String onlineUrl = properties.getProperty("onlineurl");
                                            onlineUrl = onlineUrl.replace("<MERCHANT-NAME>", UtilConstants.pathVariable(merchantDo.getMerchantName(), (short) 1))
                                                    .replace("<CITY-NAME>", UtilConstants.pathVariable(cityDo.getCityName(), (short) 1)) + URLEncoder.encode(saveRes.split("\\|")[1].trim(), "UTF-8");

                                            cityResp += " | saveRes: " + saveRes.split("\\|")[0] + " | onlineHitRes: " + UtilConstants.processURL(onlineUrl);
                                        } else {
                                            cityResp += " | saveRes: " + saveRes;
                                        }
                                    }

                                } else {
                                    cityResp = merchantDo == null ? "merchantDo is " + merchantDo : "cityDo is " + cityDo;
                                }
                            } else {
                                cityResp = "cat size is 0";
                            }
                        } else {
                            cityResp = "set location doc is null";
                        }
                    } else {
                        cityResp = "set location response is null";
                    }
                } catch (Exception e) {
                    logger.error("[BigBasket][process][" + cityName + "] " + GetStackElements.getRootCause(e, getClass().getName()));
                } finally {
                    logger.info("[BigBasket][process] cityName: " + cityName + " | resp: " + cityResp);
                }
                Thread.sleep(1000 * 60);
            }
            resp = "Process complete";
        } catch (Exception e) {
            resp = "Internal Server Error";
            logger.error("[BigBasket][process] Exception: " + GetStackElements.getRootCause(e, getClass().getName()));
        } finally {
            logger.info("[BigBasket][process] resp: " + resp);
            out.println(resp);
        }
    }

    private Map<String, String> cityMap() {
        Map<String, String> map = new LinkedHashMap<String, String>();

        map.put("9|gurgaon|1", "{\"pincode\":\"122003\",\"id\":1611,\"display_name\":\"Sector 50, Nirvana Country\",\"landmark\":\"\",\"area\":\"\",\"location\":[77.066294497743,28.412222967198],\"street\":\"\",\"type\":\"area\"}");
        map.put("9|noida|1", "{\"pincode\":\"201301\",\"id\":10518,\"display_name\":\"Sector 18, Noida\",\"landmark\":\"\",\"area\":\"\",\"location\":[77.3218196,28.570317],\"street\":\"\",\"type\":\"area\"}");
        map.put("9|delhi|1", "{\"pincode\":\"110001\",\"id\":1838,\"display_name\":\"New Delhi H.O \",\"landmark\":\"\",\"area\":\"\",\"location\":[77.240774671424,28.619881147975],\"street\":\"\",\"type\":\"area\"}");
        map.put("4|mumbai|1", "{\"pincode\":\"400049\",\"id\":383,\"display_name\":\"Juhu\",\"landmark\":\"\",\"area\":\"\",\"location\":[72.8320717,19.098821],\"street\":\"\",\"type\":\"area\"}");
        //  map.put("1|bangalore|0", "{\"pincode\":\"560034\",\"id\":4883,\"display_name\":\"Koramangala 1a Block-SBI Colony\",\"landmark\":\"\",\"area\":\"\",\"location\":[77.63249189999999,12.9255756],\"street\":\"\",\"type\":\"area\"}");

        return map;
    }

    private String parseCatItems(DBUtil dbUtil, MerchantDo merchantDo, CityDo cityDo, String catName, Response response2, Map<String, String> subCatMap) {
        String resp = null;
        try {
            String cityName = cityDo.getCityName();
            for (Map.Entry<String, String> map2 : subCatMap.entrySet()) {

                String subCatName = map2.getKey();
                String subCatUrl = map2.getValue();
                String nextPage = null;

                do {
                    try {
                        Connection connection2 = hitSubCatUrl("http://www.bigbasket.com/" + (nextPage == null ? subCatUrl : nextPage), response2.cookies());
                        if (connection2 != null) {
                            Response response3 = connection2.execute();
                            Document doc = response3.parse();
                            Elements elements2 = null;

                            if (nextPage == null) {
                                Element element2 = doc.getElementById("products-container");
                                elements2 = element2.getElementsByClass("uiv2-shopping-lis-sku-cards");
                                nextPage = element2.getElementById("next-product-page") != null ? element2.getElementById("next-product-page").attr("href") : null;
                            } else {
                                elements2 = doc.getElementsByClass("uiv2-shopping-lis-sku-cards");
                                nextPage = doc.getElementById("next-product-page") != null ? doc.getElementById("next-product-page").attr("href") : null;
                            }

                            for (Element element3 : elements2) {

                                Element element4 = element3.getElementsByClass("uiv2-our-recommendations-list-box").first();
                                Elements elements3 = element4.children();
//								System.out.println(elements3);
                                for (Element element5 : elements3) {

                                    if (element5.hasAttr("id") && element5.hasAttr("name")) {
                                        String id = element5.attr("id");
                                        String name = element5.attr("name");
                                        if (id.toLowerCase().contains("product_") && name.toLowerCase().contains("widget_")) {

                                            String prodId = name.split("_")[1];
                                            String variant = null;
                                            Elements elements = element5.getElementsByClass("uiv2-field-wrap").first().select("select");
                                            if (elements != null && elements.size() > 0) {
                                                Elements elements4 = elements.first().children();
                                                for (Element element : elements4) {
                                                    String value = element.attr("value");
                                                    if (element.hasAttr("selected") && value != null && !"".equals(value.trim())) {
                                                        variant = element.text().split("-")[0].trim();
                                                        break;
                                                    }
                                                }
                                            } else {
                                                variant = element5.getElementsByClass("uiv2-field-wrap").first().ownText().trim();
                                            }

                                            String link = "http://www.bigbasket.com" + element5.getElementsByClass("uiv2-list-box-img-title").first().getElementsByClass("uiv2-title-tool-tip").get(1).child(0).attr("href");

                                            String itemName = element5.getElementsByClass("uiv2-tool-tip-hover").first().text().trim().replace(variant, "");
                                            String image = "http:" + element5.getElementsByClass("uiv2-list-box-img-block").first().getElementsByTag("img").first().attr("src");

                                            String sp = element5.getElementsByClass("uiv2-rate-count-avial").first().text();
                                            Elements elements1 = element5.getElementsByClass("Rate_count_low");
                                            String mrp = (elements1 == null || elements1.isEmpty()) ? sp : (elements1.first().text().split(":")[1]);

                                            mrp = mrp != null && mrp.toLowerCase().trim().startsWith("rs.") ? mrp.trim().toLowerCase().replaceAll("rs.", "").trim() : mrp.trim();
                                            sp = sp != null && sp.trim().toLowerCase().startsWith("rs.") ? sp.trim().toLowerCase().replaceAll("rs.", "").trim() : sp.trim();

                                            String offer = element5.getElementsByClass("uiv2-combo-block") != null ? element5.getElementsByClass("uiv2-combo-block").text() : "";

                                            Element element = element5.getElementsByClass("uiv2-product-notify") != null && !element5.getElementsByClass("uiv2-product-notify").isEmpty() ? element5.getElementsByClass("uiv2-product-notify").first().child(0) : null;
                                            short isAvail = element != null && element.text().trim().equalsIgnoreCase("OUT OF STOCK") ? (short) 2 : (short) 1;
                                            if (itemName != null && variant != null && mrp != null && sp != null && mrp.trim().matches("[0-9]+(?:\\.[0-9]+)?") && sp.trim().matches("[0-9]+(?:\\.[0-9]+)?")) {
                                                String respDB = dbUtil.saveOrUpdateByPIdAndVId(catName, subCatName, itemName, variant, mrp, offer, sp, image, cityDo, merchantDo, "", Long.valueOf(prodId.trim()), 0, "", isAvail, link);
                                                logger.info("[BigBasket][process][" + cityName + "] cat: " + catName + " | subcat: " + subCatName + " | item: " + itemName + " | itemid: " + prodId + " | variant: " + variant + " | sp: " + sp + " | mrp: " + mrp + " | offer: " + offer + " | image: " + image + " | link: " + link + " | avail: " + (isAvail == 2 ? "Out of Stock" : "available") + " | respDB: " + respDB);
                                            } else {
                                                logger.info("[BigBasket][process][" + cityName + "] cat: " + catName + " | subcat: " + subCatName + " | item: " + itemName + " | itemid: " + prodId + " | variant: " + variant + " | sp: " + sp + " | mrp: " + mrp + " | offer: " + offer + " | image: " + image + " | link: " + link + " | avail: " + (isAvail == 2 ? "Out of Stock" : "available") + " | Some thing missed");
                                            }
                                            Thread.sleep(100);
                                        }
                                    }
                                }
                            }
                        } else {
                            nextPage = null;
                        }
                    } catch (Exception e) {
                        logger.error("[BigBasket][process][" + cityName + "] catName: " + catName + " | subCatName: " + subCatName + " | url hit:  http://www.bigbasket.com/" + (nextPage == null ? subCatUrl : nextPage) + " | Exception: " + GetStackElements.getRootCause(e, getClass().getName()));
                        nextPage = null;
                        Thread.sleep(1000 * 60 * 1);
                    }
                } while (nextPage != null);
            }
            resp = "done";
        } catch (Exception e) {
            resp = "Internal Error";
            logger.error("[BigBasket][parseCatItems] " + GetStackElements.getRootCause(e, getClass().getName()));
        }
        return resp;
    }


    private Connection hitSubCatUrl(String url, Map<String, String> cookies) {
        Connection connection = null;

        try {
            connection = Jsoup.connect(url)
                    .method(Method.GET)
                    .cookie("_bb_vid", cookies.get("_bb_vid"))
                    .cookies(cookies)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36")
                    .header("Connection", "keep-alive")
                    .timeout(25000);


        } catch (Exception e) {
            logger.error("[BigBasket][hitSubCatUrl] url: " + url + " | Exception: " + GetStackElements.getRootCause(e, getClass().getName()));
        }

        return connection;

    }

    private Response setLocality(Map<String, String> cookies, String cityId, String cityJson) {
        Response response = null;
        try {

            String csrfToken = cookies.get("csrftoken");

            Connection connection2 = Jsoup.connect("http://www.bigbasket.com/choose-city/")
                    .method(Method.POST)
                    .cookies(cookies)
                    .data("places", cityJson)
                    .data("csrfmiddlewaretoken", csrfToken)
                    .data("address_id", "")
                    .data("city_id", cityId)
                    .data("next", "")
                    .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36")
                    .header("Connection", "keep-alive")
                    .timeout(25000);

            response = connection2.execute();

        } catch (Exception e) {
            logger.error("[BigBasket][setLocality] Exception: " + GetStackElements.getRootCause(e, getClass().getName()));
        }
        return response;
    }

    private Map<String, Map<String, String>> getCategories(Document doc, Map<String, String> mp) {
        Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
        Set<String> set = new HashSet<String>();
        try {
            Element element = doc.getElementById("basket_menu").child(0);
            Elements elements = element.getElementsByClass("normal");

            for (Element element2 : elements) {
                Element child1 = element2.child(0);
                String catName = child1.ownText();
                String catUrl = child1.attr("href");
                Connection cn = new BigBasket().hitSubCatUrl("http://bigbasket.com/" + catUrl, mp);
                Document doc1 = cn.execute().parse();
                if (doc1 != null && !"".equals(doc1)) {
                    Map<String, String> subCatMap = new HashMap<String, String>();
                    Elements elements1 = doc1.getElementById("facetsCategories").children().get(0).children();
                    for (Element element3 : elements1) {
                        String subCatName = element3.child(0).ownText();
                        if (set.add(subCatName.trim())) {
                            String subCatUrl = element3.child(0).attr("href");
                            subCatMap.put(subCatName.trim(), subCatUrl);
                        }
                    }
                    map.put(catName, subCatMap);
                } else {
                    logger.error("[BigBasket][getCategories] doc is null");
                }

            }
        } catch (Exception e) {
            logger.error("[BigBasket][getCategories] Exception: " + GetStackElements.getRootCause(e, getClass().getName()));
        }
        return map;
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

//	public static void main(String[] args){
//			
//			BigBasket bigbasket = new BigBasket();
//
//						
//				try{
//					
//				
//					Map<String, String> map = bigbasket.cityMap();
//					
//					for(Map.Entry<String, String> mp:map.entrySet()){
//						System.out.println(mp.getKey());
//						String key = mp.getKey();				
//						String cityId = key.split("\\|")[0];
//						String cityName = key.split("\\|")[1];	
////						String saveOnLoc = key.split("\\|")[2];	
//						String cityJson = mp.getValue();
////						String cityResp =null;
//						Connection connection1 = null;
////						
////						MerchantDo merchantDo = null;
////						CityDo cityDo = null;
////					
//////################ First Hit and get response cookies #####################
//					connection1 =	Jsoup.connect("http://www.bigbasket.com/choose-city/?next=/")
//						.followRedirects(true)
//						.userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36")
//						.method(Method.GET)
//						.header("Connection", "keep-alive")
//						.timeout(25000);
////			
//					Response res = connection1.execute();
//					System.out.println("[BigBasket][process]["+cityName+"] status Code: "+res.statusCode()+" | status Message: "+res.statusMessage());
////
////
//////################ Set Locality ###########################################				
//					Response response2 = bigbasket.setLocality(res.cookies(), cityId, cityJson);
////					
//					if(response2!=null){
//						Document docLoc = response2.parse();
//						
//						if(docLoc!=null){
//							System.out.println("[BigBasket][process]["+cityName+"] set Locality: "+docLoc.getElementById("uiv2-selection").ownText());
////################ Get Categories #########################################							
//							Map<String, Map<String, String>> mapCategories = bigbasket.getCategories(docLoc,response2.cookies());
//							
////							for(Map.Entry<String, Map<String, String>> mp2 : mapCategories.entrySet()){
////								String catName = mp2.getKey();
////								for(Map.Entry<String, String> mp1 : mp2.getValue().entrySet()){
////									System.out.println(catName+" | "+mp1.getKey()+" | "+mp1.getValue());
////								}
////							}
//						}
//					}
//					break;
//					}
//		}catch(Exception e){
//			
//		}
//	}

}
