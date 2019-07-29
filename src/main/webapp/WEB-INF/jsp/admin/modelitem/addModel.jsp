<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<script src="media/admin/plugins/beyond/js/select2/select2.js"></script>
<div id="modelItem-add">
	<div class="page-header">
		<div class="header-title">
			<h1>添加模型</h1>
		</div>
	</div>
	<div class="page-body">
		<div class="row">
			<div class="col-lg-12">
				<form class="bv-form form-horizontal validate-form" action="admin/modelItem/createModelItem">
					<div class="form-group">
						<label class="col-lg-2 control-label" for="name">名称</label>
						<div class="col-lg-5">
							<input type="text" placeholder="名字不能为空" data-bv-notempty="true" data-bv-notempty-message="名字不能为空" class="form-control name" name="modelItem.name" />
						</div>
					</div>
					
					<div class="form-group modelType">
						<label class="col-lg-2 control-label" for="modelItem.type">实体类型</label>
						<div class="col-lg-5">
							<select class=" modelItemType" name="modelItem.type">
								<option value="1">普通实体</option>
								<option value="101">sql片段统计实体</option>
							</select>
						</div>
					</div>
					
					<div class="form-group sourceModelDiv"  style="display:none;">
						<label class="col-lg-2 control-label" for="miModelStat.sourceCode">来源实体</label>
						<div class="col-lg-5">
							<select class=" sourceModelSel" name="miModelStat.sourceCode">
								<option value="122">来源1</option>
								<option value="222">来源2</option>
							</select>
						</div>
					</div>
					
					<div class="form-group"  style="display:none;">
						<label class="col-lg-2 control-label" for="miModel.needHistory">是否记录历史</label>
						<div class="col-lg-5">
							<select class="" name="miModel.needHistory">
								<option selected value="0">否</option>
								<option value="1">是</option>
							</select>
						</div>
					</div>
					
					
					<div class="form-group">
						<label class="col-lg-2 control-label" for="modelItem.description">描述</label>
						<div class="col-lg-5">
							<textarea  class="form-control" name="modelItem.description" />
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
var $page = $('#modelItem-add');	
$('select').css("width","100%").select2();
$($page).on("change", ".modelItemType", function() { 

	var miType = $(this).val();
	
	if ("101" == miType) {
	 	Ajax.ajax('admin/modelItem/getModelList', '', function(data) {			
			if (data.code == "400") {
				 Dialog.notice(data.msg, "error");
				 $CPF.closeLoading();
			} else{
				debugger;
				var $select = $('.sourceModelSel');
				$select.html("");
				var str = "";
				
				var modelList = data.modelList;
				for ( var key in modelList) {
					str+="<option value='"+modelList[key].code+"'>"+modelList[key].name+"</option>";
				}

				$select.append(str);
				$('select').css("width","100%").select2();
				$('.sourceModelDiv').show();
			}
			
			$CPF.closeLoading();
		}); 
	} else {
		$('.sourceModelDiv').hide();
	}
	
	});
});

</script>