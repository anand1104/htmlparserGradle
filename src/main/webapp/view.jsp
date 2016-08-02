<!DOCTYPE html>
<%@page import="com.ahoy.parser.domain.MerchantSubCatDo"%>
<%@page import="com.ahoy.parser.domain.ParseDataDo"%>
<%@page import="com.ahoy.parser.domain.ShopezzySubCatDo"%>
<%@page import="com.ahoy.parser.util.UtilConstants"%>
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

    <script>

        function processverified(merchantId, cityId, listtype) {
            alert('Your Request is going to Processed');
            window.location = '<%=request.getContextPath()%>/pv?merchantid='+merchantId+'&cityid='+cityId+'&listtype='+listtype;
    }
    
    
    function changeStatus(value1,value2){
      
      
      if(document.getElementById('isstatus'+value1).checked==true){
            var xmlhttp;        
            if (window.XMLHttpRequest){
//            code for IE7+, Firefox, Chrome, Opera, Safari
                xmlhttp=new XMLHttpRequest();
          }else{
//            code for IE6, IE5
                xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
              }
            xmlhttp.onreadystatechange=function(){
                if(xmlhttp.readyState==4 && xmlhttp.status==200){

                  document.getElementById("notEditDiv"+value1).innerHTML=xmlhttp.responseText;
                  

                }
              }
            xmlhttp.open("GET","uc?id="+value1+"&status=1&idx="+value2,true);
            xmlhttp.send();
          }else{
            alert("You have already verified !!");
            document.getElementById('isstatus'+value1).checked=true;
            return false;
          }
      
      
       
      
    }
    
    function onSubmit(){
      var id = document.getElementById("id").value;
      var scat = document.getElementById("scat").value;
      var ssubcat = document.getElementById("ssubcat").value;
      var idx = document.getElementById("idx").value;

       var xmlhttp;        
        if (window.XMLHttpRequest){
//        code for IE7+, Firefox, Chrome, Opera, Safari
            xmlhttp=new XMLHttpRequest();
      }else{
//        code for IE6, IE5
            xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
          }
        xmlhttp.onreadystatechange=function(){
            if(xmlhttp.readyState==4 && xmlhttp.status==200){

              document.getElementById("notEditDiv"+id).innerHTML=xmlhttp.responseText;
              document.getElementById("edit-row"+id).remove();
              document.getElementById("notEditDiv"+id).style.display ="table-row";

            }
          }
        xmlhttp.open("GET","uc?id="+id+"&scat="+scat+"&ssubcat="+ssubcat+"&idx="+idx,true);
        xmlhttp.send(); 

    }
      
      function onChange(str){
        var xmlhttp;        
        if (window.XMLHttpRequest){
//        code for IE7+, Firefox, Chrome, Opera, Safari
            xmlhttp=new XMLHttpRequest();
      }else{
//        code for IE6, IE5
            xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
          }
        xmlhttp.onreadystatechange=function(){
            if(xmlhttp.readyState==4 && xmlhttp.status==200){
              
             document.getElementById("ssubcat").innerHTML=xmlhttp.responseText;
              
            }
          }
        xmlhttp.open("GET","jsps/div/subcat.jsp?id="+str,true);
        xmlhttp.send();       
      }
      
      
      </script>
    
    <script type="text/javascript">
    
    function globaledit(pkey,idx,status){
     
      var rowTag = document.getElementsByTagName("tr"); 
      var tableid = document.getElementById("mytable");
      
      if(status==0 || status==1){
        for(var i=2; i<rowTag.length; i++){
              if(i<rowTag.length){
                if(rowTag[i].getAttribute("id").indexOf("edit-row")!=-1){
                  tableid.deleteRow(i);
                  
                }
                if(rowTag[i].style.display=="none"){
                  rowTag[i].style.display="table-row";
                }
                
              }

              if(idx%50== (i-2)){
                var row = tableid.insertRow(i);
                row.id="edit-row"+pkey;
              }
            }
              document.getElementById("notEditDiv"+pkey).style.display="none";
              
              var xmlhttp;        
              if (window.XMLHttpRequest){
//              code for IE7+, Firefox, Chrome, Opera, Safari
                  xmlhttp=new XMLHttpRequest();
            }else{
//              code for IE6, IE5
                  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
                }
              xmlhttp.onreadystatechange=function(){
                  if(xmlhttp.readyState==4 && xmlhttp.status==200){
                    document.getElementById("edit-row"+pkey).innerHTML=xmlhttp.responseText;
                  }
                }
              xmlhttp.open("GET","jsps/div/edit.jsp?id="+pkey+"&idx="+idx,true);
              xmlhttp.send(); 
      }else{
        
      alert("You have Processed, You can't change status");
      return false;
       
        
      }            
      }
    </script>
          
  </head> 
  <body>
    <%
      List<ParseDataDo>  parseDataDos=(List<ParseDataDo>)request.getAttribute("parseDataDos");
      long totalCount = Long.valueOf(request.getAttribute("totalCount")!=null&&request.getAttribute("totalCount").toString().matches("[0-9]+")?request.getAttribute("totalCount").toString():"0");
      String merchantId = (String)request.getAttribute("merchantid");
      String listtype = (String)request.getAttribute("listtype");
      String cityid = (String)request.getAttribute("cityid");
      
      String merchantName = (String)request.getAttribute("merchantName");
      String cityName = (String)request.getAttribute("cityName");
      
      long pageno = Long.valueOf(request.getAttribute("pageno")!=null&&request.getAttribute("pageno").toString().matches("[0-9]+")?request.getAttribute("pageno").toString():"0");
      long curpage = Long.valueOf(request.getAttribute("curpage")!=null&&request.getAttribute("curpage").toString().matches("[0-9]+")?request.getAttribute("curpage").toString():"0");
      long curpagegroup = Long.valueOf(request.getAttribute("curpagegroup")!=null&&request.getAttribute("curpagegroup").toString().matches("[0-9]+")?request.getAttribute("curpagegroup").toString():"0");
      long totalpage = Long.valueOf(request.getAttribute("totalpage")!=null&&request.getAttribute("totalpage").toString().matches("[0-9]+")?request.getAttribute("totalpage").toString():"0");
      boolean resultpagegroup = Boolean.valueOf(request.getAttribute("resultpagegroup")!=null&&!"null".equalsIgnoreCase(request.getAttribute("resultpagegroup").toString())?request.getAttribute("resultpagegroup").toString():"false");
    %>
    <div class="auto-1000">
      <div class="main-container">
          <div class="tab-container"> 
            <img style="float: left;width: 180px;height: 65px;" src="images/shopezzy.png">    
              <ul>
                <li><a id="surfexcel"  href="#"  class="active" ><span><%=merchantName.toUpperCase()%> - <%=cityName.toUpperCase()%></span></a></li>
                <li><a id="surfexcel"  href="smry"  class="" ><span>Summary</span></a></li> 
                <li><a id="surfexcel" href="categorymap.jsp" class="" ><span>Category-Map</span></a></li>
