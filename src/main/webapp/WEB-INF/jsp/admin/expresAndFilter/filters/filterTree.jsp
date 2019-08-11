<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<link rel="stylesheet" href="media/admin/expresAndFilter/css/filterTree.css">
<script src="media/admin/plugins/beyond/js/select2/select2.js"></script>
<script src="media/admin/modelitem/js/Sortable.js"></script>
<script src="media/admin/expresAndFilter/js/filterTree.js"></script>

<div id="filterEdit" data-miCode="${miCode }" data-belongModel="${belongModel}" data-filterId="${filterId }" data-type="${type }">
		
		<div class="page-header">
			<div class="header-title">
				<h1>
					<c:if test="${type eq 0}">统计实体过滤条件</c:if>
					<c:if test="${type eq 1}">事实属性过滤条件</c:if>
					<c:if test="${type eq 2}">计算属性 过滤条件</c:if>
					<c:if test="${type eq 3}">配置文件过滤条件</c:if>
				</h1>
			</div>
			<div class="header-buttons">
				<a class="export btn-toggle" title="保存" id="btn-save" href="javascript:;">
					<i class="glyphicon glyphicon-check"></i>
				</a>
			</div>
		</div>
		
        <div class="entity-edit-wrap active">
            <!-- 实体标题:begin -->
            <div class="entity-title collapse-header al-save need-ajax" data-belongModel="${belongModel}" data-order="">
                <div class="icon-label-master">
                    <i class="icon-root icon"></i>
                    <span class="text">过滤条件</span>
                </div>
                <span class="span-title">xxxxxx</span>
                <span class="span-title">ddddddd</span>
               
                <div class="btn-wrap">
                	<!-- <i class="icon icon-save"></i> -->
                    <i style="margin: 12px;" class="icon icon-add"></i>
                    <!-- <i class="icon icon-trash"></i> -->
                    <%-- <a title="预览" style="font-size: 14px;position:absolute;right:98px;" nodeId="${item.id }" id="preview" href="javascript:;">预览</a> --%>
                    <i class="icon icon-arrow"></i>
                </div>
            </div>
            <!-- 实体标题:end -->
            <!-- 标签 不能拖拽 始终在第一个-->
           
            <!-- 拖拽排序wrap -->
            <ul class="dragEdit-wrap rootFilterNode dragEdit-wrap-1 collapse-content  collapse-content-active" id="dragEdit-1">
                <!-- 属性-->
               
                <!-- 属性组 -->
               
                <!-- 多值属性 -->
               
                <!-- 关系 -->

            </ul>

        </div>
    </div>
