<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script>

	var invocation = new XMLHttpRequest();
	var url = 'http://bigbasket.com';
	var invocationHistoryText;

	function doSomething(){
		 if(invocation)
	        {
	            invocation.open('GET', url, true);
	            invocation.withCredentials = true;
	            invocation.onreadystatechange = handler;
	            invocation.send(); 
	        }
	        
	}
	
	function handler(evtXHR)
    {	
		alert('inside handler');
        if (invocation.readyState == 4){
        	
        	alert('inside ready state 4');
     		if (invocation.status == 200){
     			alert('inside status 200');
                var response = invocation.responseText;
                alert(response);
      		}else
            	alert("Invocation Errors Occured " + invocation.readyState+" | "+invocation.status+" | "+invocation.statusText);
	     		var response = invocation.responseText;
	            alert(response);
        }else{
           alert('not state 4');
        }
    }
</script>
</head>
<body>
	<button onclick="doSomething()">setCity</button>
</body>
</html>