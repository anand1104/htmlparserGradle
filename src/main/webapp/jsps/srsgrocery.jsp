<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>

<script type="text/javascript">
	
	function setCity(city){
		if(city != -1){		
		var http;
// 		url="http://www.srsgrocery.com/Handler/PinCodePopUpHandler.ashx?action=SUBMIT&PinCode=&MID=600f0371-3cf1-478b-a67e-c33fe2b5523b&IsLocCode=true&LocCode="+loccode+"&LocName="+city;
		url ="http://www.srsgrocery.com/SelectLocation.aspx?Location="+city+"&ReturnUrl=http://www.srsgrocery.com/";
		if(window.XMLHttpRequest){
			http = new XMLHttpRequest();
		}else{
			http=new ActiveXObject("Microsoft.XMLHTTP");
		}
		http.onreadystatechange = function() {
		    if(http.readyState == 4 && http.status == 200) {
		        alert(http.responseText);
		    }
		}		
		http.withCredentials=true;	
		http.open("GET", url, true);
		http.send();
		
		}else{
			alert('aaaaaaaaaaaaaaaaaaaa');
		}
	}
	
// 	Action:AddToCart
// 	Mid:600f0371-3cf1-478b-a67e-c33fe2b5523b
// 	ProductID:8049716
// 	Quantity:1
// 	VarProductID:4807170
// 	SelectedProducts:[]
// 	DeliveryMode:
// 	ShoppingListId:0
// 	OrderId:0
// 	ReorderItemIds:
// 	IsHTMLRequired:false
// 	w:1366
	
	function addtocart(pid,qty,vpid){
		var http;
		var url = "http://www.srsgrocery.com/Handler/ExpCheckOutHandler.ashx";
		var params = "Action=AddToCart&Mid=600f0371-3cf1-478b-a67e-c33fe2b5523b&ProductID="+pid+"&Quantity="+qty+"&VarProductID="+vpid+"&SelectedProducts=%5B%5D&DeliveryMode=&ShoppingListId=0&OrderId=0&ReorderItemIds=&IsHTMLRequired=false&w=1366";
		if(window.XMLHttpRequest){
			http = new XMLHttpRequest();
		}else{
			http=new ActiveXObject("Microsoft.XMLHTTP");
		}
		http.onreadystatechange = function() {
		    if(http.readyState == 4 && http.status == 200) {
		        alert(http.responseText);
		    }
		}		
		http.withCredentials=true;	
		http.open("POST", url, true);
		http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
// 		http.setRequestHeader("Access-Control-Allow-Origin", "*");
		http.send(params);
		alert('add to cart');
	}

	function deletefromcart(pid,qty,vpid){
		var http;
		var url = "http://www.srsgrocery.com/Handler/ExpCheckOutHandler.ashx";
		var params = "Action=DeleteFromCart&Mid=600f0371-3cf1-478b-a67e-c33fe2b5523b&Quantity=0&Key=172e006e-63b6-4714-b2f6-a71a549a0957&VarProductID="+vpid;
		if(window.XMLHttpRequest){
			http = new XMLHttpRequest();
		}else{
			http=new ActiveXObject("Microsoft.XMLHTTP");
		}
		http.onreadystatechange = function() {
		    if(http.readyState == 4 && http.status == 200) {
		        alert(http.responseText);
		    }else{
		    	alert(http.responseText);
		    }
		}		
		http.withCredentials=true;	
		http.open("POST", url, true);
		// http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
		// http.setRequestHeader("Origin", "http://www.srsgrocery.com");
		// http.setRequestHeader("Referer", "http://www.srsgrocery.com/");
// 		http.setRequestHeader("Access-Control-Allow-Origin", "*");
		http.send(params);
		alert('Delete From cart');
	}
	
	function rediection(){
		window.open("http://www.srsgrocery.com",'_blank');
	}

</script>
	
</head>
<body>

	<select name="city" id="city" onchange="setCity(this.value)">
		<option value="-1">--Please-Select--</option>
		<option value="13080">Gurgaon</option>
		<option value="13096">Noida/Indirapuram</option>
		<option value="13112">Faridabad</option>
		<option value="14044">South Delhi</option>
	</select>
	<br><br><br>	
	<button onclick="addtocart('8191336','1','4966442')">Add 8191336-4966442</button>
	<button onclick="deletefromcart('8191336','1','4966442')">Del 8191336-4966442</button>
	<!--<br><br>
	<button onclick="addtocart('8207560','2','4971630')">Add 8207560-4971630</button>
	<button onclick="deletefromcart('8207560','2','4971630')">Del 8207560-4971628</button>
	<br><br>
	<button onclick="addtocart('8207560','3','4971628')">Add 8207560-4971628</button>
	<button onclick="deletefromcart('8207560','3','4971628')">Del 8207560-4971628</button>-->
	<br><br><br>
	
<!-- 	<button onclick="addtocart('8816238','1','0')">add to cart</button> -->
	
	<button onclick="rediection()">redirect</button>
	
</body>
</html>