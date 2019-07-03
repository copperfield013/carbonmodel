seajs.use(['dialog','utils', 'ajax', '$CPF'], function(Dialog, Utils, Ajax, $CPF){
	var $page = $('#structTreeFieldAdd');	
	var sbId = $page.attr("sbId");
	var structType = $page.attr("structType");
	var sbPid = $page.attr("sbPid");
	var $structBaseForm = $page.find(".structBaseForm");
	
	$(function(){
		
		 Ajax.ajax('admin/structBase/getDefaultAttrByMType', {
			 sbId:sbId,
			 structType: structType,
			 sbPid:sbPid
		}, function(data) {			
			if (data.code == "400") {
				 Dialog.notice("属性初始化失败", "error");
				 $CPF.closeLoading();
			} else{
				var viewLabelList = data.viewLabelList;
				if (viewLabelList.length>0) {
					initAttrLabel(viewLabelList, $structBaseForm);
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
	//$structBaseForm  往这个内部插入这些标签
	function initAttrLabel(viewLabelList, $structBaseForm) {
		for ( var key in viewLabelList) {
			var htmLabel = DedeTagParse(viewLabelList[key]);
			$structBaseForm.prepend(htmLabel);
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
				strLabel+='<'+	viewLabel.label +' type="' +viewLabel.type+'" name="' +viewLabel.name+'" value="' +viewLabel.value +'" placeholder="不能为空" data-bv-notempty="true" data-bv-notempty-message="不能为空" class="form-control name '+viewLabel.viewClazz+'" />';
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
		} else if (viewLabel.label =="multSelect") {
			
			var strLabel = '<div class="form-group">';
			strLabel+= '<label class="col-lg-2 control-label " for="'+viewLabel.name+'">'+viewLabel.showName+'</label>';
			strLabel+= '<div class="col-lg-5">';
			strLabel+= '<select multiple="multiple"  class=" '+viewLabel.viewClazz+'" name="'+viewLabel.name+'">';
			//这里存放optiong
			debugger;
			
			var vDomainMap = viewLabel.valueDomain;
			for ( var key in vDomainMap) {
				
				if (viewLabel.value.indexOf(key) > -1) {
					strLabel+='<option selected value="'+key+'">'+vDomainMap[key]+'</option>';
				} else {
					strLabel+='<option value="'+key+'">'+vDomainMap[key]+'</option>';
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
		 
		 //这里获取属性的值
		 var formdom = $page.find(".structBaseForm")[0];
	     var fData = new FormData(formdom);
		 
		 Ajax.ajax('admin/structBase/createStrucBaseAttrField',fData, function(data) {			
			if (data.code == "400") {
				 Dialog.notice(data.msg, "error");
				 $CPF.closeLoading();
			} else{
				Dialog.notice(data.msg, "success");
				 ////
				var strucBase = data.strucBase;

				var page = $page.getLocatePage();
				if(page.getPageObj() instanceof Dialog){
					var afterSave = page.getPageObj().getEventCallbacks('afterSave');
					if(typeof afterSave == 'function'){
						afterSave.apply(page, [strucBase]);
					}
				}
				$page.getLocatePage().close();
				 /////
			}
			$CPF.closeLoading();
		}); 
	 });
	 
	 // 关系属性 改变的时候 ， 改变miCode
	 $($page).on("change", ".radioStrucRela", function() {
		 var relaCode = $(this).val();
		 
		 var $strucMiCodeItemCode = $(".strucMiCodeItemCode");
		 
		 if ($strucMiCodeItemCode.length>=1) {
			 Ajax.ajax('admin/modelRelationType/getModelRelation', {
				 typeCode:relaCode
	    	 }, function(data) {
	    		 
	    		 if (data.code==200) {
	    			 var modelRela = data.modelRelationType;
	    			 Ajax.ajax('admin//modelItem/getShowCode', {
	    				 miCode:modelRela.rightModelCode
	    	    	 }, function(data) {
	    	    		 var modelItemList = data.modelItemList;
	    	    		 var str = "";  
	    	    		 
	    	        	for (var key in modelItemList) { //遍历json数组时，这么写p为索引，0,1
	    	               str = str + "<option value=\"" + modelItemList[key].code + "\">" + modelItemList[key].name + "</option>"; 
	    	            }
	    	            	
	    	        		$strucMiCodeItemCode.empty().append(str);
	    	            }); 
	    			 
	    			 
	    		 } else {
	    			 
	    			 //这里报错
	    		 }
	    		
	            }); 
		 }
		 
	 });
	 
	  	//
	    $($page).on("change", ".strucMiCodeItemCode", function() {  
	    	var miCode = $(this).val();
	    	 var cnName = $(this).find("option:selected").text();
	    	$(".strucBaseTitle").val(cnName);
	    	
	    	var $strucFieldValueType = $(".strucFieldValueType");
	    	
	    	// 改变数据类型
	    	if ($strucFieldValueType.length>=1) {
	    		Ajax.ajax('admin/structBase/getValueType', {
		    		 miCode:miCode
		    	 }, function(data) {
		    		 var valueTypeMap = data.valueTypeMap;
		    		 var str = "";  
		    		 
	            	for (var key in valueTypeMap) { //遍历json数组时，这么写p为索引，0,1
	                   str = str + "<option value=\"" + key + "\">" + valueTypeMap[key] + "</option>"; 
	                }
	                	
	                	$strucFieldValueType.empty().append(str);
		            }); 
	    	 }
	    	
	    	 // 实体code， 改变的时候， 改变多选关系值域
	    	 
	    	 var $multStrucRela = $(".multStrucRela");
	    	 if ($multStrucRela.length>=1) {
	    		 // 获取左实体code
	    		var sbPid =  $("#structTreeFieldAdd").attr("sbPid");
	    		 debugger;
	    		Ajax.ajax('admin/structBase/getStrucMiCode', {
	    			sbId:sbPid
		    	 }, function(data) {
		    		 var strucMiCode = data.strucMiCode;
		    		 debugger;
		    		// 获取多选关系值域
		    		 Ajax.ajax('admin/modelRelationType/getModelRela', {
		    			 leftModelCode:strucMiCode.itemCode,
		    			rightModelCode:miCode
			    	 }, function(data) {
			    		 var modelRelaList = data.modelRelaList;
			    		 var str = "";  
			    		 debugger;
		            	for (var key in modelRelaList) { //遍历json数组时，这么写p为索引，0,1
		                   str = str + "<option value=\"" + modelRelaList[key].typeCode + "\">" + modelRelaList[key].name + "</option>"; 
		                }
		                	
		            	$multStrucRela.empty().append(str);
			            }); 
		    		 
		         }); 
	    		 
	    	 }
	    	 
	    });
	
});