<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div id="cascadedictBasicItem-add">
	<div class="page-header">
		<div class="header-title">
			<h1>添加级联引用孩子</h1>
		</div>
	</div>
	<div class="page-body">
		<div class="row">
			<div class="col-lg-12">
				<form class="bv-form form-horizontal validate-form" action="admin/modelItem/doAddCasRefChild">
					<div class="form-group">
					
						<input type="hidden" name="code" value="${pmiCode }">
						<label class="col-lg-2 control-label" for="name">选择引用</label>
						<div class="col-lg-5">
							<select name="subCode">
								<c:forEach items="${modelItemList }" var="item">
									<option value="${item.code }">${item.name }</option>
								</c:forEach>
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