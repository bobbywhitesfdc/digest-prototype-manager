<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Digest Maker Login Page</title>
</head>
<body>

<form action="${pageContext.request.contextPath}/OAuthCallback">

	<table style="width:600px;border:solid 1px black;">
	  <caption><b>Chatter Digest Prototype Authorization</b></caption>
	   <tbody>
	         <tr>
	         
	         <td style="align:right;">Login:</td>
	      <td>
			<select name="login_url" title="Select which instance of Salesforce you would like to use">
			  <option value="https://login.salesforce.com">Salesforce Production Login</option>
			  <option value="https://test.salesforce.com">Salesforce Sandbox Login</option>
			  <option value="https://gus.my.salesforce.com">Salesforce GUS Org Login</option>
			  <option value="https://org62.my.salesforce.com">Salesforce Org62 Login</option>
			  <!-- 
			  <option value="https://login-blitz01.soma.salesforce.com/">Blitz01 Login</option>			  
			  <option value="https://login-blitz02.soma.salesforce.com/">Blitz02 Login</option>			  
			  <option value="https://login-blitz03.soma.salesforce.com/">Blitz03 Login</option>			  
			  <option value="https://login-blitz04.soma.salesforce.com/">Blitz04 Login</option>			  
			  <option value="https://mobile1.t.salesforce.com/">Mobile1 Login</option>			  
			  <option value="https://mobile2.t.salesforce.com/">Mobile2 Login</option>			  
			  -->
			  <option value="https://bobbywhitenc-dev-ed.my.salesforce.com">Bobby's Demo Org</option>
              <option value="https://unilever.my.salesforce.com">Unilever Production</option>
			  <!--  <option value="https://gus-communities.force.com/ssc">GUS Community Login</option> -->
			  <option value="https://success.salesforce.com">Success Community Login</option>
			</select>
			</td>
			</tr>
			<tr style="height:60px;">
			<td style="align:right;">
					<input type="hidden" name="start" value="start"></td>
			
			<td><input type="submit" value="Authorize" title="Clicking Authorize will redirect you to Salesforce to authorize access using the OAuth 2.0 protocol"/></td>
	
			</tr>
	
		</tbody>
	</table>
</form>
<br/>
<br/>
<span style="font-style:italic;width:600px;">You'll be redirected to Salesforce to authenticate and authorize.  If you're already logged into the selected Org, you won't be asked to Authenticate, you'll be asked to Authorize this application to login on your behalf.
</span>

</body>
</html>