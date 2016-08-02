<!DOCTYPE html>
<%@page import="com.ahoy.parser.domain.MerchantSubCatDo"%>
<%@page import="com.ahoy.parser.domain.ParseDataDo"%>
<%@page import="com.ahoy.parser.domain.ShopezzyCategoryDo"%>
<%@page import="com.ahoy.parser.domain.ShopezzySubCatDo"%>
<%@page import="java.util.List"%>


<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>Dashboard</title>

	<meta http-equiv="Content-Type"
		  content="text/html; charset=utf-8"/>

	<link rel="stylesheet" type="text/css"
		  href="css/style.css">
	<link rel="stylesheet" type="text/css"
		  href="css/font-awesome.css">
	<link rel="stylesheet" type="text/css"
		  href="css/font-awesome.min.css">
	<link rel="stylesheet" type="text/css"
		  href="css/bootstrap.min.css" media="screen">

	<link rel="stylesheet" href="css/jquery-ui.css">
	<script src="js/jquery-1.10.2.js"></script>
	<script src="js/jquery-ui.js"></script>

	<script type="text/javascript"
			src="js/jquery.min.js"></script>
	<script src="js/jquery-1.9.0.js"></script>
	<script src="js/jquery-ui-1.10.0.custom.js"></script>

	<script>

		function onChange(str) {
			var xmlhttp;
			if (window.XMLHttpRequest) {
//   			code for IE7+, Firefox, Chrome, Opera, Safari
				xmlhttp = new XMLHttpRequest();
			} else {
// 				code for IE6, IE5
				xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
			}
			xmlhttp.onreadystatechange = function () {
				if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
					document.getElementById("subcatdiv").innerHTML = xmlhttp.responseText;
				}
			}
			xmlhttp.open("GET", "jsps/div/subcat.jsp?id=" + str, true);
			xmlhttp.send();
		}


	</script>

</head>
<body>
<%
	ParseDataDo parseDataDo = (ParseDataDo)request.getAttribute("parseDataDo");
	MerchantSubCatDo merchantSubCatDo = parseDataDo.getMerchantSubCatDo();
	ShopezzySubCatDo shopezzySubCatDo = parseDataDo.getShopezzySubCatDo();
	
	List<ShopezzyCategoryDo> shopezzyCategoryDos = (List<ShopezzyCategoryDo>)request.getAttribute("shopezzyCategoryDos");
	List<ShopezzySubCatDo> shopezzySubCatDos = (List<ShopezzySubCatDo>)request.getAttribute("shopezzySubCatDos");
	
	String merchantName = (String)request.getAttribute("merchantName");
	String cityName = (String)request.getAttribute("cityName");
	String listtype = (String)request.getAttribute("listtype");
	
	String resp = (String)request.getAttribute("resp");
	
%>
		<div class="auto-1000">
			<div class="main-container">
    			<div class="tab-container"> 
    				<img style="float: left;width: 160px;height: 65px;" src="images/shopezzy.png">	
         			<ul>
         				<li><a id="surfexcel"  href="#"  class="active" ><span><%=merchantName.toUpperCase()%> - <%=cityName.toUpperCase()%></span></a></li>
         				<li>
							<a id="surfexcel"  href="view?cityid=<%=parseDataDo.getCityDo().getCityId()%>&merchantid=<%=parseDataDo.getMerchantDo().getMerchantId()%>&listtype=<%=listtype%>"  class="" >
								<span>View</span>
							</a>
						</li>
