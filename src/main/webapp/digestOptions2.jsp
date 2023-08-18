<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Chatter Digest Prototype Options Page 2</title>
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
<caption><b>Advanced Digest Options</b></caption>
<tbody>
 <tr><td align="right">Duration:</td>
 <td>
 <select name="frequency">
 <option value="W">Weekly</option>
 <option value="D">Daily</option>
 <option value="E">Your full feed, not limited</option>
 </select></td></tr>
 
 <tr><td align="right">Updates:</td>
 <td>
  <select name="depth">
    <option value="F" title="Fewer updates will exclude field tracked changes and reduce the number of posts">Fewer Updates</option>
    <option value="A" title="All updates in your news feed">All Updates</option>
 </select></td>
 </tr>
 
 <tr><td align="right">Feed Item Count:</td>
 <td><select name="pageSize">
 <option value="100" title="100 is the maximum number of Items that can be requested">100</option>
 <option value="75" title="75">75</option>
 <option value="50" title="50">50</option>
 <option value="25" title="25">25</option>
 </select></td>
 </tr>
 
  <tr><td align="right">Max Comments:</td>
 <td>
 <select name="commentCount">
  <option value="0" title="No comments">0</option>
  <option value="3" title="3 is the current default number of comments">3</option>
  <option value="10" title="10">10</option>
  <option value="25" title="25">25</option>
 </select></td>
 </tr>

 
     <tr><td align="right">Feed:</td>
     <td>
       <select name="feedOption">
           <c:forEach var="feed" items="${connection.availableFeeds}" varStatus="counter" >
               <option value="${feed.feedElementsUrl}" title="${feed.feedElementsUrl}">${feed.label}</option>
           </c:forEach>
       
       </select>
       </td>
    </tr>
    
     <tr><td align="right">Feed Filter:</td>
	 <td><select name="filter">
	 <option value="None" title="None">None</option>
	 <option value="AllQuestions" title="All Questions">All Questions</option>
	 <option value="SolvedQuestions" title="Solved Questions">Solved Questions</option>
	 <option value="UnansweredQuestions" title="Unanswered Questions">Unanswered Questions</option>
	 </select></td>
 </tr>
 
    
 
 
 <tr style="height:60px;">
 <td align="right" padding="10px">
    <input type="submit" name="optionsPage2" value="Previous"  title="Return to the first Options Page to change settings"/>
 </td>
 <td align="left" padding="10px"><input type="submit" name="optionsPage2" value="Next" title="Run the Digest with the options you've selected"/></td>
 </tr>
</tbody>
</table>
</form>
</body>
</html>