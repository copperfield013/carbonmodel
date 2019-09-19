<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div id="modelRelation-add">
	<div class="page-header">
		<div class="header-title">
			<h1>添加关系</h1>
		</div>
		 <div class="header-title">
	    	<label>
	       		 <input id="add_rela_symmetry" class="checkbox-slider slider-icon colored-blue" type="checkbox">
	       		 <span class="text">添加对称关系</span>
	   		</label>
		</div>
	</div>
	<div class="page-body">
		<div class="row">
			<div class="col-lg-12">
				<form class="bv-form form-horizontal validate-form" action="admin/modelRelationType/doAdd">
					 <input type="hidden" name="leftModelCode" value="${leftModelCode }">
				     <input type="hidden" name="symmetry" id="symmetry">
				         
				      <input type="hidden" name="typeCode" id="typeCode" />
				      <input type="hidden" name="reverseCode" id="reverseCode" />
				         
				         
				    <div class="form-group">
						<label class="col-lg-2 control-label" for="name">实体名称</label>
						<div class="col-lg-5">
							${leftModelName }
						</div>
					</div>
				    <div class="form-group">
						<label class="col-lg-2 control-label" for="name">关系名称</label>
						<div class="col-lg-5">
							<input type="text" placeholder="名字不能为空" data-bv-notempty="true" data-bv-notempty-message="名字不能为空"  class="form-control name" name="leftName" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 control-label" for="name">关系类型</label>
						<select id="leftRelationType" name="leftRelationType" style="width:30%">
			           		<option value="5">对多</option>
			           		<option value="6">对一</option>
				        </select>
					</div>
				          
				    <div class="form-group">
						<label class="col-lg-2 control-label" for="name">巨型关系</label>
						<select id="leftgiant" name="leftgiant" style="width:30%">
				           		<option value="0" selected="selected">否</option>
				           		<option value="1">是</option>
				            </select>
					</div>
					
					
					 <div class="form-group reverseRef">
						<label class="col-lg-2 control-label" for="name">选择逆向实体</label>
						<select id="rightModelCode" name="rightModelCode" style="width:30%">
				        	<c:forEach items="${modelItemList }" var="item">
				        		<option value="${item.code }">${item.name }</option>
				        	</c:forEach>    	
				        </select>
					</div>
					<div class="form-group reverseRef">
						<label class="col-lg-2 control-label" for="name">逆向关系名称</label>
						<div class="col-lg-5">
							<input type="text" placeholder="名字不能为空" data-bv-notempty="true" data-bv-notempty-message="名字不能为空"  class="form-control name" name="rightName" />
						</div>
					</div>
					
					<div class="form-group reverseRef">
						<label class="col-lg-2 control-label" for="name">逆向关系类型</label>
						<select id="rightRelationType" name="rightRelationType" style="width:30%">
			           		<option value="5">对多</option>
			           		<option value="6">对一</option>
				        </select>
					</div>
				          
				    <div class="form-group reverseRef">
						<label class="col-lg-2 control-label" for="name">逆向巨型关系</label>
						<select id="rightgiant" name="rightgiant" style="width:30%">
				           		<option value="0" selected="selected">否</option>
				           		<option value="1">是</option>
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
	var $page = $('#modelRelation-add');


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