<!--                 <li><a id="surfexcel"  href="offlineitem"  class="" ><span>Offline Report</span></a></li>   -->
<!--                 <li><a id="surfexcel"  href="itemlist"  class="" ><span>ItemList</span></a></li>   -->
                <li><a id="surfexcel" href="selectCity.jsp" class="" ><span>BACK</span></a></li>
              </ul>  
            </div>
            <div class="report-container">
              <h2>Today's <%if(listtype.equals("1")){%>New<%}else if(listtype.equals("2")){ %>Update<%}else{%>Total<%}%> Items</h2>
                <div class="ad-report">                   
                    <ul style="list-style-type: none;margin-bottom: 41px;">
                      <li style="float: left;color: #E48810;font-size: large;margin-left: -40px;">Total Items is <%=totalCount%></li>
                      <%if(parseDataDos !=null && parseDataDos.size()>0){ %>
                      <li style="float: right;font-size: large;"><a href="javascript:processverified(<%=merchantId %>,<%=cityid%>,<%=listtype%>)" style="background: #25ad53;padding: 7px;color:white;">Process-Verified</a></li>
                      <%} %>
                    </ul>                   
                    <table id="mytable">
                      <thead>
                        <tr>
                          <td width="5%" rowspan="2" >SNo.</td> 
                            <td width="17%" rowspan="2">Description</td>
                            <td width="8%" rowspan="2">Weight</td>
                            <td width="8%" rowspan="2">Max Price</td>
                            <td width="8%" rowspan="2">sell Price</td>
                            <td width="10%" rowspan="2">Offers</td>
                        <td  colspan="2" >Shopezzy</td> 
                            <td width="5%" rowspan="2">Edit</td>
                            <td width="5%" rowspan="2">Status</td>
                        </tr>  
                    <tr>                  
                        <td width="15%">Category</td>                           
                            <td width="15%">Sub Category</td>
                </tr>
                      </thead> 
