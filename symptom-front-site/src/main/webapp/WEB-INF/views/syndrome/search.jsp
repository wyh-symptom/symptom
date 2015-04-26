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

				<div class="magage-box">
					<form class="form-horizontal bind-search-from"
						action="${SPM_CONTEXT}/syndrome/search" method="post">
						<div class="control-group">
							<div class="row">
								<div class="form-group col-md-8">
									<label for="symptomName" class="col-sm-2 control-label">症状名:</label>
									<div class="col-sm-4">
										<select class="form-control bind-search-symptom-name-select" name="symptomName"
											data-bind="options: keys,  optionsText: 'symptomName', 
						                      optionsValue: 'symptomName'"></select>
									</div>

									<label for="description" class="col-sm-2 control-label">描述:</label>
									<div class="col-sm-4">
									    <select class="form-control" name="description">
										</select>
									</div>
								</div>
								<div class="form-group col-md-4">
									<div class="col-sm-2 ">
										<button type="button"
											class="btn btn-info bind-search-add-button">添加</button>
									</div>
									<div class="col-sm-2 ">
										<button type="button" class="btn btn-warning bind-search-remove-button" style="display: none;">删除</button>
									</div>
								</div>

							</div>
						</div>

						<div class="form-group container-fluid">
							<div class="col-sm-offset-4 col-sm-6">
								<button type="submit"
									class="btn btn-success bind-search-search-button">提交</button>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>

</body>
</html>