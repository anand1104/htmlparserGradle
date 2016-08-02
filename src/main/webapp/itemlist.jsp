<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>Dashboard</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

    <link rel="stylesheet" type="text/css" href="css/style.css">
    <link rel="stylesheet" type="text/css" href="css/font-awesome.css">
    <link rel="stylesheet" type="text/css" href="css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css" media="screen">

    <script type="text/javascript" src="js/jquery.min.js"></script>
    <script src="js/jquery-1.9.0.js"></script>
    <script src="js/jquery-ui-1.10.0.custom.js"></script>  

    <script type="text/javascript">
    $(document).ready(function() {
		$("#sbmtn").click(function(){
			var file = document.getElementById("file").value;
			if(file==''){
				alert('Please Upload Excel file');
				document.getElementById("file").focus();
				return false;
			}else{
				if(file.indexOf(".xls")==-1){
					alert('Please Upload only .xls or .xlsx file');
					document.getElementById("file").focus();
					return false;
				}
			}			
			return true;
		});
	});
    
    </script>   
    
          
  </head> 
	<body>
    
		<c:set value="${requestScope.totalCount ne null ? requestScope.totalCount :0}" var="totalCount"/>
		<c:set value="${requestScope.pageno ne null ? requestScope.pageno :0}" var="pageno"/>
		<c:set value="${requestScope.curpage ne null ? requestScope.curpage :0}" var="curpage"/>
		<c:set value="${requestScope.curpagegroup ne null ? requestScope.curpagegroup :0}" var="curpagegroup"/>
		<c:set value="${requestScope.totalpage ne null ? requestScope.totalpage :0}" var="totalpage"/>
		<c:set value="${requestScope.resultpagegroup ne null ? requestScope.resultpagegroup :false}" var="resultpagegroup"/>
		<c:set value="${requestScope.recordperpage}" var="recordperpage"/>
    
	    <div class="auto-1000">
			<div class="main-container">
				<div class="tab-container"> 
					<img style="float: left;width: 180px;height: 65px;" src="images/shopezzy.png">    
				  	<ul>
				  		<li><a id="surfexcel"  href="smry"  class="" ><span>Summary</span></a></li>
		                <li><a id="surfexcel" href="categorymap.jsp" class="" ><span>Category-Map</span></a></li>
		                <li><a id="surfexcel" href="selectCity.jsp" class="" ><span>Online Report</span></a></li>
		                <li><a id="surfexcel"  href="offlineitem"  class="" ><span>Offline Report</span></a></li>  
						<li><a id="surfexcel"  href="itemlist"  class="active" ><span>ItemList</span></a></li>				    	
				  	</ul>  
				</div>
				<div class="report-container">
					<h2>Items List for Offline Merchant</h2>
            <div class="input-form">
            	<form action="itemlist" method="post"  enctype="multipart/form-data">
                	<table>
                		<c:if test="${requestScope.resp ne null}">
                			<tr>
                				<td colspan="2" style="font-size: large;color: red;">${requestScope.resp}</td>
                			</tr>
                		
                		</c:if>
                  		<tr>                        
	                        <td><input type="file" name="file" id="file"/></td>
                        	<td><input type="Submit" id="sbmtn" value="Upload"></td>
                      </tr>
                    </table> 
                  </form>                 
                </div>
				    <div class="ad-report">
						<div style="font-size: large;color: red;">Total Items is ${totalCount}</div>
						<table id="mytable">
				 			<thead>
				 				
				            	<tr>
				            		<td width="10%">SNo.</td> 
				            		<td width="40%">Item Description</td>
				            		<td width="15%">Category</td>
				            		<td width="15%">Sub-Category</td>
				            		<td width="10%">Weight</td> 
				            		<td width="10%">MRP</td>                           
				    			</tr>                		
				   			</thead> 
							<c:if test="${requestScope.itemListDos ne null && requestScope.itemListDos.size()>0}">
								<c:set var="counter" value="${recordperpage *curpage}" />
				    			<c:forEach items="${requestScope.itemListDos}" var="itemList" >
				    				<c:if test="${itemList.itemDetailDos ne null && itemList.itemDetailDos.size()>0 }">
				    					<c:forEach items="${itemList.itemDetailDos}" var="itemDetail" >
					    					<tr>
						    					<td>${counter+1}</td>
						    					<td >${itemList.itemName}</td>
						    					<td>${itemList.shopezzySubCatDo.shopezzyCategoryDo.catName}</td>
						    					<td>${itemList.shopezzySubCatDo.subCatName}</td>
						    					<td>${itemDetail.weight}</td>
						    					<td>${itemDetail.mrp}</td>
					    					</tr>
					    					<c:set var="counter" value="${counter+1}" />
				    					</c:forEach>
				    				</c:if>				    				
				    			</c:forEach>
				    		</c:if>				                     
				      	</table>
				    </div>          
				</div>			  
				<div class="col-xs-12">  			   
					<c:if test="${totalCount!=0}">
						<nav class="pull-left"><a href="ditemlist"> <img src='images/xlsx.png' style="height: 40px;width: 40px;"> </a></nav>
					</c:if>               
			 		<nav class="pull-right">
			        	<ul class="pagination">
			        		<c:if test="${curpagegroup>0}">			              
			        			<li>
			                    	<a href="itemlist?pageno=${pageno-1}&curpagegroup=${curpagegroup -1}" aria-label="Previous">
			                        	<span aria-hidden="true">&laquo;</span>
			                    	</a>
			                  	</li>
			              	</c:if>
			              	<c:if test="${pageno+1<=totalpage-1}">
			              		<li><a href="itemlist?pageno=${pageno-1}&curpagegroup=${curpagegroup}">${pageno+1}</a></li>
			              	</c:if>
			              	<c:if test="${pageno+1<=totalpage-1}">
			              		<li><a href="itemlist?pageno=${pageno+1}&curpagegroup=${curpagegroup}&totalpage=0">${pageno+2}</a></li>
			              	</c:if>
			              	<c:if test="${pageno+2<=totalpage-1}">
			              		<li><a href="itemlist?pageno=${pageno+2}&curpagegroup=${curpagegroup}&totalpage=0">${pageno+3}</a></li>
			              	</c:if>
			              	<c:if test="${pageno+3<=totalpage-1}">
			              		<li><a href="itemlist?pageno=${pageno+3}&curpagegroup=${curpagegroup}&totalpage=0">${pageno+4}</a></li>
			              	</c:if>
			              	<c:if test="${pageno+4<=totalpage-1}">
			              		<li><a href="itemlist?pageno=${pageno+4}&curpagegroup=${curpagegroup}&totalpage=0">${pageno+5}</a></li>
			              	</c:if>
			              	<c:if test="${pageno+5<=totalpage-1}">
			              		<li><a href="itemlist?pageno=${pageno+5}&curpagegroup=${curpagegroup}&totalpage=0">${pageno+6}</a></li>
			              	</c:if>
			              	<c:if test="${pageno+6<=totalpage-1}">
			              		<li><a href="itemlist?pageno=${pageno+6}&curpagegroup=${curpagegroup}&totalpage=0">${pageno+7}</a></li>
			              	</c:if>
			              	<c:if test="${pageno+7<=totalpage-1}">
			              		<li><a href="itemlist?pageno=${pageno+7}&curpagegroup=${curpagegroup}&totalpage=0">${pageno+8}</a></li>
			              	</c:if>
			              	<c:if test="${pageno+8<=totalpage-1}">
			              		<li><a href="itemlist?pageno=${pageno+8}&curpagegroup=${curpagegroup}&totalpage=0">${pageno+9}</a></li>
			              	</c:if>
			              	<c:if test="${pageno+9<=totalpage-1}">
			              		<li><a href="itemlist?pageno=${pageno+9}&curpagegroup=${curpagegroup}&totalpage=0">${pageno+10}</a></li>
			              	</c:if>
			              	<c:if test="${resultpagegroup}">
			              		<li>
			      					<a href="itemlist?pageno=${pageno+10}&curpagegroup=${curpagegroup+1}&totalpage=0" aria-label="Next">
			                      		<span aria-hidden="true">&raquo;</span>
			                    	</a>
			                   </li>			              	
			              	</c:if>			 				 
			              </ul>
			        </nav>
			      </div>
			</div>  
	    </div>    
	</body> 
</html>
