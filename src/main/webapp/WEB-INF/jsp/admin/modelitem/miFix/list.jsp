<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div id="modelItemFix_list">
	<nav>
		<form class="form-inline" action="admin/module/configModule/list">
			<%--  <div class="form-group">
				<label for="name">模块</label>
				<input type="text" class="form-control" name="moduleName" value="${criteria.moduleName }" />
			</div>
			<button type="submit" class="btn btn-default">查询</button> --%>
			<a class="btn btn-primary tab" href="admin/modelItemFix/add" title="创建" target="miFix_add" >创建</a>
		</form>
	</nav>
	<div class="row list-area">
		<table class="table">
			<thead>
				<tr>
					<th>序号</th>
					<th>前缀</th>
					<th>状态</th>
					<th>描述</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${modelItemFixList }" var="item" varStatus="i">
					<tr>
						<td>${i.index + 1 }</td>
						<td>${item[1] }</td>
						<td>${item[2] eq 1?'启用':'弃用' }</td>
						<td>${item[3] }</td>
						<td>
							<a class="tab" href="admin/modelItemFix/edit?id=${item[0] }" title="编辑" target="miFix_edit" >编辑</a>
							<%-- <a href="javascript:;" itemId="${item[0] }" title="修改" id="edit">修改</a> --%>
							<a href="admin/modelItemFix/do_delete?id=${item[0] }" confirm="确认删除？">删除</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<%--  <div class="cpf-paginator" pageNo="${pageInfo.pageNo }" pageSize="${pageInfo.pageSize }" count="${pageInfo.count }"></div> --%>
	</div>
</div>
<script>
seajs.use(['dialog','utils'], function(Dialog, Utils){
		
		var $page = $('#modelItemFix_list');
		
		$("tbody", $page).on("click", "#edit", function() {
		
            var itemId=$(this).attr("itemId");
            Dialog.openDialog("admin/modelItemFix/edit?id="+itemId, "修改", "", {
                width :600,
                height : 300
            });
        });
	});
</script>