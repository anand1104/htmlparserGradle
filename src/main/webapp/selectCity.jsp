<!DOCTYPE html>
<%@page import="com.ahoy.parser.dao.CityDaoImpl"%>
<%@page import="com.ahoy.parser.domain.CityDo"%>
<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>
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

			if (str == -1) {
				document.getElementById("merchantdiv").style.display = "none";
			} else {
				document.getElementById("merchantdiv").style.display = "table-row";
				var xmlhttp;
				if (str == "") {
					document.getElementById("txtHint").innerHTML = "";
					return;
				}
				if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
					xmlhttp = new XMLHttpRequest();
				}
				else {// code for IE6, IE5
					xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
				}
				xmlhttp.onreadystatechange = function () {
					if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
						document.getElementById("merchantdiv").innerHTML = xmlhttp.responseText;
					}
				}
				xmlhttp.open("GET", "jsps/div/selectmerchant.jsp?cityid=" + str, true);
				xmlhttp.send();
			}
		}


		$(document).ready(function () {
			$("#sbmtn").click(function () {
				var cityid = document.getElementById("cityid").value;
				if (cityid == -1) {
					alert('please select City');
					document.getElementById("cityid").focus();
					return false;
				} else {
					var merchantid = document.getElementById("merchantid").value;

					if (merchantid == -1) {
						alert('please select merchantid');
						document.getElementById("merchantid").focus();
						return false;
					}
				}

// 					var listtype = document.getElementById("listtype").value;

// 					if(listtype==-1){
// 						alert('please select List type');
// 						document.getElementById("listtype").focus();
// 						return false;
// 					}

				return true;
			});
		});

	</script>

</head>

<body>

<%
		Logger logger = LoggerFactory.getLogger("selectCity.jsp");
	try{
	%>
		<%List<CityDo> cityDos = new CityDaoImpl().selectAll(); %>
		<div class="auto-1000">
			<div class="main-container">
    			<div class="tab-container"> 
    				<img style="float: left;width: 180px;height: 65px;" src="images/shopezzy.png">	
         			<ul>
         				<li><a id="surfexcel"  href="smry"  class="" ><span>Summary</span></a></li>
         				<li><a id="surfexcel" href="selectCity.jsp" class="active" ><span>Online Report</span></a></li>
         				<li><a id="surfexcel" href="categorymap.jsp" class="" ><span>Category-Map</span></a></li>
<!--                 		<li><a id="surfexcel"  href="offlineitem"  class="" ><span>Offline Report</span></a></li>   -->
<!--                 		<li><a id="surfexcel"  href="itemlist"  class="" ><span>ItemList</span></a></li>	 -->
        			</ul>	 
        		</div>
      			<div class="report-container">
        			<h2>Get Online Merchant Detail Citywise</h2>
            		<div class="select-form">
            			<form action="view" method="post">
            			<table>            				
            				<tr>
            					<td>Cities</td>
            					<td>
            						<select name="cityid" id="cityid" style="background: white;" onchange="onChange(this.value);" >
	 									<option value="-1">--Please Select--</option>
	 									<% if(cityDos!=null&&cityDos.size()>0){
	 										for(CityDo cityDo:cityDos){
	 									%>
	 											<option value="<%=cityDo.getCityId() %>"><%=cityDo.getCityName().substring(0, 1).toUpperCase()+cityDo.getCityName().substring(1)%></option>
	 									<%		
	 										}
	 									} %>
	 									
	 								</select>
            					</td>
            				</tr>
            				<tr id="merchantdiv">            					
            				</tr>
            				<tr>
            					<td>Todays List</td>
            					<td>
            						<select name="listtype" id="listtype" style="background: white;">
	 									<option value="-1">Total Items</option>
	 									<option value="1">New Items</option>
	 									<option value="2">Updated Items</option>
	 								</select>
	 							</td>	
	 						</tr>		
            				<tr>
            					<td colspan="2"><input type="Submit" id="sbmtn" value="Show Report"></td>
            				</tr>
            			</table>
            			</form>               			
           			</div>          
        		</div>        		
    		</div>	
		</div>
		<%	
	} catch(Exception e){
		logger.error("[selectCity.jsp]"+e);
	}%>
	</body>
	
 
</html>
