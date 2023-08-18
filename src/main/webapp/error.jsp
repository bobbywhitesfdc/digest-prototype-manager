<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Error Page</title>
</head>
<body>
<span style="font-color:red;font-weight:bold;font-size:18px;">Whoops! An Error Has Occurred</span>
<%
    if (request.getParameter("msg") != null) {
        out.println("<br/>Message: <b>"+request. getParameter("msg")+"</b><br/>");
    }
%>

<br><br>
<a href="${pageContext.request.contextPath}/login.jsp">Retry Login</a>

</body>
</html>