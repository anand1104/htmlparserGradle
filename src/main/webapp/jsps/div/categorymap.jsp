<%@page import="com.ahoy.parser.dao.*"%>
<%@page import="com.ahoy.parser.domain.*"%>
<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%@page import="java.util.List"%>
<%
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	
	MerchantDo merchantDo = (MerchantDo)request.getAttribute("merchantDo");
	List<MerchantCategoryDo> merchantCategoryDos = (List<MerchantCategoryDo>)request.getAttribute("merchantCategoryDos");
	List<ShopezzyCategoryDo> shopezzyCategoryDos= (List<ShopezzyCategoryDo>)request.getAttribute("shopezzyCategoryDos");
	
	if(merchantDo!=null){
%>


<table id="mytable">
 	<thead>
    	<tr>
        	<td width="5%" rowspan="2" >SNo.</td> 
            <td  colspan="2" ><%=merchantDo.getMerchantName().substring(0, 1).toUpperCase()+merchantDo.getMerchantName().substring(1)%></td>
  			<td  colspan="2" >Shopezzy</td> 
            
      	</tr>  
		<tr>                  
  			<td width="20%">Category</td>                           
            <td width="20%">Sub Category</td>
            <td width="20%">Category</td>                           
            <td width="20%">Sub Category</td>
		</tr>
	</thead> 
<% 
	if(merchantCategoryDos!=null && merchantCategoryDos.size()>0){ 
		int i = 0;
		MerchantSubCatDao merchantSubCatDao = new MerchantSubCatDaoImpl();
		MerchantShopezzyCatMapDao merchantShopezzyCatMapDao = new MerchantShopezzyCatMapDaoImpl();
		for(MerchantCategoryDo merchantCategoryDo: merchantCategoryDos){
			List<MerchantSubCatDo> merchantSubCatDos = merchantSubCatDao.selectByCategory(merchantCategoryDo);
			if(merchantSubCatDos !=null && merchantSubCatDos.size()>0){
				
				for(MerchantSubCatDo merchantSubCatDo: merchantSubCatDos){
					
					MerchantShopezzyCatMapDo merchantShopezzyCatMapDo = merchantShopezzyCatMapDao.select(merchantSubCatDo);
					ShopezzySubCatDo shopezzySubCatDo = merchantShopezzyCatMapDo!=null?merchantShopezzyCatMapDo.getShopezzySubCatDo():null;
	
%>
	<tr id="table-row<%=i%> ">      
		<td><%=i+1%></td>             
 		<td><%=merchantCategoryDo.getCatName().substring(0, 1).toUpperCase()+merchantCategoryDo.getCatName().substring(1)%></td>                           
        <td><%=merchantSubCatDo.getSubCatName().substring(0, 1).toUpperCase()+merchantSubCatDo.getSubCatName().substring(1)%></td>
        <% if(shopezzySubCatDo!=null){ 
        	List<ShopezzySubCatDo> shopezzySubCatDos = new ShopezzySubCatDaoImpl().selectByCategory(shopezzySubCatDo.getShopezzyCategoryDo());
        %>
	        <td>
	        	
	        	<select name="scat<%=i%>" id="scat<%=i%>" style="width: 140px;margin-left: 40px;" onchange="onChange(this.value,<%=i%>)">
	     			<option value="-1">--Please-Select--</option>
	     			<% if(shopezzyCategoryDos!=null&&shopezzyCategoryDos.size()>0){
	     				for(ShopezzyCategoryDo shopezzyCategoryDo:shopezzyCategoryDos){
	     			%>
	     				<option value="<%=shopezzyCategoryDo.getCatId()%>" <%if(shopezzySubCatDo.getShopezzyCategoryDo().getCatName().trim().equalsIgnoreCase(shopezzyCategoryDo.getCatName().trim())){%> selected="selected" <%} %> ><%=shopezzyCategoryDo.getCatName().substring(0, 1).toUpperCase()+shopezzyCategoryDo.getCatName().substring(1) %></option>
	     			<%		
	     				}
	     				
	     			} %>
	     		</select>
	        </td>                           
	        <td>
	     		<select name="ssubcat<%=i%>" id="ssubcat<%=i%>" style="width: 140px;margin-left: 40px;" onchange="selectSubCategory(this.value,<%=merchantSubCatDo.getSubCatId()%>,<%=i%>)">
	     			<% if(shopezzySubCatDos!=null && shopezzySubCatDos.size()>0){
	     				for(ShopezzySubCatDo shopezzySubCatDo2:shopezzySubCatDos){
	     			
	     			%>
	     					<option value="<%=shopezzySubCatDo2.getSubCatId() %>" <% if(shopezzySubCatDo.getSubCatName().equalsIgnoreCase(shopezzySubCatDo2.getSubCatName())){ %> selected="selected"  <% }%>><%=shopezzySubCatDo2.getSubCatName().substring(0, 1).toUpperCase()+shopezzySubCatDo2.getSubCatName().substring(1)%></option>
	     				
	     			<%
	     				}
	     			}%>
	     		</select>
	     	</td>
	     <%}else{%>
	     	<td>
	     		<select name="scat<%=i%>" id="scat<%=i%>" style="width: 140px;margin-left: 40px;" onchange="onChange(this.value,<%=i%>)">
	     			<option value="-1">--Please-Select--</option>
	     			<% if(shopezzyCategoryDos!=null&&shopezzyCategoryDos.size()>0){
	     				for(ShopezzyCategoryDo shopezzyCategoryDo:shopezzyCategoryDos){
	     			%>
	     				<option value="<%=shopezzyCategoryDo.getCatId()%>"><%=shopezzyCategoryDo.getCatName().substring(0, 1).toUpperCase()+shopezzyCategoryDo.getCatName().substring(1)%></option>
	     			<%		
	     				}
	     				
	     			} %>
	     		</select>
	     	</td>
	     	<td>
	     		<select name="ssubcat<%=i%>" id="ssubcat<%=i%>" style="width: 140px;margin-left: 40px;"  onchange="selectSubCategory(this.value,<%=merchantSubCatDo.getSubCatId()%>,<%=i%>)">
	     			<option value="-1">--Please-Select--</option>
	     		</select>
	     	</td>
	     <%} %>
	</tr>
	
<%			
				i++;}
			}else{
				//When sub category not found
			}
		}
	} else{
%>
		<tr>      
			<td colspan="5">Category not found</td>             
 			
		</tr>
	<%} %>
 </table>
 <%}
	
 %>