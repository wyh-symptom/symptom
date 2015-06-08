<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>症状种查询</title>
<%@include file="../common/resource.jsp"%>
<link rel="stylesheet" href="${SPM_CONTEXT}/css/jquery.treegrid.css" />
<script src="${SPM_CONTEXT}/js/lib/bootstrap/jquery.treegrid.js"></script>
<script src="${SPM_CONTEXT}/js/lib/bootstrap/jquery.treegrid.bootstrap3.js"></script>
<script src="${SPM_CONTEXT}/js/views/syndrome/search.js"></script>
</head>
<body>
	<%@ include file="../common/header.jsp"%>
	<div class="container-fluid">
		<div class="row">
			<%@ include file="../common/menu.jsp"%>
			<div class="col-sm-10">
				<div class="row">
					<form class="form-horizontal bind-create-from">
						<div class="control-group">
							<div class="col-sm-8">
								<div class="form-group">
									<label for="symptomName" class="col-sm-2 control-label">请输入症状：</label>
									<div class="col-sm-4">
										<input type="text" class="form-control bind-search-symptom-name" placeholder="症状名称">
									</div>
									<div class="col-sm-4">
										<select class="form-control bind-search-symptom-name-select"
											data-bind="options: selectSymptomNames,  optionsText: 'symptomName', 
		                                              optionsValue: 'symptomName', event: {change: $root.selectSymptomNameChange }"></select>
									</div>
								</div>
							</div>
						</div>
					</form>
				</div>
				
			    <div class="row">
			        <div class="col-sm-3">
	                    <div class="panel panel-primary">
                          <div class="panel-heading">症状名称</div>
							<table class="table tree-2 table-bordered table-striped table-condensed">
								<tbody data-bind="foreach: { data: keys, as: 'key' }">
									<tr data-bind="css: key.treegrCss">
										<td data-bind="click: onClick">
											<span data-bind="css: key.treeIconCss"></span>
											<!-- ko text: key.syndromeCategoryName -->
											     Root node 1
											<!-- /ko -->
										</td>
									</tr>
									<!-- ko foreach: { data: key.syndromeNames, as: 'sn' } -->
									<tr data-bind="css: sn.treegrCss, visible: $parent.isVisible">
										<td data-bind="click: sn.onClick">
											<span class="treegrid-expander"></span>
											<!-- ko text: sn.symptomName -->
											    Node 1-1
											<!-- /ko -->
										</td>
									</tr>
									<!-- /ko -->
								</tbody>
							</table>

						</div>
			        </div>
			        
			        <div class="col-sm-6">
			            <div class="panel panel-primary">
						  <div class="panel-heading">症状属</div>
						  <div class="list-group" data-bind="foreach: { data: values, as: 'value' }">
                              <a href="#" data-bind="text: ($index() +1) + '：' +value.description, css: checkedCss, click: onClick">
                              </a>
                            </div>
						</div>
			        </div>
			        <div class="col-sm-3">
	                    <div class="panel panel-primary">
                          <div class="panel-heading">已选症状集合</div>
                          <ul class="list-group" data-bind="foreach: symptomNames">
						    <li class="list-group-item">
						      <!-- <a class="badge" data-bind="click: symptom.onDelete">X</a> -->
						      <span data-bind="text: $data"></span>
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