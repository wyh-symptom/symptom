<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查询</title>
<%@include file="../common/resource.jsp"%>
<script src="${SPM_CONTEXT}/js/views/syndrome/search.js"></script>
</head>
<body>
	<%@ include file="../common/header.jsp"%>
	<div class="container-fluid">
		<div class="row">
			<%@ include file="../common/menu.jsp"%>
			<div class="col-sm-10">
			    <div class="row">
			        <div class="col-sm-4">
			         	请输入症状：<input type="text" class="form-control bind-search-symptom-name" placeholder="症状名称">
			        </div>
                    <div class="col-sm-4">
                        <select class="form-control bind-search-symptom-name-select" 
                                            data-bind="options: selectSymptomNames,  optionsText: 'symptomName', 
                                              optionsValue: 'symptomName', event: {change: $root.selectSymptomNameChange }"></select>
                    </div>
			    </div>
			    <h1></h1>
			    <div class="row">
			        <div class="col-sm-4">
	                    <div class="panel panel-primary">
                          <div class="panel-heading">症状名称</div>
                          <div class="list-group" data-bind="foreach: { data: keys, as: 'key' }">
                              <a href="#" data-bind="text: key.symptomName, css: checkedCss, click: onClick">
                              </a>
                            </div>
                        </div>
			        </div>
			        <div class="col-sm-4">
			            <div class="panel panel-primary">
						  <div class="panel-heading">症状属</div>
						  <div class="list-group" data-bind="foreach: { data: values, as: 'value' }">
                              <a href="#" data-bind="text: value.description, css: checkedCss, click: onClick">
                              </a>
                            </div>
						</div>
			        </div>
			        <div class="col-sm-4">
	                    <div class="panel panel-primary">
                          <div class="panel-heading">已选症状集合</div>
                          <ul class="list-group" data-bind="foreach: { data: symptomNames, as: 'symptom' }">
						    <li class="list-group-item">
						      <a class="badge" data-bind="click: symptom.onDelete">X</a>
						      <span data-bind="text: symptom.symptomName"></span>
						    </li>
						  </ul>
                        </div>
			        </div>
				</div>
				<h1></h1>
				<div class="magage-box">
                    <form class="form-horizontal bind-search-from" action="${SPM_CONTEXT}/syndrome/search" method="post" target="_blank">
                        <!-- ko foreach: { data: symptoms, as: 'symptom' } -->
                            <input type="hidden" name="symptomName" data-bind="value: symptom.symptomName">
                            <input type="hidden" name="description" 
                            data-bind="value: symptom.symptomName + '&&' + symptom.description + '&&' + symptom.syndromeElementStart + '##' + symptom.syndromeElementEnd">
                        <!-- /ko -->
                         
                        <div class="form-group container-fluid">
                            <div class="col-sm-offset-4 col-sm-6">
                                <button type="button" class="btn btn-success bind-search-submit-button">提交</button>
                            </div>
                        </div>
                    </form>
                </div>
			</div>
		</div>
	</div>
</body>
</html>