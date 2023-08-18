<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Chatter Digest Prototype Options Page</title>
</head>
<body>

<table style="width:600px;border:1px solid black;padding:15px;">
   <caption><b>User Information</b></caption>
   <tbody>
       <tr>
       <td align="right">Name:</td><td>${connection.displayName}</td>
       </tr>
       <tr>
       <td align="right">Username:</td><td>${connection.username}</td>
       </tr>
       <tr>
       <td align="right">Email:</td><td>${connection.email}</td>
       </tr>
       <tr>
       <td align="right">UserType:</td><td>${connection.userType}</td>
       </tr>
       
       <tr>
       <td align="right">Organization:</td><td>${connection.organizationName}</td>
       </tr>
       <tr>
       <td align="right">Id:</td><td>${connection.organizationId}</td>
       </tr>  
       <tr>
       <td align="right">URL:</td><td>${connection.loginUrl}</td>
       </tr>     
          
            
   </tbody>
</table>

<form action="${pageContext.request.contextPath}/OAuthCallback">
<table style="width:600px;border:1px solid black">
<caption><b>Select Community</b></caption>
<tbody>
 
 <c:forEach var="network" items="${connection.networkScopes}" varStatus="counter" >
    <tr>
        <td><input type="radio" name="network" value="${network.networkId}" title="<c:out value="${network.description}" />" <c:if test="${counter.index==0}">checked</c:if> ><c:out value="${network.name}"/></<input></td>
    </tr>
 </c:forEach>
 <!-- 
     <tr><td align="right">Feed:</td>
     <td>
       <select name="feedOption">
           <c:forEach var="feed" items="${connection.availableFeeds}" varStatus="counter" >
               <option value="${feed.feedElementsUrl}" title="${feed.feedElementsUrl}">${feed.label}</option>
           </c:forEach>
       
       </select>
       </td>
    </tr>
 -->   
 
 
 <tr style="height:60px;">
 <td align="right" padding="10px">
    <input type="submit" name="optionsPage1" value="Previous"  title="Return to the Login Page to pick another Org"/>
 </td>
 <td align="left" padding="10px"><input type="submit" name="optionsPage1" value="Next" title="Advance to the Next Page to set Advanced Options"/></td>
 </tr>
</tbody>
</table>
</form>
</body>
</html>