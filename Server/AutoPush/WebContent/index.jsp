<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Test</title>
</head>
<body>

<form action="./PushResist" method="post">
<p>URL : <input type="text" name="url"></p>
<p>ID : <input type="text" name="id"></p>
<p>PASSWORD : <input type="password" name="pwd"></p>
<input type="submit" name="button">
</form>
<%
String path = (String)request.getAttribute("loginimg");
System.out.println(path);
if(path!=null&&(!path.equals(""))){%>
<img src="<%=path%>">
<%
}
%>


</body>
</html>