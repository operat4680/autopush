<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Test</title>
</head>
<body>

<input type="button" name="button" onclick=getUrl() />
<iframe name='test' id='i' src='http://www.daum.net'>

</iframe>
<!--
<form action="./PushResist" method="post">
<p>URL : <input type="text" name="url"></p>
<p>ID : <input type="text" name="id"></p>
<p>PASSWORD : <input type="password" name="pwd"></p>
<input type="submit" name="button">
</form>
-->
<script type="text/javascript">
var win = document.getElementById('i').contentWindow;
window.addEventListener('message',function(e){
	var message = e.origin;
	alert(message);
});
function getUrl(){
	try{
	win.postMessage('hi',"http://211.189.127.143/AutoPush/index.jsp");
	}catch(e){
		alert(e);
	}
}

</script>



</body>
</html>