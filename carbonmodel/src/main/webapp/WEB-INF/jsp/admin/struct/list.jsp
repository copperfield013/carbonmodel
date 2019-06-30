<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div id="strucBase_list">
	<nav>
		<form class="form-inline" action="admin/structBase/list">
			<div class="form-group">
				<label for="name">结构体名称</label>
				<input type="text" class="form-control" name="title" value="${strucBase.title }" />
			</div>
			<button type="submit" class="btn btn-default">查询</button>
			<a id="add" class="btn btn-primary tab" title="创建结构体" target="strucBase_add" >创建结构体</a>
			<a class="btn btn-primary tab" href="#" title="快速生成" target="strucBase_create" >快速生成</a>
		</form>
	</nav>
	<div class="row list-area">
		<table class="table">
			<thead>
				<tr>
					<th>序号</th>
					<th>结构体编号</th>
					<th>结构体名称</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${strucBaseList }" var="item" varStatus="i">
					<tr>
						<td>${i.index + 1 }</td>
						<td>${item.id }</td>
						<td>${item.title }</td>
						<td>
						
							<a id="edit" structId="${item.id }" href="javascript:;"  title="修改" >修改</a>
							<a class="tab" href="admin/structBase/structTree?sbId=${item.id }" title="配置子结构" target="struct_tree" >配置子结构</a>
							<%-- <a title="预览" nodeId="${item.id }" id="preview" href="javascript:;">预览</a>
							<a id="download" nodeId="${item.id }" href="javascript:;" >下载</a>
							
							<a href="admin/node/basicItemNode/copyNode?nodeId=${item.id }" confirm="确认复制【${item.name }】？">复制</a>
							<a href="admin/node/basicItemNode/do_delete?id=${item.id }&isDelChil=false" confirm="确认删除？">删除</a>
							<a href="admin/module/configModule/do_add?moduleTitle=${item.name }&mappingId=${item.id }" confirm="确认生成？">生成模块</a>
						
						 --%>
						
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<div class="cpf-paginator" pageNo="${pageInfo.pageNo }" pageSize="${pageInfo.pageSize }" count="${pageInfo.count }"></div>
	</div>
</div>
<script>
	seajs.use(['dialog','utils', 'ajax'], function(Dialog, Utils, Ajax){
		var $page = $('#strucBase_list');

		// 添加弹出框
		$("form", $page).on("click", "#add", function() {
	        Dialog.openDialog("admin/structBase/addStruct", "创建结构体", "", {
	            width :1000,
	            height : 500
	        });
	    });

		// 修改弹出框
		$("tbody", $page).on("click", "#edit", function() {

			var sbId = $(this).attr("structId");
			
	        Dialog.openDialog("admin/structBase/editStruct?sbId=" + sbId, "修改结构体", "", {
	            width :1000,
	            height : 500
	        });
	    });
		
	$("tbody", $page).on("click", "#download", function (e) {
		var nodeId = $(this).attr("nodeId");
		
		Dialog.confirm('确认下载配置文件？', function(yes){
        	if(yes){
        		var url="admin/node/basicItemNode/download?nodeId=" + nodeId;
        		Ajax.download(url);
        	}
		});
		
  	 });  
	
	//预览弹出框
	$("tbody", $page).on("click", "#preview", function (e) {
		var h = $(window).height(); 
		var w = $(window).width();
		var width = w - 200;
		var height = h - 100;
		var nodeId = $(this).attr("nodeId");
		Dialog.openDialog("admin/node/basicItemNode/preview?nodeId="+nodeId,"预览", "basicItemNode_preview",{
			width:width,
			height:height
		});
  	 });  
	
	});
</script>