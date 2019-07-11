<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<!-- <script src="media/admin/modelitem/js/addAttr.js"></script> -->
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<script src="media/admin/plugins/beyond/js/select2/select2.js"></script>

<div id="filterRgroupAdd" >
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
				<form  class="forbid-enter-submit bv-form form-horizontal validate-form dataForm">
					<input type="hidden" name="miFilterGroup.id" value="${id }"> 
					<input type="hidden" name="miFilterGroup.type" value="2">
					<input type="hidden" name="miFilterGroup.pid" value="${pid }"> 
					
					<div class="form-group">
						<label class="col-lg-2 control-label" for="miFilterGroup.name">关系组名称</label>
						<div class="col-lg-5">
							<input type="text" data-bv-notempty="true" class="form-control" name="miFilterGroup.name" value="${miFilterGroup.name }"/>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-lg-2 control-label" for="miFilterGroup.logicalOperator">运算逻辑</label>
						<div class="col-lg-5">
							<select class="" name="miFilterGroup.logicalOperator">
								<option value="2">AND</option>
							</select>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-lg-2 control-label" for="miFilterGroup.rightMiCode">右实体模型</label>
						<div class="col-lg-5">
							<select class="" name="miFilterGroup.rightMiCode">
								<c:forEach items="${rightModelList }" var="item">
									<option <c:if test="${item.code eq miFilterGroup.rightMiCode}">selected</c:if> value="${item.code }">${item.name }</option>
								</c:forEach>
							</select>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-lg-2 control-label" for="miFilterRgroup.inRelationType">存在关系</label>
						<div class="col-lg-5">
							<select multiple="multiple" class="" name="miFilterRgroup.inRelationType">
								<option value=""></option>
								<c:forEach items="${relationList }" var="item">
									<option <c:if test="${fn:contains(miFilterRgroup.inRelationType , item.typeCode)}">selected</c:if> value="${item.typeCode }">${item.name }</option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 control-label" for="miFilterRgroup.inRightCode">存在具体数据</label>
						<div class="col-lg-5">
							<input type="text" data-bv-notempty="true" class="form-control"  name="miFilterRgroup.inRightCode" value="${miFilterRgroup.inRightCode }" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 control-label" for="miFilterRgroup.exRelationType">不存在关系</label>
						<div class="col-lg-5">
							<select multiple="multiple" class="" name="miFilterRgroup.exRelationType">
								<option value=""></option>
								<c:forEach items="${relationList }" var="item">
									<option <c:if test="${fn:contains(miFilterRgroup.exRelationType, item.typeCode)}">selected</c:if> value="${item.typeCode }">${item.name }</option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 control-label" for="miFilterRgroup.exRelationType">不存在具体数据</label>
						<div class="col-lg-5">
							<input type="text" data-bv-notempty="true"  class="form-control" name="miFilterRgroup.exRightCode" value="${miFilterRgroup.exRightCode }" />
						</div>
					</div>
					
				</form>
			</div>
		</div>
	</div>
</div>


 <script type="text/javascript">

seajs.use(['dialog','utils', 'ajax', '$CPF'], function(Dialog, Utils, Ajax, $CPF){

	var $page = $('#filterRgroupAdd');	

	 $('select',$page).css("width","100%").select2();

	  $($page).on("click", "#btn-save", function (e) {

		 	//这里获取属性的值
			 var formdom = $page.find(".dataForm")[0];
		     var fData = new FormData(formdom);
			 
			 Ajax.ajax('admin/expressionAndFilter/createFilterRGroup',fData, function(data) {			
				if (data.code == "400") {
					 Dialog.notice(data.msg, "error");
					 $CPF.closeLoading();
				} else{
					Dialog.notice(data.msg, "success");
					 ////
					var miFilterContainer = data.miFilterContainer;
	
					var page = $page.getLocatePage();
					if(page.getPageObj() instanceof Dialog){
						
						var afterSave = page.getPageObj().getEventCallbacks('afterSave');
						if(typeof afterSave == 'function'){
							afterSave.apply(page, [miFilterContainer]);
						}
					}
					$page.getLocatePage().close();
					
				}
				$CPF.closeLoading();
			}); 
		 }); 
	
		 
	});

</script>