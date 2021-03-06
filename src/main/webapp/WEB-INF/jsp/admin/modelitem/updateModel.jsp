<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<script src="media/admin/plugins/beyond/js/select2/select2.js"></script>
<div id="modelItem-update">
	<div class="page-header">
		<div class="header-title">
			<h1>修改模型</h1>
		</div>
	</div>
	<div class="page-body">
		<div class="row">
			<div class="col-lg-12">
				<form class="bv-form form-horizontal validate-form" action="admin/modelItem/createModelItem">
				
					
					
					<c:if test="${sourceModelItem ne null }">
						<div class="form-group">
							<label class="col-lg-2 control-label" for="name">来源实体</label>
							<div class="col-lg-5">
								${sourceModelItem.name }
							</div>
						</div>
					</c:if>
				
					<div class="form-group">
						<input type="hidden" name="modelItem.code" value="${modelItem.code }">
						<input type="hidden" name="modelItem.type" value="${modelItem.type }">
						<input type="hidden" name="modelItem.usingState" value="${modelItem.usingState }">
						<input type="hidden" name="modelItem.belongModel" value="${modelItem.belongModel }">
						<label class="col-lg-2 control-label" for="name">名称</label>
						<div class="col-lg-5">
							<input type="text" placeholder="名字不能为空" data-bv-notempty="true" data-bv-notempty-message="名字不能为空" class="form-control name" name="modelItem.name" value="${modelItem.name }" />
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-lg-2 control-label" for="miModel.needHistory">记录历史</label>
						<div class="col-lg-5">
							<select class="" name="miModel.needHistory">
								<option  value="0" <c:if test="${miModel.needHistory eq 0 }">selected</c:if>>否</option>
								<option value="1" <c:if test="${miModel.needHistory eq 1 }">selected</c:if>>是</option>
							</select>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-lg-2 control-label" for="miModel.needCache">缓存</label>
						<div class="col-lg-5">
							<select class="" name="miModel.needCache">
								<option value="0" <c:if test="${miModel.needCache eq 0 }">selected</c:if>>否</option>
								<option value="1" <c:if test="${miModel.needCache eq 1 }">selected</c:if>>是</option>
							</select>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-lg-2 control-label" for="miModel.needRunningLogger">运行日志</label>
						<div class="col-lg-5">
							<select class="" name="miModel.needRunningLogger">
								<option value="0" <c:if test="${miModel.needRunningLogger eq 0 }">selected</c:if>>否</option>
								<option value="1" <c:if test="${miModel.needRunningLogger eq 1 }">selected</c:if>>是</option>
							</select>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-lg-2 control-label" for="modelItem.description">描述</label>
						<div class="col-lg-5">
							<textarea  class="form-control" name="modelItem.description">${modelItem.description }</textarea>
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

$('select').css("width","100%").select2();

/* function checkName() {
	var $cnName = $("form").find(".name");
	var nameValue = $cnName.val();
	
	var reg = /[^\u4e00-\u9fa5 \w \(（）\)]/;
    var istrue = reg.test(nameValue);
    $cnName.siblings("#req").remove();
    if (istrue) {
    	
    	$cnName.after(" <span id=\"req\" style=\"color: red;\">名称只能输入中文、英文、下划线、中英括号！</span>");
    	return;
    } 
} */

</script>