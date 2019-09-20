<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<script src="media/admin/plugins/beyond/js/select2/select2.js"></script>
<div id="miFix_edit">
	<div class="page-header">
		<div class="header-title">
			<h1>编辑</h1>
		</div>
	</div>
	<div class="page-body">
		<div class="row">
			<div class="col-lg-12">
				<form class="bv-form form-horizontal validate-form" action="admin/modelItemFix/do_edit">
						
						<input type="hidden" name="id" value="${modelItemFix.id }">
						
					<div class="form-group">
						<label class="col-lg-2 control-label" for="prefix">前缀名称<font color="red">*</font></label>
						<div class="col-lg-5">
							<input type="text" value="${modelItemFix.prefix }" placeholder="只能填英文字母"  data-bv-notempty="true" data-bv-notempty-message="前缀名称必填" class="form-control" id="prefix" name="prefix" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 control-label" for="usingState">状态</label>
						<div class="col-lg-5">
							<select style="width: 30%;" id="usingState" class="ser-list" name="usingState">
								<option ${modelItemFix.usingState eq 1 ? 'selected="selected"' : ''} value="1">启用</option>
								<option ${modelItemFix.usingState eq 0 ? 'selected="selected"' : ''} value="0">弃用</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 control-label" for="describeTxt">描述</label>
						<div class="col-lg-5">
							<textarea name="describeTxt" rows="3" cols="55">${modelItemFix.describeTxt }</textarea>
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
		var $page = $('#miFix_edit');
		
		 $(function(){
	    	 $(".ser-list", $page).css({"width":"100%"}).select2();
	    })
	    
	});
</script>