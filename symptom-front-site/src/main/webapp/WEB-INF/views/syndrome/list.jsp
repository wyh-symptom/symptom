<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>症状列表</title>
<%@include file="../common/resource.jsp"%>
</head>
<body>
	<%@ include file="../common/header.jsp"%>
	<div class="container-fluid">
		<div class="row">
			<%@ include file="../common/menu.jsp"%>
			<div class="col-sm-10">
			    <p class="text-right">
					<a href="${SPM_CONTEXT}/syndrome/create"><button type="button" class="btn btn-info">创建</button></a>
				</p>
                
				<table class="table table-striped table-bordered">
                        <thead>
                          <tr>
                            <th>ID</th>
                            <th>症状名</th>
                            <th>描述</th>
                            <th>症素A</th>
                            <th>症素B</th>
                            <th colspan="3"></th>
                          </tr>
                        </thead>
                        <tbody data-bind="foreach: {data: syndromes, as: 'syndrome'}">
                          <tr>
                            <th data-bind="text: syndrome.id">1</th>
                            <td data-bind="text: syndrome.symptomName">Mark</td>
                            <td data-bind="text: syndrome.description">Mark</td>
                            <td data-bind="text: syndrome.syndromeElementEnd">Mark</td>
                            <td data-bind="text: syndrome.syndromeElementStart">Mark</td>
                            <td><a data-bind="attr: { href: '${SPM_CONTEXT}/syndrome/update/' + syndrome.id() }">修改</a></td>
                            <td><a href="#" data-bind="click: deleteItem">删除</a></td>
                          </tr>
                        </tbody>
                      </table>
			</div>
		</div>
	</div>

	<script src="${SPM_CONTEXT}/js/views/syndrome/list.js"></script>
</body>
</html>