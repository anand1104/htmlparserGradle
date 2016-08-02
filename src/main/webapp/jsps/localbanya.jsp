<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript">

	function addtocart(pid,qty,weightcode){
		var http;
		var url = "http://www.localbanya.com/cart/add";
		var params = "prod_id="+pid+"&weight_code="+weightcode+"&quantity="+qty;
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
		window.open("http://www.localbanya.com",'_blank');
	}

</script>

</head>
<body>
	<button onclick="addtocart('18491','1','a')">add to cart</button>
	<button onclick="addtocart('18738','1','a')">add to cart</button>
	<button onclick="rediection()">redirect</button>
</body>
</html>