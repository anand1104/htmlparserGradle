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
    	
    	function changeMerchant(value1){    		
            window.location.href=  "offlineitem?merchantid="+value1;            
    	}
    	
    	$(document).ready(function() {
			$("#sbmtn").click(function(){
				var merchantid = document.getElementById("merchantid").value;
				if(merchantid==-1){
					alert('Please Select Any One Merchant');
					document.getElementById("merchantid").focus();
					return false;
				}
				
				var file = document.getElementById("file").value;

				if(file==''){
					alert('please upload xls file');
					document.getElementById("file").focus();
				}else{
				
					if(file.indexOf(".xls")==-1){
						alert('please upload only xls and xls File');
						document.getElementById("file").focus();
						return false;
					}
				}
				console.log("===ok==");
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
    	<c:set value="${requestScope.merchantId ne null ? requestScope.merchantId: 0}" var="merchantid"/>
    	
	    <div class="auto-1000">
			<div class="main-container">
				<div class="tab-container"> 
					<img style="float: left;width: 180px;height: 65px;" src="images/shopezzy.png">    
				  	<ul>
				  		<li><a id="surfexcel"  href="smry"  class="" ><span>Summary</span></a></li>
		                <li><a id="surfexcel" href="categorymap.jsp" class="" ><span>Category-Map</span></a></li>
		                <li><a id="surfexcel" href="selectCity.jsp" class="" ><span>Online Report</span></a></li>
		                <li><a id="surfexcel"  href="offlineitem"  class="active" ><span>Offline Report</span></a></li>   
						<li><a id="surfexcel"  href="itemlist"  class="" ><span>ItemList</span></a></li>				    	
				  	</ul>  
				</div>
				<div class="report-container">
					<h2>Offline Merchant Report and Upload Items</h2>
            <div class="input-form">
            	<form action="offlineitem" method="post"  enctype="multipart/form-data">
                	<table>
                		<c:if test="${requestScope.resp ne null}">
                			<tr>
                				<td colspan="2" style="font-size: large;color: red;">${requestScope.resp}</td>
                			</tr>
                		
                		</c:if>
                  		
                      	<tr>                        
	                        <td>Merchants</td>
                        	<td>
                        		<select name="merchantid" id="merchantid" onchange="changeMerchant(this.value)">
                        			<option value="-1">All</option>
                        			<c:if test="${requestScope.merchantDos ne null && requestScope.merchantDos.size()>0}">
                        				<c:forEach items="${requestScope.merchantDos}" var="merchantDo" >
                        					<option value="${merchantDo.merchantId}" <c:if test="${merchantid ne null && merchantid==merchantDo.merchantId}">selected="selected" </c:if> >${merchantDo.merchantName}</option>
                        				</c:forEach>
                        			</c:if>
                        		</select>
                        	</td>
                     	</tr>
                     	<tr>  
                     		<td>Upload</td>                      
	                        <td><input type="file" name="file" id="file"/></td>                        	
                      	</tr>
                      	<tr>                        
                        	<td colspan="2"><input type="Submit" id="sbmtn" value="Upload"></td>
                      	</tr>
                    </table> 
                  </form>                 
                </div>
				    <div class="ad-report" >
						<div style="font-size: large;color: red;">Total Items is ${totalCount}</div>
						<table id="mytable">
				 			<thead>
				 				
				            	<tr>
				            		<td width="5%">SNo.</td> 
				            		<td width="25%">Item Description</td>
				            		<td width="10%">Category</td>
				            		<td width="10%">Sub-Category</td>
				            		<td width="7%">Weight</td> 
				            		<td width="7%">MRP</td> 
				            		<td width="7%">Sell Price</td>  
				            		<td width="7%">Offer</td> 				            		
				            		<td width="15%">Merchant</td>				            		                         
				    			</tr>                		
				   			</thead> 
				   			
				   			<c:choose>
				   				<c:when test="${requestScope.offlineItemDos ne null && requestScope.offlineItemDos.size()>0}">
				   					<c:set var="counter" value="${recordperpage *curpage}" />
				    				<c:forEach items="${requestScope.offlineItemDos}" var="offlineItemDo" >
				    				<c:if test="${offlineItemDo.itemDetailDo.itemListDo ne null}">
				    					<tr>
						    				<td>${counter+1}</td>
						    				<td>${offlineItemDo.itemDetailDo.itemListDo.itemName}</td>
						    				<td>${offlineItemDo.itemDetailDo.itemListDo.shopezzySubCatDo.shopezzyCategoryDo.catName}</td>
						    				<td>${offlineItemDo.itemDetailDo.itemListDo.shopezzySubCatDo.subCatName}</td>
						    				<td>${offlineItemDo.itemDetailDo.weight}</td>
						    				<td>${offlineItemDo.itemDetailDo.mrp}</td>
						    				<td>${offlineItemDo.sellPrice}</td>
						    				<td>${offlineItemDo.offer}</td>						    				
						    				<td>${offlineItemDo.merchantDo.merchantName}</td>
					    				</tr>
					    					
				    					
				    				</c:if>	
				    				<c:set var="counter" value="${counter+1}" />			    				
				    			</c:forEach>
				   				
				   				</c:when>
				   				<c:otherwise>
				   						<tr>
						    				<td style="color: red;font-size: x-large;" colspan="9">Record Not Found</td>
						    			</tr>
				   				</c:otherwise>
				   			</c:choose>
				   						                     
				      	</table>
				    </div>          
				</div>			  
				<div class="col-xs-12">  			   
<%-- 					<c:if test="${totalCount!=0}"> --%>
<!-- 						<nav class="pull-left"><a href="ditemlist"> <img src='images/xlsx.png' style="height: 40px;width: 40px;"> </a></nav> -->
<%-- 					</c:if>                --%>
			 		<nav class="pull-right">
			        	<ul class="pagination">
			        		<c:if test="${curpagegroup>0}">			              
			        			<li>
			                    	<a href="offlineitem?pageno=${pageno-1}&curpagegroup=${curpagegroup -1}" aria-label="Previous">
			                        	<span aria-hidden="true">&laquo;</span>
			                    	</a>
			                  	</li>
			              	</c:if>
			              	<c:if test="${pageno+1<=totalpage-1}">
			              		<li><a href="offlineitem?pageno=${pageno-1}&curpagegroup=${curpagegroup}">${pageno+1}</a></li>
			              	</c:if>
			              	<c:if test="${pageno+1<=totalpage-1}">
			              		<li><a href="offlineitem?pageno=${pageno+1}&curpagegroup=${curpagegroup}&totalpage=0">${pageno+2}</a></li>
			              	</c:if>
			              	<c:if test="${pageno+2<=totalpage-1}">
			              		<li><a href="offlineitem?pageno=${pageno+2}&curpagegroup=${curpagegroup}&totalpage=0">${pageno+3}</a></li>
			              	</c:if>
			              	<c:if test="${pageno+3<=totalpage-1}">
			              		<li><a href="offlineitem?pageno=${pageno+3}&curpagegroup=${curpagegroup}&totalpage=0">${pageno+4}</a></li>
			              	</c:if>
			              	<c:if test="${pageno+4<=totalpage-1}">
			              		<li><a href="offlineitem?pageno=${pageno+4}&curpagegroup=${curpagegroup}&totalpage=0">${pageno+5}</a></li>
			              	</c:if>
			              	<c:if test="${pageno+5<=totalpage-1}">
			              		<li><a href="offlineitem?pageno=${pageno+5}&curpagegroup=${curpagegroup}&totalpage=0">${pageno+6}</a></li>
			              	</c:if>
			              	<c:if test="${pageno+6<=totalpage-1}">
			              		<li><a href="offlineitem?pageno=${pageno+6}&curpagegroup=${curpagegroup}&totalpage=0">${pageno+7}</a></li>
			              	</c:if>
			              	<c:if test="${pageno+7<=totalpage-1}">
			              		<li><a href="offlineitem?pageno=${pageno+7}&curpagegroup=${curpagegroup}&totalpage=0">${pageno+8}</a></li>
			              	</c:if>
			              	<c:if test="${pageno+8<=totalpage-1}">
			              		<li><a href="offlineitem?pageno=${pageno+8}&curpagegroup=${curpagegroup}&totalpage=0">${pageno+9}</a></li>
			              	</c:if>
			              	<c:if test="${pageno+9<=totalpage-1}">
			              		<li><a href="offlineitem?pageno=${pageno+9}&curpagegroup=${curpagegroup}&totalpage=0">${pageno+10}</a></li>
			              	</c:if>
			              	<c:if test="${resultpagegroup}">
			              		<li>
			      					<a href="offlineitem?pageno=${pageno+10}&curpagegroup=${curpagegroup+1}&totalpage=0" aria-label="Next">
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
