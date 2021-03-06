<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div id="serviceBizzData-list">
	<nav>
		<form class="form-inline" action="admin/dataservice/serviceBizzData/list">
			 <div class="form-group">
				<label for="name">数据服务</label>
				<input type="text" class="form-control" name="name" value="${criteria.name }" />
			</div>
			<button type="submit" class="btn btn-default">查询</button>
			<a class="btn btn-primary tab" href="admin/dataservice/serviceBizzData/add" title="创建" target="serviceBizzData_add" >创建</a>
		</form>
	</nav>
	<div class="row list-area">
		<table class="table">
			<thead>
				<tr>
					<th>序号</th>
					<th>IP</th>
					<th>端口</th>
					<th>服务名</th>
					<th>描述</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${list }" var="item" varStatus="i">
					<tr>
						<td>${i.index + 1 }</td>
						<td>${item.ip }</td>
						<td>${item.port }</td>
						<td>${item.name }</td>
						<td>${item.describe }</td>
						<td>
							
							<a href="admin/dataservice/serviceBizzData/testService?id=${item.id }">
							<font color="<c:if test="${item.state eq 1 }">green</c:if><c:if test="${item.state eq 2 }">red</c:if>">
							测试
							</font>
							</a>
							
							 <a class="tab" href="admin/dataservice/serviceBizzData/edit?id=${item.id }" title="编辑" target="serviceBizzData_edit" >编辑</a>
							<a href="admin/dataservice/serviceBizzData/do_delete?id=${item.id }" confirm="确认移除？">移除</a>
							<a href="admin/dataservice/serviceBizzData/refreshERXmlDom?id=${item.id }" confirm="确认刷新？">刷新应用配置</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		 <div class="cpf-paginator" pageNo="${pageInfo.pageNo }" pageSize="${pageInfo.pageSize }" count="${pageInfo.count }"></div>
	</div>
</div>
<script>
	seajs.use(['utils'], function(Utils){
		var $page = $('#serviceBizzData-list');
		Utils.datepicker($('#date', $page));
	});
</script>