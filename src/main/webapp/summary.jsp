<!DOCTYPE html>
<%@page import="com.ahoy.parser.domain.CityDo" %>
<%@page import="com.ahoy.parser.domain.MerchantDo" %>
<%@page import="java.util.HashMap" %>
<%@page import="java.util.List" %>


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
</head>
<body>
<%
%>
<div class="auto-1000">
    <div class="main-container">
        <div class="tab-container">
            <img style="float: left;width: 180px;height: 65px;"
                 src="images/shopezzy.png">
            <ul>
                <li><a id="surfexcel" href="smry"
                       class="active"><span>Summary</span></a>
                </li>
                <li><a id="surfexcel" href="selectCity.jsp"
                       class=""><span>Online Report</span></a>
                </li>
                <li><a id="surfexcel" href="categorymap.jsp"
                       class=""><span>Category-Map</span></a>
                </li>
                <!--                 <li><a id="surfexcel"  href="offlineitem"  class="" ><span>Offline Report</span></a></li>   -->
                <!--                 <li><a id="surfexcel"  href="itemlist"  class="" ><span>ItemList</span></a></li>	 -->
            </ul>
        </div>
        <div class="report-container">
            <h2>Summary of all Active Online Merchants</h2>
            <div class="sumary-report">
                <%
                    List<MerchantDo> merchantDos = (List<MerchantDo>) request.getAttribute("merchantDos");
                    HashMap<String, HashMap<String, HashMap<String, String>>> map = (HashMap<String, HashMap<String, HashMap<String, String>>>) request.getAttribute("map");

                %>
                <table id="mytable">
                    <thead>
                    <tr>
                        <td width="10%" rowspan="2">SNo.
                        </td>
                        <td width="25%" rowspan="2">Merchant
                            Name
                        </td>
                        <td colspan="3">New Items</td>
                        <td colspan="3">Updated Items</td>

                    </tr>
                    <tr>

                        <td width="10%">Pending</td>
                        <td width="10%">Verify</td>
                        <td width="15%">Processed</td>
                        <td width="10%">Pending</td>
                        <td width="10%">Verify</td>
                        <td width="15%">Processed</td>
                    </tr>
                    </thead>

                    <%
                        int i = 0;
                        for (MerchantDo merchantDo : merchantDos) {
                            List<CityDo> cityDos = merchantDo.getCityList();

                            for (CityDo cityDo : cityDos) {
                                HashMap<String, HashMap<String, String>> map1 = map.get(merchantDo.getMerchantName() + "-" + cityDo.getCityName());
                                HashMap<String, String> newItemsS = map1.get("createdOn");
                                HashMap<String, String> updateItemsS = map1.get("updatedOn");
                    %>


                    <tr>
                        <td><%=++i%>
                        </td>
                        <td><%=merchantDo.getMerchantName().substring(0, 1).toUpperCase() + merchantDo.getMerchantName().substring(1)%>
                            - <%=cityDo.getCityName().substring(0, 1).toUpperCase() + cityDo.getCityName().substring(1)%>
                        </td>
                        <% if (newItemsS != null && newItemsS.size() > 0) {
                        %>
                        <td><%=newItemsS.get("0") != null ? newItemsS.get("0") : 0%>
                        </td>
                        <td><%=newItemsS.get("1") != null ? newItemsS.get("1") : 0%>
                        </td>
                        <td><%=newItemsS.get("2") != null ? newItemsS.get("2") : 0%>
                        </td>
                        <%} else { %>
                        <td colspan="3"> No New Items</td>
                        <%
                            }
                            if (updateItemsS != null && updateItemsS.size() > 0) {
                        %>
                        <td><%=updateItemsS.get("0") != null ? updateItemsS.get("0") : 0%>
                        </td>
                        <td><%=updateItemsS.get("1") != null ? updateItemsS.get("1") : 0%>
                        </td>
                        <td><%=updateItemsS.get("2") != null ? updateItemsS.get("2") : 0%>
                        </td>

                        <%} else { %>
                        <td colspan="3"> No any Updates</td>
                        <%}%>
                    </tr>

                    <%
                            }
                        }
                    %>


                </table>
            </div>
        </div>


    </div>
</div>
</body>
</html>