<%  
            
              if(parseDataDos !=null && parseDataDos.size()>0){
                long i =UtilConstants.RECORD_PER_PAGE*curpage;
                for(ParseDataDo parseDataDo : parseDataDos){
                  MerchantSubCatDo merchantSubCatDo = parseDataDo.getMerchantSubCatDo();
                  ShopezzySubCatDo shopezzySubCatDo = parseDataDo.getShopezzySubCatDo();
                  
                  String  catName = shopezzySubCatDo.getShopezzyCategoryDo().getCatName();
                  String subCatName = shopezzySubCatDo.getSubCatName();
                
 %>
                <tr id="notEditDiv<%=parseDataDo.getDataPkey()%>">
                  <td><%=i+1%></td>                   
                  <td title="<%=parseDataDo.getDescription() %>"><%=parseDataDo.getDescription()%></td>
                  <td title="<%=parseDataDo.getWeight() %>"><%=parseDataDo.getWeight() %></td>
                  <td title="<%=parseDataDo.getMaxPrice() %>"><%=parseDataDo.getMaxPrice() %></td>
                  <td title="<%=parseDataDo.getSellPrice() %>"><%=parseDataDo.getSellPrice() %></td>
                  <td title="<%=parseDataDo.getOffer()%>"><%=parseDataDo.getOffer()%></td>
                  <td title="<%=catName%>"><%=catName.length()>20?(catName.substring(0,1).toUpperCase()+catName.substring(1, 18)+ ""):(catName.substring(0,1).toUpperCase()+catName.substring(1)) %></td>
                  <td title="<%=subCatName%>"><%=subCatName.length()>20?subCatName.substring(0,1).toUpperCase()+subCatName.substring(1, 18)+ "" :subCatName.substring(0,1).toUpperCase()+subCatName.substring(1) %></td>
                  <td><a href="javascript:globaledit(<%=parseDataDo.getDataPkey()%>,<%=i%>,<%=parseDataDo.getStatus()%>)"><i class="glyphicon glyphicon-edit" <%if(parseDataDo.getStatus()==2){%> style="color: #25ad53;" <%}%> ></i></a></td>
                  <td>
                    <div class="input-group status">
                      <% if(parseDataDo.getStatus()==0){ %>
                        <input type="checkbox" name="isstatus<%=parseDataDo.getDataPkey()%>" id="isstatus<%=parseDataDo.getDataPkey()%>" onchange="changeStatus(<%=parseDataDo.getDataPkey()%>,<%=i%>)">
                          <label for="disable">Pending</label>
                        <%}else{
                          if(parseDataDo.getStatus()==1){
                        %>                          
                            <input type="checkbox" name="isstatus<%=parseDataDo.getDataPkey()%>" id="isstatus<%=parseDataDo.getDataPkey()%>" checked="checked" onchange="changeStatus(<%=parseDataDo.getDataPkey()%>,<%=i%>)">
                            <label for="disable">Verified</label>
                        <%  }else{
                        %>
                            <input type="checkbox" name="isstatus<%=parseDataDo.getDataPkey()%>" id="isstatus<%=parseDataDo.getDataPkey()%>" checked="checked" onchange="changeStatus(<%=parseDataDo.getDataPkey()%>,<%=i%>)">
                            <label for="disable">Processed</label>
                        <%  }
                        } %>
                   </div>                   
                  </td>
                </tr>     
                                            
<%            i++;  }
                  }else{
%>
                <tr>
                  <td colspan="9" style="font-size: large;color: red;"> No New Updates</td>
                </tr>   
<%              
                  } 
