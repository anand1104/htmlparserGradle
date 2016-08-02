<%@page import="com.ahoy.parser.dao.MerchantShopezzyCatMapDao"%>
<%@page import="com.ahoy.parser.dao.MerchantShopezzyCatMapDaoImpl"%>
<%@page import="com.ahoy.parser.dao.ShopezzyCategoryDaoImpl"%>
<%@page import="com.ahoy.parser.dao.ShopezzySubCatDaoImpl"%>
<%@page import="com.ahoy.parser.domain.MerchantShopezzyCatMapDo"%>
<%@page import="com.ahoy.parser.domain.MerchantSubCatDo"%>
<%@page import="com.ahoy.parser.domain.ShopezzyCategoryDo"%>
<%@page import="com.ahoy.parser.domain.ShopezzySubCatDo"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>
<%
	String idx = request.getParameter("idx");
	String sSubCatId = request.getParameter("ssubcat");
	String mSubCatId = request.getParameter("msubcat");
	
	MerchantSubCatDo merchantSubCatDo = new MerchantSubCatDo();
	merchantSubCatDo.setSubCatId(Long.valueOf(mSubCatId));
	
	ShopezzySubCatDo shopezzySubCatDo = new ShopezzySubCatDo();
	shopezzySubCatDo.setSubCatId(Long.valueOf(sSubCatId));
	
	
	MerchantShopezzyCatMapDao merchantShopezzyCatMapDao = new MerchantShopezzyCatMapDaoImpl();
	
	MerchantShopezzyCatMapDo merchantShopezzyCatMapDo = merchantShopezzyCatMapDao.select(merchantSubCatDo);
	
	
	if(merchantShopezzyCatMapDo==null){
		merchantShopezzyCatMapDo = new MerchantShopezzyCatMapDo();
		merchantShopezzyCatMapDo.setMerchantSubCatDo(merchantSubCatDo);
		merchantShopezzyCatMapDo.setShopezzySubCatDo(shopezzySubCatDo);
		merchantShopezzyCatMapDo.setCreatedOn(new Timestamp(new Date().getTime()));
		merchantShopezzyCatMapDo.setUpdatedOn(new Timestamp(new Date().getTime()));
		merchantShopezzyCatMapDao.saveOrUpdate(merchantShopezzyCatMapDo);
	}else{
		merchantShopezzyCatMapDo.setShopezzySubCatDo(shopezzySubCatDo);
		merchantShopezzyCatMapDo.setUpdatedOn(new Timestamp(new Date().getTime()));
		merchantShopezzyCatMapDao.saveOrUpdate(merchantShopezzyCatMapDo);
	}
	
	merchantShopezzyCatMapDo = merchantShopezzyCatMapDao.select(merchantSubCatDo, shopezzySubCatDo);
	
	List<ShopezzyCategoryDo> shopezzyCategoryDos = new ShopezzyCategoryDaoImpl().selectAll();
	List<ShopezzySubCatDo> shopezzySubCatDos = new ShopezzySubCatDaoImpl().selectByCategory(merchantShopezzyCatMapDo.getShopezzySubCatDo().getShopezzyCategoryDo()); 
%>

<td><%=Long.valueOf(idx)+1%></td>             
<td><%=merchantShopezzyCatMapDo.getMerchantSubCatDo().getMerchantCategoryDo().getCatName().substring(0, 1).toUpperCase()+merchantShopezzyCatMapDo.getMerchantSubCatDo().getMerchantCategoryDo().getCatName().substring(1) %></td>                           
<td><%=merchantShopezzyCatMapDo.getMerchantSubCatDo().getSubCatName().substring(0, 1).toUpperCase()+merchantShopezzyCatMapDo.getMerchantSubCatDo().getSubCatName().substring(1)%></td>


	<td>
		<select name="scat" id="scat" style="width: 140px;margin-left: 40px;" onchange="onChange(this.value)">
			<option value="-1">--Please Select--</option>
<% 
		if(shopezzyCategoryDos!=null && shopezzyCategoryDos.size()>0){
			for(ShopezzyCategoryDo shopezzyCategoryDo:shopezzyCategoryDos){
%>
				<option value="<%=shopezzyCategoryDo.getCatId()%>" <%if(shopezzyCategoryDo.getCatName().equalsIgnoreCase(merchantShopezzyCatMapDo.getShopezzySubCatDo().getShopezzyCategoryDo().getCatName())){%> selected="selected"<%}%>  ><%=shopezzyCategoryDo.getCatName().substring(0, 1).toUpperCase()+shopezzyCategoryDo.getCatName().substring(1)%> </option>
<%
		}
}	
%>
		</select>
	</td> 	                          
	<td>
		<select name="ssubcat<%=idx%>" id="ssubcat<%=idx%>" style="width: 140px;margin-left: 40px;" onchange="selectSubCategory(this.value,<%=merchantSubCatDo.getSubCatId()%>,<%=idx%>)">
<%
		if(shopezzySubCatDos!=null && shopezzySubCatDos.size()>0){
			for(ShopezzySubCatDo shopezzySubCatDo2: shopezzySubCatDos){
%>
	
				<option value="<%=shopezzySubCatDo2.getSubCatId()%>" <%if(shopezzySubCatDo2.getSubCatName().equalsIgnoreCase(merchantShopezzyCatMapDo.getShopezzySubCatDo().getSubCatName())){%> selected="selected"<%}%>  ><%=shopezzySubCatDo2.getSubCatName().substring(0, 1).toUpperCase()+shopezzySubCatDo2.getSubCatName().substring(1)%></option>
<%				
			}
			
		}
%>
		
		</select>	
	</td>
