
<%@page import="com.ahoy.parser.dao.ShopezzySubCatDaoImpl"%>
<%@page import="com.ahoy.parser.domain.ShopezzyCategoryDo"%>
<%@page import="com.ahoy.parser.domain.ShopezzySubCatDo"%>
<%@page import="java.util.List"%>
<%
	String id = request.getParameter("id");
	ShopezzyCategoryDo shopezzyCategoryDo = new ShopezzyCategoryDo();
	shopezzyCategoryDo.setCatId(Long.valueOf(id.trim()));
	List<ShopezzySubCatDo> shopezzySubCatDos = new ShopezzySubCatDaoImpl().selectByCategory(shopezzyCategoryDo);
	
%>

	<% if(shopezzySubCatDos!=null && shopezzySubCatDos.size()>0){ 
    			for(ShopezzySubCatDo shopezzySubCatDo2: shopezzySubCatDos){
    	%>
    			<option value="<%=shopezzySubCatDo2.getSubCatId()%>" ><%=shopezzySubCatDo2.getSubCatName()%></option>		
        <%
        		}
        	} %>
	