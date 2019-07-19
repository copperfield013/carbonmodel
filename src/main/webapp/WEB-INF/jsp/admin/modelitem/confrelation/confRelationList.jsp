<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div id="modelitemRelation_list">
	<nav>
		<form class="form-inline" action="admin/modelRelationType/list">
		
			<input type="hidden" class="form-control" name="leftModelCode" value="${modelRelationType.leftModelCode }" />
			<div class="form-group">
				<label for="name">关系编码</label>
				<input type="text" class="form-control" name="typeCode" value="${modelRelationType.typeCode }" />
			</div>
			<div class="form-group">
				<label for="name">关系名称</label>
				<input type="text" class="form-control" name="name" value="${modelRelationType.name }" />
			</div>
			<button type="submit" class="btn btn-default">查询</button>
			<a id="add" leftModelCode="${modelRelationType.leftModelCode }" class="btn btn-primary tab" title="创建">创建</a>
		</form>
	</nav>
	<div class="row list-area">
		<table class="table">
			<thead>
				<tr>
					<th>序号</th>
					<th>关系编码</th>
					<th>左模型编码</th>
					<th>关系名称</th>
					<th>右模型编码</th>
					<th>逆关系编码</th>
					<th>状态</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${modelRelaList }" var="item" varStatus="i">
					<tr>
						<td>${i.index + 1 }</td>
						<td>${item[0] }</td>
						<td>${item[8] }</td>
						<td>${item[1] }</td>
						<td>${item[9] }</td>
						<td>${item[4] }</td>
						
						<td>${item[6] }</td>
						<td>
							  <a id="update" typeCode="${item[0] }"href="javascript:;"  title="修改">修改</a>
							<c:if test="${item[6]  eq 2}">
								<a href="admin/modelRelationType/saveStatus?typeCode=${item[0] }&usingState=${item[6] }" confirm="改变状态？">解除弃用</a>
							</c:if>
							<c:if test="${item[6]  ne 2}">
								<a href="admin/modelRelationType/saveStatus?typeCode=${item[0] }&usingState=${item[6] }" confirm="改变状态？">弃用关系</a>
							</c:if>
							
							<a href="admin/modelRelationType/delete?typeCode=${item[0] }" confirm="确认删除？">删除</a>
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
		var $page = $('#modelitemRelation_list');

		$("form", $page).on("click", "#add", function() {


		var leftModelCode = $(this).attr("leftModelCode");
            Dialog.openDialog("admin/modelRelationType/add?leftModelCode=" + leftModelCode, "创建", "", {
                width :1000,
                height : 500
            });
        });

		$("tbody", $page).on("click", "#update", function() {

			
			var typeCode = $(this).attr("typeCode");
            Dialog.openDialog("admin/modelRelationType/edit?typeCode="+typeCode, "修改", "", {
                width :1000,
                height : 500
            });
        });


		
		
	
	
	});
</script>