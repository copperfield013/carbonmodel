<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>

<link rel="stylesheet" href="media/admin/struct/css/structTree.css">
<script src="media/admin/plugins/beyond/js/select2/select2.js"></script>
<script src="media/admin/struct/js/Sortable.js"></script>
<script src="media/admin/struct/js/structTree.js"></script>

<div id="structBaseEdit" data-structBaseId="${strucBase.id}" data-miCode="${strucMiCode.itemCode }">
		
		<div class="entity_list clear-fix">					
			<div  class="entity_attr active">				
					${strucBase.title}				
			</div>			
		</div>
		
        <div class="entity-edit-wrap active">
            <!-- 实体标题:begin -->
            <div class="entity-title collapse-header al-save need-ajax" data-sbId="${strucBase.id}" data-miCode="${strucMiCode.itemCode }" >
                <div class="icon-label-master">
                    <i class="icon-root icon"></i>
                    <span class="text">struct</span>
                </div>
                <span class='span-title' title="结构体编码">${strucBase.id}</span>
                <span class='span-title' title="结构体名称">${strucBase.title}</span>
                <span class='span-title' title="实体名称">${modelItem.name}</span>
              
                <div class="btn-wrap">
                	<i class="icon icon-save"></i>
                    <i class="icon icon-add"></i>
                    <i class="icon icon-trash"></i>
                    <a title="预览" style="font-size: 14px;position:absolute;right:98px;" nodeId="${item.id }" id="preview" href="javascript:;">预览</a>
                    <i class="icon icon-arrow"></i>
                </div>
            </div>
            <!-- 实体标题:end -->
            <!-- 标签 不能拖拽 始终在第一个-->
           
            <!-- 拖拽排序wrap -->
            <ul class="dragEdit-wrap dragEdit-wrap-1 collapse-content  collapse-content-active" id="dragEdit-1">
                <!-- 属性-->
               
                <!-- 属性组 -->
               
                <!-- 多值属性 -->
               
                <!-- 关系 -->

            </ul>

        </div>
    </div>
