<!DOCTYPE html>
<%@page import="com.ahoy.parser.dao.MerchantDaoImpl"%>
<%@page import="com.ahoy.parser.domain.MerchantDo"%>
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
    <script type="text/javascript"
            src="js/jquery.min.js"></script>
    <script src="js/jquery-1.9.0.js"></script>
    <script src="js/jquery-ui-1.10.0.custom.js"></script>

    <script type="text/javascript">

        function onChange(str, i) {

            if (str == -1) {
                alert("Please select any shopezzy category");
                document.getElementById("scat" + i).focus();
                return false;
            } else {

                var xmlhttp;
                if (window.XMLHttpRequest) {
//         		code for IE7+, Firefox, Chrome, Opera, Safari
                    xmlhttp = new XMLHttpRequest();
                } else {
//         		code for IE6, IE5
                    xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
                }
                xmlhttp.onreadystatechange = function () {
                    if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                        document.getElementById("ssubcat" + i).innerHTML = xmlhttp.responseText;
                    }
                }
                xmlhttp.open("GET", "jsps/div/cmapsubcat.jsp?id=" + str, true);
                xmlhttp.send();

                alert("Please Select Shopezzy sub category");
                document.getElementById("ssubcat" + i).focus();
            }
        }

        function selectSubCategory(val1, val2, val3) {

            if (val1 == -1) {
                alert("Please Select any Shopezzy Sub-Category");

            } else {

                var xmlhttp;
                if (window.XMLHttpRequest) {
//         code for IE7+, Firefox, Chrome, Opera, Safari
                    xmlhttp = new XMLHttpRequest();
                } else {
//         code for IE6, IE5
                    xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
                }
                xmlhttp.onreadystatechange = function () {
                    if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {

                        document.getElementById("table-row" + val3).innerHTML = xmlhttp.responseText;

                    }
                }
                xmlhttp.open("GET", "jsps/div/cmaprow.jsp?ssubcat=" + val1 + "&msubcat=" + val2 + "&idx=" + val3, true);
                xmlhttp.send();
            }
        }

        function getCategories(value) {
            var xmlhttp;
            if (window.XMLHttpRequest) {
//            code for IE7+, Firefox, Chrome, Opera, Safari
                xmlhttp = new XMLHttpRequest();
            } else {
//            code for IE6, IE5
                xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
            }
            xmlhttp.onreadystatechange = function () {
                if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {

                    document.getElementById("categorymap").innerHTML = xmlhttp.responseText;


                }
            }
            xmlhttp.open("GET", "c?merchantid=" + value, true);
            xmlhttp.send();

        }

    </script>

</head>
<body>
<%
    	List<MerchantDo> merchantDos = new MerchantDaoImpl().getByType((short)1);
    
    %>
    <div class="auto-1000">
      <div class="main-container">
          <div class="tab-container"> 
            <img style="float: left;width: 180px;height: 65px;" src="images/shopezzy.png">    
              <ul>
<%--                 <li><a id="surfexcel"  href="#"  class="active" ><span><%=merchantName.toUpperCase()%> - <%=cityName.toUpperCase()%></span></a></li> --%>
                	<li><a id="surfexcel"  href="smry"  class="" ><span>Summary</span></a></li>
         			<li><a id="surfexcel" href="selectCity.jsp" class="" ><span>Online Report</span></a></li>
         			<li><a id="surfexcel" href="categorymap.jsp" class="active" ><span>Category-Map</span></a></li>
<!--                 		<li><a id="surfexcel"  href="offlineitem"  class="" ><span>Offline Report</span></a></li>   -->
<!--                 		<li><a id="surfexcel"  href="itemlist"  class="" ><span>ItemList</span></a></li>	 -->
<!--                  <li><a id=""  href="logout" ><span>Logout</span></a></li> -->
              </ul>  
            </div>
            <div class="report-container">
              <h2>Online Merchant Category Mapping With Shopezzy</h2>
              	<div class="input-form">
              		<table>
                  		<tr> 
                  			<td>Merchants</td>
                  			<td>
                  				<select name="merchantid" id="merchantid" onchange="getCategories(this.value)"> 
                  					<option value="-1" >--Please-Select--</option>
                  					<%if(merchantDos!=null && merchantDos.size()>0){
                  						for(MerchantDo merchantDo: merchantDos){	
                  					%>
                  							<option value="<%=merchantDo.getMerchantId()%>" ><%=merchantDo.getMerchantName().substring(0, 1).toUpperCase()+merchantDo.getMerchantName().substring(1)%></option>
                  					<%
                  						}
                  					}
                  					%>
                  				</select>
                  			</td>
                  		</tr>
                  	</table>              		
              	</div>
                <div class="ad-report" id="categorymap"></div>
            </div>
          </div>
    </div>    
  </body> 
</html>