<!--          				<li><a id=""  href="logout" ><span>Logout</span></a></li> -->
        			</ul>	 
        		</div>
      			<div class="report-container">
        			<h2>Items Editable Form</h2>
            		<div class="select-form">
            			
            			<form action="update" method="post">
            			<table> 
            				<% if(resp!=null &&!"".equals(resp.trim())&&!"null".equalsIgnoreCase(resp.trim())){ %>
            				<tr><td colspan="2" style="color: red;font-size: large;text-align: left;"><%=resp %></td></tr>
            				<%} %>
            				          				
            				<tr>
            					<td>Id</td>
            					<td>
            						<input type="text" name="id" id="id" value="<%=parseDataDo.getDataPkey()%>"  readonly="readonly">
            					</td>
            				</tr>
            				<tr>
            					<td>Description</td>
            					<td>
            						<input type="text" name="desc" id="desc" value="<%=parseDataDo.getDescription()%>">
            					</td>
            				</tr>            				
            				<tr>
            					<td>Max Price</td>
            					<td>
            						<input type="text" name="mprice" id="mprice" value="<%=parseDataDo.getMaxPrice()%>">
            					</td>
            				</tr>            				
            				<tr>
            					<td>Offer</td>
            					<td>
            						<input type="text" name="offer" id="offer" value="<%=parseDataDo.getOffer()%>">
            					</td>
            				</tr>            				
            				<tr>
            					<td>Sell Price</td>
            					<td>
            						<input type="text" name="sprice" id="sprice"  value="<%=parseDataDo.getSellPrice()%>">
            					</td>
            				</tr>
            				<tr>
            					<td>Weight</td>
            					<td>
            						<input type="text" name="weight" id="weight" value="<%=parseDataDo.getWeight()%>">
            					</td>
            				</tr>     				     				
            				
            				<tr>
            					<td>Merchant Category</td>
            					<td>
            						<input type="text" name="mcat" id="mcat" value="<%= merchantSubCatDo.getMerchantCategoryDo().getCatName() %>" >
            					</td>
            				</tr>
            				<tr>
            					<td>Merchant Sub-Category</td>
            					<td>
            						<input type="text" name="msubcat" id="msubcat" value="<%= merchantSubCatDo.getSubCatName() %>" >
            					</td>
            				</tr>
            				<tr>
            					<td>Shopezzy Category</td>
            					<td>
            						<select name="scat" id="scat" onchange="onChange(this.value);">
            							<option value="-1">-- Please-Select --</option>
            							<% if(shopezzyCategoryDos!=null && shopezzyCategoryDos.size()>0){ 
            									for(ShopezzyCategoryDo shopezzyCategoryDo: shopezzyCategoryDos){
            							%>
            										<option value="<%=shopezzyCategoryDo.getCatId()%>" <%if(shopezzyCategoryDo.getCatName().equalsIgnoreCase(shopezzySubCatDo.getShopezzyCategoryDo().getCatName().trim())) {%> selected="selected" <%}%> ><%=shopezzyCategoryDo.getCatName() %></option>		
            							<%
            									}
            								} %>
            						</select>
            					</td>
            				</tr>
							<tr id="subcatdiv">
            					<td>Shopezzy Sub-Category</td>
            					<td>
            						<select name="ssubcat" id="ssubcat">
            							<option value="-1">-- Please-Select --</option>
            							<% if(shopezzySubCatDos!=null && shopezzySubCatDos.size()>0){ 
            									for(ShopezzySubCatDo shopezzySubCatDo2: shopezzySubCatDos){
            							%>
            										<option value="<%=shopezzySubCatDo2.getSubCatId()%>" <%if(shopezzySubCatDo2.getSubCatName().equalsIgnoreCase(shopezzySubCatDo.getSubCatName().trim())){%> selected="selected" <%} %>  ><%=shopezzySubCatDo2.getSubCatName()%></option>		
            							<%
            									}
            								} %>
            						</select>
            					</td>
            				</tr>
            				<tr>
            					
            					<td colspan="2">
            						<input type="hidden" name="merchantName" id="merchantName" value="<%=merchantName %>" >
            						<input type="hidden" name="cityName" id="cityName" value="<%=cityName %>" >
            						<input type="hidden" name="listtype" id="listtype" value="<%=listtype %>" >
            						<input type="Submit" id="sbmtn" value="Update"></td>
            				</tr>
            			</table>
            			</form>               			
           			</div>          
        		</div>        		
    		</div>	
		</div>
		
	</body>
	
 
</html>
