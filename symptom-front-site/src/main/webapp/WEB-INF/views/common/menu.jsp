<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
	
<div class="col-sm-2 col-md-2">
	<div class="list-group">
		<security:authorize ifAllGranted="ROLE_ADMIN">
    		<a href="${SPM_CONTEXT}/syndrome/list" class="list-group-item bind-menu-syndrome">症状管理</a> 
			<a href="${SPM_CONTEXT}/syndrome/element/list" class="list-group-item bind-menu-syndrome-element">症素管理</a> 
		</security:authorize>
		<security:authorize ifAllGranted="ROLE_USER">
    		<a href="${SPM_CONTEXT}/syndrome/search" class="list-group-item bind-menu-syndrome-search">症状种</a> 
		</security:authorize>
	</div>
</div>