<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div id="twoLevelAttr_list">
	<div class="row list-area">
		<table class="table">
			<thead>
				<tr>
					<th>序号</th>
					<th>级联属性编码</th>
					<th>孩子编码</th>
					<th>等级</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${miCascadeList }" var="item" varStatus="i">
					<tr>
						<td>${i.index + 1 }</td>
						<td>${item.code }</td>
						<td>${item.subCode }</td>
						<td>${item.level }</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<%-- <div class="cpf-paginator" pageNo="${pageInfo.pageNo }" pageSize="${pageInfo.pageSize }" count="${pageInfo.count }"></div> --%>
	</div>
</div>
