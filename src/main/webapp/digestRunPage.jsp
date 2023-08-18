<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Chatter Digest Prototype Run Page</title>
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
       <td align="right">Network:</td><td>${connection.newsFeedOptions.network}</td>
       </tr>  
        
       <tr>
       <td align="right">URL:</td><td>${connection.loginUrl}</td>
       </tr>     
          
            
   </tbody>
</table>

<form action="${pageContext.request.contextPath}/OAuthCallback">
<table style="width:600px;border:1px solid black">
<caption><b>Summary of Digest Options</b></caption>
<tbody>
 <tr><td align="right">Duration:</td>
 <td>${connection.newsFeedOptions.frequency.label}</td>
 </tr>
 
 <tr><td align="right">Updates:</td>
 <td>${connection.newsFeedOptions.depth.label}</td>
 </tr>
 
 <tr><td align="right">Feed Item Count:</td>
 <td>${connection.newsFeedOptions.pageSize}</td>
 </tr>
 
  <tr><td align="right">Max Comments:</td>
 <td>${connection.newsFeedOptions.recentCommentCount}
</td>
 </tr>
<tr>
     <td align="right">Feed:</td>
     <td>${connection.selectedFeed}
         <br>${connection.newsFeedOptions.feedResourceOverride}</td>
</tr>
<tr><td align="right">Feed Filter:</td><td>${connection.newsFeedOptions.filter.label}</td>
</tr>
 
 
 <tr style="height:60px;">
 <td align="right" padding="10px">
    <input type="submit" name="run" value="Previous"  title="Return to the second options Page to change settings"/>
 </td>
 <td align="left" padding="10px"><input type="submit" name="run" value="Run" title="Run the Digest with the options you've selected"/></td>
 </tr>
</tbody>
</table>
</form>
</body>
</html>