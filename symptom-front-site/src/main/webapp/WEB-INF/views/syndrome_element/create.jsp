<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>创建</title>
<%@include file="../common/resource.jsp"%>
</head>
<body>
	<%@ include file="../common/header.jsp"%>
	<div class="container-fluid">
		<div class="row">
			<%@ include file="../common/menu.jsp"%>
			<div class="col-sm-10">
				<div class="magage-box">
					<form class="form-horizontal bind-create-from"
						action="${SPM_CONTEXT}/syndrome/element/create" method="post">
						<div class="control-group">
							<div class="form-group">
								<label for="syndromeElementStart" class="col-sm-2 control-label">症素：</label>
								<div class="col-sm-3">
									<input type="text" class="form-control"
										name="syndromeElementStart" id="syndromeElementStart"
										placeholder="症素A">
								</div>

								<div class="col-sm-3">
									<input type="text" class="form-control"
										name="syndromeElementEnd" id="syndromeElementEnd"
										placeholder="症素B">
								</div>
								
								
								
								<div class="checkbox col-sm-1">
                                    <label> 
                                       <input name="isRelate" value="1" type="checkbox" checked="checked">
                                    </label>
                                </div>
							</div>
							<div class="form-group">
                                <label for="relateType" class="col-sm-2 control-label">关系类型:</label>
                                <div class="col-sm-6">
                                	<select class="form-control" name="relateType" id="relateType">
                                		<c:forEach items="${relateTypes}" var="type">
                                				<option value="${type.key}">${type.value}</option>
                                		</c:forEach>
                                	</select>
                                </div>
                            </div>
							<div class="form-group">
                                <label for="description" class="col-sm-2 control-label">关系备注:</label>
                                <div class="col-sm-6">
                                    <textarea class="form-control" name="description" id="description" rows="3" cols=""></textarea>
                                </div>
                            </div>
							

							
							<div class="form-group">
								<div class="col-sm-offset-2 col-sm-6">
									<button type="button"
										class="btn btn-primary bind-create-submit-button">提交</button>
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>

	<script src="${SPM_CONTEXT}/js/views/syndrome_element/create.js"></script>
</body>
</html>