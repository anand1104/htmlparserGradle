<%@page import="com.ahoy.parser.dao.ParseDataDaoImpl"%>
<%@page import="com.ahoy.parser.dao.ShopezzyCategoryDaoImpl"%>
<%@page import="com.ahoy.parser.dao.ShopezzySubCatDaoImpl"%>
<%@page import="com.ahoy.parser.domain.ParseDataDo"%>
<%@page import="com.ahoy.parser.domain.ShopezzyCategoryDo"%>
<%@page import="com.ahoy.parser.domain.ShopezzySubCatDo"%>
<%@page import="java.util.List"%>



<%
	String id = request.getParameter("id");
	String idx = request.getParameter("idx");
	ParseDataDo parseDataDo = new ParseDataDaoImpl().selectById(Long.valueOf(id.trim()));	
	List<ShopezzyCategoryDo> shopezzyCategoryDos = new ShopezzyCategoryDaoImpl().selectAll();
	
	List<ShopezzySubCatDo> shopezzySubCatDos = new ShopezzySubCatDaoImpl().selectByCategory(parseDataDo.getShopezzySubCatDo().getShopezzyCategoryDo());
	
	ShopezzySubCatDo shopezzySubCatDo = parseDataDo.getShopezzySubCatDo();

%>

<td>
	<%=Long.valueOf(idx)+1%>
	<input type="hidden" name="id" id="id" value="<%=parseDataDo.getDataPkey() %>">
	<input type="hidden" name="idx" id="idx" value="<%=idx%>">	
</td> 									
<td><%=parseDataDo.getDescription().length()>25?parseDataDo.getDescription().substring(0, 23)+"..":parseDataDo.getDescription() %></td>
<td><%=parseDataDo.getWeight() %></td>
<td><%=parseDataDo.getMaxPrice() %></td>
<td><%=parseDataDo.getSellPrice() %></td>
<td><%=parseDataDo.getOffer()%></td>
<td>
	<select name="scat" id="scat" onchange="onChange(this.value);" style="width: 110px;height: 20px;">
   		<% if(shopezzyCategoryDos!=null && shopezzyCategoryDos.size()>0){ 
			for(ShopezzyCategoryDo shopezzyCategoryDo: shopezzyCategoryDos){
		%>
		<option value="<%=shopezzyCategoryDo.getCatId()%>" <%if(shopezzyCategoryDo.getCatName().equalsIgnoreCase(shopezzySubCatDo.getShopezzyCategoryDo().getCatName().trim())) {%> selected="selected" <%}%> ><%=shopezzyCategoryDo.getCatName().substring(0, 1).toUpperCase()+shopezzyCategoryDo.getCatName().substring(1)%></option>		
		<%
			}
		} %>
    </select>
</td>
<td>
	<select name="ssubcat" id="ssubcat" style="width: 110px;height: 20px;">
		<% if(shopezzySubCatDos!=null && shopezzySubCatDos.size()>0){ 
    		for(ShopezzySubCatDo shopezzySubCatDo2: shopezzySubCatDos){
        %>
        <option value="<%=shopezzySubCatDo2.getSubCatId()%>" <%if(shopezzySubCatDo2.getSubCatName().equalsIgnoreCase(shopezzySubCatDo.getSubCatName().trim())){%> selected="selected" <%} %>  ><%=shopezzySubCatDo2.getSubCatName().substring(0, 1).toUpperCase()+shopezzySubCatDo2.getSubCatName().substring(1)%></option>		
        <%
        	}
       	} %>
   	</select>
</td>
<td><input type="Submit" id="sbmtn" value="Update" onclick="onSubmit()"  style="width: 50px;padding: 5px 4px;font-size: inherit;background: rgb(53, 113, 54);-webkit-box-shadow: inset 0px 2px 0px rgb(53, 113, 54);"></td>
<td>
	<% if(parseDataDo.getStatus()==0){%>Pending<%}else if(parseDataDo.getStatus()==1){%>Verify<%}else if(parseDataDo.getStatus()==2){%>Processed<%}%>
</td>
