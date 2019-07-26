
seajs.use(['dialog','utils', 'ajax', '$CPF'], function(Dialog, Utils, Ajax, $CPF){
	var $page = $('#structBaseEdit');	
	
	var structBaseId = $page.attr("data-structBaseId");
	var miCode = $page.attr("data-miCode"); // 结构体对应的实体code
	
    $(function(){
	    $CPF.showLoading();
	    drag($(".dragEdit-wrap", $page).length);       
	    //初始化  本模型的group 即 本模型的孩子
	    //belongModel 指的事父code
	    initStructBaseChild(structBaseId);
	    
	    $(".label-bar", $page).addClass("al-save");
	    $CPF.closeLoading();
    })
    
     //初始化  本模型的group 即 本模型的孩子
    function initStructBaseChild(sbPid) {
		//获取本模型有哪些组
    	Ajax.ajax('admin/structBase/getStructStairChild', {
    		sbPid: sbPid
		}, function(data) {			
			if (data.code == "400") {
				 Dialog.notice(data.msg, "error");
				 $CPF.closeLoading();
			} else{
				
				var strucBaseList = data.strucBaseList;
				var $parent = $(".collapse-header[data-sbid='"+sbPid+"']", $page).next(".collapse-content")[0];	
				for ( var key in strucBaseList) {
					var strucBase = strucBaseList[key];
					
					// 通用初始化属性
					if (strucBase.type == 101) {//单行属性分组
						initGroup(strucBase, $parent);
					} else if (strucBase.type == 102) {
						initMoreGroup(strucBase, $parent);
					} else if (strucBase.type == 103) {
						initRStruc(strucBase, $parent);
					}else {
						initCommAttr(strucBase, $parent);
					}
					
				}
				
			}
			
			$CPF.closeLoading();
		});
	}
    
    //结构体和关系结构， 跳转到过滤条件定义页面
    $("#structBaseEdit").on("click", ".strucFilterView", function() {    
       // 结构体 ， 属性过滤
        var sbId = $(this).closest(".collapse-header").attr("data-sbid");
        Dialog.openDialog("admin/expressionAndFilter/skipFilter?type=3&miCode=" + sbId, "过滤条件页面", "", {
            width :1000,
            height : 500
        });
        
    });
    
    /**
     * 二维组添加过滤条件
     */
    $("#structBaseEdit").on("click", ".strucGroup2DFilterView", function() {    
        // 结构体 ， 属性过滤
         var sbId = $(this).closest(".collapse-header").attr("data-sbid");
         Dialog.openDialog("admin/expressionAndFilter/skipFilter?type=5&miCode=" + sbId, "过滤条件页面", "", {
             width :1000,
             height : 500
         });
         
     });
    
    
    
    //	字段组初始化方法
    function initGroup(strucBase, $parent) {
    	var attrGroupHtml = getGroupNode(strucBase);
	    var $html = $(attrGroupHtml).prependTo($($parent));
	   /* $html.find("select").css({"width":"7%","marginLeft":"2"}).select2();*/
        drag($(".dragEdit-wrap").length);
    }
    
    /**
     * 字段组
      */
    function addGroup(el, strucBase) {// 这个有用
        var $content = $(el).closest(".collapse-header").siblings(".collapse-content");
        var attrGroupHtml = getGroupNode(strucBase);
        var $html = $(attrGroupHtml).prependTo($content);  
	    $html.find("select").css({"width":"7%","marginLeft":"2"}).select2();
	    addUnfold(el)
        drag($(".dragEdit-wrap").length);
    };
    
  //字段组
    function getGroupNode(strucBase) {
    	var dragWrapLen = $(".dragEdit-wrap").length + 1 ;
        var attrGroupHtml = "<li class='attr-group'>" +
            "<div class='attr-group-title collapse-header' data-sbid='"+strucBase.id+"'  data-structType='"+strucBase.type+"' data-sbPid='"+strucBase.parentId+"'>" +
            "<div class='icon-label attr-group'>" +
            "<i class='icon icon-attr-group'></i>" +
            "<span class='text'>"+strucBase.showType+"</span>" +
            "</div>" +
            "<div class='label-bar al-save attr-group'>" +
            "<span id='spanCode' class='span-title'>"+strucBase.id+"</span>"+
            "<span id='spanName' class='span-title'>"+strucBase.title+"</span>";
	         attrGroupHtml += "<div class='btn-wrap'>" +
	        /* "<i class='glyphicon glyphicon-pencil'></i>"+*/
	         "<i class='icon fa fa-edit icon-edit'></i>"+
            "<i class='icon icon-add-sm group'></i>" +
            
            	"<i class='icon glyphicon glyphicon-trash delStruct'></i>" +
            "<i class='icon icon-arrow-sm active'></i>" +
            "</div>" +
            "</div>" +
            "</div>" +
            "<ul class='attr-group-drag-wrap dragEdit-wrap collapse-content collapse-content-inactive need-ajax' id='dragEdit-"+dragWrapLen+"'>" +
            "</ul>" +
            "</li>";
	         
	  return attrGroupHtml;
    }
    
    //多行组初始化方法
    function initMoreGroup(strucBase, $parent) {
    	var attrGroupHtml = getGroupMoreNode(strucBase);
	    var $html = $(attrGroupHtml).prependTo($($parent));
	   /* $html.find("select").css({"width":"7%","marginLeft":"2"}).select2();*/
        drag($(".dragEdit-wrap").length);
    }
    
    /**
     * 多行组
      */
    function addMoreGroup(el, strucBase) {// 这个有用
        var $content = $(el).closest(".collapse-header").siblings(".collapse-content");
        var attrGroupHtml = getGroupMoreNode(strucBase);
        var $html = $(attrGroupHtml).prependTo($content);  
	    $html.find("select").css({"width":"7%","marginLeft":"2"}).select2();
	    addUnfold(el)
        drag($(".dragEdit-wrap").length);
    };
    
    //多行组
    function getGroupMoreNode(strucBase) {
    	var dragWrapLen = $(".dragEdit-wrap").length + 1 ;
        var attrGroupHtml = "<li class='attr-group'>" +
            "<div class='attr-group-title collapse-header'   data-sbid='"+strucBase.id+"'  data-structType='"+strucBase.type+"' data-sbPid='"+strucBase.parentId+"'>" +
            "<div class='icon-label more-attr'>" +
            "<i class='icon icon-attr-group'></i>" +
            "<span class='text'>"+strucBase.showType+"</span>" +
            "</div>" +
            "<div class='label-bar al-save attr-group'>" +
            "<span id='spanCode' class='span-title'>"+strucBase.id+"</span>"+
            "<span id='spanName' class='span-title'>"+strucBase.title+"</span>";
	         attrGroupHtml += "<div class='btn-wrap'>" +
	         "<i class='icon glyphicon glyphicon-filter strucGroup2DFilterView'></i>"+
	         "<i class=' icon fa fa-edit icon-edit'></i>"+
            "<i class='icon icon-add-sm group'></i>" +
            
            	"<i class='icon glyphicon glyphicon-trash delStruct'></i>" +
            "<i class='icon icon-arrow-sm active'></i>" +
            "</div>" +
            "</div>" +
            "</div>" +
            "<ul class='attr-group-drag-wrap dragEdit-wrap collapse-content collapse-content-inactive need-ajax' id='dragEdit-"+dragWrapLen+"'>" +
            "</ul>" +
            "</li>";
	         
	  return attrGroupHtml;
    }
    
    //	关系结构初始化方法
    function initRStruc(strucBase, $parent) {
    	var attrGroupHtml = getRStrucNode(strucBase);
	    var $html = $(attrGroupHtml).prependTo($($parent));
	   /* $html.find("select").css({"width":"7%","marginLeft":"2"}).select2();*/
        drag($(".dragEdit-wrap").length);
    }
    
    /**
     * 关系结构
      */
    function addRStruc(el, strucBase) {// 这个有用
        var $content = $(el).closest(".collapse-header").siblings(".collapse-content");
        var attrGroupHtml = getRStrucNode(strucBase);
        var $html = $(attrGroupHtml).prependTo($content);  
	    $html.find("select").css({"width":"7%","marginLeft":"2"}).select2();
	    addUnfold(el)
        drag($(".dragEdit-wrap").length);
    };
    
    //关系结构
    function getRStrucNode(strucBase) {
    	var dragWrapLen = $(".dragEdit-wrap").length + 1 ;
        var attrGroupHtml = "<li class='attr-group'>" +
            "<div class='attr-group-title collapse-header' data-sbid='"+strucBase.id+"'  data-structType='"+strucBase.type+"' data-sbPid='"+strucBase.parentId+"'>" +
            "<div class='icon-label attr-relative'>" +
            "<i class='icon icon-attr-relative'></i>" +
            "<span class='text'>"+strucBase.showType+"</span>" +
            "</div>" +
            "<div class='label-bar al-save attr-group'>" +
            "<span id='spanCode' class='span-title'>"+strucBase.id+"</span>"+
            "<span id='spanName' class='span-title'>"+strucBase.title+"</span>";
	         attrGroupHtml += "<div class='btn-wrap'>" +
	        /* "<i class='glyphicon glyphicon-pencil'></i>"+*/
	         "<i class='icon glyphicon glyphicon-filter strucFilterView'></i>"+
	         "<i class='icon fa fa-edit icon-edit'></i>"+
            "<i class='icon icon-add'></i>" +
            
            	"<i class='icon glyphicon glyphicon-trash delStruct'></i>" +
            "<i class='icon icon-arrow-sm active'></i>" +
            "</div>" +
            "</div>" +
            "</div>" +
            "<ul class='attr-group-drag-wrap dragEdit-wrap collapse-content collapse-content-inactive need-ajax' id='dragEdit-"+dragWrapLen+"'>" +
            "</ul>" +
            "</li>";
	         
	  return attrGroupHtml;
    }
    
    /**
     * 添加通用属性方法
      */
    function addCommAttr(el, strucBase) {// 这个有用
        var $content = $(el).closest(".collapse-header").siblings(".collapse-content");
        var attrGroupHtml = getCommAttrNode(strucBase);
        var $html = $(attrGroupHtml).prependTo($content);  
	    $html.find("select").css({"width":"7%","marginLeft":"2"}).select2();
	    addUnfold(el)
        drag($(".dragEdit-wrap").length);
    };
    
    //通用属性初始化方法
    function initCommAttr(strucBase, $parent) {
    	var attrGroupHtml = getCommAttrNode(strucBase);
	    var $html = $(attrGroupHtml).prependTo($($parent));
	   /* $html.find("select").css({"width":"7%","marginLeft":"2"}).select2();*/
        drag($(".dragEdit-wrap").length);
    }
    
    //通用属性节点
    function getCommAttrNode(strucBase) {
    	var dragWrapLen = $(".dragEdit-wrap").length + 1 ;
        var attrGroupHtml = "<li class='add-attr clear-fix'>" +
            "<div class='attr-group-title collapse-header'  data-sbid='"+strucBase.id+"'  data-structType='"+strucBase.type+"' data-sbPid='"+strucBase.parentId+"'>" +
            "<div class='icon-label attr'>" +
            "<i class='icon icon-attr'></i>" +
            "<span class='text'>"+strucBase.showType+"</span>" +
            "</div>" +
            "<div class='label-bar al-save attr'>" +
            "<span id='spanCode' class='span-title'>"+strucBase.id+"</span>"+
            "<span id='spanName' class='span-title'>"+strucBase.title+"</span>";
	         attrGroupHtml += "<div class='btn-wrap'>" +
	        /* "<i class='glyphicon glyphicon-pencil'></i>"+*/
	         "<i class='icon fa fa-edit icon-edit'></i>"+
	         "<i class='icon glyphicon glyphicon-trash delStruct'></i>" +
           /* "<i class='icon icon-add-sm group'></i>" +*/
           /* "<i class='icon delStruct'></i>" +*/
            /*"<i class='icon icon-arrow-sm active'></i>" +*/
            "</div>" +
            "</div>" +
            "</div>" +
            "<ul class='attr-group-drag-wrap dragEdit-wrap collapse-content collapse-content-inactive need-ajax' id='dragEdit-"+dragWrapLen+"'>" +
            "</ul>" +
            "</li>";
	         
	  return attrGroupHtml;
    }
    
	function addUnfold(el) {	
		
		if($(el).hasClass("icon-add") && $(el).siblings(".icon-arrow").hasClass("active")) {
        	$(el).siblings(".icon-arrow").trigger("click");        	
        }else if($(el).hasClass("icon-add-sm") && $(el).siblings(".icon-arrow-sm").hasClass("active")){
        	$(el).siblings(".icon-arrow-sm").trigger("click");
        }else if($(el).hasClass("icon-add-abc") && $(el).siblings(".icon-arrow-sm").hasClass("active")){
        	$(el).siblings(".icon-arrow-sm").trigger("click");
        } else if($(el).hasClass("icon-add-filterGroup") && $(el).siblings(".icon-arrow-sm").hasClass("active")){
        	$(el).siblings(".icon-arrow-sm").trigger("click");
        } else if($(el).hasClass("icon-add-filter") && $(el).siblings(".icon-arrow-sm").hasClass("active")){
        	$(el).siblings(".icon-arrow-sm").trigger("click");
        }
	} 	
	
	function addEntityOPT() {
		var $select = $("#structBaseEdit .entity-title").find(".node-ops-type");	
		var selectedVal = $select.attr("data-val");
		var html = "";		
		
		var nodePosType = nodePosTypeABC;
	    for(var i=0; i<nodePosType.length; i++) {
	    	if(nodePosType[i] === selectedVal ) {
	    		html += "<option value='"+nodePosType[i]+"' selected>"+nodePosType[i]+"</option>";  	
	    	}else {
	    		html += "<option value='"+nodePosType[i]+"'>"+nodePosType[i]+"</option>"; 
	    	}
            	         
         };                
         $select.append(html);              
         $select.css({"width":"12%","font-size":"18px","marginLeft":"20px","margin-top": "-12px"}).select2();
	}
	
	 //拖拽排序方法
    function drag(length) {/*
    	
    	var dragWrap = document.getElementById("dragEdit-"+length); 
		var name = "dragEdit-"+length;		
    	Sortable.create(dragWrap, {
	        group: {
	            name: name,
	            pull: false,
	            put: false
	        },
	        filter: ".no-dragger",	
	        handle: ".icon-label",
	        sort: true,
	        forceFallback: true, 
	        animation: 100,
	        onStart: function (evt) {
//	        	judgeSave();
	        },
	        onEnd: function (evt) {
	        	var current = $(evt.item);
	        	var before = $(evt.item).prev("li");
	        	var after = $(evt.item).next("li");
	        	var currentId = "";
	        	var beforeId = "";
	        	var afterId = "";	        	
	        	if(before.length == 0) {	        		
	        		beforeId = "";
	        	}else if(before.hasClass("attr-group") || before.hasClass("more-attr") || before.hasClass("attr-relative") || before.hasClass("entity-ch-wrap")){	        			        		
	        		beforeId = before.children(".collapse-header").attr("data-id");
	        	}else {	        		
	        		beforeId = before.children(".label-bar").attr("data-id");
	        	}
	        	
	        	if(after.length == 0) {	        		
	        		afterId = "";
	        	}else if(after.hasClass("attr-group") || after.hasClass("more-attr") || after.hasClass("attr-relative") || after.hasClass("entity-ch-wrap")){
	        		afterId = after.children(".collapse-header").attr("data-id");
	        	}else {
	        		afterId = after.children(".label-bar").attr("data-id");
	        	}	        	
	        	if(current.hasClass("attr-group") || current.hasClass("more-attr") || current.hasClass("attr-relative") || current.hasClass("entity-ch-wrap")){	        			        		
	        		currentId = current.children(".collapse-header").attr("data-id");
	        	}else {	        		
	        		currentId = current.children(".label-bar").attr("data-id");
	        	}
	        	$CPF.showLoading();
	    		Ajax.ajax('admin/node/basicItemNode/nodeSort', {
	    			currentId: currentId,
	    			beforeId: beforeId,
	    			afterId: afterId
	    		 }, function(data) {
	    			 console.log(data);
	    			 $CPF.closeLoading();
	    	    });	        	
	        }
	    });     
    */};
    
  //普通 引用->引用属性初始化方法
    function initrRefattribute(abcattr,abcattrCode,dataType,id,name,opt,order,parent,commList, relAbcnodeId, allAbc, subdomain, rRefattributeList) {  
    	var dataTypeList = dataTypeCASCADETYPEList;
			var attrHtml = "<li class='add-attr clear-fix'>"
				+"<div class='icon-label attr' data-type='15'>";
            if(abcattrCode=="") {
            	attrHtml=attrHtml+"<i class='icon icon-error-cross'></i>";
            }
            attrHtml=attrHtml+"<i class='icon icon-attr'></i>" +
            "<span class='text'>R引用属性</span>" +
            "</div>" +
            "<div class='label-bar refattribute-attr al-save' data-type='15'  data-order='"+order+"' data-id='"+id+"'>" +
            "<input type='text' disabled class='edit-input text' value='"+name+"'>" +
            "<select disabled class='abc-attr'>"            
            for(var i=0; i<commList.length; i++) {  
            	if(commList[i][0] == abcattrCode) {
            		attrHtml += "<option item-data-type='"+commList[i][2]+"' data-id='"+commList[i][0]+"' value='"+commList[i][1]+"' selected>"+commList[i][1]+"</option>";
            		
            		if ("5" == commList[i][2]) {
    					dataTypeList=dataTypeSTRINGList;
    				} else if ("6"== commList[i][2]) {
    					dataTypeList=dataTypeDATEList;
    				}else if ("7"== commList[i][2]) {
    					dataTypeList=dataTypeTIMEList;
    				}else if ("1"== commList[i][2]) {
    					dataTypeList=dataTypeINTList;
    				}else if ("15"== commList[i][2]) {
    					dataTypeList=dataTypeDOUBLEList;
    				}else if ("11"== commList[i][2]) {
    					dataTypeList=dataTypeREFERENCEList;
    				}else if ("8"== commList[i][2]) {
    					dataTypeList=dataTypeFILEList;
    				}else if ("14"== commList[i][2]) {
    					dataTypeList=dataTypeENUMList;
    				}else if ("17"== commList[i][2]) {
    					dataTypeList=dataTypeCASCADETYPEList;
    				}
            	}else {
            		attrHtml += "<option item-data-type='"+commList[i][2]+"' data-id='"+commList[i][0]+"' value='"+commList[i][1]+"'>"+commList[i][1]+"</option>";
            	}
            	
            }
			attrHtml += "</select>";
			attrHtml += "<select disabled class='data-type attr-type'>";    
			
		    	for(var i=0; i<dataTypeList.length; i++) {
		    		if(dataTypeList[i][0] == dataType) {
		    			attrHtml += "<option value='"+dataTypeList[i][0]+"' selected>"+dataTypeList[i][1]+"</option>";
		    		}else {
		    			attrHtml += "<option value='"+dataTypeList[i][0]+"'>"+dataTypeList[i][1]+"</option>";
		    		}
	            	          
	            };
	            attrHtml += "</select>";
				attrHtml += "<select disabled class='node-ops-type'>";	
				var nodePosType=nodePosTypeREFATTRIBUTE;
			    for(var i=0; i<nodePosType.length; i++) {
			    	if(nodePosType[i] == opt) {
			    		attrHtml += "<option value='"+nodePosType[i]+"' selected>"+nodePosType[i]+"</option>";
			    	}else {
			    		attrHtml += "<option value='"+nodePosType[i]+"'>"+nodePosType[i]+"</option>";
			    	}
		            	          
		        };
		        attrHtml += "</select>";
		        
		        attrHtml +="<select disabled class='subdomain'>"
		        	for(var i=0; i<rRefattributeList.length; i++) {            	
		             	attrHtml += "<option data-id='"+rRefattributeList[i][0]+"' value='"+rRefattributeList[i][1]+"' item-data-type='"+rRefattributeList[i][2]+"'>"+rRefattributeList[i][1]+"</option>";                
		             }
		 			attrHtml += "</select>";
		        
		        attrHtml += "<select disabled class='relAbcnodeId'>";
			    for(var i=0; i<allAbc.length; i++) {
			    	if (relAbcnodeId == allAbc[i].id) {
			    		attrHtml += "<option selected='selected' value='"+allAbc[i].id+"'>"+allAbc[i].name+"</option>"; 
			    	} else {
			    		attrHtml += "<option value='"+allAbc[i].id+"'>"+allAbc[i].name+"</option>"; 
			    	}
		         };
			         
		        attrHtml += "</select>";
		        attrHtml += "<div class='btn-wrap'>" +
		        "<i class='icon icon-save'></i>" +
		        "<i class='icon delStruct'></i>" +
		        "<i class='icon-simulate-trashsm'></i>" +
		        "</div>" +
		        "</div>" +
		        "</li>";		           		        
		        var $html = $(attrHtml).prependTo($(parent));
		        $html.find("select").css({"width":"11%","marginLeft":"16px"}).select2();
    }
    
    
	
    //多值属性下的属性初始化方法
    function initAttrM(abcattr,abcattrCode,dataType,id,name,opt,order,parent,repeatChildList) {  
    	/*var repeatId = $(parent).prev(".collapse-header")
			.find(".abc-attr")
			.find("option:selected")
			.attr("data-id");*/
    	
    	var dataTypeList = dataTypeSTRINGList;
			var attrHtml = "<li class='add-attr clear-fix'>"
				+ "<div class='icon-label attr'>";
			 if(abcattrCode=="") {
				 attrHtml = attrHtml+"<i class='icon icon-error-cross'></i>";
		    }
			 attrHtml = attrHtml+"<i class='icon icon-attr'></i>" +
            "<span class='text'>属性</span>" +
            "</div>" +
            "<div class='label-bar attr  al-save' data-order='"+order+"' data-id='"+id+"'>" +
            "<input type='text' disabled class='edit-input text' value='"+name+"'>" +
            "<select disabled class='abc-attr'>"  
            for(var i=0; i<repeatChildList.length; i++) {    
            	
            	if(repeatChildList[i].cnName == abcattr) {
            		attrHtml += "<option item-data-type='"+repeatChildList[i].oneLevelItem.dataType+"' data-id='"+repeatChildList[i].code+"' value='"+repeatChildList[i].cnName+"' selected>"+repeatChildList[i].cnName+"</option>";
            		
            		if ("5" == repeatChildList[i].oneLevelItem.dataType) {
    					dataTypeList=dataTypeSTRINGList;
    				} else if ("6"== repeatChildList[i].oneLevelItem.dataType) {
    					dataTypeList=dataTypeDATEList;
    				}else if ("7"== repeatChildList[i].oneLevelItem.dataType) {
    					dataTypeList=dataTypeTIMEList;
    				}else if ("1"== repeatChildList[i].oneLevelItem.dataType) {
    					dataTypeList=dataTypeINTList;
    				}else if ("15"== repeatChildList[i].oneLevelItem.dataType) {
    					dataTypeList=dataTypeDOUBLEList;
    				}else if ("11"== repeatChildList[i].oneLevelItem.dataType) {
    					dataTypeList=dataTypeREFERENCEList;
    				}else if ("8"== repeatChildList[i].oneLevelItem.dataType) {
    					dataTypeList=dataTypeFILEList;
    				}else if ("14"== repeatChildList[i].oneLevelItem.dataType) {
    					dataTypeList=dataTypeENUMList;
    				}else if ("17"== repeatChildList[i].oneLevelItem.dataType) {
    					dataTypeList=dataTypeCASCADETYPEList;
    				}
            	}else {
            		attrHtml += "<option item-data-type='"+repeatChildList[i].oneLevelItem.dataType+"' data-id='"+repeatChildList[i].code+"' value='"+repeatChildList[i].cnName+"'>"+repeatChildList[i].cnName+"</option>";
            	}
            	
            	
            	                
            }
			attrHtml += "</select>";
			attrHtml += "<select disabled class='data-type attr-type'>";  
		   // Ajax.ajax('admin/node/basicItemNode/getDataType', {
		    	//dataType:repeatChildList[0].oneLevelItem.dataType
		   // }, function(data){		    	
		    //	var dataTypeList = data.dataType; 
			
				
		    	for(var i=0; i<dataTypeList.length; i++) {
		    		if(dataTypeList[i][0] == dataType) {
		    			attrHtml += "<option value='"+dataTypeList[i][0]+"' selected>"+dataTypeList[i][1]+"</option>";
		    		}else {
		    			attrHtml += "<option value='"+dataTypeList[i][0]+"'>"+dataTypeList[i][1]+"</option>";
		    		}
	            	          
	            };
	            attrHtml += "</select>";
				attrHtml += "<select disabled class='node-ops-type'>";		
				var nodePosType=nodePosTypeATTRIBUTE;
			    for(var i=0; i<nodePosType.length; i++) {
			    	if(nodePosType[i] == opt) {
			    		attrHtml += "<option value='"+nodePosType[i]+"' selected>"+nodePosType[i]+"</option>";
			    	}else {
			    		attrHtml += "<option value='"+nodePosType[i]+"'>"+nodePosType[i]+"</option>";
			    	}
		            	          
		        };
		        attrHtml += "</select>";
		        attrHtml += "<div class='btn-wrap'>" +
		        "<i class='icon icon-save'></i>" +
		        "<i class='icon delStruct'></i>" +
		        "<i class='icon-simulate-trashsm'></i>" +
		        "</div>" +
		        "</div>" +
		        "</li>";		           		        
		        var $html = $(attrHtml).prependTo($(parent));
		        $html.find("select").css({"width":"15%","marginLeft":"16px"}).select2();
		  //  }, {async: false})
	   // }, {async: false});	
    }
    
    
    
    //多值属性初始化方法
    function initMoreAttr(abcattrCode,abcattr,dataType,id,name,opt,order,parent,repeatList) {    
    	var entityId = $(".entity_attr.active", $page).attr("data-code");     	
        var dragWrapLen = $(".dragEdit-wrap").length + 1 ;        
            var moreAttrHtml = "<li class='more-attr clear-fix'>" +
            "<div class='more-attr-title more-attr collapse-header' data-abcattrCode='"+abcattrCode+"' data-order='"+order+"' data-id='"+id+"'>"
            + "<div class='icon-label more-attr'>";
            if(abcattrCode=="") {
            	moreAttrHtml=moreAttrHtml+  "<i class='icon icon-error-cross'></i>";
		    } 
            moreAttrHtml= moreAttrHtml +  "<i class='icon icon-more-attr'></i>" +
            "<span class='text'>多值属性</span>" +
            "</div>" +
            "<div class='label-bar al-save more-attr'>" +
            "<input type='text' disabled class='edit-input text' value='"+name+"'>" +
            "<select disabled class='abc-attr'>"            
            for(var i=0; i<repeatList.length; i++) {
            	
            	if(repeatList[i].code == abcattrCode) {            		
            		moreAttrHtml += "<option data-id='"+repeatList[i].code+"' value='"+repeatList[i].cnName+"' selected>"+repeatList[i].cnName+"</option>"; 
            	}else {            		
            		moreAttrHtml += "<option data-id='"+repeatList[i].code+"' value='"+repeatList[i].cnName+"'>"+repeatList[i].cnName+"</option>"; 
            	}            	               
            }           
            moreAttrHtml += "</select>";           
	        moreAttrHtml += "<select disabled class='node-ops-type'>";	
	        var nodePosType=nodePosTypeMULTIATTR;
			for(var i=0; i<nodePosType.length; i++) {
			   if(nodePosType[i] == opt) {            		
				    moreAttrHtml += "<option value='"+nodePosType[i]+"' selected>"+nodePosType[i]+"</option>";			    			
		          }else {            				            		
		            moreAttrHtml += "<option value='"+nodePosType[i]+"'>"+nodePosType[i]+"</option>";
		          }			    		         
		       };
		   moreAttrHtml += "</select>";
		   moreAttrHtml += "<div class='btn-wrap'>" +
		   "<i class='icon icon-save'></i>" +
		   "<i class='icon icon-add-sm group'></i>" +
		   "<i class='icon delStruct'></i>" +
		   "<i class='icon icon-arrow-sm active'></i>" +
		   "</div>" +
		   "</div>" +
		   "</div>" +
		   "<ul class='more-attr-drag-wrap dragEdit-wrap collapse-content collapse-content-inactive need-ajax' id='dragEdit-"+dragWrapLen+"'>" +
		   "</ul>" +
		   "</li>"		   
		   var $html = $(moreAttrHtml).prependTo($(parent));		   
		   $html.find("select").css({"width":"15%","marginLeft":"16px"}).select2();
		   drag($(".dragEdit-wrap").length);			    		   
	   // }, {async: false});                                
    }

    //关系初始化方法
    function initRelative(abcattr, abcattrCode, id, name, opt,order, parent) {
    	var entityId = $(".entity_attr.active", $page).attr("data-code");
        var dragWrapLen = $(".dragEdit-wrap").length + 1 ;        				
            var relativeHtml = "<li class='attr-relative'>" +
            "<div class='attr-relative-title attr-relative collapse-header' data-order='"+order+"' data-id='"+id+"'>"
           + "<div class='icon-label attr-relative'>";
            if(abcattrCode=="") {
            	relativeHtml=relativeHtml+ "<i class='icon icon-error-cross'></i>";
		    } 
            relativeHtml=relativeHtml+ "<i class='icon icon-attr-relative'></i><span class='text'>关系</span>" +
            "</div>" +
            "<div class='label-bar attr-relative al-save'>" +
            "<input type='text' disabled class='edit-input text' value='"+name+"'>" +
            "<select disabled class='abc-attr'>" +         		            		
            "<option data-id='"+abcattrCode+"' value='"+abcattr+"' selected>"+abcattr+"</option>" +
            "</select>" ; 
            relativeHtml += "<select disabled class='node-ops-type'>";	
            var nodePosType=nodePosTypeRELATION;
		    for(var i=0; i<nodePosType.length; i++) {
		    	if(nodePosType[i] === opt) {
		    		relativeHtml += "<option value='"+nodePosType[i]+"' selected>"+nodePosType[i]+"</option>";  	
		    	}else {
		    		relativeHtml += "<option value='"+nodePosType[i]+"'>"+nodePosType[i]+"</option>"; 
		    	}
	            	         
	         };
	        relativeHtml += "</select>";
	        relativeHtml += "<div class='btn-wrap'>" +
            "<i class='icon icon-save'></i>" + 
            "<i class='icon delStruct'></i>" +
            "<i class='icon icon-arrow-sm active'></i>" +
            "</div>" +
            "</div>" +
            "</div>" +            
            "<ul class='need-ajax attr-relative-drag-wrap dragEdit-wrap collapse-content collapse-content-inactive ' id='dragEdit-"+dragWrapLen+"'>" +
            "</ul>" +
            "</li>";         		    
		    var $html = $(relativeHtml).prependTo($(parent));
            $html.find("select").css({"width":"15%","marginLeft":"16px"}).select2();
		    drag($(".dragEdit-wrap").length);		    			                                    
    }
    
	/**
     * 获取实体信息方法 示例     
     */
	function getEntity(entity) {		
		var cnName = $(entity).attr("data-cnname");		
		$("#structBaseEdit .entity-title>.edit-input").val(cnName);
		$("#structBaseEdit .entity-title>.entity-only-title").html(cnName);
		$("#structBaseEdit .entity-edit-wrap").addClass("active");
	}
	
    /**
     * 跟实体添加页面弹出方法
     * @param {当前点击元素,dom对象} el 
     */
    function pop(el) {// 这个有用
    	
        var relativeLength = $(el).closest(".collapse-header")
            .siblings(".collapse-content")
            .children(".attr-relative").length;
        
      var modelType =   $(el).closest(".collapse-header").attr("data-type");
      
      var html = "<ul class='card'>";        
      html += "<li class='card-list add-GROUP1D' structType='101' structShowName='添加字段组'>" +
          "<i class='icon icon-card-attr-group'></i>" +
          "<span class='text'>添加字段组</span>" +
          "</li>" +
          "<li class='card-list add-GROUP2D' structType='102' structShowName='添加二维组'>" +
          "<i class='icon icon-card-more-attr'></i>" +
          "<span class='text'>添加二维组</span>" +
          "</li>"+
          "<li class='card-list add-RSTRUC' structType='103' structShowName='添加关系结构'>" +
          "<i class='icon icon-card-more-attr'></i>" +
          "<span class='text'>添加关系结构</span>" +
          "</li>"+
          "</ul>";

        var wrap = $("#structBaseEdit");
        var offsetx = $(el).offset().left;
        var offsety = $(el).offset().top;
        var wrapOffsetx = wrap.offset().left;
        var wrapOffsety = wrap.offset().top;
        var top = offsety - wrapOffsety + 30;
        var left = offsetx -wrapOffsetx - 90;
        var popHtml = $(html).appendTo(wrap);
        popHtml.css({
            "top": top,
            "left": left
        });

    };

    /**
     * 结构体属性页面
    */
    function popsm(el) {
        var addTagLength = $(el).closest(".collapse-header")
            .next(".add-tag").length;
        
        var structtype = $(el).closest(".collapse-header").attr("data-structtype");
        
        var html = "<ul class='card'>";
        html += "<li class='card-list add_FIELD' structType='2' structShowName='添加字段'>" +
        "<i class='icon icon-card-attr'></i>" +
        "<span class='text'>添加字段</span>" +
        "</li>" +
        "<li class='card-list add_ENUMFIELD' structType='201' structShowName='添加枚举字段'>" +
        "<i class='icon icon-card-attr'></i>" +
        "<span class='text'>添加枚举字段</span>" +
        "</li>";
       
        if (structtype == 101) {
        	html += "<li class='card-list add_RFIELD' structType='202' structShowName='添加RFIELD'>" +
            "<i class='icon icon-card-attr'></i>" +
            "<span class='text'>添加关系字段</span>" +
            "</li>";
        } else {
        	
        }
        
        html += "<li class='card-list add_RFIELD' structType='203' structShowName='添加REFFIELD'>" +
	        "<i class='icon icon-card-attr'></i>" +
	        "<span class='text'>添加引用字段</span>" +
	        "</li>"
	        +"<li class='card-list add_RFIELD' structType='204' structShowName='添加RREFFIELD'>" +
	        "<i class='icon icon-card-attr'></i>" +
	        "<span class='text'>添加引用关联</span>" +
	        "</li>";
           
        html +="</ul>";
        var wrap = $("#structBaseEdit");
        var offsetx = $(el).offset().left;
        var offsety = $(el).offset().top;
        var wrapOffsetx = wrap.offset().left;
        var wrapOffsety = wrap.offset().top;
        var top = offsety - wrapOffsety + 30;
        var left = offsetx - wrapOffsetx - 90;
        var popHtml = $(html).appendTo(wrap);
        popHtml.css({
            "top": top,
            "left": left
        });
    };
    
    /**
     * filterGroup 页面弹出方法
    */
    function popFilterGroup(el) {
    	
        var html = "<ul class='card'>";
            html += "<li class='card-list add-filterGroup'>" +
                "<i class='icon icon-card-attr'></i>" +
                "<span class='text'>添加filterGroup</span>" +
                "</li>" +
                "</ul>";
        var wrap = $("#structBaseEdit");
        var offsetx = $(el).offset().left;
        var offsety = $(el).offset().top;
        var wrapOffsetx = wrap.offset().left;
        var wrapOffsety = wrap.offset().top;
        var top = offsety - wrapOffsety + 30;
        var left = offsetx - wrapOffsetx - 90;
        var popHtml = $(html).appendTo(wrap);
        popHtml.css({
            "top": top,
            "left": left
        });
    };
    
    
    /**
     * filter 页面弹出方法
    */
    function popFilter(el) {
    	
    	var source = $(el).closest(".collapse-header").attr("source");
    	
        var html = "<ul class='card'>";
            html += "<li class='card-list add-filter'>" +
                "<i class='icon icon-card-attr'></i>" +
                "<span class='text'>添加filter</span>" +
                "</li>";
            
            if (source != "moreAttr") {
            	html +="<li class='card-list add-rFilter'>" +
                "<i class='icon icon-card-attr'></i>" +
                "<span class='text'>添加rfilter</span>" +
                "</li>";
            }
               html += "</ul>";
        var wrap = $("#structBaseEdit");
        var offsetx = $(el).offset().left;
        var offsety = $(el).offset().top;
        var wrapOffsetx = wrap.offset().left;
        var wrapOffsety = wrap.offset().top;
        var top = offsety - wrapOffsety + 30;
        var left = offsetx - wrapOffsetx - 90;
        var popHtml = $(html).appendTo(wrap);
        popHtml.css({
            "top": top,
            "left": left
        });
    };
    

    /**
     * 添加标签页面弹出方法
    */
    function popTag(el) {
        var $tag = $(el).closest(".label-bar.tag")
            .find(".tag-content").find("li:not(.icon)");
        var hasArray = [];
        for (var i = 0; i < $tag.length; i++) {
            hasArray.push($($tag[i]).children("span").text());
        };  
        
       var entityCode = $(el).closest(".collapse-content").siblings(".collapse-header").attr("data-abcattrcode");
       
       if(entityCode == undefined) {
    	   entityCode =  $(el).closest(".collapse-content").siblings(".collapse-header").closest(".collapse-content").siblings(".collapse-header").attr("data-abcattrcode");
       }
       
        $CPF.showLoading();
		Ajax.ajax('admin/node/basicItemNode/getCommLab', {
			entityCode:entityCode
		}, function(data) {			
			var data = data.commLab;
			var html = "<ul class='tag-card'>"+						
					"<li class='tag-card-search'>" +
					"<input type='text' class='tag-search'>"+
					"</li>"
			var has; //判断是否已经选中
			for(var i=0; i<data.length; i++) {
				has = false; //每次都重置
				for(var j=0; j<hasArray.length; j++) {
					if(hasArray[j] == data[i].name) {
						has = true;
						break;
					}
				};
				if(has) {
					 html += "<li class='tag-card-list'>" +
			            "<span class='tag-checkbox tag-checkbox-checked'>" +
			            "<input data-id='"+data[i].id+"' type='checkbox' class='tag-checkbox-input' value='"+data[i].name+"'>" +
			            "<span class='tag-checkbox-inner'></span>" +
			            "</span>" +
			            "<span>"+data[i].name+"</span>" +
			            "</li>" 
				}else {
					 html += "<li class='tag-card-list'>" +
			            "<span class='tag-checkbox'>" +
			            "<input data-id='"+data[i].id+"' type='checkbox' class='tag-checkbox-input' value='"+data[i].name+"'>" +
			            "<span class='tag-checkbox-inner'></span>" +
			            "</span>" +
			            "<span>"+data[i].name+"</span>" +
			            "</li>" 
				}				
			};
			html += "<li class='tag-card-info'>没有找到匹配项" +
					"</li>"+
					"</ul>"
			var wrap = $("#structBaseEdit");
		    var offsetx = $(el).offset().left;
		    var offsety = $(el).offset().top;
		    var wrapOffsetx = wrap.offset().left;
		    var wrapOffsety = wrap.offset().top;
		    var top = offsety - wrapOffsety + 25;
		    var left = offsetx - wrapOffsetx - 90;
		    var popHtml = $(html).appendTo(wrap);
		    popHtml.css({
		        "top": top,
		        "left": left
		     });
		    $CPF.closeLoading();
	    });		                
    };
    
    $("#structBaseEdit").on("click", ".tag-search", function (e) {
        e.stopPropagation();        
    });
    
    //搜索标签点击事件绑定
    $("#structBaseEdit").on("input propertychange", ".tag-search", function (e) {
        e.stopPropagation();               
        
        var val = $(this).val();         
        val = val.replace(/\s+/g,"");  
        var $searchLi = $(".tag-card", $page).find("li:contains("+val+")");        
        if(val !== "") {     
        	if($searchLi.length == 0) {
            	$(".tag-card", $page).find("li").addClass("tag-hide");
            	$(".tag-card-info", $page).addClass("tag-show");
            	return;
            }else {
            	$(".tag-card", $page).find("li").addClass("tag-hide");        	
            	$searchLi.removeClass("tag-hide");
            	$(".tag-card-info", $page).removeClass("tag-show");
            }        	  
        }else {
        	$(".tag-card", $page).find("li").removeClass("tag-hide");
        }
    });
    
    /**
     * 添加关系下标签页面弹出方法
    */
    function popRelativeTag(el) {
        var $tag = $(el).closest(".label-bar.tag")
            .find(".tag-content").find("li:not(.icon)");
        var hasArray = [];
        for (var i = 0; i < $tag.length; i++) {
            hasArray.push($($tag[i]).children("span").text());
        };     
        if($(el).closest(".label-bar.tag")
				.closest(".collapse-content")
				.prev(".collapse-header")
				.closest(".collapse-content")
				.prev(".collapse-header")
				.hasClass("entity-title")){
        	var leftRecordType = $(".entity_attr", $page).attr("data-code");
        }else {
        	var leftRecordType = $(el).closest(".label-bar.tag")
									.closest(".collapse-content")
									.prev(".collapse-header")
									.closest(".collapse-content")
									.prev(".collapse-header")
									.find(".entity-only-title")
									.attr("data-abcattrcode");
        }
        var rightRecordType = $(el).closest(".label-bar.tag")
        						.closest(".collapse-content")
        						.prev(".collapse-header")
        						.find(".abc-attr")
        						.find("option:selected")
        						.attr("data-id");               
        $CPF.showLoading();
		Ajax.ajax('admin/node/basicItemNode/getLabRela', {
			leftRecordType: leftRecordType, 
			rightRecordType: rightRecordType
		}, function(data) {			
			var data = data.labRela;
			if(data.length == 0) {
				Dialog.notice("需先在数据模型中添加关系", "warning");
			};
			var html = "<ul class='tag-card'>";
			var has; //判断是否已经选中
			for(var i=0; i<data.length; i++) {
				has = false; //每次都重置
				for(var j=0; j<hasArray.length; j++) {
					if(hasArray[j] == data[i].name) {
						has = true;
						break;
					}
				};
				if(has) {
					 html += "<li class='tag-card-list'>" +
			            "<span class='tag-checkbox tag-checkbox-checked'>" +
			            "<input data-id='"+data[i].typeCode+"' type='checkbox' class='tag-checkbox-input' value='"+data[i].name+"'>" +
			            "<span class='tag-checkbox-inner'></span>" +
			            "</span>" +
			            "<span>"+data[i].name+"</span>" +
			            "</li>" 
				}else {
					 html += "<li class='tag-card-list'>" +
			            "<span class='tag-checkbox'>" +
			            "<input data-id='"+data[i].typeCode+"' type='checkbox' class='tag-checkbox-input' value='"+data[i].name+"'>" +
			            "<span class='tag-checkbox-inner'></span>" +
			            "</span>" +
			            "<span>"+data[i].name+"</span>" +
			            "</li>" 
				}				
			};
			html += "</ul>"
			var wrap = $("#structBaseEdit");
		    var offsetx = $(el).offset().left;
		    var offsety = $(el).offset().top;
		    var wrapOffsetx = wrap.offset().left;
		    var wrapOffsety = wrap.offset().top;
		    var top = offsety - wrapOffsety + 25;
		    var left = offsetx - wrapOffsetx - 90;
		    var popHtml = $(html).appendTo(wrap);
		    popHtml.css({
		        "top": top,
		        "left": left
		     });
		    $CPF.closeLoading();
	    });		                
    };

    /**
     * 删除属性标签页弹出方法
      */
    function popAttr(el) {
        var html = "<div class='delete-list'>" +
            "<p>" +
            "<i class='icon icon-mark'></i><span class='text'>确定要删除此条数据?</span>" +
            "</p>" +
            "<div class='delete-list-btn'>" +
            "<span class='opera cancel'>取消</span>" +
            "<span class='opera confirm'>确认</span>" +
            "</div>" +
            "</div>"

        var wrap = $("#structBaseEdit");
        var offsetx = $(el).offset().left;
        var offsety = $(el).offset().top;
        var wrapOffsetx = wrap.offset().left;
        var wrapOffsety = wrap.offset().top;
        var top = offsety - wrapOffsety - 114;
        var left = offsetx - wrapOffsetx - 121;
        var popHtml = $(html).appendTo(wrap);
        popHtml.css({
            "top": top,
            "left": left
        });
    }

    /**
     * 删除组别标签页弹出方法
      */
    function popGroupAttr(el) {
        var html = "<div class='delete-list-c'>" +
            "<p>" +
            "<i class='icon icon-mark'></i><span class='text'>确定同时删除组和组内内容?</span>" +
            "</p>" +
            "<div class='delete-list-btn'>" +
            "<span class='opera cancel'>取消</span>" +
            "<span class='opera confirm'>确认</span>" +
            "<span class='opera only-group'>仅删除组</span>" +
            "</div>" +
            "</div>"

        var wrap = $("#structBaseEdit");
        var offsetx = $(el).offset().left;
        var offsety = $(el).offset().top;
        var wrapOffsetx = wrap.offset().left;
        var wrapOffsety = wrap.offset().top;
        var top = offsety - wrapOffsety - 124;
        var left = offsetx - wrapOffsetx - 121;
        var popHtml = $(html).appendTo(wrap);
        popHtml.css({
            "top": top,
            "left": left
        });
    }


    /**
     * remove 添加页方法
      */
    function removePop() {
        $(".card").remove();
        $(".tag-card").remove();
        $(".delete-list").remove();
        $(".delete-list-c").remove();
        $(".icon-add").removeClass("active");
        $(".icon-add-tag").removeClass("active");
        $(".icon-add-tag-relative").removeClass("active");
        $(".icon-trash").removeClass("active");
        $(".delStruct").removeClass("active");

    };

    //标签页标签ul长度设置
    function menuWidth(ul) {
        var menuW = 0;
        var $li = $(ul).children("li");
        var contentW = $(ul).parent(".tag-content").width();
        for (var i = 0; i < $li.length; i++) {
            menuW += parseFloat($($li[i]).css("width")) + 8;
        }
        if(menuW == 0 || menuW < contentW){
        	menuW = "auto"
        }
        $(ul).width(menuW);
    }

    //标签删除的方法
    function tagRemoveTag(el, text) {
        var $tagUl = $(el).closest(".label-bar.tag")
            .find(".tag-content")
            .children("ul");
        var $li = $tagUl.children("li");
        for (var i = 0; i < $li.length; i++) {
            if ($($li[i]).children("span").text() == text) {
                $($li[i]).remove();
                menuWidth($tagUl[0]);
                var ulWidth = parseFloat($tagUl.css("width"));
                var marginLeft = parseFloat($tagUl.css("margin-left"));
                var contentWidth = parseFloat($tagUl.parent(".tag-content").css("width"));
                
                if(ulWidth + marginLeft < contentWidth) {                	
                	var marginLeft = contentWidth - ulWidth;                	
                	if(marginLeft > 0) {
                		marginLeft = 0;
                	}
                	$tagUl.css("marginLeft",marginLeft);
                }
                return;
            }
        }
    }

    //标签添加上去的方法
    function tagAddTag(el, text, id) {    	   
        var $tagUl = $(el).closest(".label-bar.tag")
            .find(".tag-content")
            .children("ul");
        var html = "<li data-id='" + id + "' data-text='" + text + "'>" +
            "<span>" + text + "</span>" +
            "<i class='icon icon-delete'></i>" +
            "</li>"
       
        $tagUl.append(html);
        menuWidth($tagUl[0]);
    }

    //标签两边箭头的展现和隐藏
    function judegArrow(ul) {
        var ulWidth = parseFloat($(ul).width());
        var wrapWidth = parseFloat($(ul).parent(".tag-content").width());
        if (ulWidth > wrapWidth) {
            $(ul).closest(".label-bar.tag").find(".icon-toleft")
                .addClass("active");
            $(ul).closest(".label-bar.tag").find(".icon-toright")
                .addClass("active");
        } else {
            $(ul).closest(".label-bar.tag").find(".icon-toleft")
                .removeClass("active");
            $(ul).closest(".label-bar.tag").find(".icon-toright")
                .removeClass("active");
        }
    }

    /**
     * 添加标签方法
     * @param {当前点击元素对应的加号} el
      */
    function addTag(el) {   
        var $content = $(el).closest(".collapse-header").siblings(".collapse-content");
        var tagHtml = "<li class='add-tag clear-fix'>" +
            "<div class='icon-label tag'>" +
            "<i class='icon icon-tag'></i>" +
            "<span class='text'>标签</span>" +
            "</div>" +
            "<div class='label-bar tag edit' data-order='' data-id=''>" +
            "<input type='text' class='edit-input text' value='标签名称'>" +
            "<span class='icon icon-toleft'></span>" +
            "<div class='tag-content'>" +
            "<ul class='clear-fix'>" +
            "</ul>" +
            "</div>" +
            "<span class='icon icon-toright ban'></span>" 
            tagHtml += "<select class='node-ops-type'>";
        var nodePosType=nodePosTypeLABEL;
		    for(var i=0; i<nodePosType.length; i++) {
		    	if(nodePosType[i] === "写") {
		    		tagHtml += "<option value='"+nodePosType[i]+"' selected>"+nodePosType[i]+"</option>";  	
		    	}else {
		    		tagHtml += "<option value='"+nodePosType[i]+"'>"+nodePosType[i]+"</option>"; 
		    	}
	            	         
	         };
	         tagHtml += "</select>";
	         tagHtml += "<div class='btn-wrap'>" +
            "<i class='icon tag icon-save'></i>" +
            "<i class='icon tag icon-add-tag'></i>" +
            "<i class='icon delStruct'></i>" +
            "<i class='icon icon-simulate-trashsm'></i>" +            
            "</div>" +
            "</div>" +
            "</li>"
        var $html = $(tagHtml).prependTo($content);
    	$html.find("select").css({"width":"7%","marginLeft":"2px"}).select2();
        addUnfold(el)
    };
    
    
    /**
     * 添加引用->引用属性方法
      */
    function addrRefattributeAttr(el) {
    	$CPF.showLoading();
        var $content = $(el).closest(".collapse-header").siblings(".collapse-content");
        var entityId = $(el).closest(".collapse-header").attr("data-abcattrcode");
        if (entityId == undefined) {
        	entityId = $(el).closest(".collapse-header").closest(".collapse-content").siblings(".collapse-header").attr("data-abcattrcode");
        }
        
		Ajax.ajax('admin/dictionary/basicItem/getComm', {
			entityId: entityId,
		}, function(data) {	
			
			if (data.code == 400) {
				Dialog.notice("操作失败！刷新后重试", "warning");
				$CPF.closeLoading();		
				return;
			}
			var data = data.commList;
			if(data.length == 0) {
				Dialog.notice("没有属性可选， 请在模型中添加属性", "warning");
				$CPF.closeLoading();		
				return;
			}
			
			var attrHtml = "<li class='add-attr clear-fix'>" +
            "<div class='icon-label attr' data-type='15' data-order='' data-id=''>" +
            "<i class='icon icon-attr'></i>" +
            "<span class='text'>R引用属性</span>" +
            "</div>" +
            "<div class='label-bar rRefattribute-attr edit' data-type='15' data-order='' data-id=''>" +
            "<input type='text' class='edit-input text' value='"+data[0][1]+"'>" +
            "<select class='abc-attr'>"       
            for(var i=0; i<data.length; i++) {            	
            	attrHtml += "<option data-id='"+data[i][0]+"' value='"+data[i][1]+"' item-data-type='"+data[i][2]+"'>"+data[i][1]+"</option>";                
            }
			attrHtml += "</select>";
			attrHtml += "<select class='data-type attr-type'>";     
			
		   Ajax.ajax('admin/node/basicItemNode/getDataType', {
			   dataType:data[0][2]
		   }, function(data){		    	
		    	var dataTypeList = data.dataType;
		    	for(var i=0; i<dataTypeList.length; i++) {
		    		if(dataTypeList[i][0] === "STRING") {
		    			attrHtml += "<option value='"+dataTypeList[i][0]+"' selected>"+dataTypeList[i][1]+"</option>"; 	
		    		}else {
		    			attrHtml += "<option value='"+dataTypeList[i][0]+"'>"+dataTypeList[i][1]+"</option>"; 
		    		}	            	        
	            };
	            attrHtml += "</select>";
				attrHtml += "<select class='node-ops-type'>";	
				var nodePosType = nodePosTypeREFATTRIBUTE;
			    for(var i=0; i<nodePosType.length; i++) {
			    	if(nodePosType[i] === "写") {
			    		attrHtml += "<option value='"+nodePosType[i]+"' selected>"+nodePosType[i]+"</option>";  	
			    	}else {
			    		attrHtml += "<option value='"+nodePosType[i]+"'>"+nodePosType[i]+"</option>"; 
			    	}
		            	         
		         };
		         attrHtml += "</select>";
		         
		         
		         attrHtml +="<select class='subdomain'>"
		       //获取引用类型
		        	 Ajax.ajax('admin/dictionary/basicItem/getAppointTypeAttr', {
				 			parentCode: entityId,
				 			dataType :11
				 		}, function(data2) {
				 			
				 			var appointTypeAttr = data2.appointTypeAttr;
		 			 for(var i=0; i<appointTypeAttr.length; i++) {            	
		             	attrHtml += "<option data-id='"+appointTypeAttr[i][0]+"' value='"+appointTypeAttr[i][1]+"' item-data-type='"+appointTypeAttr[i][2]+"'>"+appointTypeAttr[i][1]+"</option>";                
		             }
		 			attrHtml += "</select>";
		         attrHtml += "<select class='relAbcnodeId'>";
		         Ajax.ajax('admin/node/basicItemNode/getAllAbcNode', '', function(data1) {
		             var allAbc = data1.allAbc;
		             
					    for(var i=0; i<allAbc.length; i++) {
					    	attrHtml += "<option value='"+allAbc[i].id+"'>"+allAbc[i].name+"</option>"; 
				         };
				         attrHtml += "</select>";
		         
		         attrHtml += "<div class='btn-wrap'>" +
		         "<i class='icon icon-save'></i>" +
		         "<i class='icon delStruct'></i>" +
		         "<i class='icon-simulate-trashsm'></i>" +
		         "</div>" +
		         "</div>" +
		         "</li>";
		         var $html = $(attrHtml).prependTo($content);
		         $html.find("select").css({"width":"9%","marginLeft":"16px"}).select2();		            
		         addUnfold(el);
		         $CPF.closeLoading();	
		         })
		 		})
		    })
	    });		                      
    };
    
    /**
     * 添加引用属性方法
      */
    function addRefattributeAttr(el) {
        var $content = $(el).closest(".collapse-header").siblings(".collapse-content");
        var entityId = $(el).closest(".collapse-header").attr("data-abcattrcode");
        if (entityId == undefined) {
            entityId = $(el).closest(".collapse-header").closest(".collapse-content").siblings(".collapse-header").attr("data-abcattrcode");
        }
        
        $CPF.showLoading();
        //获取引用类型
		Ajax.ajax('admin/dictionary/basicItem/getAppointTypeAttr', {
			parentCode: entityId,
			dataType :11
		}, function(data) {	
			if (data.code == 400) {
				Dialog.notice("操作失败！刷新后重试", "warning");
				$CPF.closeLoading();		
				return;
			}
			var data = data.appointTypeAttr;
			if(data.length == 0) {
				Dialog.notice("没有引用属性可选， 请在模型中添加引用属性", "warning");
				$CPF.closeLoading();		
				return;
			}
			
			var attrHtml = "<li class='add-attr clear-fix'>" +
            "<div class='icon-label attr' data-type='14' data-order='' data-id=''>" +
            "<i class='icon icon-attr'></i>" +
            "<span class='text'>引用属性</span>" +
            "</div>" +
            "<div class='label-bar refattribute-attr edit' data-type='14' data-order='' data-id=''>" +
            "<input type='text' class='edit-input text' value='"+data[0][1]+"'>" +
            "<select class='abc-attr'>"       
            for(var i=0; i<data.length; i++) {            	
            	attrHtml += "<option data-id='"+data[i][0]+"' value='"+data[i][1]+"' item-data-type='"+data[i][2]+"'>"+data[i][1]+"</option>";                
            }
			attrHtml += "</select>";
			attrHtml += "<select class='data-type attr-type'>";     
			
		   Ajax.ajax('admin/node/basicItemNode/getDataType', {
			   dataType:data[0][2]
		   }, function(data){		    	
		    	var dataTypeList = data.dataType;
		    	for(var i=0; i<dataTypeList.length; i++) {
		    		if(dataTypeList[i][0] === "STRING") {
		    			attrHtml += "<option value='"+dataTypeList[i][0]+"' selected>"+dataTypeList[i][1]+"</option>"; 	
		    		}else {
		    			attrHtml += "<option value='"+dataTypeList[i][0]+"'>"+dataTypeList[i][1]+"</option>"; 
		    		}	            	        
	            };
	            attrHtml += "</select>";
				attrHtml += "<select class='node-ops-type'>";	
				var nodePosType = nodePosTypeREFATTRIBUTE;
			    for(var i=0; i<nodePosType.length; i++) {
			    	if(nodePosType[i] === "写") {
			    		attrHtml += "<option value='"+nodePosType[i]+"' selected>"+nodePosType[i]+"</option>";  	
			    	}else {
			    		attrHtml += "<option value='"+nodePosType[i]+"'>"+nodePosType[i]+"</option>"; 
			    	}
		            	         
		         };
		         attrHtml += "</select>";
		         attrHtml += "<select class='relAbcnodeId'>";
		         Ajax.ajax('admin/node/basicItemNode/getAllAbcNode', '', function(data1) {
		             var allAbc = data1.allAbc;
		             
					    for(var i=0; i<allAbc.length; i++) {
					    	attrHtml += "<option value='"+allAbc[i].id+"'>"+allAbc[i].name+"</option>"; 
				         };
				         attrHtml += "</select>";
		         
		         attrHtml += "<div class='btn-wrap'>" +
		         "<i class='icon icon-save'></i>" +
		         "<i class='icon delStruct'></i>" +
		         "<i class='icon-simulate-trashsm'></i>" +
		         "</div>" +
		         "</div>" +
		         "</li>";
		         var $html = $(attrHtml).prependTo($content);
		         $html.find("select").css({"width":"11%","marginLeft":"16px"}).select2();		            
		         addUnfold(el);
		         $CPF.closeLoading();	
		         })
		    })
	    });		                      
    };
    
    /**
     * 添加级联属性方法
      */
    function addCascadeAttr(el) {
        var $content = $(el).closest(".collapse-header").siblings(".collapse-content");
        var entityId = $(el).closest(".collapse-header").attr("data-abcattrcode");
        if (entityId == undefined) {
            entityId = $(el).closest(".collapse-header").closest(".collapse-content").siblings(".collapse-header").attr("data-abcattrcode");
        }
        $CPF.showLoading();
		Ajax.ajax('admin/dictionary/basicItem/getAppointTypeAttr', {
			parentCode: entityId,
			dataType: 17
		}, function(data) {	
			
			if (data.code == 400) {
				Dialog.notice("操作失败！刷新后重试", "warning");
				$CPF.closeLoading();		
				return;
			}
			var data = data.appointTypeAttr;
			if(data.length == 0) {
				Dialog.notice("没有级联属性可选， 请在模型中添加级联属性", "warning");
				$CPF.closeLoading();		
				return;
			}
			
			var attrHtml = "<li class='add-attr clear-fix'>" +
            "<div class='icon-label attr' data-type='7' data-order='' data-id=''>" +
            "<i class='icon icon-attr'></i>" +
            "<span class='text'>级联属性</span>" +
            "</div>" +
            "<div class='label-bar cascade-attr edit' data-type='7' data-order='' data-id=''>" +
            "<input type='text' class='edit-input text' value='"+data[0][1]+"'>" +
            "<select class='abc-attr'>"            
            for(var i=0; i<data.length; i++) {            	
            	attrHtml += "<option data-id='"+data[i][0]+"' value='"+data[i][1]+"' item-data-type='"+data[i][2]+"'>"+data[i][1]+"</option>";                
            }
			attrHtml += "</select>";
			attrHtml += "<select class='data-type attr-type'>";     
			
		   Ajax.ajax('admin/node/basicItemNode/getDataType', {
			   dataType:data[0][2]
		   }, function(data){		    	
		    	var dataTypeList = data.dataType;
		    	for(var i=0; i<dataTypeList.length; i++) {
		    		if(dataTypeList[i][0] === "STRING") {
		    			attrHtml += "<option value='"+dataTypeList[i][0]+"' selected>"+dataTypeList[i][1]+"</option>"; 	
		    		}else {
		    			attrHtml += "<option value='"+dataTypeList[i][0]+"'>"+dataTypeList[i][1]+"</option>"; 
		    		}	            	        
	            };
	            attrHtml += "</select>";
				attrHtml += "<select class='node-ops-type'>";	
				var nodePosType = nodePosTypeCASATTRIBUTE;
			    for(var i=0; i<nodePosType.length; i++) {
			    	if(nodePosType[i] === "写") {
			    		attrHtml += "<option value='"+nodePosType[i]+"' selected>"+nodePosType[i]+"</option>";  	
			    	}else {
			    		attrHtml += "<option value='"+nodePosType[i]+"'>"+nodePosType[i]+"</option>"; 
			    	}
		            	         
		         };
		         attrHtml += "</select>";
		         attrHtml += "<div class='btn-wrap'>" +
		         "<i class='icon icon-save'></i>" +
		         "<i class='icon delStruct'></i>" +
		         "<i class='icon-simulate-trashsm'></i>" +
		         "</div>" +
		         "</div>" +
		         "</li>";
		         var $html = $(attrHtml).prependTo($content);
		         $html.find("select").css({"width":"15%","marginLeft":"16px"}).select2();		            
		         addUnfold(el);
		         $CPF.closeLoading();			    			    
		    })
	    });		                      
    };
    
    /**
     * 添加多值级联属性方法
      */
    function addMoreCascadeAttr(el) {
        var $content = $(el).closest(".collapse-header").siblings(".collapse-content");
        var repeatId = $(el).closest(".collapse-header")
					.find(".abc-attr")
					.find("option:selected")
					.attr("data-id");
        $CPF.showLoading();
		Ajax.ajax('admin/dictionary/basicItem/getAppointTypeAttr', {
			parentCode: repeatId,
			dataType:17
		}, function(data) {	
			if (data.code == 400) {
				Dialog.notice("操作失败！刷新后重试", "warning");
				$CPF.closeLoading();		
				return;
			}
			var data = data.appointTypeAttr;
			if(data.length == 0) {
				Dialog.notice("没有级联属性可选， 请在模型中添加级联属性", "warning");
				$CPF.closeLoading();		
				return;
			}
			
			var attrHtml = "<li class='add-attr clear-fix'>" +
            "<div class='icon-label attr' data-order='' data-id=''>" +
            "<i class='icon icon-attr'></i>" +
            "<span class='text'>级联属性</span>" +
            "</div>" +
            "<div class='label-bar cascade-attr edit' data-type='7' data-order='' data-id=''>" +
            "<input type='text' class='edit-input text' value='"+data[0][1]+"'>" +
            "<select class='abc-attr' >"            
            for(var i=0; i<data.length; i++) {
            	attrHtml += "<option data-id='"+data[i][0]+"' value='"+data[i][1]+"' item-data-type='"+data[i][2]+"'>"+data[i][1]+"</option>";                
            }
			attrHtml += "</select>";
			attrHtml += "<select class='data-type attr-type'>"; 
		    Ajax.ajax('admin/node/basicItemNode/getDataType', {
		    	dataType:data[0][2]
		    }, function(data){		    	
		    	var dataTypeList = data.dataType;
		    	for(var i=0; i<dataTypeList.length; i++) {
		    		if(dataTypeList[i][0] === "STRING") {
		    			attrHtml += "<option value='"+dataTypeList[i][0]+"' selected>"+dataTypeList[i][1]+"</option>"; 	
		    		}else {
		    			attrHtml += "<option value='"+dataTypeList[i][0]+"'>"+dataTypeList[i][1]+"</option>"; 
		    		}	            	        
	            };
	            attrHtml += "</select>";
				attrHtml += "<select class='node-ops-type'>";	
				
				var nodePosType= nodePosTypeCASATTRIBUTE;
			    for(var i=0; i<nodePosType.length; i++) {
			    	if(nodePosType[i] === "写") {
			    		attrHtml += "<option value='"+nodePosType[i]+"' selected>"+nodePosType[i]+"</option>";  	
			    	}else {
			    		attrHtml += "<option value='"+nodePosType[i]+"'>"+nodePosType[i]+"</option>"; 
			    	}
		            	         
		        };
		        attrHtml += "</select>";
		        attrHtml += "<div class='btn-wrap'>" +
		        "<i class='icon icon-save'></i>" +
		        "<i class='icon delStruct'></i>" +
		        "<i class='icon-simulate-trashsm'></i>" +
		        "</div>" +
		        "</div>" +
		        "</li>";
		            
		        var $html = $(attrHtml).prependTo($content);
		        $html.find("select").css({"width":"15%","marginLeft":"16px"}).select2();
		        addUnfold(el);
		        $CPF.closeLoading();			    
			    
		    })
	    });		                      
    };
    
    
  //关系属性保存修改方法
    function rAttrSave(el) {
    	var $attrBar = $(el).closest(".label-bar");
    	var type = $attrBar.attr("data-type");
    	var dataType = $attrBar.children(".data-type").find("option:selected").val();
    	var opt = $attrBar.children(".node-ops-type").find("option:selected").val();
    	var name = $attrBar.children(".edit-input").val();    	
    	var order = $attrBar.attr("data-order");
    	var id = $attrBar.attr("data-id");
    	var parentId = $attrBar.closest(".collapse-content").prev(".collapse-header")
    						.attr("data-id"); 
    	var subdomain = $attrBar.children(".relationCode").find("option:selected").val();
    	var abcattrCode = $attrBar.children(".rattrType").find("option:selected").attr("data-id");
    	
    	switch (opt) {
	        case "读":
	            opt = 1;
	            break;
	        case "写":
	            opt = 2;
	            break;
	        case "补":
	            opt = 3;
	            break;
	        case "增":
	            opt = 4;
	            break;
	        case "并":
	            opt = 5;
	            break;
	        default:
	            break;
	    }
    	$CPF.showLoading();
    	Ajax.ajax('admin/node/basicItemNode/saveOrUpdate', {
			 type: type,
			 name: name,
			 abcattrCode: abcattrCode,
			 dataType: dataType,
			 opt: opt,
			 order: order,
			 parentId: parentId,
			 id: id,
			 subdomain:subdomain
		 }, function(data) {
			 if(data.state == "400") {
				 Dialog.notice(data.msg, "warning");
				 $CPF.closeLoading();
				 return;
			 }
			 var data = data.node;
			 //设置当前节点order和id
			 var order = data.order;
			 var id = data.id;
			 $attrBar.attr("data-order",order)
			 	.attr("data-id", id);
			 saveSuccess(el)
			 $CPF.closeLoading();
		});
    };
    
    
  //引用->引用属性保存修改方法
    function rRefattributeAttrSave(el) {
    	var $attrBar = $(el).closest(".label-bar");
    	var type = $attrBar.attr("data-type");
    	var dataType = $attrBar.children(".data-type").find("option:selected").val();
    	var opt = $attrBar.children(".node-ops-type").find("option:selected").val();
    	var name = $attrBar.children(".edit-input").val();    	
    	var order = $attrBar.attr("data-order");
    	var id = $attrBar.attr("data-id");
    	var parentId = $attrBar.closest(".collapse-content").prev(".collapse-header")
    						.attr("data-id"); 
    	var abcattr = $attrBar.children(".abc-attr").find("option:selected").val();
    	var abcattrCode = $attrBar.children(".abc-attr").find("option:selected").attr("data-id");
    	var relAbcnodeId = $attrBar.children(".relAbcnodeId").find("option:selected").val();
    	var subdomain = $attrBar.children(".subdomain").find("option:selected").val();
    	switch (opt) {
	        case "读":
	            opt = 1;
	            break;
	        case "写":
	            opt = 2;
	            break;
	        case "补":
	            opt = 3;
	            break;
	        case "增":
	            opt = 4;
	            break;
	        case "并":
	            opt = 5;
	            break;
	        default:
	            break;
	    }
    	$CPF.showLoading();
    	Ajax.ajax('admin/node/basicItemNode/saveOrUpdate', {
			 type: type,
			 name: name,
			 abcattr: abcattr,
			 abcattrCode: abcattrCode,
			 dataType: dataType,
			 opt: opt,
			 order: order,
			 parentId: parentId,
			 id: id,
			 relAbcnodeId:relAbcnodeId,
			 subdomain:subdomain
		 }, function(data) {
			 if(data.state == "400") {
				 Dialog.notice(data.msg, "warning");
				 $CPF.closeLoading();
				 return;
			 }
			 var data = data.node;
			 //设置当前节点order和id
			 var order = data.order;
			 var id = data.id;
			 $attrBar.attr("data-order",order)
			 	.attr("data-id", id);
			 saveSuccess(el)
			 $CPF.closeLoading();
		});
    };
    
    
    
    //引用属性删除方法
    function refattributeAttrDelete(el) {    	
    	var $attrBar = $(el).closest(".label-bar");    	
    	var id = $attrBar.attr("data-id");
    	var isDelChil = false;
    	var callback = function() {
    		$attrBar.parent(".add-attr").remove();    		
    	}; 
    	if($attrBar.hasClass("al-save")) {
    		deleteAjax(id, isDelChil, callback);
    	}else {
    		callback();
    		removePop();
    	}    	
    }
    
    
    //rabc属性删除方法
    function rabcDelete(el) {  
    	var $attrBar = $(el).closest(".label-bar");    	
    	var id = $attrBar.attr("data-id");
    	
    	var isDelChil = false;
    	var callback = function() {
    		$attrBar.parent().remove();    		
    	}; 
    	if($attrBar.hasClass("al-save")) {
    		deleteAjax(id, isDelChil, callback);
    	}else {
    		callback();
    		removePop();
    	}    	
    }
    
    
    /**
     * 添加多值属性下属性方法
      */
    function addAttrM(el) {
        var $content = $(el).closest(".collapse-header").siblings(".collapse-content");
        var repeatId = $(el).closest(".collapse-header")
        			.find(".abc-attr")
        			.find("option:selected")
        			.attr("data-id");
        $CPF.showLoading();
		Ajax.ajax('admin/dictionary/basicItem/getRepeatChild?repeatId', {
			repeatId: repeatId
		}, function(data) {			
			var data = data.repeatChild;
			var attrHtml = "<li class='add-attr clear-fix'>" +
            "<div class='icon-label attr' data-order='' data-id=''>" +
            "<i class='icon icon-attr'></i>" +
            "<span class='text'>属性</span>" +
            "</div>" +
            "<div class='label-bar attr edit' data-order='' data-id=''>" +
            "<input type='text' class='edit-input text' value='属性名'>" +
            "<select class='abc-attr'>"            
            for(var i=0; i<data.length; i++) {
            	attrHtml += "<option item-data-type='"+data[i].oneLevelItem.dataType+"' data-id='"+data[i].code+"' value='"+data[i].cnName+"'>"+data[i].cnName+"</option>";                
            }
			attrHtml += "</select>";
			attrHtml += "<select class='data-type attr-type'>";            
		    Ajax.ajax('admin/node/basicItemNode/getDataType', {
		    	dataType:data[0].oneLevelItem.dataType
		    }, function(data){		    	
		    	var dataTypeList = data.dataType;
		    	for(var i=0; i<dataTypeList.length; i++) {
		    		if(dataTypeList[i][0] === "STRING") {
		    			attrHtml += "<option value='"+dataTypeList[i][0]+"' selected>"+dataTypeList[i][1]+"</option>"; 	
		    		}else {
		    			attrHtml += "<option value='"+dataTypeList[i][0]+"'>"+dataTypeList[i][1]+"</option>"; 
		    		}	            	        
	            };
	            attrHtml += "</select>";
				attrHtml += "<select class='node-ops-type'>";
				var nodePosType = nodePosTypeATTRIBUTE;
			    for(var i=0; i<nodePosType.length; i++) {
			    	if(nodePosType[i] === "写") {
			    		attrHtml += "<option value='"+nodePosType[i]+"' selected>"+nodePosType[i]+"</option>";  	
			    	}else {
			    		attrHtml += "<option value='"+nodePosType[i]+"'>"+nodePosType[i]+"</option>"; 
			    	}
		            	         
		        };
		        attrHtml += "</select>";
		        attrHtml += "<div class='btn-wrap'>" +
		        "<i class='icon icon-save'></i>" +
		        "<i class='icon delStruct'></i>" +
		        "<i class='icon-simulate-trashsm'></i>" +
		        "</div>" +
		        "</div>" +
		        "</li>";
		            
		        var $html = $(attrHtml).prependTo($content);
		        $html.find("select").css({"width":"15%","marginLeft":"16px"}).select2();
		        addUnfold(el);
		        $CPF.closeLoading();			    			    
		    })
	    });		                      
    };


    /**
     * 添加关系方法
      */
    function addRelative(el) {
        var $content = $(el).closest(".collapse-header").siblings(".collapse-content");
        var entityId;
        if($(el).closest(".collapse-header").hasClass("entity-title")){
        	entityId = $(el).closest(".collapse-header").attr("data-abcattrcode");
        }else {
        	entityId = $(el).closest(".collapse-header").find(".label-bar")
        					.find(".entity-only-title").attr("data-abcattrcode");
        }
        var dragWrapLen = $(".dragEdit-wrap").length + 1 ;
        $CPF.showLoading();
		Ajax.ajax('admin/node/basicItemNode/entityList',{
			leftRecordType:entityId
		}, function(data) {			
			var data = data.entity;			            
            var relativeHtml = "<li class='attr-relative'>" +
            "<div class='attr-relative-title collapse-header' data-order='' data-id=''>" +
            "<div class='icon-label attr-relative'>" +
            "<i class='icon icon-attr-relative'></i><span class='text'>关系</span>" +
            "</div>" +
            "<div class='label-bar attr-relative edit'>" +
            "<input type='text' class='edit-input text' value='关系名称'>" +
            "<select class='abc-attr'>"
            for(var i=0; i<data.length; i++) {
            	relativeHtml += "<option data-id='"+data[i].code+"' value='"+data[i].cnName+"'>"+data[i].cnName+"</option>";                
            }
            relativeHtml += "</select>";
            relativeHtml += "<select class='node-ops-type'>";	
            var nodePosType = nodePosTypeRELATION;
		    for(var i=0; i<nodePosType.length; i++) {
		    	if(nodePosType[i] === "写") {
		    		relativeHtml += "<option value='"+nodePosType[i]+"' selected>"+nodePosType[i]+"</option>";  	
		    	}else {
		    		relativeHtml += "<option value='"+nodePosType[i]+"'>"+nodePosType[i]+"</option>"; 
		    	}
	            	         
	         };
	        relativeHtml += "</select>";
	        relativeHtml += "<div class='btn-wrap'>" +
            "<i class='icon icon-save'></i>" +  
            "<i class='icon delStruct'></i>" +
            "<i class='icon icon-arrow-sm'></i>" +
            "</div>" +
            "</div>" +
            "</div>" +            
            "<ul class='attr-relative-drag-wrap dragEdit-wrap collapse-content collapse-content-active' id='dragEdit-"+dragWrapLen+"'>" +
            "</ul>" +
            "</li>";         		    
		    var $html = $(relativeHtml).prependTo($content);
            $html.find("select").css({"width":"15%","marginLeft":"16px"}).select2();
            addUnfold(el);
		    drag($(".dragEdit-wrap").length);
		    $CPF.closeLoading();			    
	    });                                 
    }; 
    
    /**
     * 添加过滤条件
      */
    function addFilters(el, source) {
        var $content = $(el).closest(".collapse-header").siblings(".collapse-content");
        
        var entityId;
        if($(el).closest(".collapse-header").hasClass("entity-title")){
        	entityId = $(el).closest(".collapse-header").attr("data-abcattrcode");
        }else if ($(el).closest(".collapse-header").hasClass("more-attr-title")) {
        	entityId = $(el).closest(".collapse-header").attr("data-abcattrcode");
        }else {
        	entityId = $(el).closest(".collapse-header").find(".label-bar")
        					.find(".entity-only-title").attr("data-abcattrcode");
        }
        
        var dragWrapLen = $(".dragEdit-wrap").length + 1 ;
        $CPF.showLoading();
            var relativeHtml = "<li class='attr-relative'>" +
            "<div class='attr-relative-title collapse-header' source='"+source+"' entityId='"+entityId+"' data-order='' data-id=''>" +
            "<div class='icon-label attr-relative'>" +
            "<i class='icon icon-attr-relative'></i><span class='text'>filters</span>" +
            "</div>" +
            "<div class='label-bar filters edit'>" +
            "<input type='text' class='edit-input text' value='filters名称'>";
            relativeHtml += "<select class='node-ops-type'>";	
            var nodePosType = optFILTERS;
            for(var key in nodePosType) {
 		    	relativeHtml += "<option value='"+key+"'>"+nodePosType[key]+"</option>"; 
 	         }
	        relativeHtml += "</select>";
	        relativeHtml += "<div class='btn-wrap'>" +
            "<i class='icon icon-save'></i>" +  
            "<i class='icon icon-add-filterGroup group'></i>" +
            "<i class='icon delStruct'></i>" +
            "<i class='icon icon-arrow-sm'></i>" +
            "</div>" +
            "</div>" +
            "</div>" +            
            "<ul class='attr-relative-drag-wrap dragEdit-wrap collapse-content collapse-content-active' id='dragEdit-"+dragWrapLen+"'>" +
            "</ul>" +
            "</li>";         		    
		    var $html = $(relativeHtml).prependTo($content);
		    $html.find("select").css({"width":"15%","marginLeft":"16px"}).select2();
            addUnfold(el);
		    drag($(".dragEdit-wrap").length);
		    $CPF.closeLoading();			    
    }; 
    
    
    
    /**
     * 添加过滤条件 rFilter
      */
    function addrFilter(el) {
        var $content = $(el).closest(".collapse-header").siblings(".collapse-content");
        var entityId = $(el).closest(".collapse-header").attr("entityId");
        var source = $(el).closest(".collapse-header").attr("source");
        var dragWrapLen = $(".dragEdit-wrap").length + 1 ;
        $CPF.showLoading();
        
        Ajax.ajax('admin/dictionary/recordRelationType/getRelation', {
			leftRecordType: entityId,
			relationType:''
		}, function(data) {			
			var relationList = data.relationList;
        Ajax.ajax('admin/node/binFilterBody/getCriteriaSymbol', {
	    	dataType:13
	    }, function(data){	
	    	
            var relativeHtml = "<li class='attr-relative'>" +
            "<div class='attr-relative-title collapse-header' source='"+source+"' entityId='' data-order='' data-id=''>" +
            "<div class='icon-label attr-relative'>" +
            "<i class='icon icon-attr-relative'></i><span class='text'>rFilter</span>" +
            "</div>" +
            "<div class='label-bar rFilter edit'>" +
            "<input type='text' class='edit-input text' value='rFilter名称'>";
            
            relativeHtml += "<select class='relationData'>";	
		    for(var key in relationList) {
		    	relativeHtml += "<option rightRecordType='"+relationList[key].rightRecordType+"' value='"+relationList[key].typeCode+"'>"+relationList[key].name+"</option>"; 
	         };
	         relativeHtml += "</select>";
            
            relativeHtml += "<select class='node-Symbol-type'>";	
    	    	
    	    	var nodeSymbolType = data.symbolMap;
		    for(var key in nodeSymbolType) {
		    	relativeHtml += "<option value='"+key+"'>"+nodeSymbolType[key]+"</option>"; 
	         };
	         relativeHtml += "</select>";

	         relativeHtml += "<select class='node-ops-type'>";	
            var nodePosType = optFILTERS;
            for(var key in nodePosType) {
 		    	relativeHtml += "<option value='"+key+"'>"+nodePosType[key]+"</option>"; 
 	         }
	         relativeHtml += "</select>";
	         relativeHtml += "<div class='btn-wrap'>" +
            "<i class='icon icon-save'></i>" +  
            "<i class='icon icon-add-filterGroup group'></i>" +
            "<i class='icon delStruct'></i>" +
            "<i class='icon icon-arrow-sm'></i>" +
            "</div>" +
            "</div>" +
            "</div>" +            
            "<ul class='attr-relative-drag-wrap dragEdit-wrap collapse-content collapse-content-active' id='dragEdit-"+dragWrapLen+"'>" +
            "</ul>" +
            "</li>";         		    
		    var $html = $(relativeHtml).prependTo($content);
            $html.find("select").css({"width":"15%","marginLeft":"16px"}).select2();
            addUnfold(el);
		    drag($(".dragEdit-wrap").length);
		    $CPF.closeLoading();	
    	    })
		 })
    }; 
    //提醒有未保存的节点
    function judgeSave() {    	
        /*var editBar = $("#structBaseEdit").find(".label-bar.edit");
        var editEntity = $("#structBaseEdit").find(".entity-edit-wrap.edit");
        if(editBar.length > 0 || editEntity.length > 0) {
            Dialog.notice("请先保存正在编辑的节点", "warning");
            return true;
        }*/
    }
    
    //根实体保存修改方法
    function entitySave(el) {    	
    	var $entityTitle = $(el).closest(".entity-title");    	
    	var type = 1;
    	var name = $entityTitle.children(".edit-input").val();
    	var abcattr = $(".entity_attr.active", $page).attr("data-cnName");
    	var abcattrCode = $(".entity_attr.active", $page).attr("data-code");
    	var order = $entityTitle.attr("data-order");
    	var id = $entityTitle.attr("data-id");
    	var dataType = "STRING";
    	var opt = $entityTitle.children(".node-ops-type").find("option:selected").val();
    	
    	var ret = /.{3,}/;
        if(!ret.test(name.trim())){
          Dialog.notice("【"+name + "】 必须三个字符及以上", "warning");
          return;
        }
    	
    	switch (opt) {
        	case "读":
        		opt = 1;
        		break;
        	case "写":
        		opt = 2;
        		break;
        	case "补":
        		opt = 3;
        		break;
        	case "增":
        		opt = 4;
        		break;
        	case "并":
        		opt = 5;
        		break;
        	default:
        		break;
    	}
    	$CPF.showLoading();
		Ajax.ajax(' admin/node/basicItemNode/saveOrUpdate', {
			 type: type,
			 name: name,
			 abcattr: abcattr,
			 abcattrCode: abcattrCode,
			 dataType: dataType,
			 opt: opt,
			 order: order,
			 id: id
		 }, function(data) {
			 if(data.state == "400") {
				 Dialog.notice(data.msg, "warning");
				 $CPF.closeLoading();
				 return;
			 }
			 var data = data.node;
			 //设置跟实体的order和id
			 var order = data.order;
			 var id = data.id;
			 $entityTitle.attr("data-order",order)
			 	.attr("data-id", id);
			 saveSuccess(el)
			 $CPF.closeLoading();
	    });
    }
    
    //标签保存修改方法
    function tagSave(el) {
    	 var entityCode = $(el).closest(".collapse-content").siblings(".collapse-header").attr("data-abcattrcode");
         if(entityCode == undefined) {
      	   entityCode =  $(el).closest(".collapse-content").siblings(".collapse-header").closest(".collapse-content").siblings(".collapse-header").attr("data-abcattrcode");
         }
         var abcattrCode = "";
        
		 Ajax.ajax(' admin/dictionary/basicItem/getLableObj', {
       	 code:entityCode
		 }, function(data) {
			var lableObj = data.lableObj; 
			
    	var $tagBar = $(el).closest(".label-bar");
    	if($(el).next(".icon-add-tag-relative").length > 0) { //关系下的标签 
    		if($tagBar.children(".tag-content").children("ul").children("li").length == 0) {
    			 Dialog.notice("请至少选择一个关系", "warning");
    			$tagBar.addClass("edit");
    			return;
    		}
    	}else {
    		abcattrCode =lableObj.code;
    		if($tagBar.children(".tag-content").children("ul").children("li").length == 0) {
    			Dialog.notice("请至少选择一个标签", "warning");
    			$tagBar.addClass("edit");
    			return;
    		}
    	}
    	var type = 3;
    	var dataType = "STRING";
    	var opt = $tagBar.children(".node-ops-type").find("option:selected").val();
    	switch (opt) {
        	case "读":
        		opt = 1;
        		break;
        	case "写":
        		opt = 2;
        		break;
        	case "补":
        		opt = 3;
        		break;
        	case "增":
        		opt = 4;
        		break;
        	case "并":
        		opt = 5;
        		break;
        	default:
        		break;
    	}
    	var name = $tagBar.children(".edit-input").val();    	
    	var order = $tagBar.attr("data-order");
    	var id = $tagBar.attr("data-id");
    	var parentId = $tagBar.closest(".collapse-content").prev(".collapse-header")
    						.attr("data-id"); 
    	var subdomain = []; 
    	var $tags = $tagBar.find(".tag-content").children("ul").children("li");
    	for(var i=0; i<$tags.length; i++) {
    		subdomain.push($($tags[i]).attr("data-text"));
    	}
    	subdomain = subdomain.join(",");
    	$CPF.showLoading();
    	Ajax.ajax(' admin/node/basicItemNode/saveOrUpdate', {
			 type: type,
			 name: name,
			 subdomain: subdomain,
			 abcattr: "",
			 dataType: dataType,
			 opt: opt,
			 order: order,
			 parentId: parentId,
			 id: id,
			 abcattrCode:abcattrCode
		 }, function(data) {
			 if(data.state == "400") {
				 Dialog.notice(data.msg, "warning");
				 $CPF.closeLoading();
				 return;
			 }
			 var data = data.node;
			 //设置当前节点order和id
			 var order = data.order;
			 var id = data.id;
			 $tagBar.attr("data-order",order)
			 	.attr("data-id", id);
			 saveSuccess(el)
			 $CPF.closeLoading();
		});
    	
		 });
    };
    
    //属性保存修改方法
    function attrSave(el) {
    	var $attrBar = $(el).closest(".label-bar");
    	var type = 2;
    	var dataType = $attrBar.children(".data-type").find("option:selected").val();
    	var opt = $attrBar.children(".node-ops-type").find("option:selected").val();
    	var name = $attrBar.children(".edit-input").val();    	
    	var order = $attrBar.attr("data-order");
    	var id = $attrBar.attr("data-id");
    	var parentId = $attrBar.closest(".collapse-content").prev(".collapse-header")
    						.attr("data-id"); 
    	var abcattr = $attrBar.children(".abc-attr").find("option:selected").val();
    	var abcattrCode = $attrBar.children(".abc-attr").find("option:selected").attr("data-id");
    	switch (opt) {
	        case "读":
	            opt = 1;
	            break;
	        case "写":
	            opt = 2;
	            break;
	        case "补":
	            opt = 3;
	            break;
	        case "增":
	            opt = 4;
	            break;
	        case "并":
	            opt = 5;
	            break;
	        default:
	            break;
	    }
    	$CPF.showLoading();
    	Ajax.ajax('admin/node/basicItemNode/saveOrUpdate', {
			 type: type,
			 name: name,
			 abcattr: abcattr,
			 abcattrCode: abcattrCode,
			 dataType: dataType,
			 opt: opt,
			 order: order,
			 parentId: parentId,
			 id: id
		 }, function(data) {
			 if(data.state == "400") {
				 Dialog.notice(data.msg, "warning");
				 $CPF.closeLoading();
				 return;
			 }
			 var data = data.node;
			 //设置当前节点order和id
			 var order = data.order;
			 var id = data.id;
			 $attrBar.attr("data-order",order)
			 	.attr("data-id", id);
			 saveSuccess(el)
			 $CPF.closeLoading();
		});
    };
    
    //属性组保存修改方法
    function attrGroupSave(el) {
    	var $attrGroupBar = $(el).closest(".label-bar");
    	var type = 6;    	
    	var name = $attrGroupBar.children(".edit-input").val();    	
    	var order = $attrGroupBar.closest(".collapse-header").attr("data-order");
    	var opt = $attrGroupBar.children(".node-ops-type").find("option:selected").val();
    	switch (opt) {
        	case "读":
        		opt = 1;
        		break;
        	case "写":
        		opt = 2;
        		break;
        	case "补":
        		opt = 3;
        		break;
        	case "增":
        		opt = 4;
        		break;
        	case "并":
        		opt = 5;
        		break;
        	default:
        		break;
    	}
    	var id = $attrGroupBar.closest(".collapse-header").attr("data-id");
    	var parentId = $attrGroupBar.closest(".collapse-content").prev(".collapse-header")
    						.attr("data-id"); 
    	        	
    	$CPF.showLoading();
    	Ajax.ajax('admin/node/basicItemNode/saveOrUpdate', {
			 type: type,
			 name: name,			
			 order: order,
			 parentId: parentId,
			 dataType: "STRING",
			 abcattr: name,			 
			 opt: opt,
			 id: id
		 }, function(data) {
			 if(data.state == "400") {
				 Dialog.notice(data.msg, "warning");
				 $CPF.closeLoading();
				 return;
			 }
			 var data = data.node;
			 //设置当前节点order和id
			 var order = data.order;
			 var id = data.id;
			 $attrGroupBar.closest(".collapse-header")
			 	.attr("data-order",order)
			 	.attr("data-id", id);
			 saveSuccess(el)
			 $CPF.closeLoading();
		});
    };
    
    //多值属性保存修改方法
    function moreAttrSave(el) {
    	var $moreAttrBar = $(el).closest(".label-bar");
    	var type = 4;
    	var dataType = $moreAttrBar.children(".data-type").find("option:selected").val();
    	var opt = $moreAttrBar.children(".node-ops-type").find("option:selected").val();
    	var name = $moreAttrBar.children(".edit-input").val();    	
    	var order = $moreAttrBar.closest(".collapse-header").attr("data-order");
    	var id = $moreAttrBar.closest(".collapse-header").attr("data-id");
    	var parentId = $moreAttrBar.closest(".collapse-content").prev(".collapse-header")
    						.attr("data-id"); 
    	var abcattr = $moreAttrBar.children(".abc-attr").find("option:selected").val();
    	var abcattrCode = $moreAttrBar.children(".abc-attr").find("option:selected").attr("data-id");    	  
    	switch (opt) {
	        case "读":
	            opt = 1;
	            break;
	        case "写":
	            opt = 2;
	            break;
	        case "补":
	            opt = 3;
	            break;
	        case "增":
	            opt = 4;
	            break;
	        case "并":
	            opt = 5;
	            break;
	        default:
	            break;
	    }
    	$CPF.showLoading();
    	Ajax.ajax('admin/node/basicItemNode/saveOrUpdate', {
			 type: type,
			 name: name,
			 abcattr: abcattr,
			 abcattrCode: abcattrCode,
			 dataType: dataType,
			 opt: opt,
			 order: order,
			 parentId: parentId,
			 id: id
		 }, function(data) {
			 if(data.state == "400") {
				 Dialog.notice(data.msg, "warning");
				 $CPF.closeLoading();
				 return;
			 }
			 var data = data.node;
			 //设置当前节点order和id
			 var order = data.order;
			 var id = data.id;
			 $moreAttrBar.closest(".collapse-header")
			 	.attr("data-order",order)
			 	.attr("data-id", id);
			 saveSuccess(el)
			 $CPF.closeLoading();
		});
    };
    
    //关系保存修改方法
    function relativeSave(e) {   
    	var $relativeBar = $(e).closest(".label-bar");
    	var type = 5;
    	var dataType = "STRING";
    	var opt = $relativeBar.children(".node-ops-type").find("option:selected").val();
    	switch (opt) {
        	case "读":
        		opt = 1;
        		break;
        	case "写":
        		opt = 2;
        		break;
        	case "补":
        		opt = 3;
        		break;
        	case "增":
        		opt = 4;
        		break;
        	case "并":
        		opt = 5;
        		break;
        	default:
        		break;
    	}
    	var name = $relativeBar.children(".edit-input").val();  
    	var abcattr = $relativeBar.children(".abc-attr").find("option:selected").val();
    	var abcattrCode = $relativeBar.children(".abc-attr").find("option:selected").attr("data-id");
    	var order = $relativeBar.closest(".collapse-header").attr("data-order");
    	var id = $relativeBar.closest(".collapse-header").attr("data-id");
    	var parentId = $relativeBar.closest(".collapse-content").prev(".collapse-header")
    						.attr("data-id");  
    	var dragWrapLen = $(".dragEdit-wrap").length + 1 ;    	
    	$CPF.showLoading();
    	Ajax.ajax('admin/node/basicItemNode/saveOrUpdate', {
			 type: type,
			 name: name,
			 abcattr: abcattr,	
			 abcattrCode: abcattrCode,
			 dataType: dataType,
			 opt: opt,
			 order: order,
			 parentId: parentId,
			 id: id
		 }, function(data) {
			 if(data.state == "400") {
				 Dialog.notice(data.msg, "warning");
				 $CPF.closeLoading();
				 return;
			 }
			 var data = data.node;
			 //设置当前节点order和id
			 var order = data.order;
			 var id = data.id;
			 $relativeBar.closest(".collapse-header")
			 	.attr("data-order",order)
			 	.attr("data-id", id);
			 var chLength = $(".entity-ch-wrap", $page).length;
			 var nest = "no-repeat";
			 if(chLength >= 2) {
				 nest = "repeat"
			 }
			 
			 Ajax.ajax('admin/node/basicItemNode/getAllAbcNode', '', function(data1) {
		            var allAbc = data1.allAbc;
			 
			 //展现出关系下的标签和abc HTML			 
			 var tagHtml = "<li class='add-tag clear-fix'>" +
	            "<div class='icon-label tag'>" +
	            "<i class='icon icon-tag'></i>" +
	            "<span class='text'>标签</span>" +
	            "</div>" +
	            "<div class='label-bar tag edit' data-order='' data-id=''>" +
	            "<input type='text' class='edit-input text' value='标签名称'>" +
	            "<span class='icon icon-toleft'></span>" +
	            "<div class='tag-content'>" +
	            "<ul class='clear-fix'>" +
	            "</ul>" +
	            "</div>" +
	            "<span class='icon icon-toright ban'></span>"
	            tagHtml += "<select class='node-ops-type'>";	
			 	var nodePosType = nodePosTypeLABEL;
			    for(var i=0; i<nodePosType.length; i++) {
			    	if(nodePosType[i] === "写") {
			    		tagHtml += "<option value='"+nodePosType[i]+"' selected>"+nodePosType[i]+"</option>";  	
			    	}else {
			    		tagHtml += "<option value='"+nodePosType[i]+"'>"+nodePosType[i]+"</option>"; 
			    	}
		            	         
		         };
		         tagHtml += "</select>";
		         tagHtml += "<div class='btn-wrap'>" +
	            "<i class='icon tag icon-save'></i>" +
	            "<i class='icon tag icon-add-tag-relative'></i>" +
	            "<i class='icon-simulate-trashsm'></i>" +
	            "</div>" +
	            "</div>" +
	            "</li>";
	           //----------------------------------------------------
		         var rabcHtml ="<li class='entity-ch-wrap rabc'>" +
	            "<div class='attr-abc-title collapse-header' data-abcattrcode='"+data.basicItem.code+"'  data-order='' data-id=''>" +
	            "<div class='icon-label rabc'>" +
	            "<i class='icon icon-abc'></i><span class='text'>RABC</span>" +
	            "</div>" +
	            "<div class='label-bar rabc edit' data-id=''>";
	            
		            rabcHtml += "<input class='edit-input text' value='"+allAbc[0].name+"'>"+
		            "<select class='relAbcnodeId'>";
		            
				    for(var i=0; i<allAbc.length; i++) {
				    	rabcHtml += "<option value='"+allAbc[i].id+"'>"+allAbc[i].name+"</option>"; 
			         };
			         rabcHtml += "</select>";
	            /*});*/
		         rabcHtml += "<div class='btn-wrap'>" +
	            "<i class='icon icon-save'></i>" +
	            "<i class='icon delStruct'></i>" +
	            /*"<i class='icon icon-add-abc group'></i>" +	          
	             "<i class='icon icon-arrow-sm'></i>" + */ 
	            "</div>" +
	            "</div>" +
	            "</div>" +
	            "<ul class='drag-wrap-repeat dragEdit-wrap collapse-content collapse-content-active' data-abcattrcode='"+data.basicItem.code+"' id='dragEdit-"+dragWrapLen+"'>" +
	            "</ul>" +
	            "</li>";
	            
	            //---------------------------------------------------
	            var abcHtml = "<li class='entity-ch-wrap "+nest+" abc'>" +
	            "<div class='attr-abc-title collapse-header' data-abcattrcode='"+data.basicItem.code+"'  data-order='' data-id=''>" +
	            "<div class='icon-label abc'>" +
	            "<i class='icon icon-abc'></i><span class='text'>ABC</span>" +
	            "</div>" +
	            "<div class='label-bar abc edit'>" +
	            "<input class='edit-input text' value='"+abcattr+"'>"+
	            "<span class='entity-only-title' data-abcattrcode='"+data.basicItem.code+"' data-abcattr='"+abcattr+"'>"+abcattr+"</span>";
	            abcHtml += "<select class='node-ops-type'>";	
	            var nodePosType = nodePosTypeLABEL;
			    for(var i=0; i<nodePosType.length; i++) {
			    	if(nodePosType[i] === "写") {
			    		abcHtml += "<option value='"+nodePosType[i]+"' selected>"+nodePosType[i]+"</option>";  	
			    	}else {
			    		abcHtml += "<option value='"+nodePosType[i]+"'>"+nodePosType[i]+"</option>"; 
			    	}
		            	         
		         };
		         abcHtml += "</select>";
		         abcHtml += "<div class='btn-wrap'>" +
	            "<i class='icon icon-save'></i>" +
	            "<i class='icon icon-add-abc group'></i>" +	            
	            "<i class='icon icon-arrow-sm'></i>" +
	            "</div>" +
	            "</div>" +
	            "</div>" +
	            "<ul class='drag-wrap-repeat dragEdit-wrap collapse-content collapse-content-active' data-abcattrcode='"+data.basicItem.code+"' id='dragEdit-"+dragWrapLen+"'>" +
	            "</ul>" +
	            "</li>";	
		         
		         var $content = $relativeBar.parent(".collapse-header").next(".collapse-content");	
		         
	        	 if(!$content.children("li").hasClass("add-tag")) {
	        		 var $thtml = $(tagHtml).appendTo($content);
	        		 $($thtml.find("select")).css({"width":"7%","marginLeft":"2px"}).select2();
	        	 }
	        	 if(!$content.children("li").hasClass("rabc")) {
					 var $rhtml = $(rabcHtml).appendTo($content);
					 $($rhtml.find("select")).css({"width":"13%","marginLeft":"60px"}).select2();
				}
	        	 
				if(!$content.children("li").hasClass("abc")) {
					var $ahtml = $(abcHtml).appendTo($content);   
					 $($ahtml.find("select")).css({"width":"13%","marginLeft":"60px"}).select2();
				 }
				
	         saveSuccess(e);
			 $CPF.closeLoading();
			  drag($(".dragEdit-wrap").length);
	       });
		});
		 
    };
    
    //abc属性保存修改方法
    function abcSave(el) {
    	var $abcBar = $(el).closest(".label-bar");
    	var type = 1;
    	var dataType = "STRING";
    	var opt = $abcBar.children(".node-ops-type").find("option:selected").val();
    	switch (opt) {
        	case "读":
        		opt = 1;
        		break;
        	case "写":
        		opt = 2;
        		break;
        	case "补":
        		opt = 3;
        		break;
        	case "增":
        		opt = 4;
        		break;
        	case "并":
        		opt = 5;
        		break;
        	default:
        		break;
    	}
    	var name = $abcBar.children(".edit-input").val();    	
    	var order = $abcBar.closest(".collapse-header").attr("data-order");
    	var id = $abcBar.closest(".collapse-header").attr("data-id");
    	var parentId = $abcBar.closest(".collapse-content").prev(".collapse-header")
    						.attr("data-id"); 
    	var abcattr = $abcBar.children(".entity-only-title").attr("data-abcattr");    	
    	var abcattrCode = $abcBar.children(".entity-only-title").attr("data-abcattrcode");    	  
    	
    	$CPF.showLoading();
    	Ajax.ajax('admin/node/basicItemNode/saveOrUpdate', {
			 type: type,
			 name: name,
			 abcattr: abcattr,
			 abcattrCode: abcattrCode,
			 dataType: dataType,
			 opt: opt,
			 order: order,
			 parentId: parentId,
			 id: id
		 }, function(data) {
			 if(data.state == "400") {
				 Dialog.notice(data.msg, "warning");
				 $CPF.closeLoading();
				 return;
			 }
			 var data = data.node;
			 //设置当前节点order和id
			 var order = data.order;
			 var id = data.id;
			 $abcBar.closest(".collapse-header")
			 	.attr("data-order",order)
			 	.attr("data-id", id);
			 saveSuccess(el)
			 $CPF.closeLoading();
		});
    };
    
    
    //abc属性保存修改方法
    function rAbcSave(el) {
    	var $abcBar = $(el).closest(".label-bar");
    	var type = 9;
    	var name = $abcBar.children(".edit-input").val();    	
    	var order = $abcBar.closest(".collapse-header").attr("data-order");
    	var id = $abcBar.closest(".collapse-header").attr("data-id");
    	var parentId = $abcBar.closest(".collapse-content").prev(".collapse-header")
    						.attr("data-id"); 
    	var relAbcnodeId = $abcBar.children(".relAbcnodeId").find("option:selected").val();
    	var opt = "1";
    	var dataType = "STRING";
    	$CPF.showLoading();
    	Ajax.ajax('admin/node/basicItemNode/saveOrUpdate', {
			 type: type,
			 name: name,
			 order: order,
			 parentId: parentId,
			 id: id,
			 relAbcnodeId:relAbcnodeId,
			 opt:opt,
			 dataType:dataType
		 }, function(data) {
			 if(data.state == "400") {
				 Dialog.notice(data.msg, "warning");
				 $CPF.closeLoading();
				 return;
			 }
			 var data = data.node;
			 //设置当前节点order和id
			 var order = data.order;
			 var id = data.id;
			 $abcBar.closest(".collapse-header")
			 	.attr("data-order",order)
			 	.attr("data-id", id);
			 saveSuccess(el)
			 $CPF.closeLoading();
		});
    };
    
  //filters  保存修改方法
    function filtersSave(el) {
    	var $abcBar = $(el).closest(".label-bar");
    	var name = $abcBar.children(".edit-input").val();    	
    	var id = $abcBar.closest(".collapse-header").attr("data-id");
    	var parentId = $abcBar.closest(".collapse-content").prev(".collapse-header")
    						.attr("data-id"); 
    	var opt = $abcBar.children(".node-ops-type").find("option:selected").val();
    	
    	$CPF.showLoading();
    	Ajax.ajax('admin/node/binFilterBody/saveOrUpdate', {
			 name: name,
			 parentId: parentId,
			 id: id,
			 isFilters: true,
			 dataType: 10,
			 opt:opt,
			 signFilter: "nodeFilter"
		 }, function(data) {
			if(data.code == "400") {
				 Dialog.notice(data.msg, "warning");
				 $CPF.closeLoading();
				 return;
			 }
			
			if (data.binFilter!= undefined) {
				var data = data.binFilterBody;
				 //设置当前节点order和id
				 var order = data.order;
				 var id = data.id;
				 $abcBar.closest(".collapse-header")
				 	.attr("data-order",order)
				 	.attr("data-id", id);
			}
			 
			 saveSuccess(el)
			 $CPF.closeLoading();
		});
    };
    
  //filterGroup  保存修改方法
    function filterGroupSave(el) {
    	var $abcBar = $(el).closest(".label-bar");
    	var name = $abcBar.children(".edit-input").val();    	
    	var id = $abcBar.closest(".collapse-header").attr("data-id");
    	var parentId = $abcBar.closest(".collapse-content").prev(".collapse-header")
    						.attr("data-id"); 
    	var opt = $abcBar.children(".node-ops-type").find("option:selected").val();
    	$CPF.showLoading();
    	Ajax.ajax('admin/node/binFilterBody/saveOrUpdate', {
			 name: name,
			 parentId: parentId,
			 id: id,
			 isFilters: false,
			 dataType: 11,
			 opt:opt,
			 signFilter: "nodeFilter"
		 }, function(data) {
			if(data.code == "400") {
				 Dialog.notice(data.msg, "warning");
				 $CPF.closeLoading();
				 return;
			 }
			
				var data = data.binFilterBody;
				 //设置当前节点order和id
				 var order = data.order;
				 var id = data.id;
				 $abcBar.closest(".collapse-header")
				 	.attr("data-order",order)
				 	.attr("data-id", id);
			 
			 saveSuccess(el)
			 $CPF.closeLoading();
		});
    };
    
  //filterSave  保存修改方法
    function filterSave(el) {
    	var $abcBar = $(el).closest(".label-bar");
    	var name = $abcBar.children(".filterName").val();    	
    	var id = $abcBar.closest(".collapse-header").attr("data-id");
    	var parentId = $abcBar.closest(".collapse-content").prev(".collapse-header")
    						.attr("data-id"); 
    	var opt = $abcBar.children(".node-ops-type").find("option:selected").val();
    	var filterType = $abcBar.children(".node-Symbol-type").find("option:selected").val();
    	var valueStr = $abcBar.children(".valueStr").val();  
    	
    	var abcattrCode = $abcBar.children(".abcattrCodeData").find("option:selected").val();
    	
    	$CPF.showLoading();
    	Ajax.ajax('admin/node/binFilterBody/saveOrUpdate', {
			 name: name,
			 parentId: parentId,
			 id: id,
			 isFilters: false,
			 dataType: 12,
			 opt:opt,
			 filterType: filterType,
			 value:valueStr,
			 abcattrCode:abcattrCode,
			 signFilter: "nodeFilter"
		 }, function(data) {
			if(data.code == "400") {
				 Dialog.notice(data.msg, "warning");
				 $CPF.closeLoading();
				 return;
			 }
			
				var data = data.binFilterBody;
				 //设置当前节点order和id
				 var order = data.order;
				 var id = data.id;
				 $abcBar.closest(".collapse-header")
				 	.attr("data-order",order)
				 	.attr("data-id", id);
			 
			 saveSuccess(el)
			 $CPF.closeLoading();
		});
    };
    
  //rFilterSave  保存修改方法
    function rFilterSave(el) {
    	var $abcBar = $(el).closest(".label-bar");
    	var name = $abcBar.children(".edit-input").val();    	
    	var id = $abcBar.closest(".collapse-header").attr("data-id");
    	var parentId = $abcBar.closest(".collapse-content").prev(".collapse-header")
    						.attr("data-id"); 
    	var opt = $abcBar.children(".node-ops-type").find("option:selected").val();
    	var filterType = $abcBar.children(".node-Symbol-type").find("option:selected").val();
    	
    	var relationData = $abcBar.children(".relationData").find("option:selected").val();
    	var rightRecordType = $abcBar.children(".relationData").find("option:selected").attr("rightRecordType");
    	$CPF.showLoading();
    	Ajax.ajax('admin/node/binFilterBody/saveOrUpdate', {
			 name: name,
			 parentId: parentId,
			 id: id,
			 isFilters: false,
			 dataType: 13,
			 opt:opt,
			 filterType: filterType,
			 subdomain:relationData,
			 signFilter: "nodeFilter"
		 }, function(data) {
			if(data.code == "400") {
				 Dialog.notice(data.msg, "warning");
				 $CPF.closeLoading();
				 return;
			 }
				var data = data.binFilterBody;
				 //设置当前节点order和id
				 var order = data.order;
				 var id = data.id;
				 $abcBar.closest(".collapse-header")
				 	.attr("data-order",order)
				 	.attr("data-id", id)
				 	.attr("entityid", rightRecordType);
			 
			 saveSuccess(el)
			 $CPF.closeLoading();
		});
    };
    
    $page.on("click", function () {    	
        removePop();
    });
      

    //收缩事件绑定
    $("#structBaseEdit").on("click", ".icon-arrow, .icon-arrow-sm", function (e) {
    	var $labelBar = $(this).closest(".label-bar");
    	var pSbId = $labelBar.closest(".collapse-header").attr("data-sbid");
    	
    	var entityId = $(this).closest(".collapse-header").attr("data-abcattrCode");//只有获取abc时， 这个才有值，否则为undefined
    	
    	var attr_group = $(this).closest(".attr-group").hasClass("attr-group");//如果为分组
    	if (attr_group) {
    		entityId = $(this).closest(".dragEdit-wrap").siblings(".entity-title").attr("data-abcattrCode");
    	}
    	
    	var more_attr = $(this).closest(".collapse-header").hasClass("more-attr");
    	if (more_attr) {
    		entityId = $(this).closest(".collapse-header").attr("data-abcattrCode");
    	}
    	
    	var attr_relative = $(this).closest(".collapse-header").hasClass("attr-relative");
    	if (attr_relative) {
    		entityId = $(this).closest(".dragEdit-wrap").siblings(".entity-title").attr("data-abcattrCode");
    	}
    	
    	e.stopPropagation();
    	var bar = $(this).closest(".label-bar")[0];
        var $content = $(this).closest(".collapse-header")
            .siblings(".collapse-content");
        var isRelative = $(this).closest(".label-bar").hasClass("attr-relative");        
        var needAjax = $content.hasClass("need-ajax");  //判断是否需要ajax获取数据   
        var nodeId = $(this).closest(".collapse-header").attr("data-id");
        $(this).toggleClass("active");
        if ($content.hasClass("collapse-content-active")) {
            $content
                .removeClass("collapse-content-active")
                .addClass("collapse-content-inactive");
        } else {
            $content
                .removeClass("collapse-content-inactive")
                .addClass("collapse-content-active");
        }              
       if(needAjax) {
    	   $content.removeClass("need-ajax");  
    	   initStructBaseChild(pSbId);
        }
    })

    //跟实体添加事件绑定
    $("#structBaseEdit").on("click", ".icon-add, .icon-add-abc", function (e) {
        e.stopPropagation();
        var hasSave = judgeSave();
        if(hasSave){
            return;
        }
        
        removePop();
        pop(this);
        $(this).addClass("active")
    });

    //属性组 多值属性 添加事件绑定
    $("#structBaseEdit").on("click", ".icon-add-sm", function (e) {
        e.stopPropagation();
        var hasSave = judgeSave();
        if(hasSave){
            return;
        }
        
        removePop();
        popsm(this);
        $(this).addClass("active")
    });

    //标签添加
    $("#structBaseEdit").on("click", ".icon-add-tag", function (e) {
        e.stopPropagation();
        $(this).closest(".label-bar.tag").addClass("edit");
        removePop();
        popTag(this);
        $(this).addClass("active");
    });
    
    //关系下标签添加
    $("#structBaseEdit").on("click", ".icon-add-tag-relative", function (e) {
        e.stopPropagation();
        $(this).closest(".label-bar.tag").addClass("edit");
        removePop();        
        popRelativeTag(this);
        $(this).addClass("active");
    });
    
    //filterGroup 事件绑定
    $("#structBaseEdit").on("click", ".icon-add-filterGroup", function (e) {
       
    	e.stopPropagation();
        var hasSave = judgeSave();
        if(hasSave){
            return;
        }
        removePop();
        popFilterGroup(this);
        $(this).addClass("active")
    });
    
  //filter 事件绑定
    $("#structBaseEdit").on("click", ".icon-add-filter", function (e) {
       
    	e.stopPropagation();
        var hasSave = judgeSave();
        if(hasSave){
            return;
        }
        removePop();
        popFilter(this);
        $(this).addClass("active")
    });
    
    //删除属性事件绑定
    $("#structBaseEdit").on("click", ".icon-trash , .delStruct", function (e) {
        e.stopPropagation();
        removePop();
        var $label = $(this).closest(".label-bar");

        popAttr(this);
        $(this).addClass("active")
    })

    //添加页中的事件绑定
    //这个有用
    $("#structBaseEdit").on("click", ".card>li.card-list", function (e) {
        e.stopPropagation();
        if ($("#structBaseEdit").find(".icon-add.active").length > 0) {
            var el = $("#structBaseEdit").find(".icon-add.active")[0];
        } else if ($("#structBaseEdit").find(".icon-add-sm.active").length > 0) {
            var el = $("#structBaseEdit").find(".icon-add-sm.active")[0];
        } else if ($("#structBaseEdit").find(".icon-add-abc.active").length > 0) {
            var el = $("#structBaseEdit").find(".icon-add-abc.active")[0];
        }else if ($("#structBaseEdit").find(".icon-add-filterGroup.active").length > 0) {
            var el = $("#structBaseEdit").find(".icon-add-filterGroup.active")[0];
        }else if ($("#structBaseEdit").find(".icon-add-filter.active").length > 0) {
            var el = $("#structBaseEdit").find(".icon-add-filter.active")[0];
        }
        
     if ($(this).hasClass("add-GROUP1D") || $(this).hasClass("add-GROUP2D")) { // 添加字段组和二维组
    	 var sbPid = $(el).closest(".collapse-header").attr("data-sbid");
    	 //这个暂时没用到
    	 var miCode = $(el).closest(".collapse-header").attr("data-micode");
    	 
         var structType = $(this).attr("structType");
         var structShowName = $(this).attr("structShowName");
         
         //这里要弹出框， 保存并回显
         Dialog.openDialog("admin/structBase/addAttrField?structType="+structType+"&sbPid=" +sbPid, structShowName, "", {
            width :920,
            height : 400,
            events		: {
         	   afterSave	: function(strucBase){
					if(strucBase){
						console.log(strucBase.id);
						
						if (strucBase.type == 101) {//单行属性分组
 							addGroup(el, strucBase);
						} else if (strucBase.type == 102) {
							addMoreGroup(el, strucBase)
						}
					}
				}
			}
        });
    	
      } else {
    	  
    	  var sbPid = $(el).closest(".collapse-header").attr("data-sbid");
     	 //这个暂时没用到
          var structType = $(this).attr("structType");
          var structShowName = $(this).attr("structShowName");
          
          //这里要弹出框， 保存并回显
          Dialog.openDialog("admin/structBase/addAttrField?structType="+structType+"&sbPid=" +sbPid, structShowName, "", {
             width :920,
             height : 400,
             events		: {
          	   afterSave	: function(strucBase){
 					if(strucBase){
 						console.log(strucBase.id);
 					
 						if (strucBase.type == 103) {
 							// 添加关系结构
 							addRStruc(el, strucBase);
						} else {
							addCommAttr(el, strucBase);
						}
						
 					}
 				}
 			}
         });
     	
      }
        
        removePop();
        $(el).removeClass("active");
    });

    //弹出页中的事件绑定添加标签
    $("#structBaseEdit").on("click", ".tag-checkbox-input", function (e) {   
        e.stopPropagation();
        var el = $("#structBaseEdit").find(".icon-add-tag.active");
        if(el.length == 0) {
        	el = $("#structBaseEdit").find(".icon-add-tag-relative.active")[0];
        }else {
        	el = $("#structBaseEdit").find(".icon-add-tag.active");
        }   
        var text = $(this).val();
        var id = $(this).attr("data-id");
        var ul = $(el).closest(".label-bar.tag").find("ul");
        var $parent = $(this).parent(".tag-checkbox");
        if ($parent.hasClass("tag-checkbox-checked")) {
            $parent.removeClass("tag-checkbox-checked");
            tagRemoveTag(el, text);            
        } else {
            $parent.addClass("tag-checkbox-checked");
            tagAddTag(el, text, id);            
        };
        judegArrow(ul)
    });

    //标签向左移动事件绑定
    $("#structBaseEdit").on("click", ".icon-toleft", function () {
        if ($(this).hasClass("ban")) {
            return;
        }
        var $ul = $(this).next(".tag-content").children("ul");
        var marginLeft = parseFloat($ul.css("marginLeft"));
        var ulWidth = parseFloat($ul.css("width"));
        var wrapWidth = parseFloat($ul.parent(".tag-content").css("width"));
        $("#structBaseEdit").find(".icon-toright").removeClass("ban");
        if (ulWidth - wrapWidth + marginLeft < 80) {
            marginLeft = wrapWidth - ulWidth;
            $(this).addClass("ban");
        } else {
            marginLeft = marginLeft - 80;
        }
        $ul.css("marginLeft", marginLeft);
    })

    //标签向右移动事件绑定
    $("#structBaseEdit").on("click", ".icon-toright", function () {
        if ($(this).hasClass("ban")) {
            return;
        }
        var $ul = $(this).prev(".tag-content").children("ul");
        var marginLeft = parseFloat($ul.css("marginLeft"));
        $("#structBaseEdit").find(".icon-toleft").removeClass("ban");
        if (marginLeft > -80) {
            marginLeft = 0;
            $(this).addClass("ban");
        } else {
            marginLeft = marginLeft + 80;
        }
        $ul.css("marginLeft", marginLeft);
    })
    
    //标签删除图标点击事件绑定
    $("#structBaseEdit").on("click", ".tag-content .icon-delete", function(){
    	var $tagUl = $(this).closest(".tag-content")
        .children("ul");     	
    	$(this).parent("li").remove();
    	menuWidth($tagUl[0]);
        judegArrow($tagUl);
        var ulWidth = parseFloat($tagUl.css("width"));
        var marginLeft = parseFloat($tagUl.css("margin-left"));
        var contentWidth = parseFloat($tagUl.parent(".tag-content").css("width"));
        
        if(ulWidth + marginLeft < contentWidth) {        	
        	var marginLeft = contentWidth - ulWidth;        	        	        	
        	if(marginLeft > 0) {
        		marginLeft = 0;
        	}
        	$tagUl.css("marginLeft",marginLeft);
        }
    })    

    //双击编辑
    $("#structBaseEdit").on("dblclick", ".label-bar", function(){
    	if(!$(this).hasClass("attr-relative")){
    		$(this).find(".edit-input").removeAttr("disabled");
        	$(this).find("select").removeAttr("disabled");
    	}else {
    		$(this).find(".edit-input").removeAttr("disabled");
    		$(this).find(".node-ops-type").removeAttr("disabled");
    		
    		//弹出关系的孩子
    		 var $content = $(this).parent(".collapse-header").next(".collapse-content");	
	         if($content.hasClass("collapse-content-inactive")){	
	        	 $(this).find(".icon-arrow-sm").trigger("click");	
			 }
    	}    	
        $(this).addClass("edit");
        
    })
    
    //双击编辑
    $("#structBaseEdit").on("dblclick", ".entity-title", function(){ 
    	$(this).find(".edit-input").removeAttr("disabled");
    	$(this).find("select").removeAttr("disabled");
        $(this).addClass("edit");
    })
    
     //保存
    $("#structBaseEdit").on("click", ".icon-edit", function() {        
        var $labelBar = $(this).closest(".label-bar");
        var sbId = $labelBar.closest(".collapse-header").attr("data-sbid");
        var structType = $labelBar.closest(".collapse-header").attr("data-structtype");
        var sbPid = $labelBar.closest(".collapse-header").attr("data-sbpid");
        
        //这里要弹出框， 保存并回显
        Dialog.openDialog("admin/structBase/addAttrField?sbId="+sbId+"&structType="+structType+"&sbPid=" +sbPid, "编辑属性" +sbId, "", {
            width :920,
            height : 400,
            events		: {
				afterSave	: function(strucBase){
					if(strucBase){
						console.log(strucBase.id);
						updateNode(strucBase, $labelBar);
					}
				}
			}
        });
        
    });
    //更改节点span  显示的值
    function updateNode(strucBase, $labelBar) {
    	$labelBar.find("#spanName").html(strucBase.title);
    }
    
    //删除-全部
    $("#structBaseEdit").on("click", ".opera.confirm", function(e) {  
    	e.stopPropagation();    
        var entityTitle = $(".icon-trash.active").closest(".entity-title");
        var labelBar = $(".delStruct.active").closest(".label-bar");
        if(entityTitle.length > 0) {
        	var el = $(".icon-trash.active")[0];
        	entityDelete(el);
        	return;
        }
        var el = $(".delStruct.active")[0];  
        
        attrDelete(el);  
    })
    
     //属性删除方法
    function attrDelete(el) {    	
    	var $attrBar = $(el).closest(".label-bar");    	
    	var sbId = $attrBar.closest(".collapse-header").attr("data-sbid");
    	var callback = function() {
    		$attrBar.parent().remove();    		
    	};    	
    	if($attrBar.hasClass("al-save")) {
    		deleteAjax(sbId, callback);
    	}else {
    		callback();
    		removePop();
    	}    
    }
    
    function entityDelete(el) {
    	var $entityTitle = $(el).closest(".entity-title");
    	var sbId = $entityTitle.attr("data-sbid");
    	var callback = function() {
    		$entityTitle.next(".collapse-content").html();
    		$entityTitle.parent(".entity-edit-wrap")
    			.removeClass("active")
    			.addClass("edit");
    	};
    	if($entityTitle.hasClass("al-save")) {
    		deleteAjax(sbId, callback);
    	}else {
    		callback();
    		removePop();
    	}
    }
    
    
    //删除的请求方法
    function deleteAjax(sbId, callback) {
    	$CPF.showLoading();
    	Ajax.ajax('admin/structBase/delStruct', {			
    		sbId: sbId
		 }, function(data) {	
			 
			 if (data.code == 200) {
				 callback();
				 removePop();
				 Dialog.notice(data.msg, "success");
			 } else {
				 Dialog.notice(data.msg, "error");
			 }
			
			 $CPF.closeLoading();
		});
    };
    
    //删除-仅组
    $("#structBaseEdit").on("click", ".opera.only-group", function(e) { 
    	e.stopPropagation();
    	var el = $(".delStruct.active")[0];       
        var labelBar = $(".delStruct.active").closest(".label-bar");        
        if(labelBar.hasClass("more-attr")) {
        	moreAttrDelete(el, false);
        }else if(labelBar.hasClass("attr-group")) {
        	attrGroupDelete(el, false);
        }
    })
    
    //实体选择点击事件绑定
    $("#structBaseEdit").on("click", ".entity_attr", function() {
    	var $attrArray = $(".entity_attr",$page);
    	for(var i=0; i<$attrArray.length; i++) {
    		if($($attrArray[i]).hasClass("active")) { //已经有选择过的了 就不能再点击选择了
    			return;
    		}
    	}    	
    	$(this).addClass("active");    		
    	getEntity(this);
    }) 
    
    
    function saveSuccess(el) {
		 $(el).closest(".label-bar").removeClass("edit");
		 $(el).closest(".entity-title").removeClass("edit");
	     $(el).closest(".entity-edit-wrap").removeClass("edit");
	     $(el).closest(".label-bar").find(".edit-input").attr("disabled", "true");
	     $(el).closest(".entity-title").find(".edit-input").attr("disabled", "true");
	     $(el).closest(".label-bar").find("select").attr("disabled", "true");
	     $(el).closest(".entity-title").find("select").attr("disabled", "true");
	     $(el).closest(".label-bar").addClass("al-save");
	}
})