<%@page import="org.jsoup.Connection"%>
<%@page import="org.jsoup.Connection.Response"%>
<%@page import="org.jsoup.Jsoup"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.DataOutputStream"%>
<%@page import="java.io.InputStreamReader"%>
<%--<%@page import="org.jsoup.Connection.Method"%>--%>
<%@page import="java.net.HttpURLConnection"%>
<%--<%@page import="org.jsoup.nodes.Document"%>--%>
<%@page import="java.net.URL"%>
<%@page import="java.util.Map"%>
<%

	String url1 = "http://shopration.com/";
	Connection connection = Jsoup.connect(url1).timeout(25000);
	Response response1 = connection.execute();
	Map<String, String> mapResponseCookie = response1.cookies();
	String cookie="";
	for(Map.Entry<String, String> map: mapResponseCookie.entrySet()){
		cookie+=map.getKey()+"="+map.getValue()+";";
	}
	
	String userAgent =request.getHeader("User-Agent");
	System.out.println(userAgent);
	URL url =  new URL("http://shopration.com/index.php?route=checkout/cart/add");
	HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
	httpURLConnection.setRequestMethod("POST");
	httpURLConnection.setDoOutput(true);
	httpURLConnection.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
	httpURLConnection.setRequestProperty("Accept-Encoding","gzip,deflate");
	httpURLConnection.setRequestProperty("Accept-Language","en-GB,en-US;q=0.8,en;q=0.6");
	httpURLConnection.setRequestProperty("Connection","keep-alive");
	httpURLConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
	httpURLConnection.setRequestProperty("Host","shopration.com");
	httpURLConnection.setRequestProperty("Origin","http://shopration.com");
	httpURLConnection.setRequestProperty("Referer","http://shopration.com/");
	httpURLConnection.setRequestProperty("User-Agent",userAgent);
	httpURLConnection.setRequestProperty("X-Requested-With","XMLHttpRequest");
	httpURLConnection.setRequestProperty("route", "checkout/cart/add");
	httpURLConnection.setRequestProperty("Cookie", cookie);
	
	DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
	dos.write("route=checkout/cart/add&product_id=1426&quantity=1".getBytes());
	dos.close();
	
	BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

    String inputLine;
    StringBuffer buffer = new StringBuffer();

    while ((inputLine = in.readLine()) != null) {
    	buffer.append(inputLine);
    }
    
    System.out.println(buffer);
    
    Cookie cookie2 = new Cookie("Cookie",cookie);  
   
	response.addCookie(cookie2);
	response.sendRedirect("http://shopration.com");
%>