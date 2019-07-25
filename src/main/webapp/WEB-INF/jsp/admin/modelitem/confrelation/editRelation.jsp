<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div id="modelRelation-edit">
	<div class="page-header">
		<div class="header-title">
			<h1>修改关系</h1>
		</div>
		 <div class="header-title">
	    	<label>
	       		 <input id="add_rela_symmetry" class="checkbox-slider slider-icon colored-blue" type="checkbox">
	       		 <span class="text">修改对称关系</span>
	   		</label>
		</div>
	</div>
	<div class="page-body">
		<div class="row">
			<div class="col-lg-12">
				<form class="bv-form form-horizontal validate-form" action="admin/modelRelationType/do_edit">
				         
				      <input type="hidden" name="typeCode" id="typeCode" value="${relation.typeCode }"/>
				         
				    <div class="form-group">
						<label class="col-lg-2 control-label" for="leftName">关系名称</label>
						<div class="col-lg-5">
							<input type="text" value="${relation.name }" placeholder="名字不能为空" data-bv-notempty="true" data-bv-notempty-message="名字不能为空"  class="form-control name" name="leftName" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 control-label" for="name">关系类型</label>
						<select id="leftRelationType" name="leftRelationType" style="width:30%">
			           		<option value="5" <c:if test="${relation.relationType eq 5 }">selected</c:if>>对多</option>
			           		<option value="6" <c:if test="${relation.relationType eq 6 }">selected</c:if>>对一</option>
				        </select>
					</div>
				          
				    <div class="form-group">
						<label class="col-lg-2 control-label" for="name">巨型关系</label>
						<select id="leftgiant" name="leftgiant" style="width:30%">
				           		<option value="0" <c:if test="${relation.giant eq 0 }">selected</c:if>>否</option>
				           		<option value="1" <c:if test="${relation.giant eq 1 }">selected</c:if>>是</option>
				            </select>
					</div>
				
					<div class="form-group">
			        	<div class="col-lg-offset-3 col-lg-3">
			        		<input class="btn btn-block btn-darkorange" type="submit" value="提交"  />
				        </div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
<script>
seajs.use(['dialog','utils', 'ajax'], function(Dialog, Utils, Ajax){
	var $page = $('#modelRelation-edit');


	var $form = $page.find("form");
	
	$("div", $page).on("click", "#add_rela_symmetry", function() {
		
		if ($(this).is(':checked')) {
			$form.find('.reverseRef').hide();
			$form.find('#symmetry').val('symmetry');
    	} else {
    		$form.find('.reverseRef').show();
    		$form.find('#symmetry').val("");
    	}
        
    });


});

</script>