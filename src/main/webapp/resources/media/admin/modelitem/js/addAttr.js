seajs.use(['dialog','utils', 'ajax', '$CPF'], function(Dialog, Utils, Ajax, $CPF){
	var $page = $('#itemtreeAttrAdd');	
	var modelItemCode = $page.attr("modelItemCode");
	var modelItemType = $page.attr("modelItemType");
	var mipCode = $page.attr("mipCode");
	var $modelItemForm = $page.find(".modelItemForm");
	
	$(function(){
		 Ajax.ajax('admin/modelItem/getDefaultAttrByMType', {
			 modelItemCode:modelItemCode,
			 modelItemType: modelItemType,
			 mipCode:mipCode
		}, function(data) {			
			if (data.code == "400") {
				 Dialog.notice("属性初始化失败", "error");
				 $CPF.closeLoading();
			} else{
				
				var viewLabelList = data.viewLabelList;
				if (viewLabelList.length>0) {
					initAttrLabel(viewLabelList, $modelItemForm);
					$CPF.closeLoading();
				}
				
				//select 格式化
				var $select =  $page.find("select");
				 $select.css("width","100%").select2();
			}
			
			$CPF.closeLoading();
		}); 
		    
	})
	
	//初始化HTML标签
	//$modelItemForm  往这个内部插入这些标签
	function initAttrLabel(viewLabelList, $modelItemForm) {
		for ( var key in viewLabelList) {
			
			var htmLabel = DedeTagParse(viewLabelList[key]);
			$modelItemForm.prepend(htmLabel);
		}
		
	}
	
	//标签解析器， 返回正确的标签
	function DedeTagParse(viewLabel) {
		if (viewLabel.label =="input") {
			if (viewLabel.type == "hidden") {
				var strLabel = '<'+	viewLabel.label +' type="' +viewLabel.type+'" name="'+viewLabel.name+'" value="'+viewLabel.value +'" />';
				return strLabel;
			} else if(viewLabel.type == "text") {
				var strLabel = '<div class="form-group">';
				strLabel+='<label class="col-lg-2 control-label" for="'+viewLabel.name+'">'+viewLabel.showName+'</label>';
				strLabel+='<div class="col-lg-5">';
				strLabel+='<input type="' +viewLabel.type+'" name="' +viewLabel.name+'" value="' +viewLabel.value +'" placeholder="不能为空"   data-bv-notempty="true" data-bv-notempty-message="不能为空"   class="form-control '+viewLabel.viewClazz+' name" />';
				strLabel+='</div></div>';
				
				return strLabel;
			}  else if(viewLabel.type == "number") {
				var strLabel = '<div class="form-group">';
				strLabel+='<label class="col-lg-2 control-label" for="'+viewLabel.name+'">'+viewLabel.showName+'</label>';
				strLabel+='<div class="col-lg-5">';
				strLabel+='<input type="number" name="' +viewLabel.name+'" value="' +viewLabel.value +'" placeholder="只能为大于0数字"    data-bv-notempty="true" data-bv-notempty-message="不能为空"   class="form-control '+viewLabel.viewClazz+' name" />';
				strLabel+='</div></div>';
				
				return strLabel;
			} else if (viewLabel.type == "radio") {
				var checkedstr1 = "";
				var checkedstr0 = "checked";
				if (viewLabel.value == 1) {
					checkedstr1 = "checked";
					checkedstr0 = "";
				} 
				
			var strLabel ='<div class="form-group">';
			strLabel += ' <label class="col-lg-2 control-label" for="'+viewLabel.name+'">'+viewLabel.showName+'</label>';
			strLabel += ' <div class="col-lg-5">';
			strLabel += ' <input name="'+viewLabel.name+'" type="radio" value="1" style="opacity:1;position: static;height:13px;" '+checkedstr1+'/> ';
			strLabel += ' <label for="1">是</label> ';
			strLabel += ' <input name="'+viewLabel.name+'" type="radio" value="0" style="opacity:1;position: static;height:13px;" '+checkedstr0+'/> ';
			strLabel += ' <label for="0">否</label> ';
				
			return strLabel;	
			}
		} else if (viewLabel.label =="textarea"){
			
			var strLabel = '<div class="form-group">';
			strLabel+= '<label class="col-lg-2 control-label" for="'+viewLabel.name+'">'+viewLabel.showName+'</label>';
			strLabel+= '<div class="col-lg-5">';
			strLabel+= '<textarea  class="form-control" name="'+viewLabel.name+'">' + viewLabel.value;
			strLabel+='</textarea></div></div>';
			return strLabel;
		} else if (viewLabel.label =="select") {
			var strLabel = '<div class="form-group">';
			strLabel+= '<label class="col-lg-2 control-label " for="'+viewLabel.name+'">'+viewLabel.showName+'</label>';
			strLabel+= '<div class="col-lg-5">';
			strLabel+= '<select  class=" '+viewLabel.viewClazz+'" name="'+viewLabel.name+'">';
			//这里存放optiong
			var vDomainMap = viewLabel.valueDomain;
			for ( var key in vDomainMap) {
				if (viewLabel.value == key) {
					strLabel+='<option selected value="'+key+'">'+vDomainMap[key]+'</option>   ';
				} else {
					strLabel+='<option value="'+key+'">'+vDomainMap[key]+'</option>   ';
				}
			}
			strLabel+='</select></div></div>';
			return strLabel;
		}
		
		return "";
	}
	$('form', $page).submit(function(e){
		
		e.stopDefault();
		return false;
	});
	
	 $($page).on("click", "#btn-save", function (e) {
		 
		 if ($(".required").val().length <=0) {
			 Dialog.notice("字段不能为空！！", "warning");
			 return false;
		 }
		 
		 //这里获取属性的值
		 var formdom = $page.find(".modelItemForm")[0];
	     var fData = new FormData(formdom);
		 
		 Ajax.ajax('admin/modelItem/createModelItemAttr',fData, function(data) {			
			if (data.code == "400") {
				 Dialog.notice(data.msg, "error");
				 $CPF.closeLoading();
			} else{
				Dialog.notice(data.msg, "success");
				 ////
				var modelItem = data.modelItem;

				var page = $page.getLocatePage();
				if(page.getPageObj() instanceof Dialog){
					var afterSave = page.getPageObj().getEventCallbacks('afterSave');
					if(typeof afterSave == 'function'){
						afterSave.apply(page, [modelItem]);
					}
				}
				$page.getLocatePage().close();
				 /////
			}
			$CPF.closeLoading();
		}); 
	 });
	 
	 
	 //数据类型改变的时候， 数据长度也需要改变
	 $($page).on("change", ".miValueDataType", function() {  
		 var dataType = $(this).val();
		var $dataLength =  $page.find(".miValueDataLength");
		 
		$dataLength.closest(".form-group").show();
		 switch (dataType) {
			case "101":
				$dataLength.val("11");
				break;
			case "102":
				$dataLength.val("10,2");
				break;
			case "103":
				$dataLength.val("20");
				break;
			case "104":
				$dataLength.val("10,2");
				break;
			case "105":
				$dataLength.val("32");
				break;
			case "106":
				$dataLength.val("");
				$dataLength.closest(".form-group").hide();
				break;
			case "107":
				$dataLength.val("3");
				$dataLength.closest(".form-group").hide();
				break;
			case "108":
				$dataLength.val("3");
				$dataLength.closest(".form-group").hide();
				break;
		 }
	 })
	 
	  //引用属性   中  引用实体改变的时候   识别属性和展示属性需要动态改变
	    $($page).on("change", ".miReferenceModelCode", function() {  
	    	
	    	var miCode = $(this).val();
	    	var $selectRecogn = $page.find(".miReferenceRecognitionCode");
	    	var $selectShow = $page.find(".miReferenceShowCode");
	    	
	    	 Ajax.ajax('admin/modelItem/getShowCode', {
	    		 miCode:miCode
	    	 }, function(data) {
	    		 var modelItemList = data.modelItemList;
	    		 var str = "<option value=''>---请选择---</option>"; 
                	for (var p in modelItemList) { //遍历json数组时，这么写p为索引，0,1
                       str = str + "<option value=\"" + modelItemList[p].code + "\">" + modelItemList[p].name + "</option>"; 
                    }
                	//识别属性
                	$selectShow.empty().append(str);
                	$selectShow.trigger("change");
                	
                	//展示属性
                	$selectRecogn.empty().append(str);
                	$selectRecogn.trigger("change");
	            }); 
	    	 
	    	/* Ajax.ajax('admin/modelItem/getShowCode', {
	    		 miCode:miCode
	    	 }, function(data) {
	    		 var modelItemList = data.modelItemList;
	    		 var str1 = "<option value=''>---请选择---</option>"; 
                	for (var p in modelItemList) { //遍历json数组时，这么写p为索引，0,1
                       str1 = str1 + "<option value=\"" + modelItemList[p].code + "\">" + modelItemList[p].name + "</option>"; 
                    }
                	
                	$selectRecogn.empty().append(str1);
                	$selectRecogn.trigger("change");
	            }); */
	    });
	
});