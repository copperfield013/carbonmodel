<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<script src="media/admin/struct/js/addAttrField.js"></script>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<script src="media/admin/plugins/beyond/js/select2/select2.js"></script>

<div id="structTreeFieldAdd" sbId="${sbId }" structType="${structType }" sbPid="${sbPid }">
	<div class="page-header">
		<div class="header-title">
			<h1>添加属性</h1>
		</div>
		<div class="header-buttons">
			<a class="export btn-toggle" title="保存" id="btn-save" href="javascript:;">
				<i class="glyphicon glyphicon-check"></i>
			</a>
		</div>
	</div>
	<div class="page-body">
		<div class="row">
			<div class="col-lg-12">
				<form  class="forbid-enter-submit bv-form form-horizontal validate-form structBaseForm">
					
				</form>
			</div>
		</div>
	</div>
</div>


