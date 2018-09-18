<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <title>AJAX跨域请求测试</title>
</head>
<body>
  <input type='button' value='开始测试' onclick='crossDomainRequest()' />
  <div id="content"></div>

  <script type="text/javascript">
    //<![CDATA[
   	var url = 'http://10.148.83.221:10190/bigdata/access/control/user/all';
   	fetch(url, {
   		method: 'POST',
   		cache: 'no-cache',
   		mode: 'cros',
   		headers: {
   			
   		},
   	})
   	.then(res => {
   		return res.json()
   	})
   	.then(data => {
   		console.log(data)
   	})
    //]]>
  </script>

</body>
</html>