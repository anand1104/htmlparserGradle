package com.ahoy.parser.api;

import com.ahoy.parser.dao.MerchantDaoImpl;
import com.ahoy.parser.domain.MerchantDo;
import com.ahoy.parser.util.DBUtil;
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

public class EasyRation extends HttpServlet {

    private static final long serialVersionUID = 1L;
    Logger logger = LoggerFactory.getLogger(EasyRation.class);

    protected void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = null;
        String resp = "";
        try {
            out = response.getWriter();
            HashMap<String, HashMap<String, String>> mapCategories = this.getCategories();
            if (mapCategories != null && mapCategories.size() > 0) {
                MerchantDo merchantDo = new MerchantDaoImpl().getByMerchantname("easyration");
                DBUtil dbUtil = new DBUtil();

                if (merchantDo != null) {

                    for (Map.Entry<String, HashMap<String, String>> mapEntry : mapCategories.entrySet()) {

                        String catName = mapEntry.getKey();
                        HashMap<String, String> mapSubCat = mapEntry.getValue();

                        for (Map.Entry<String, String> mEntry : mapSubCat.entrySet()) {
                            String subCatname = mEntry.getKey();
                            String subCatUrl = mEntry.getValue();
                            logger.info("[EasyRation][process] catName: " + catName + " | subCatname: " + subCatname + " | subCatUrl: " + subCatUrl);

                            Document doc = this.hitUrlByJsoup(subCatUrl);

                            Elements elements = doc != null ? doc.select("div.az_plist.product-grid") : null;

                            if (elements != null && !elements.isEmpty()) {

                                for (Element element : elements) {
                                    Elements elements2 = element.select("div.boxContents");
                                    for (Element element2 : elements2) {
                                        String productName = "";
                                        String productImage = "";
                                        String maxPrice = "";
                                        String sellPrice = "";

                                        productName = element2.select("div.name").first().text();
                                        productImage = element2.select("div.image").first().getElementsByTag("img").first().attr("src");

                                        Element element3 = element2.select("div.price").first();
                                        Element element4 = element3.select("span.az_productPrice").first();

                                        if (element4 != null) {
                                            maxPrice = element4.select("span.normalPrice").text();
                                            sellPrice = element4.select("span.az_special_price2").text();
                                        } else {
                                            sellPrice = element3.text();
                                        }

//										String respDB = dbUtil.saveOrUpdateDB(catName, subCatname, productName, "", maxPrice, "", sellPrice, productImage, "", "", merchantDo, "");
//										logger.info("[EasyRation][process] catName: "+catName+" | subCatname: "+subCatname+"productName: "+productName+" | productImage: "+productImage+" | maxprice: "+maxPrice+" | sellprice: "+sellPrice+" | respDB: "+respDB);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    logger.info("[EasyRation][process] merchant detail not found for easyration");
                }
            } else {
                logger.info("[EasyRation][process] mapCategories" + mapCategories);
            }

            resp = "process complete";
        } catch (Exception e) {
            resp = "Internal Server Error";
            logger.error("[EasyRation][process] Exception: " + e);
        } finally {
            logger.info("[EasyRation][process] resp: " + resp);
            out.println(resp);
        }

    }

    private HashMap<String, HashMap<String, String>> getCategories() {
        HashMap<String, HashMap<String, String>> mapCategories = null;
        try {
            mapCategories = new HashMap<String, HashMap<String, String>>();

//			Document doc = Jsoup.connect("http://www.easyration.com/").get();
            Document doc = this.hitUrlByJsoup("http://www.easyration.com/");

            if (doc != null) {
                Elements elements = doc.getElementsByClass("box-category");
                for (Element element : elements) {
                    Elements elements2 = element.getElementsByTag("li");
                    for (Element element2 : elements2) {
                        if (!element2.attr("lang").isEmpty()) {
                            Elements elements3 = element2.select("div.az_box_top_m_new");
                            Elements elements4 = element2.select("ul.az_cat_child");
                            String catName = "";
                            String catUrl = "";
                            for (Element element3 : elements3) {
                                Elements elements5 = element3.getElementsByTag("a");
                                catUrl = elements5.attr("href");
                                catName = elements5.text();
                            }
                            HashMap<String, String> mapSubCat = new HashMap<String, String>();
                            if (!elements4.isEmpty()) {
                                for (Element element3 : elements4) {
                                    String subCatName = "";
                                    String subCatUrl = "";
                                    Elements elements5 = element3.getElementsByTag("a");
                                    for (Element element4 : elements5) {
                                        subCatUrl = element4.attr("href");
                                        subCatName = element4.text();
                                        mapSubCat.put(subCatName, subCatUrl);
                                    }
                                }
                            } else {
                                mapSubCat.put(catName, catUrl);
                            }
                            mapCategories.put(catName, mapSubCat);
                        }
                    }
                }
            } else {
                logger.info("[EasyRation][getCategories] doc is null");
            }


        } catch (Exception e) {
            logger.error("[EasyRation][getCategories] Exception: " + e);
        }
        return mapCategories;
    }

    private Document hitUrlByJsoup(String url) {
        Document document = null;

        try {
            document = Jsoup.connect(url)
                    .method(Method.GET)
                    .timeout(25000)
                    .get();

        } catch (Exception e) {
            logger.error("[EasyRation][hitUrlByJsoup] Exception: " + e);
        }
        return document;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

}
