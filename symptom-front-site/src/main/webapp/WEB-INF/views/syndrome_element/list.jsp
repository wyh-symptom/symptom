<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>症素列表</title>
<%@include file="../common/resource.jsp"%>
</head>
<body>
	<%@ include file="../common/header.jsp"%>
	<div class="container-fluid">
		<div class="row">
			<%@ include file="../common/menu.jsp"%>
			<div class="col-sm-10">
			    <p class="text-right">
					<a href="${SPM_CONTEXT}/syndrome/element/create"><button type="button" class="btn btn-info">创建</button></a>
				</p>
                
				<table class="table table-striped table-bordered">
                        <thead>
                          <tr>
                            <th>ID</th>
                            <th>症素A</th>
                            <th>症素B</th>
                            <th>关系类型</th>
                            <th>症素关系</th>
                            <th colspan="2"></th>
                          </tr>
                        </thead>
                        <tbody data-bind="foreach: {data: syndromeElements, as: 'syndromeElement'}">
                          <tr>
                            <th data-bind="text: syndromeElement.id">1</th>
                            <td data-bind="text: syndromeElement.syndromeElementStart">Mark</td>
                            <td data-bind="text: syndromeElement.syndromeElementEnd">Mark</td>
                            <!-- ko if: syndromeElement.isRelate() == 1 -->
	                            <td>是</td>
						    <!-- /ko -->
                            <!-- ko if: syndromeElement.isRelate() == 0 -->
                                <td>否</td>
						    <!-- /ko -->
						    <td data-bind="text: syndromeElement.syndromeElementEnd">Mark</td>
                            <!-- ko if: syndromeElement.relateType() == 1 -->
	                            <td>因果关系</td>
						    <!-- /ko -->
                            <!-- ko if: syndromeElement.relateType() == 2 -->
                                <td>从属关系</td>
						    <!-- /ko -->
						     <!-- ko if: syndromeElement.relateType() == 3 -->
                                <td>并列关系</td>
						    <!-- /ko -->
                            <td><a data-bind="attr: { href: '${SPM_CONTEXT}/syndrome/element/update/' + syndromeElement.id() }">修改</a></td>
                            <td><a href="#" data-bind="click: deleteItem">删除</a></td>
                          </tr>
                        </tbody>
                    </table>
                <ul id="paginator"></ul>                    
			</div>
		</div>
	</div>

	<script src="${SPM_CONTEXT}/js/views/syndrome_element/list.js"></script>
</body>
</html>