%>
                                 
                  </table>
                </div>          
            </div>
            
            <div class="col-xs-12">  
              <%if(totalCount!=0){ %>
                <nav class="pull-left">
                  <a href="dcsv?merchantid=<%=merchantId%>&listtype=<%=listtype%>&cityid=<%=cityid%>"> <img src='images/excel.png'> </a>
                </nav>
                <%} %>              
                  <nav class="pull-right">
                      <ul class="pagination">
                        <%if(curpagegroup >0){%>
                            <li>
                              <a href="view?merchantid=<%=merchantId%>&listtype=<%=listtype%>&cityid=<%=cityid%>&pageno=<%=pageno-1%>&curpagegroup=<%=curpagegroup -1%>" aria-label="Previous">
                                  <span aria-hidden="true">&laquo;</span>
                              </a>
                            </li>
            <%}if(pageno+1<=totalpage-1){%>
              <li><a href="view?merchantid=<%=merchantId%>&listtype=<%=listtype%>&cityid=<%=cityid%>&pageno=<%=pageno%>&curpagegroup=<%=curpagegroup%>"><%=pageno+1%></a></li>
            <%}if(pageno+1<=totalpage-1){ %>
              <li><a href="view?merchantid=<%=merchantId%>&listtype=<%=listtype%>&cityid=<%=cityid%>&pageno=<%=pageno+1%>&curpagegroup=<%=curpagegroup%>&totalpage=0"><%=pageno+2%></a></li>
            <%}if(pageno+2<=totalpage-1){ %>
              <li><a href="view?merchantid=<%=merchantId%>&listtype=<%=listtype%>&cityid=<%=cityid%>&pageno=<%=pageno+2%>&curpagegroup=<%=curpagegroup%>&totalpage=0"><%=pageno+3%></a></li>
            <%}if(pageno+3<=totalpage-1){%>
              <li><a href="view?merchantid=<%=merchantId%>&listtype=<%=listtype%>&cityid=<%=cityid%>&pageno=<%=pageno+3%>&curpagegroup=<%=curpagegroup%>&totalpage=0"><%=pageno+4%></a></li>
            <%}if(pageno+4<=totalpage-1){%>
              <li><a href="view?merchantid=<%=merchantId%>&listtype=<%=listtype%>&cityid=<%=cityid%>&pageno=<%=pageno+4%>&curpagegroup=<%=curpagegroup%>&totalpage=0"><%=pageno+5%></a></li>
            <%}if(pageno+5<=totalpage-1){%>
              <li><a href="view?merchantid=<%=merchantId%>&listtype=<%=listtype%>&cityid=<%=cityid%>&pageno=<%=pageno+5%>&curpagegroup=<%=curpagegroup%>&totalpage=0"><%=pageno+6%></a></li>
            <%}if(pageno+6<=totalpage-1){%>
              <li><a href="view?merchantid=<%=merchantId%>&listtype=<%=listtype%>&cityid=<%=cityid%>&pageno=<%=pageno+6%>&curpagegroup=<%=curpagegroup%>&totalpage=0"><%=pageno+7%></a></li>
            <%}if(pageno+7<=totalpage-1){%>
              <li><a href="view?merchantid=<%=merchantId%>&listtype=<%=listtype%>&cityid=<%=cityid%>&pageno=<%=pageno+7%>&curpagegroup=<%=curpagegroup%>&totalpage=0"><%=pageno+8%></a></li>
            <%}if(pageno+8<=totalpage-1){%>
              <li><a href="view?merchantid=<%=merchantId%>&listtype=<%=listtype%>&cityid=<%=cityid%>&pageno=<%=pageno+8%>&curpagegroup=<%=curpagegroup%>&totalpage=0"><%=pageno+9%></a></li>
            <%}if(pageno+9<=totalpage-1){%>
              <li><a href="view?merchantid=<%=merchantId%>&listtype=<%=listtype%>&cityid=<%=cityid%>&pageno=<%=pageno+9%>&curpagegroup=<%=curpagegroup%>&totalpage=0"><%=pageno+10%></a></li>
            <%}if(resultpagegroup==true){%>
               <li>
                <a href="view?merchantid=<%=merchantId%>&listtype=<%=listtype%>&cityid=<%=cityid%>&pageno=<%=pageno+10%>&curpagegroup=<%=curpagegroup+1%>&totalpage=0" aria-label="Next">
                                <span aria-hidden="true">&raquo;</span>
                              </a>
                             </li>
            <% }%>
                        </ul>
                  </nav>
                </div>
          </div>  
    </div>    
  </body> 
</html>
