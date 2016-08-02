<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Natures Basket</title>

	<script type="text/javascript">
		function setCity(city){
			var loccode='';
			if(city=='Mumbai')
				loccode='01'+city;
			else if(city=='Pune')
				loccode='02'+city;
			else if(city=='Bengaluru')
				loccode='03'+city;
			else if(city=='Delhi')
				loccode='04'+city;
			else if(city=='Gurgaon')
				loccode='05'+city;
			else if(city=='Noida')
				loccode='06'+city;
			else if(city=='Hyderabad')
				loccode='07'+city;
			else
				return false;	
			
			var http;
			url="http://www.naturesbasket.co.in/Handler/PinCodePopUpHandler.ashx?action=SUBMIT&PinCode=&MID=23c09df6-eb5f-49c2-b8a7-48a42be26ed9&IsLocCode=true&LocCode="+loccode+"&LocName="+city;
			
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
			alert('City Set '+city);
		}
		
		function addtocart(pid,qty,vpid){
			var http;
			var url = "http://www.naturesbasket.co.in/Handler/ExpCheckOutHandler.ashx";
			var params = "Action=AddToCart&Mid=23c09df6-eb5f-49c2-b8a7-48a42be26ed9&ProductID="+pid+"&Quantity="+qty+"&VarProductID="+vpid+"&SelectedProducts=[]";
			
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
			window.open("http://www.naturesbasket.co.in",'_blank');
		}
	</script>

</head>
<body>
	<select name="city" id="city" onchange="setCity(this.value)">
		<option value="Mumbai" >Mumbai</option>
		<option value="Pune" >Pune</option>
		<option value="Bengaluru" >Bengaluru</option>
		<option value="Delhi" >Delhi</option>
		<option value="Gurgaon" >Gurgaon</option>
		<option value="Noida" >Noida</option>
		<option value="Hyderabad" >Hyderabad</option>
	</select>
	<button onclick="addtocart('7865598','1','0')">add to cart</button>
	<button onclick="addtocart('7865368','1','0')">add to cart</button>
	<button onclick="addtocart('7865410','1','0')">add to cart</button>
	<button onclick="rediection()">redirect</button>


</body>

	
	
</html>