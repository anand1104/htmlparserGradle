<!DOCTYPE html>
<%@page import="com.ahoy.parser.dao.CityDaoImpl"%>
<%@page import="com.ahoy.parser.domain.CityDo"%>
<%@page import="com.ahoy.parser.domain.MerchantDo"%>
<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%@page import="java.util.Set"%>

<%
	Logger logger = LoggerFactory.getLogger("selectmerchant.jsp");
	try{
	String cityId = request.getParameter("cityid");
	CityDo cityDo = new CityDaoImpl().getByCityId(Long.valueOf(cityId));
	Set<MerchantDo> merchantDos = cityDo.getMerchantDos();
	
// 	Set<MerchantCityMapDo> merchantCityMapDos = cityDo.getMerchantCityMapDos();
// 	List<MerchantDo> merchantDos = new ArrayList<MerchantDo>();
// 	for(MerchantCityMapDo merchantCityMapDo:merchantCityMapDos){
// 		if(merchantCityMapDo.getMerchantDo().getStatus()==0)
// 			merchantDos.add(merchantCityMapDo.getMerchantDo());
// 	}
%>
<td>Merchants</td>
<td>
<select name="merchantid" id="merchantid" style="background: white;" >
	<option value="-1">--Please Select--</option>
	<% if(merchantDos!=null&&merchantDos.size()>0){
		for(MerchantDo merchantDo:merchantDos){
	%>
	<option value="<%=merchantDo.getMerchantId() %>"><%=merchantDo.getMerchantName().substring(0, 1).toUpperCase()+merchantDo.getMerchantName().substring(1)%></option>
	<%		
		}
	} %>
</select>            					
</td>	
<%}catch(Exception e){
	logger.error("[selectmerchant.jsp] Exception: "+e);
} %>
	
 

