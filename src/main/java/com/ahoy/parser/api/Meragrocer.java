package com.ahoy.parser.api;

import com.ahoy.parser.dao.CityDaoImpl;
import com.ahoy.parser.dao.MerchantDaoImpl;
import com.ahoy.parser.domain.CityDo;
import com.ahoy.parser.domain.MerchantDo;
import com.ahoy.parser.util.DBUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Map;


@WebServlet("/Meragrocer")
public class Meragrocer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	Logger logger = LoggerFactory.getLogger(Meragrocer.class);
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String cityarea = request.getParameter("area");
		  
		Document useurl;
		Document doc;

		try {
			
			
//****************************hit basic url*****************************
			
			
			useurl =Jsoup.connect("http://meragrocer.com").timeout(30000).get();
			logger.info("Title : " + useurl.select("title").text());
			Elements location = useurl.select("select.city_dropdown");
			
			
			
//************************getting location***************************			
			
			for(Element Element0 : location){
				Elements loc = Element0.select("option[value]");
				
				for (Element Element1 : loc) {
					String url = Element1.attr("value").replaceAll("http://meragrocer.com/index.php/?","");
					String area = Element1.text();
		
					if(area.equalsIgnoreCase(cityarea)){
				
					String arerurl =Element1.attr("value");
					logger.info("area : " + area + " | city url : " + arerurl);
					String url2 ="http://meragrocer.com/index.php/"+url;
					doc = Jsoup.connect("http://meragrocer.com/index.php/"+url).cookies(getCookiesForSession(url2)).timeout(30000).get();
					Elements div =doc.select("div.pannel");

					
//****************************getting category************************	

					for(Element Element2 : div){
					Elements links = Element2.select("h3");
						for(Element Element3 : links){
							Elements link1 =Element3.select("a[href]");

					for(Element Element4 : link1){
						String urlmain = Element4.attr("href");
						doc = Jsoup.connect(urlmain).cookies(getCookiesForSession(url2)).timeout(30000).get();

						String cat = doc.title();
//						logger.info("Category : " + cat);
						Elements links1 = doc.select("div.mtc-menu");

//***********************getting sub category*******************						
						
						for(Element Element5 : links1){
							Elements links2 = Element5.select("a[href]");

							for(Element Element6 : links2){
								String link2 = Element6.attr("href");

								Document doc2 = Jsoup.connect(link2).cookies(getCookiesForSession(url2)).timeout(30000).get();
								String subcat = doc2.title();
//								logger.info("sub-category :" + subcat);
							
								
								Elements links3 = doc2.select("a.adj-nav-pad3");
								for(Element Element7 : links3){
									
									String link3 = Element7.select("a").attr("href");
									
//********************************************getting sub-sub-category*****************
									
									Document doc4 = Jsoup.connect(link3).cookies(getCookiesForSession(url2)).timeout(300000).get();
									logger.info("sub-sub-category : "+doc4.title());

//******************************************getting total no. of pages**************************									
									
									int i = 0;
									Elements pageno =doc4.select("div.pager");
									if(doc4.select("div.pager").select("div.pages").select("li").hasText()){
										for(Element p : pageno){
										Elements page = p.select("li");
										
											for(Element no:page){
											String on = no.text();
																	
											String url3 = "?p="+on;
											if(!url3.equals("?p=") ){
											i = Integer.parseInt(on);
									//		System.out.println(url4);
											}
											}
										}
									}else{
										i =1;
									}
									int x = 1;
									
//***************************************scraping data according to pages*********************************									
									
									while(x<=i){
										
									doc4 = Jsoup.connect(link3+"?p="+x).cookies(getCookiesForSession(url2)).timeout(300000).get();
									x++;
									Elements group =doc4.select("div.controlcontent_r");
						
									for(Element Element8 : group){
									Elements nodeBlogStats = Element8.select("div.bucketgroup");
									
									for(Element Element9 : nodeBlogStats){
									Elements bucketitem = Element9.select("div.bucket"); 

										for(Element Element10 : bucketitem){

											String name = Element10.select("h4").text();
											
											String productlink =Element10.select("h4").select("a").attr("href");
											
											String image = Element10.select("img").attr("src");
											
											Elements select = Element10.select("select.VarientDrpDwn");

											for(Element Element11 : select){
												Elements el = Element11.select("option");
												String productid = Element11.attr("data-id");
																							
												for(Element Element12 : el){
													
													String varientId = Element12.attr("data-id");
													
													String weight = Element12.text();
																									
													String saleing = Element12.attr("data-sprice");
	
																									
													String MRP = Element12.attr("data-price");
													String ofr;
													DecimalFormat df = new DecimalFormat("#.##");
													if(Float.parseFloat(saleing.replace(",", "")) > 0){
														ofr =String.valueOf(Float.parseFloat(MRP.replace(",", "")) - Float.parseFloat(saleing.replace(",", "")));
														
												        ofr ="Sava Rs. "+ df.format(Float.parseFloat(ofr));
													}else{
														ofr ="";
													}
													
													
													if(Float.parseFloat(saleing.replace(",", "")) == 0.00){
														saleing = MRP;
													}
													int isAvail = 1 ;
													DBUtil dbUtil =new DBUtil();
													MerchantDo merchantDo = new MerchantDaoImpl().getByMerchantname("Meragrocer");
													CityDo cityDo = new CityDaoImpl().getByName(area);
											//		logger.info("Image : " + image +"| Product Name :" + name + "| Product Id : " + productid + "| Varient Id : "+ varientId + "| Weight : "+ weight + "| Selling Price : " + saleing + "| M.R.P. : " + MRP +"| offer : " + ofr );
													if(Float.parseFloat(MRP.replace(",", "")) != 0.00){
													if(name!=null && weight!=null && MRP !=null && saleing!=null&&MRP.trim().matches("[0-9]+(?:\\.[0-9]+)?") && saleing.trim().matches("[0-9]+(?:\\.[0-9]+)?")){
														String respDB = dbUtil.saveOrUpdateByPIdAndVId(cat, subcat, name, weight, MRP, ofr, saleing, image, cityDo, merchantDo, "", Long.valueOf(productid.trim()),Long.valueOf(varientId), "",(short) isAvail,productlink);
														logger.info("[Meragrocer][process]["+area+"] cat: "+cat+" | subcat: "+subcat+" | item: "+name+" | itemid: "+productid+" | variant: "+weight+" | sp: "+saleing+" | mrp: "+MRP+" | offer: "+ofr+" | image: "+image+" | avail: "+(isAvail==2?"Out of Stock":"available")+" | respDB: "+respDB);
													}else{
														logger.info("[Meragrocer][process]["+area+"] cat: "+cat+" | subcat: "+subcat+" | item: "+name+" | itemid: "+productid+" | variant: "+weight+" | sp: "+saleing+" | mrp: "+MRP+" | offer: "+ofr+" | image: "+image+" | avail: "+(isAvail==2?"Out of Stock":"available")+" | Some thing missed");
													}	
									             }
									         }
										}
									}
							     }		
							  }
							
						    }
						  }
				        }
				      }	
			        }
			      }
			    }
		      }
		    }
		  }
		} catch (IOException e) {
			e.printStackTrace();
		}

	  }

//*******************************method to get cookies*****************************
	
	public static Map<String, String> getCookiesForSession(String url) throws IOException {
	    Connection.Response res = Jsoup.connect(url)
	            .method(Connection.Method.GET).timeout(50000).execute();
	    return res.cookies();
	}


	}