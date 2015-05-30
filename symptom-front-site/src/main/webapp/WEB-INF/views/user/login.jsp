<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ISO—R中医智能辨证决策支持系统（1.0）</title>
<%@include file="../common/resource.jsp"%>
<!-- Custom styles for this template -->
<link href="${SPM_CONTEXT }/css/signin.css" rel="stylesheet">
</head>
<body>
	<div class="container">
		<c:if test="${not empty param.error}">
		    <div class="alert alert-warning alert-dismissible" role="alert">
	            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
	                <span aria-hidden="true">&times;</span>
	            </button>
            <strong>警告！</strong> 用户名或密码错误。
        </div>
		</c:if>
		<form class="form-signin" action="${SPM_CONTEXT }/j_spring_security_check" method="post">
			<h2 class="form-signin-heading">登陆</h2>
			<label for="inputEmail" class="sr-only">Email address</label> 
			<input type="text" name="j_username" class="form-control" placeholder="用户名" required autofocus> 
			<label for="inputPassword" class="sr-only">Password</label> 
			<input type="password" name="j_password" class="form-control" placeholder="密码" required>
			<!-- <div class="checkbox">
          <label>
            <input type="checkbox" name="_spring_security_remember_me"> 记住密码
          </label>
        </div> -->
			<button class="btn btn-lg btn-primary btn-block" type="submit">登录</button>
		</form>
	</div>
	<!-- /container -->
</body>
</html>