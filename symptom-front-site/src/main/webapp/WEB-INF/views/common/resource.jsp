<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<!-- Resources -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="SPM_CONTEXT" value="${pageContext.request.contextPath}" scope="application" />
<link rel="stylesheet" href="${SPM_CONTEXT}/css/bootstrap/bootstrapv3.min.css" />
<link rel="stylesheet" href="${SPM_CONTEXT}/css/bootstrap/bootstrap.min.css" />

<script src="${SPM_CONTEXT}/js/lib/jquery/jquery.min.js"></script>
<script src="${SPM_CONTEXT}/js/lib/bootstrap/bootstrap.min.js"></script>
<script src="${SPM_CONTEXT}/js/lib/bootstrap/bootstrap-paginator.min.js"></script>
<script src="${SPM_CONTEXT}/js/lib/bootstrap/bootstrapv3.js"></script>
<script src="${SPM_CONTEXT}/js/lib/jquery-validate/jquery.validate.min.js"></script>
<script src="${SPM_CONTEXT}/js/lib/jquery-validate/messages_zh.min.js"></script>
<script src="${SPM_CONTEXT}/js/lib/knockout/knockout-3.3.0.js"></script>
<script src="${SPM_CONTEXT}/js/lib/knockout/knockout.mapping.js"></script>

<script type="text/javascript">
	window.URL = window.URL || window.webkitURL || window.mozURL || window.msURL;  	

	(function($) {
		$.SPM = {};
		$.SPM.pages = {};
		$.SPM.context = "${SPM_CONTEXT}";
	})(jQuery);
</script>

<!-- /Resources -->