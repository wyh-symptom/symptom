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
			             <input type="text" class="form-control"  placeholder="症状名">
			        </div>
                    <div class="col-sm-4">
                        <select class="form-control" name="symptomName">
                            <option>1</option>
                            <option>2</option>
                            <option>3</option>
                        </select>
                    </div>
			    </div>
			    <h1></h1>
			    <div class="row">
			        <div class="col-sm-4">
	                    <div class="panel panel-primary">
                          <div class="panel-heading">症状名</div>
                          <div class="list-group">
                              <a href="#" class="list-group-item list-group-item-info">
                                                                                        症素
                              </a>
                              <a href="#" class="list-group-item">Dapibus ac facilisis in</a>
                              <a href="#" class="list-group-item">Morbi leo risus</a>
                              <a href="#" class="list-group-item">Porta ac consectetur ac</a>
                              <a href="#" class="list-group-item">Vestibulum at eros</a>
                            </div>
                        </div>
			        </div>
			        <div class="col-sm-4">
			            <div class="panel panel-primary">
						  <div class="panel-heading">症素</div>
						  <div class="list-group">
                              <a href="#" class="list-group-item list-group-item-info">
                                                                                        症素
                              </a>
                              <a href="#" class="list-group-item">Dapibus ac facilisis in</a>
                              <a href="#" class="list-group-item">Morbi leo risus</a>
                              <a href="#" class="list-group-item">Porta ac consectetur ac</a>
                              <a href="#" class="list-group-item">Vestibulum at eros</a>
                            </div>
						</div>
			        </div>
			        <div class="col-sm-4">
	                    <div class="panel panel-primary">
                          <div class="panel-heading">检索条件</div>
                          <ul class="list-group">
						    <li class="list-group-item">
						      <a class="badge">X</a>
						      <span>Cras justo odio</span>
						    </li>
						    <li class="list-group-item">
						      <a class="badge">X</a>
						      <span>Cras justo odio</span>
						    </li>
						    <li class="list-group-item">
						      <a class="badge">X</a>
						      <span>Cras justo odio</span>
						    </li>
						    <li class="list-group-item">
						      <a class="badge">X</a>
						      <span>Cras justo odio</span>
						    </li>
						  </ul>
                        </div>
			        </div>
				</div>
				<h1></h1>
				<div class="magage-box">
                    <form class="form-horizontal bind-search-from" action="${SPM_CONTEXT}/syndrome/search" method="post">
                        <div class="form-group container-fluid">
                            <div class="col-sm-offset-4 col-sm-6">
                                <button type="submit" class="btn btn-success bind-search-search-button">提交</button>
                            </div>
                        </div>
                    </form>
                </div>
			</div>
		</div>
	</div>
	
	

</body>
</html>