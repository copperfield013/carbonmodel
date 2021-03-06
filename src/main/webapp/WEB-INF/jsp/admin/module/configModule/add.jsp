<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<script src="media/admin/plugins/beyond/js/select2/select2.js"></script>
<div id="module_add">
	<div class="page-header">
		<div class="header-title">
			<h1>添加</h1>
		</div>
	</div>
	<div class="page-body">
		<div class="row">
			<div class="col-lg-12">
				<form class="bv-form form-horizontal validate-form" action="admin/module/configModule/do_add">
					<div class="form-group">
						<label class="col-lg-2 control-label" for="moduleTitle">模块名称<font color="red">*</font></label>
						<div class="col-lg-5">
							<input type="text" data-bv-notempty="true" data-bv-notempty-message="模块名称必填" class="form-control" id="moduleTitle" name="moduleTitle" />
						</div>
					</div>
					<div class="form-group">
						
						<label class="col-lg-2 control-label" for="moduleName">模块标识</label>
						<div class="col-lg-5">
							<input type="text" class="form-control" placeholder="不填时将由后台生成随机编码"  name="moduleName" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 control-label" for="mappingName">结构体名称<font color="red">*</font></label>
						<div class="col-lg-5">
							<select data-bv-notempty="true" data-bv-notempty-message="配置名称必填"  style="width: 30%;" id="mappingId" class="ser-list" name="mappingId">
								<c:forEach items="${strucList }" var="item">
									<option value="${item.id }" data-id="${item.id }">${item.title }</option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 control-label" for="codeName">编码字段</label>
						<div class="col-lg-5">
							<select style="width: 30%;" id="codeName" class="ser-list" name="codeName">
								<option selected="selected" value="">唯一编码</option>
								<%-- <c:forEach items="${childNode }" var="item">
									<option value="${item.name }">${item.name }</option>
								</c:forEach> --%>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 control-label" for="titleName">名称字段</label>
						<div class="col-lg-5">
							<select style="width: 30%;" id="titleName" class="ser-list" name="titleName">
								<c:forEach items="${strucChildList }" var="item">
									<option value="${item.title }">${item.title }</option>
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
	seajs.use(['dialog','utils', 'ajax', '$CPF'], function(Dialog, Utils, Ajax, $CPF){
		var $page = $('#module_add');
		
	/* 	$(".page-body", $page).on("click", ".ser-list", function (e) {
			 $(".ser-list", $page).css({"width":"30%"}).select2();
	    });  */
	    
	    $(function(){
	    	 $(".ser-list", $page).css({"width":"30%"}).select2();
	    })
	    
	    
	    $(".page-body", $page).on("change", "#mappingId", function() {

		    
	    	var $this = $(this);
	    	var options=$("#mappingId option:selected"); //获取选中的项
	    	var sbId = options.attr("data-id");
	    	
	    	 Ajax.ajax('admin/structBase/getGroup1DChild', {
	    		 sbId:sbId
	    	 }, function(data) {

	    		 var child = data.group1dChild;
	    		 var str = "<option selected=\"selected\" value=\"\">唯一编码</option>";
	    		 var str1 = "<option selected=\"selected\" value=\"\"></option>";
	    		 for (var p in child) { //遍历json数组时，这么写p为索引，0,1
                     str = str + "<option value=\"" + child[p].title + "\">" + child[p].title + "</option>";
                     str1 = str1 + "<option value=\"" + child[p].title + "\">" + child[p].title + "</option>"; 
                 }
	    		 
	    		 /* $("#codeName").empty().append(str); */
	    		 $("#titleName").empty().append(str1);
	    		 $("#codeName").options.selectedIndex = 0; //回到初始状态
	    		 $("#titleName").options.selectedIndex = 0; //回到初始状态
	    	 }) 
	    });
	});
</script>