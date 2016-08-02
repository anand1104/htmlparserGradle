package com.ahoy.parser.api;

import com.ahoy.parser.dao.CityDaoImpl;
import com.ahoy.parser.dao.MerchantDaoImpl;
import com.ahoy.parser.domain.CityDo;
import com.ahoy.parser.domain.MerchantDo;
import com.ahoy.parser.util.DBUtil;
import com.ahoy.parser.util.GetStackElements;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@WebServlet("/AskmeGrocery")
public class AskmeGrocery extends HttpServlet {
    private static final long serialVersionUID = 1L;
    static Logger logger = LoggerFactory.getLogger(AskmeGrocery.class);

    @SuppressWarnings({"rawtypes", "unused"})
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String state = request.getParameter("state");
        String city = request.getParameter("city");
        String areaname = request.getParameter("area");
        String stateId = null;
        String cityId = null;
        String areaId = null;
        String area = null;
        String zonalCode = null;
        String stataeName = null;
        String cityName = null;

        try {
            Connection con = Jsoup.connect("http://www.askmegrocery.com/")
                    .timeout(25000);

            Response res = con.execute();
            Map<String, String> responseCookies = res.cookies();

//*******************************getting state id and name ************************************			

            String stateurl = "http://nginx.api.askmegrocery.com/api/state";
            Document doc = Jsoup.connect("http://nginx.api.askmegrocery.com/api/state")
                    .ignoreContentType(true).get();

            String statejson = this.hitUrl("http://nginx.api.askmegrocery.com/api/state", true);


            JSONArray jsonArray = new JSONArray(statejson);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String stateID = jsonObject.getString("StateID");
                String stateName = jsonObject.getString("StateName");

                if (stataeName.equalsIgnoreCase(state)) {


//***************************getting city id and name as per the state*****************************
                    String cityJson = this.hitUrl("http://nginx.api.askmegrocery.com/api/city/" + stateID, true);
                    if (cityJson != null && !"".equals(cityJson)) {
                        JSONArray cityJsonArray = new JSONArray(cityJson);
                        if (cityJsonArray != null && cityJsonArray.length() > 0)
                            ;
                        for (int j = 0; j < cityJsonArray.length(); j++) {
                            JSONObject cityjsonObject = cityJsonArray.getJSONObject(j);
                            String cityID = cityjsonObject.getString("CityID");
                            String cityNAME = cityjsonObject.getString("CityName");

                            if (cityName.equalsIgnoreCase(city)) {
//*************************getting area id ,name and zonalCode as per the city**********************								
                                String jsonArea = this.hitUrl("http://nginx.api.askmegrocery.com/api/area/" + cityId, true);

                                if (jsonArea != null && jsonArea.trim().equals("")) {
                                    JSONArray jsonArrayArea = new JSONArray(jsonArea);
                                    for (int k = 0; k < jsonArray.length(); k++) {
                                        JSONObject jsonObjectArea = jsonArrayArea.getJSONObject(k);
                                        String AreaID = jsonObject.getString("AreaID");
                                        String AreaName = jsonObject.getString("AreaName");
                                        String ZonalCode = jsonObject.getString("ZonalCode");
                                        System.out.println("AreaID: " + AreaID + " | AreaName: " + AreaName + " | ZonalCode: " + ZonalCode);

                                        if (area.trim().equalsIgnoreCase(areaname)) {


//******************************getting the link as per the state city and area**************************			

                                            con = Jsoup.connect("http://www.askmegrocery.com/shared/setArea")
                                                    .method(Method.POST)
                                                    .cookies(responseCookies)
                                                    .ignoreContentType(true)
                                                    .header("Accept", "application/json")
                                                    .data("StateID", stateId)
                                                    .data("CityID", cityId)
                                                    .data("AreaID", areaId)
                                                    .data("Area", area)
                                                    .data("ZonalCode", zonalCode)
                                                    .data("StateName", stataeName)
                                                    .data("CityName", cityName)
                                                    .timeout(25000);

                                            Response res1 = con.execute();
                                            Map<String, String> responseCookies1 = res1.cookies();


                                            Document doc1 = Jsoup.connect("http://www.askmegrocery.com")
                                                    .cookies(responseCookies1).followRedirects(true).ignoreHttpErrors(true)
                                                    .header("script", "text/javascript")
                                                    .timeout(25000)
                                                    .get();

                                            Map<String, Object> model = new HashMap<String, Object>();


//******************************getting category and its detailes************************			

                                            String url = "http://nginx.api.askmegrocery.com/api/category";

                                            String jsonCategory = this.hitUrl("http://nginx.api.askmegrocery.com/api/category", true);

                                            if (jsonCategory != null && !"".equals(jsonCategory.trim())) {
                                                JSONArray jsonArrayCategory = new JSONArray(jsonCategory);
                                                List<String> catrecord = new ArrayList<String>();
                                                for (int l = 0; l < jsonArrayCategory.length(); l++) {
                                                    JSONObject jsonObjectCategory = jsonArrayCategory.getJSONObject(l);
                                                    if (jsonObjectCategory.has("ParentID")) {

                                                        String cat_id = jsonObjectCategory.getString("CategoryID");

                                                        String ParentID = jsonObjectCategory.getString("ParentID");
                                                        catrecord.add(ParentID);
                                                        String Name = jsonObjectCategory.getString("Name");

                                                        String RootID = jsonObjectCategory.getString("RootID");

                                                        String ParentName = jsonObjectCategory.getString("ParentName");

                                                        String ViewBy = jsonObjectCategory.getString("ViewBy");

                                                        String AllChilds = jsonObjectCategory.getString("AllChilds");

                                                        String IconRed = jsonObjectCategory.getString("IconRed");

                                                        String IconBlue = jsonObjectCategory.getString("IconBlue");

                                                        String BannerHome = jsonObjectCategory.getString("BannerHome");

                                                        String BannerInner = jsonObjectCategory.getString("BannerInner");

                                                        String IsNav = jsonObjectCategory.getString("IsNav");
                                                        int isAvail = 1;
                                                        logger.info("cat_id :" + cat_id + " | " + "ParentID :" + ParentID + " | " + "Name :"
                                                                + Name + " | " + "RootID :" + RootID + " | " + "ParentName :" + ParentName + " | " + "ViewBy :" + ViewBy +
                                                                " | " + "AllChilds :" + AllChilds + " | " + "IconRed :" + IconRed + " | " + "IconBlue :" + IconBlue + " | "
                                                                + "BannerHome :" + BannerHome + " | " + "BannerInner :" + BannerInner + " | " + "isAvail :" + isAvail);
                                                        String[] urlid = AllChilds.split(",");

                                                        for (int m = 1; m < urlid.length; m++) {
                                                            catrecord.add(urlid[m]);
//********************************getting products as per category*****************************					
                                                            String url2 = "http://nginx.api.askmegrocery.com/api/products/" + zonalCode + "/" + urlid[m] + "/0/0/0/2000/1";
                                                            String getProdJson = this.hitUrl(url2, true);
                                                            if (getProdJson != null && !"".equals(getProdJson.trim())) {
                                                                JSONArray jsonArrayProd = new JSONArray(getProdJson);
                                                                for (int n = 0; n < jsonArrayProd.length(); i++) {
                                                                    JSONObject jsonObjectProd = jsonArrayProd.getJSONObject(n);
                                                                    String Brand = jsonObjectProd.getString("Brand");
                                                                    String productname = jsonObjectProd.getString("Displaytitle").toLowerCase();
                                                                    String variant = "";

                                                                    if (productname.trim().split(" ")[productname.trim().split(" ").length - 1].equals("ltr")
                                                                            || productname.trim().split(" ")[productname.trim().split(" ").length - 1].equals("mm")
                                                                            || productname.trim().split(" ")[productname.trim().split(" ").length - 1].equals("inch")
                                                                            || productname.trim().split(" ")[productname.trim().split(" ").length - 1].equals("gm")
                                                                            || productname.trim().split(" ")[productname.trim().split(" ").length - 1].contains("gm(")
                                                                            || productname.trim().split(" ")[productname.trim().split(" ").length - 1].equals("lt")
                                                                            || productname.trim().split(" ")[productname.trim().split(" ").length - 1].equals("ml")
                                                                            || productname.trim().split(" ")[productname.trim().split(" ").length - 1].equals("sticks")
                                                                            || productname.trim().split(" ")[productname.trim().split(" ").length - 1].equals("pcs")
                                                                            || productname.trim().split(" ")[productname.trim().split(" ").length - 1].equals("lt")
                                                                            || productname.trim().split(" ")[productname.trim().split(" ").length - 1].equals("ltr")
                                                                            || productname.trim().split(" ")[productname.trim().split(" ").length - 1].equals("kg")
                                                                            || productname.trim().split(" ")[productname.trim().split(" ").length - 1].contains("sachets")
                                                                            || productname.trim().split(" ")[productname.trim().split(" ").length - 1].contains("packet")
                                                                            || productname.trim().split(" ")[productname.trim().split(" ").length - 1].equals("shades")
                                                                            || productname.trim().split(" ")[productname.trim().split(" ").length - 1].equals("pellets")
                                                                            || productname.trim().split(" ")[productname.trim().split(" ").length - 1].equals("tabs")
                                                                            || productname.trim().split(" ")[productname.trim().split(" ").length - 1].equals("mtr")
                                                                            || productname.trim().split(" ")[productname.trim().split(" ").length - 1].equals("pc")
                                                                            || productname.trim().split(" ")[productname.trim().split(" ").length - 1].equals("pages")
                                                                            || productname.trim().split(" ")[productname.trim().split(" ").length - 1].equals("pads")
                                                                            || productname.trim().split(" ")[productname.trim().split(" ").length - 1].equals("box")
                                                                            || productname.trim().split(" ")[productname.trim().split(" ").length - 1].equals("n")
                                                                            || productname.trim().split(" ")[productname.trim().split(" ").length - 1].equals("stick")
                                                                            || productname.trim().split(" ")[productname.trim().split(" ").length - 1].equals("sheets")
                                                                            || productname.trim().split(" ")[productname.trim().split(" ").length - 1].equals("capsules"))

                                                                    {

                                                                        if (productname.contains(" x ")) {
                                                                            variant = productname.trim().split(" ")[productname.trim().split(" ").length - 4] + " " + productname.trim().split(" ")[productname.trim().split(" ").length - 3] + " " + productname.trim().split(" ")[productname.trim().split(" ").length - 2] + " " + productname.trim().split(" ")[productname.trim().split(" ").length - 1];
                                                                            productname = productname.replace(variant, "");
                                                                        } else if (!productname.contains(" x ")) {
                                                                            variant = productname.split(" ")[productname.split(" ").length - 2].replaceAll("[^0-9.]", "") + " " + productname.split(" ")[productname.split(" ").length - 1];
                                                                            productname = productname.replace(variant, "");
                                                                        } else if (productname.trim().split(" ")[productname.trim().split(" ").length - 1].contains("ltr")
                                                                                || productname.trim().split(" ")[productname.trim().split(" ").length - 1].contains("gm")
                                                                                || productname.trim().split(" ")[productname.trim().split(" ").length - 1].contains("mm")
                                                                                || productname.trim().split(" ")[productname.trim().split(" ").length - 1].contains("gm(")
                                                                                || productname.trim().split(" ")[productname.trim().split(" ").length - 1].contains("lt")
                                                                                || productname.trim().split(" ")[productname.trim().split(" ").length - 1].contains("ml")
                                                                                || productname.trim().split(" ")[productname.trim().split(" ").length - 1].contains("sticks")
                                                                                || productname.trim().split(" ")[productname.trim().split(" ").length - 1].contains("pcs")
                                                                                || productname.trim().split(" ")[productname.trim().split(" ").length - 1].contains("lt")
                                                                                || productname.trim().split(" ")[productname.trim().split(" ").length - 1].contains("ltr")
                                                                                || productname.trim().split(" ")[productname.trim().split(" ").length - 1].contains("kg")
                                                                                || productname.trim().split(" ")[productname.trim().split(" ").length - 1].contains("sachets")
                                                                                || productname.trim().split(" ")[productname.trim().split(" ").length - 1].contains("pellets")
                                                                                || productname.trim().split(" ")[productname.trim().split(" ").length - 1].contains("tabs")
                                                                                || productname.trim().split(" ")[productname.trim().split(" ").length - 1].contains("mtr")
                                                                                || productname.trim().split(" ")[productname.trim().split(" ").length - 1].contains("pc")
                                                                                || productname.trim().split(" ")[productname.trim().split(" ").length - 1].contains("pads")
                                                                                || productname.trim().split(" ")[productname.trim().split(" ").length - 1].contains("box")
                                                                                || productname.trim().split(" ")[productname.trim().split(" ").length - 1].contains("capsules"))

                                                                        {

                                                                            variant = productname.split(" ")[productname.split(" ").length - 1];
                                                                            productname = productname.replace(variant, "");
                                                                        }

                                                                    } else {
                                                                        variant = "1 Pack";
                                                                    }

                                                                    String Subcat = jsonObjectProd.getString("Name");
                                                                    String CategoryID = jsonObjectProd.getString("CategoryID");
                                                                    String Itemid = jsonObjectProd.getString("Itemid");
                                                                    String productlink = (("http://www.askmegrocery.com/" + productname.replace("&", "").replaceAll(" ", "-").replaceAll("---", "-") + "p" + "-" + Itemid).replace("%", "").replace("(", "")).replace(")", "-");
                                                                    String CP = jsonObjectProd.getString("CP");
                                                                    String MRP = jsonObjectProd.getString("MRP");
                                                                    String PctOff = jsonObjectProd.getString("PctOff") + "%";
                                                                    String Shippingcharges = jsonObjectProd.getString("Shippingcharges");
                                                                    String Offer = jsonObjectProd.getString("Offer");
                                                                    String Thumbnailpath = jsonObjectProd.getString("Thumbnailpath");

                                                                    List<String> tatal = new ArrayList<String>();
                                                                    tatal.add(productname);
                                                                    List<String> vrt = new ArrayList<String>();
                                                                    vrt.add(productname);
                                                                    logger.info("[AskmeGrocery][process][" + area + "] Brand :" + Brand + " | " + "productname :" + productname + " | " + "variant :" + variant + " | " + "productlink :" + productlink + " | " + "Subcat :" + Subcat + " | " + "CategoryID :" + CategoryID + " | " + "Itemid :" + Itemid + " | " + "selling :" + CP + " | " + "MRP :" + MRP + " | "
                                                                            + "PctOff :" + PctOff + " | " + "Shippingcharges :" + Shippingcharges + " | " + "Offer :" + Offer + " | " + "image :" + Thumbnailpath);
                                                                    DBUtil dbUtil = new DBUtil();

                                                                    MerchantDo merchantDo = new MerchantDaoImpl().getByMerchantname("AskmeGrocery");

                                                                    CityDo cityDo = new CityDaoImpl().getByName(cityName);
                                                                    //		logger.info("Image : " + image +"| Product Name :" + name + "| Product Id : " + productid + "| Varient Id : "+ varientId + "| Weight : "+ weight + "| Selling Price : " + saleing + "| M.R.P. : " + MRP +"| offer : " + ofr );
                                                                    if (Float.parseFloat(MRP.replace(",", "")) != 0.00) {
                                                                        if (productname != null && variant != null && MRP != null && CP != null && MRP.trim().matches("[0-9]+(?:\\.[0-9]+)?") && CP.trim().matches("[0-9]+(?:\\.[0-9]+)?")) {
                                                                            String respDB = dbUtil.saveOrUpdateByPIdAndVId(Name, Subcat, productname, variant, MRP, PctOff, CP, Thumbnailpath, cityDo, merchantDo, "", Long.valueOf(Itemid.trim()), Long.valueOf(0), "", (short) isAvail, productlink);
                                                                            logger.info("[AskmeGrocery][process][" + area + "] cat: " + Name + " | subcat: " + Subcat + " | item: " + productname + " | itemid: " + Itemid + " | variant: " + variant + " | sp: " + CP + " | mrp: " + MRP + " | offer: " + PctOff + " | image: " + Thumbnailpath + " | avail: " + (isAvail == 2 ? "Out of Stock" : "available") + " | respDB: " + respDB);
                                                                        } else {
                                                                            logger.info("[AskmeGrocery][process][" + area + "] cat: " + Name + " | subcat: " + Subcat + " | item: " + productname + " | itemid: " + Itemid + " | variant: " + variant + " | sp: " + CP + " | mrp: " + MRP + " | offer: " + PctOff + " | image: " + Thumbnailpath + " | avail: " + (isAvail == 2 ? "Out of Stock" : "available") + " | Some thing missed");
                                                                        }
                                                                    }
                                                                }

                                                            } else {

                                                            }
                                                        }
                                                    } else {

                                                    }
                                                }

                                            } else {
                                                logger.info("[AskmeGrocery][doGet] invalid category Json");
                                            }

                                        }
                                    }
                                } else {
                                    logger.info("[AskmeGrocery][doGet] invalidate jsonArea: " + jsonArea);
                                }


                            }
                        }

                    } else {
//						City json not found
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String hitUrl(String url, boolean isIgnore) {
        String json = null;
        try {
            Document doc1 = Jsoup.connect(url).ignoreContentType(isIgnore).get();
            json = doc1.body().ownText();
        } catch (Exception e) {
            logger.error("[AskmeGrocery][hitUrl] url: " + url + " | isIgnore: " + isIgnore + " | " + GetStackElements.getRootCause(e, getClass().getName()));
        }
        return json;
    }
}
