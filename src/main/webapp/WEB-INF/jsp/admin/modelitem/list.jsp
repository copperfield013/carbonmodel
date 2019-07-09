<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div id="modelitem_list">
	<nav>
		<form class="form-inline" action="admin/modelItem/list">
			<div class="form-group">
				<label for="name">模型编码</label>
				<input type="text" class="form-control" name="code" value="${modelItem.code }" />
			</div>
			<div class="form-group">
				<label for="name">模型名称</label>
				<input type="text" class="form-control" name="name" value="${modelItem.name }" />
			</div>
			<button type="submit" class="btn btn-default">查询</button>
			<a id="add" class="btn btn-primary tab" title="创建">创建</a>
			<div id="createDBTable" style="float: right; font-size: 32px;    margin-right: 50px;">
				<a title="刷新表结构" href="admin/modelItem/createDBTable" confirm="确认刷新表结构"><i class="fa fa-sitemap"></i></a>
			</div>
		</form>
		
	</nav>
	<div class="row list-area">
		<table class="table">
			<thead>
				<tr>
					<th>序号</th>
					<th>实体类型</th>
					<th>模型编码</th>
					<th>模型名称</th>
					<th>状态</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${modelItemList }" var="item" varStatus="i">
					<tr>
						<td>${i.index + 1 }</td>
						<td><font color="${item.type eq '1'? '': '#00bcd4'}">${item.showType }</font></td>
						<td>${item.code }</td>
						<td>${item.name }</td>
						<td>${item.usingState }</td>
						<td>
							<a id="update" itemCode="${item.code }" href="javascript:;"  title="修改">修改</a>
							<a class="tab" href="admin/modelItem/itemtree?itemCode=${item.code }" title="管理子节点" target="modelItem_tree" >管理子节点</a>
							<a class="tab"  href="admin/modelRelationType/list?leftModelCode=${item.code }" title="配置<${item.name }>关系" target="modelItem_confRelationList" >配置关系</a>
							
							<a href="admin/structBase/quickCreateStrucBase?belongModel=${item.code }" confirm="确认生成【${item.name }】结构体？">生成结构体</a>
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
		var $page = $('#modelitem_list');

		$("form", $page).on("click", "#add", function() {
            Dialog.openDialog("admin/modelItem/add", "创建", "", {
                width :1000,
                height : 500
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