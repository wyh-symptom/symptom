<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
	
<nav class="navbar navbar-inverse">
	<div class="container-fluid">
		<!-- Brand and toggle get grouped for better mobile display -->
		<div class="navbar-header">
			<b class="navbar-brand">症状管理</b>
		</div>

		<!-- Collect the nav links, forms, and other content for toggling -->
		<div class="collapse navbar-collapse">
			<ul class="nav navbar-nav navbar-right navbar-btn">
				<li><button class="btn btn-link btn-block logout" onclick="javascript:window.location.href='${SPM_CONTEXT}/j_spring_security_logout'">Logout</button></li>
			</ul>
		</div>
	</div>
</nav>
