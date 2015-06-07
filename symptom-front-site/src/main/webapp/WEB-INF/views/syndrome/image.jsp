<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>病机有向图</title>
<%@include file="../common/resource.jsp"%>
<link rel="stylesheet" href="${SPM_CONTEXT}/js/views/mermaid/dist/mermaid.forest.css"/>
<link rel="stylesheet" href="${SPM_CONTEXT}/js/views/mermaid/seq.css"/>
<script src="${SPM_CONTEXT}/js/views/mermaid/dist/mermaid.js"></script>
<script src="${SPM_CONTEXT}/js/views/mermaid/dist/mermaid.full.js"></script>
 <style type="text/css">
html {
  font-family: 'trebuchet ms', verdana, arial;
  color: #333333;
}
body {TEXT-ALIGN: center;}
div { MARGIN-RIGHT: auto; MARGIN-LEFT: auto; }
  </style>
  <script type="text/javascript">
  	
  	 var mermaid_config = {
	            startOnLoad:true
	        }
  	 
  	   var global_data = ${result};
       var total = global_data.length;	//总共可以画的图片数量
       var curr_index = 0 ;		//当前第几张
       jQuery(function(){
    		if (global_data && global_data.length > 0) {
    			if (global_data.length > 1) {
    				$("#nextImage").show();
    			}
    			$("#preImage").bind("click", function(){
    				$("#mermaid_image_" + curr_index).hide();
    				curr_index = curr_index - 1;
    				if(curr_index < 0) curr_index = 0;
    				if(curr_index == 0) {
    					$("#preImage").hide();
    				}
    				if (curr_index >= 0) { //则显示下一张按钮
    					$("#nextImage").show();
    				}

    				$("#mermaid_image_" + curr_index).show();
    			})
    			
    			$("#nextImage").bind("click", function(){
    				$("#mermaid_image_" + curr_index).hide();
    				curr_index = curr_index + 1;
    				if(curr_index >= total) curr_index = total - 1;
    				if (curr_index > 0) {	//当前大于第一张，则显示前一张按钮
    					$("#preImage").show();
    				}
    				if(curr_index == total - 1) {	//隐藏下一张按钮
    					$("#nextImage").hide();
    				} 
    				$("#mermaid_image_" + curr_index).show();
    				
    			})
    		}
       });
   </script>
<body>
<body>
<%@ include file="../common/header.jsp"%>
	<div class="container-fluid">
		<div class="row">
			<%@ include file="../common/menu.jsp"%>
<c:forEach items="${resultList}" var="mermaid" varStatus="index">
<div id="mermaid_image_${index.index}" <c:if test="${not index.first }">style="display:none;"</c:if>>
		<table align="center">
			<tr>
				<td colspan="2"><label for="txtname">备注:</label>
					<c:if test="${mermaid.len == 1}">
						<input id="txtname" name="txtname" size="30" value="该有向图符合ISO-R筛选法则"/>
					</c:if>
					<c:if test="${mermaid.len != 1}">
						<input id="txtname" name="txtname" size="30" value="该有向图不符合ISO-R筛选法则"/>
					</c:if>
				</td>
			</tr>
		</table>

<div  class="mermaid">
      graph TB
          subgraph 病机有向图
              subgraph ''
                 ${mermaid.relate}
              end
          end
	</div> 
	
	</div>
	</c:forEach>
	<div style="padding-top:0px;margin-top:0;">
		<button id="preImage" style="display:none;">上一张</button>
		<button id="nextImage" style="display:none;">下一张</button>
	</div>
 		
    
 
  <script type="text/javascript">
		$('.list-group-item-success').removeClass('list-group-item-success');
		$('.bind-menu-syndrome-search').addClass('list-group-item-success');
 </script>

</div>
</div>
		
		
	

</body>
</html>