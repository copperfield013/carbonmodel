<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<script src="media/admin/plugins/beyond/js/select2/select2.js"></script>
<div id="strucBase_add">
	<div class="page-header">
		<div class="header-title">
			<h1>添加结构体</h1>
		</div>
	</div>
	<div class="page-body">
		<div class="row">
			<div class="col-lg-12">
				<form class="bv-form form-horizontal validate-form" action="admin/structBase/createStrucBase">
					
					<div class="form-group">
						<label class="col-lg-2 control-label" for="strucBase.title">结构体名称</label>
						<div class="col-lg-5">
							<input type="text" placeholder="名字不能为空" data-bv-notempty="true" data-bv-notempty-message="名字不能为空" class="form-control name" name="strucBase.title" />
						</div>
					</div>
					
					<div class="form-group modelType">
						<label class="col-lg-2 control-label" for="strucMiCode.itemCode">实体模型</label>
						<div class="col-lg-5">
							<select class="strucMiCodeItemCode" name="strucMiCode.itemCode">
								
							</select>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-lg-2 control-label" for="strucBase.opt">权限配置</label>
						<div class="col-lg-5">
							<select name="strucBase.opt">
								<option value="1">读</option>
								<option value="2">写</option>
							</select>
						</div>
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
seajs.use(['dialog','utils', 'ajax', '$CPF'], function(Dialog, Utils, Ajax, $CPF){
	var $page = $('#strucBase_add');

	 $(function(){
	    $CPF.showLoading();
	   var $selectItemCode =  $page.find(".strucMiCodeItemCode");

	  var $select =  $page.find("select");
	    
	   $selectItemCode.html("");
		 Ajax.ajax('admin/modelItem/getModelList','', function(data) {
				var modelList = data.modelList;
				var optionStr = "";
				for ( var key in modelList) {
					 optionStr += "<option value='"+modelList[key].code+"'>"+modelList[key].name+"</option>";
				}

				$selectItemCode.append(optionStr);
			});

		 $select.css("width","100%").select2();
	    $CPF.closeLoading();
	 })

});
</script>