<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>

<script type="text/javascript">
function setCity(){
	var http;
	var url = "http://www.meragrocer.com/Handler/PinCodePopUpHandler.ashx?action=SUBMIT&PinCode=&MID=0147a52a-3272-4f4f-8fce-241dbbfb3ed5&IsLocCode=true&LocCode=Gurgaon&LocName=Gurgaon";
	
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
	alert('City Set');	
}

function addtocart(pid,qty,vpid){
	var http;
	var url = "http://www.meragrocer.com/Handler/ExpCheckOutHandler.ashx";
	var params = "Action=AddToCart&Mid=0147a52a-3272-4f4f-8fce-241dbbfb3ed5&ProductID="+pid+"&Quantity="+qty+"&VarProductID="+vpid+"&SelectedProducts=[]";
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
	http.send(params);
	alert('add to cart');
}

function rediection(){
	window.open("http://meragrocer.com",'_blank');
}
</script>
</head>
<body>
	<button onclick="setCity()">set City</button>
	<button onclick="addtocart('8244658','1','5020192')">add to cart</button>
	<button onclick="addtocart('8247650','1','5025202')">add to cart</button>
	<button onclick="rediection()">redirect</button>
</body>
</html>