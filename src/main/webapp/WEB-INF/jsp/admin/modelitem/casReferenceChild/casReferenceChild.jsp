<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div id="casReferenceChild_list" pmiCode="${pmiCode }">
	<nav>
		<form class="form-inline" action="admin/modelItem/list">
			<a id="add" class="btn btn-primary tab" title="创建">创建</a>
		</form>
	</nav>
	<div class="row list-area">
		<table class="table">
			<thead>
				<tr>
					<th>序号</th>
					<th>级联引用编码</th>
					<th>孩子编码</th>
					<th>等级</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${miCascadeList }" var="item" varStatus="i">
					<tr>
						<td>${i.index + 1 }</td>
						<td>${item.code }</td>
						<td>${item.subCode }</td>
						<td>${item.level }</td>
						<td>
							<a href="admin/modelItem/delCasRefChild?code=${item.code }&subCode=${item.subCode}" confirm="确认删除？">删除</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<%-- <div class="cpf-paginator" pageNo="${pageInfo.pageNo }" pageSize="${pageInfo.pageSize }" count="${pageInfo.count }"></div> --%>
	</div>
</div>
<script>
	seajs.use(['dialog','utils', 'ajax'], function(Dialog, Utils, Ajax){
		var $page = $('#casReferenceChild_list');

		var pmiCode = $page.attr("pmiCode");
		$("form", $page).on("click", "#add", function() {
			
            Dialog.openDialog("admin/modelItem/addCasRefChild?pmiCode="+pmiCode, "创建", "", {
                width :600,
                height : 400
            });
        });

		$("tbody", $page).on("click", "#update", function() {
			var itemCode = $(this).attr("itemCode");
            Dialog.openDialog("admin/modelItem/update?itemCode=" + itemCode, "修改", "", {
                width :1000,
                height : 500
            });
        });

		$("tbody", $page).on("click", "#confRelation", function() {
			var itemCode = $(this).attr("itemCode");
            Dialog.openDialog("admin/modelItem/update?itemCode=" + itemCode, "修改", "", {
                width :1000,
                height : 500
            });
        });
	
	});
</script>