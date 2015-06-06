<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>症状更新</title>
<%@include file="../common/resource.jsp"%>
</head>
<body>
    <%@ include file="../common/header.jsp" %>
    <div class="container-fluid">
        <div class="row">
		    <%@ include file="../common/menu.jsp" %>
            <div class="col-sm-10">
                <div class="magage-box">
                    <form class="form-horizontal bind-update-from"
                        action="${SPM_CONTEXT}/syndrome/update" method="post">
                        <div class="control-group">
                            <div class="form-group">
                                <label for="symptomName" class="col-sm-2 control-label">症状名称:</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control" name="symptomName"
                                        id="symptomName" value="${syndrome.symptomName }" placeholder="症状名称">
                                    
                                    <input type="hidden" name="id" value="${syndrome.id }">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="symptomCategory" class="col-sm-2 control-label">症状种类:</label>
                                <div class="col-sm-6">
                                	<select class="form-control" name="symptomCategory" id="symptomCategory">
                                		<!-- <option value="">请选择症状种类</option> -->
                                	
                                		<c:forEach items="${zzCategory}" var="categeory">
                                			<c:if test="${categeory == syndrome.symptomCategory}">
                                				<option value="${categeory}" selected="selected">${categeory}</option>
                                			</c:if>
                                			<c:if test="${categeory != syndrome.symptomCategory}">
                                				<option value="${categeory}">${categeory}</option>
                                			</c:if>
                                		</c:forEach>
                                	</select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="description" class="col-sm-2 control-label">症状描述:</label>
                                <div class="col-sm-6">
                                    <!-- <input type="text" class="form-control" name="description"
                                        id="description" placeholder="症状描述"> -->
                                    <textarea class="form-control" name="description" id="description" rows="3" cols="">${syndrome.description }</textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="syndromeElementStart" class="col-sm-2 control-label">症素关系:</label>
                                <div class="col-sm-3">
                                    <input type="text" class="form-control"
                                        name="syndromeElementStart" id="syndromeElementStart"
                                        value="${syndrome.syndromeElementStart }" placeholder="症素A">
                                </div>
                                  
                                <div class="col-sm-3">
                                    <input type="text" class="form-control"
                                        name="syndromeElementEnd" id="syndromeElementEnd"
                                        value="${syndrome.syndromeElementEnd }" placeholder="症素B">
                                </div>
                                <input type="hidden" name="isNext" data-bind="value: isNext">
                            </div>
                            <div class="form-group">
                                <div class="col-sm-offset-2 col-sm-6">
                                    <button type="button"
                                        class="btn btn-primary bind-update-submit-button" data-bind="click: update">更新</button>
                                        
                                    <button type="button"
                                        class="btn btn-primary bind-update-submit-button-next" data-bind="click: updateAndNext">保存并更新下一条</button>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script src="${SPM_CONTEXT}/js/views/syndrome/update.js"></script>
</body>
</html>