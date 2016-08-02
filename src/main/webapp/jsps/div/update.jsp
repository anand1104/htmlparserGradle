<%@page import="com.ahoy.parser.domain.ParseDataDo"%>
<%@page import="com.ahoy.parser.domain.ShopezzySubCatDo"%>
<%

	String idx = (String)request.getAttribute("idx");
	ParseDataDo parseDataDo = (ParseDataDo)request.getAttribute("parseDataDo");
	ShopezzySubCatDo shopezzySubCatDo = parseDataDo.getShopezzySubCatDo();
	String catName = shopezzySubCatDo.getShopezzyCategoryDo().getCatName();
	String subCatName = shopezzySubCatDo.getSubCatName();
%>
<td><%=Long.valueOf(idx.trim())+1%></td>                   
<td title="<%=parseDataDo.getDescription() %>"><%=parseDataDo.getDescription().length()>25?parseDataDo.getDescription().substring(0, 23)+"..":parseDataDo.getDescription() %></td>
<td title="<%=parseDataDo.getWeight() %>"><%=parseDataDo.getWeight() %></td>
<td title="<%=parseDataDo.getMaxPrice() %>"><%=parseDataDo.getMaxPrice() %></td>
<td title="<%=parseDataDo.getSellPrice() %>"><%=parseDataDo.getSellPrice() %></td>
<td title="<%=parseDataDo.getOffer()%>"><%=parseDataDo.getOffer()%></td>
<td title="<%=catName%>"><%=catName.length()>20?(catName.substring(0,1).toUpperCase()+catName.substring(1, 18)+".."):(catName.substring(0,1).toUpperCase()+catName.substring(1)) %></td>
<td title="<%=subCatName%>"><%=subCatName.length()>20?subCatName.substring(0,1).toUpperCase()+subCatName.substring(1, 18)+"..":subCatName.substring(0,1).toUpperCase()+subCatName.substring(1) %></td>
<td><a href="javascript:globaledit(<%=parseDataDo.getDataPkey()%>,<%=idx%>,<%=parseDataDo.getStatus()%>)"><i class="glyphicon glyphicon-edit" <%if(parseDataDo.getStatus()==2){%> style="color: #25ad53;" <%}%>></i></a></td>
<td>
 	<div class="input-group status">
		<% if(parseDataDo.getStatus()==0){ %>
			<input type="checkbox" name="isstatus<%=parseDataDo.getDataPkey()%>" id="isstatus<%=parseDataDo.getDataPkey()%>" onchange="changeStatus(<%=parseDataDo.getDataPkey()%>,<%=idx%>)">
	      <label for="disable">Pending</label>
	     <%}else{
	     	if(parseDataDo.getStatus()==1){
	     %>                        	
	      	<input type="checkbox" name="isstatus<%=parseDataDo.getDataPkey()%>" id="isstatus<%=parseDataDo.getDataPkey()%>" checked="checked" onchange="changeStatus(<%=parseDataDo.getDataPkey()%>,<%=idx%>)">
	       <label for="disable">Verified</label>
	     <%	}else{
	     %>
	     		<input type="checkbox" name="isstatus<%=parseDataDo.getDataPkey()%>" id="isstatus<%=parseDataDo.getDataPkey()%>" checked="checked" onchange="changeStatus(<%=parseDataDo.getDataPkey()%>,<%=idx%>)">
	      	<label for="disable">Processed</label>
	     <%	}
	     } %>
	</div> 
</td>