<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
	<script type="text/javascript">
	
		function dosomething(){
			var http;
			var url ="http://www.bigbasket.com/choose-city/?next=/";
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
			alert('Hit bigbasket.com');
		}
	
		function setCity(){
			dosomething();
			
			var places = "{\"city\":9,\"display_name\":\"Nirvana Country, Gurgaon\",\"name\":\"nirvana country, gurgaon\",\"hub\":58,\"area\":8888,\"pincode\":\"122017\",\"area_location\":[28.4124299,77.06652],\"city_name\":\"Delhi-NCR\",\"pincode_location\":[28.509238,77.04160399999999]}";
			
			var http;
			var url = "http://www.bigbasket.com/choose-city/";
			var params = "csrfmiddlewaretoken=422c722dca64be5624d18ab94b0136ac&city_id=9&address_id=&next=/?next=%2F&places="+places;
			console.log(params);
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
			http.setRequestHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			http.send(params);
			alert('set city');			
			
		}
		
		function redirection(){
			console.log("=======================================================");
			window.open("http://www.bigbasket.com",'_blank');
		}
		
	</script>
</head>
<body>
	<button onclick="setCity()">setCity</button>
	<button onclick="redirection()">redirect</button>
</body>
</html>