<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="bobby.prototype.digest.servlet.OAuthCallback" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Digest Results Page</title>
</head>
<body>
<%
    if (request.getParameter("msg") != null) {
        out.println("Message: <b>"+request. getParameter("msg")+"</b>");
    }
%>
<br/>

<a href="<% out.print(OAuthCallback.RUN_PAGE_RESOURCE); %>">Run</a>

</body>
</html>