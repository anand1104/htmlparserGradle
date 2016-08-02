<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>

<script type="text/javascript">
	
	function callscript(pid,qty){
		var http;
		var url = "http://shopration.com/index.php?route=checkout/cart/add";
		var params = "product_id="+pid+"&quantity="+qty;
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
		alert('Hello');
		
	}
	
	function rediection(){
		window.open("http://shopration.com",'_blank');
	}

</script>

</head>
<body>
	<button onclick="callscript('1228','1')">1228</button>
	<button onclick="callscript('68'),'1'">68</button>
	<button onclick="callscript('1364','2')">1364</button>
	<button onclick="callscript('1363','1')">1363</button>
	<button onclick="callscript('1039','1')">1039</button>	
	<button onclick="rediection()">redirect</button>
</body>
</html>