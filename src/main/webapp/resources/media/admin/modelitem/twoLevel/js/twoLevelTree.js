
seajs.use(['dialog','utils', 'ajax', '$CPF'], function(Dialog, Utils, Ajax, $CPF){
	
	var $page = $('#twoLevelEdit');	
	
	//获取本页面belongModel code
	var moreLineCode = $page.attr("data-code");
	
	 $(function(){
		    $CPF.showLoading();
		    drag($(".dragEdit-wrap", $page).length);       
		    //初始化， 二级属性组
		    initTwoLevelGroup(moreLineCode);
		    
		    $(".label-bar", $page).addClass("al-save");
		    $CPF.closeLoading();
	    })
	    
	       //初始化  本模型的group 即 本模型的孩子
    function initTwoLevelGroup(moreLineCode) {
		 
		//获取本模型有哪些组
		 
		 Ajax.ajax('admin/modelItem/getMiEnumList', {
          	pmiCode:moreLineCode
		   }, function(data){		    	
			 var miEnumList =  data.modelItemEnumList;
			 Ajax.ajax('admin/modelItem/getMiNoEnumList', {
           	pmiCode:moreLineCode
		   }, function(data){		    	
			 var miNoEnumList =  data.modelItemNoEnumList;
			
			 Ajax.ajax('admin/modelItem/getMiTwoMapping', {
		    		moreLineCode: moreLineCode
				}, function(data) {			
					if (data.code == "400") {
						 Dialog.notice(data.msg, "error");
						 $CPF.closeLoading();
					} else{
						var miTwoMappingList = data.miTwoMappingList;
						var $parent = $(".collapse-header[data-code='"+moreLineCode+"']", $page).next(".collapse-content")[0];	
						for ( var key in miTwoMappingList) {
							var miTwoMapping = miTwoMappingList[key];
							initGroup($parent, miTwoMapping, miNoEnumList, miEnumList)
						}
					}
					
					$CPF.closeLoading();
				});
			 
		   });
			  
		   });
     	
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
	
	//预览弹出框
	$(".entity-edit-wrap", $page).on("click", "#preview", function (e) {
		var h = $(window).height(); 
		var w = $(window).width();
		var width = w - 200;
		var height = h - 100;
		Dialog.openDialog("admin/node/basicItemNode/preview?nodeId="+nodeId,"预览", "basicItemNode_preview",{
			width:width,
			height:height
		});
  	 });  
	
	 //二级属性组初始化方法
    function initGroup($parent, miTwoMapping, miNoEnumList, miEnumList) {
    	var attrGroupHtml = initTwoAttrGroup(miTwoMapping, miNoEnumList, miEnumList);
	    var $html = $(attrGroupHtml).prependTo($($parent));
	  $html.find("select").css({"width":"15%","marginLeft":"16px"}).select2();
        drag($(".dragEdit-wrap").length);
    }
    
    function initTwoAttrGroup(miTwoMapping, miNoEnumList, miEnumList) {
    	
        var dragWrapLen = $(".dragEdit-wrap").length + 1 ;
            var moreAttrHtml = "<li class='more-attr clear-fix'>" +
            "<div class='more-attr-title collapse-header' data-id='"+miTwoMapping.id+"' mlgroupCode='"+miTwoMapping.mlgroupCode+"'>" +
            "<div class='icon-label more-attr'>" +
            "<i class='icon icon-more-attr'></i>" +
            "<span class='text'>二级组</span>" +
            "</div>" +
            "<div class='label-bar twoLevelGroup al-save'>" +
            "<input type='text' class='edit-input text' value='"+miTwoMapping.name+"'>" +
            "<select class='abc-attr enumItemCode'>"
            for(var i=0; i<miEnumList.length; i++) {
            	if (miTwoMapping.enumItemCode == miEnumList[i].code) {
            		moreAttrHtml += "<option selected value='"+miEnumList[i].code+"'>"+miEnumList[i].name+"</option>";       
            	} else {
            		moreAttrHtml += "<option value='"+miEnumList[i].code+"'>"+miEnumList[i].name+"</option>";       
            	}
            }
            moreAttrHtml += "</select>";                    		    	    			    
	        moreAttrHtml += "<select class='node-ops-type valueItemCode'>";
	       
			for(var i=0; i<miNoEnumList.length; i++) {
				if (miTwoMapping.valueItemCode == miNoEnumList[i].code) {
					  moreAttrHtml += "<option selected value='"+miNoEnumList[i].code+"'>"+miNoEnumList[i].name+"</option>";
				} else {
					  moreAttrHtml += "<option value='"+miNoEnumList[i].code+"'>"+miNoEnumList[i].name+"</option>";
				}
		    }
		    moreAttrHtml += "</select>";
		    moreAttrHtml += "<div class='btn-wrap'>" +
		    "<i class='icon icon-save'></i>" +
		    "<i class='icon icon-add-sm group'></i>" +
		    "<i class='icon icon-trash-sm'></i>" +
		    "<i class='icon icon-arrow-sm active'></i>" +
		    "</div>" +
		    "</div>" +
		    "</div>" +
		    "<ul class='more-attr-drag-wrap dragEdit-wrap collapse-content  collapse-content-inactive need-ajax' id='dragEdit-"+dragWrapLen+"'>" +
		    "</ul>" +
		    "</li>"	
		    
		    return moreAttrHtml;
    };
    
	
	
    
	function addEntityOPT() {
		var $select = $("#twoLevelEdit .entity-title").find(".node-ops-type");	
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
    function drag(length) {
    	
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
    };
    
    
    
    
    
    
    
	/**
     * 获取实体信息方法 示例     
     */
	function getEntity(entity) {		
		var cnName = $(entity).attr("data-cnname");		
		$("#twoLevelEdit .entity-title>.edit-input").val(cnName);
		$("#twoLevelEdit .entity-title>.entity-only-title").html(cnName);
		$("#twoLevelEdit .entity-edit-wrap").addClass("active");
	}
	
    /**
     * 跟实体添加页面弹出方法
     * @param {当前点击元素,dom对象} el 
     */
    function pop(el) {
    	
        var relativeLength = $(el).closest(".collapse-header")
            .siblings(".collapse-content")
            .children(".attr-relative").length;
        var html = "<ul class='card'>";        
            html +=  "<li class='card-list add-twoAttr-group'>" +
                "<i class='icon icon-card-attr-group'></i>" +
                "<span class='text'>二级属性组</span>" +
                "</li>" +
                "</ul>";
        

        var wrap = $("#twoLevelEdit");
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
     * 属性组，多值属性添加 页面弹出方法
    */
    function popsm(el) {
        var addTagLength = $(el).closest(".collapse-header")
            .next(".add-tag").length;
        var isMoreAttr = $(el).closest(".collapse-header").hasClass("more-attr-title");
        var html = "<ul class='card'>";
        if (addTagLength > 0 || isMoreAttr ) {
            html += "<li class='card-list add-twoAttr'>" +
                "<i class='icon icon-card-attr'></i>" +
                "<span class='text'>添加二级属性</span>" +
                "</li>" +
                "</ul>";
        } 

        var wrap = $("#twoLevelEdit");
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
        var wrap = $("#twoLevelEdit");
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
        var wrap = $("#twoLevelEdit");
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
			var wrap = $("#twoLevelEdit");
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
    
    $("#twoLevelEdit").on("click", ".tag-search", function (e) {
        e.stopPropagation();        
    });
    
    //搜索标签点击事件绑定
    $("#twoLevelEdit").on("input propertychange", ".tag-search", function (e) {
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
			var wrap = $("#twoLevelEdit");
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

        var wrap = $("#twoLevelEdit");
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

        var wrap = $("#twoLevelEdit");
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
        $(".icon-trash-sm").removeClass("active");

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
            "<i class='icon icon-trash-sm'></i>" +
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
		         "<i class='icon icon-trash-sm'></i>" +
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
    
    /**
     * 添加级联属性方法
      */
    
    /**
     * 添加多值级联属性方法
      */
    
    
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
    
    
    
    
    /**
     * 添加二级属性方法
      */
    function addTwoAttr(el) {
    	// 这里获取必要的信息
    	
    	var $twoGroupBar = $(el).closest(".label-bar");
    	
    	var mappingId = $twoGroupBar.closest(".collapse-header").attr("data-id");
    	var name = $twoGroupBar.children(".edit-input").val();    	
    	var mlgroupcode = $twoGroupBar.closest(".collapse-header").attr("mlgroupcode");
    	var enumItemCode = $twoGroupBar.children(".enumItemCode").find("option:selected").val();
    	var valueItemCode = $twoGroupBar.children(".valueItemCode").find("option:selected").val();
    	
        var $content = $twoGroupBar.closest(".collapse-header").siblings(".collapse-content");
        $CPF.showLoading();
		Ajax.ajax('admin/modelItem/getMiEnumChild', {
			miCode: enumItemCode
		}, function(data) {			
			var casEnumChild = data.casEnumChild;
			var attrHtml = "<li class='add-attr clear-fix'>" +
            "<div class='icon-label attr twoLevelattr'>" +
            "<i class='icon icon-attr'></i>" +
            "<span class='text'>二级属性</span>" +
            "</div>" +
            "<div class='label-bar twoLevelattr edit' mlgroupcode='"+mlgroupcode+"' data-code='' mappingId='"+mappingId+"'>" +
            "<input type='text' class='edit-input text' value='二级属性名'>" +
            "<select class='abc-attr enumId'>"            
            for(var i=0; i<casEnumChild.length; i++) {
            	attrHtml += "<option value='"+casEnumChild[i].id+"'>"+casEnumChild[i].name+"</option>";                
            }
			attrHtml += "</select>";
	        attrHtml += "<div class='btn-wrap'>" +
	        "<i class='icon icon-save'></i>" +
	        "<i class='icon icon-trash-sm'></i>" +
	        "<i class='icon-simulate-trashsm'></i>" +
	        "</div>" +
	        "</div>" +
	        "</li>";
	        
	        var $html = $(attrHtml).prependTo($content);
	        $html.find("select").css({"width":"13%","marginLeft":"16px"}).select2();
	        addUnfold(el);
	        $CPF.closeLoading();			    		   
	    });		                      
    };
    
    function initTwoAttrChild(twoAttr, casEnumChild, $content) {
    	
        $CPF.showLoading();
			
			var attrHtml = "<li class='add-attr clear-fix'>" +
            "<div class='icon-label attr twoLevelattr'>" +
            "<i class='icon icon-attr'></i>" +
            "<span class='text'>二级属性</span>" +
            "</div>" +
            "<div class='label-bar twoLevelattr al-save' mlgroupcode='"+twoAttr[5]+"' data-code='"+twoAttr[0]+"' mappingId='"+twoAttr[1]+"'>" +
            "<input type='text' class='edit-input text' value='"+twoAttr[3]+"'>" +
            "<select class='abc-attr enumId'>"            
            for(var i=0; i<casEnumChild.length; i++) {
            	if (casEnumChild[i].id == twoAttr[2]) {
            		attrHtml += "<option selected value='"+casEnumChild[i].id+"'>"+casEnumChild[i].name+"</option>";      
            	} else {
            		attrHtml += "<option value='"+casEnumChild[i].id+"'>"+casEnumChild[i].name+"</option>";      
            	}       
            }
			attrHtml += "</select>";
	        attrHtml += "<div class='btn-wrap'>" +
	        "<i class='icon icon-save'></i>" +
	        "<i class='icon icon-trash-sm'></i>" +
	        "<i class='icon-simulate-trashsm'></i>" +
	        "</div>" +
	        "</div>" +
	        "</li>";
	        
	        var $html = $(attrHtml).prependTo($content);
	        $html.find("select").css({"width":"13%","marginLeft":"16px"}).select2();
	        $CPF.closeLoading();			    		   
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
		        "<i class='icon icon-trash-sm'></i>" +
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
     * 添加属性组方法
      */
    function addGroup(el) {
        var $content = $(el).closest(".collapse-header").siblings(".collapse-content");
        var entityId = $(el).closest(".collapse-header").attr("data-abcattrcode");
        var dragWrapLen = $(".dragEdit-wrap").length + 1 ;
        var attrGroupHtml = "<li class='attr-group'>" +
            "<div class='attr-group-title collapse-header' data-abcattrcode='"+entityId+"' data-order='' data-id=''>" +
            "<div class='icon-label attr-group'>" +
            "<i class='icon icon-attr-group'></i>" +
            "<span class='text'>属性组</span>" +
            "</div>" +
            "<div class='label-bar attr-group edit'>" +
            "<input type='text' class='edit-input text' value='属性组名称'>"
            attrGroupHtml += "<select class='node-ops-type'>";	
        var nodePosType = nodePosTypeATTRGROUP;
		    for(var i=0; i<nodePosType.length; i++) {
		    	if(nodePosType[i] === "写") {
		    		attrGroupHtml += "<option value='"+nodePosType[i]+"' selected>"+nodePosType[i]+"</option>";  	
		    	}else {
		    		attrGroupHtml += "<option value='"+nodePosType[i]+"'>"+nodePosType[i]+"</option>"; 
		    	}
	            	         
	         };
	         attrGroupHtml += "</select>";
	         attrGroupHtml += "<div class='btn-wrap'>" +
            "<i class='icon icon-save'></i>" +
            "<i class='icon icon-add-sm group'></i>" +
            "<i class='icon icon-trash-sm'></i>" +
            "<i class='icon icon-arrow-sm'></i>" +
            "</div>" +
            "</div>" +
            "</div>" +
            "<ul class='attr-group-drag-wrap dragEdit-wrap collapse-content collapse-content-active' id='dragEdit-"+dragWrapLen+"'>" +
            "</ul>" +
            "</li>"        
        var $html = $(attrGroupHtml).prependTo($content);  
	    $html.find("select").css({"width":"7%","marginLeft":"2"}).select2();
	    addUnfold(el)
        drag($(".dragEdit-wrap").length);
    };

    /**
     * 添加二级组的方法
      */
    function addTwoAttrGroup(el,mlgroupCode, miNoEnumList, miEnumList) {
        var $content = $(el).closest(".collapse-header").siblings(".collapse-content");
        var entityId =$(el).closest(".collapse-header").attr("data-abcattrcode");               
        var dragWrapLen = $(".dragEdit-wrap").length + 1 ;
        $CPF.showLoading();
			/*if (data.length == 0) {
				Dialog.notice("请在模型中添加多值属性", "warning");
				$CPF.closeLoading();	
				return;
			}*/
            var moreAttrHtml = "<li class='more-attr clear-fix'>" +
            "<div class='more-attr-title collapse-header' data-order='' data-id='' mlgroupCode='"+mlgroupCode+"'>" +
            "<div class='icon-label more-attr'>" +
            "<i class='icon icon-more-attr'></i>" +
            "<span class='text'>二级组</span>" +
            "</div>" +
            "<div class='label-bar twoLevelGroup edit'>" +
            "<input type='text' class='edit-input text' value='二级组名称'>" +
            "<select class='abc-attr enumItemCode'>"
            for(var i=0; i<miEnumList.length; i++) {
            	moreAttrHtml += "<option value='"+miEnumList[i].code+"'>"+miEnumList[i].name+"</option>";                
            }
            moreAttrHtml += "</select>";                    		    	    			    
	        moreAttrHtml += "<select class='node-ops-type valueItemCode'>";
	       
			for(var i=0; i<miNoEnumList.length; i++) {
			    moreAttrHtml += "<option value='"+miNoEnumList[i].code+"'>"+miNoEnumList[i].name+"</option>";	
		    }
		    moreAttrHtml += "</select>";
		    moreAttrHtml += "<div class='btn-wrap'>" +
		    "<i class='icon icon-save'></i>" +
		    "<i class='icon icon-add-sm group'></i>" +
		    "<i class='icon icon-trash-sm'></i>" +
		    "<i class='icon icon-arrow-sm'></i>" +
		    "</div>" +
		    "</div>" +
		    "</div>" +
		    "<ul class='more-attr-drag-wrap dragEdit-wrap collapse-content collapse-content-active' id='dragEdit-"+dragWrapLen+"'>" +
		    "</ul>" +
		    "</li>"		            
		    var $html = $(moreAttrHtml).prependTo($content);
		    $html.find("select").css({"width":"15%","marginLeft":"16px"}).select2();
		    drag($(".dragEdit-wrap").length);
		    addUnfold(el);
		    $CPF.closeLoading();			 		    		  
    };
    
    /**
     * 添加关系属性方法
      */
    function addRattr(el) {
        var $content = $(el).closest(".collapse-header").siblings(".collapse-content");
        var entityId = $(el).closest(".collapse-header").attr("data-abcattrcode");
        if (entityId == undefined) {
        	entityId = $(el).closest(".collapse-header").closest(".collapse-content").siblings(".collapse-header").attr("data-abcattrcode");
        }
        
        if($(el).closest(".collapse-header").hasClass("attr-abc-title")) {
        	entityId = $(el).closest(".attr-relative")
        		.find(".attr-relative-title")
        		.find(".label-bar")
        		.find(".abc-attr ")
        		.find("option:selected").attr("data-id");
        }
        $CPF.showLoading();
		Ajax.ajax('admin/dictionary/recordRelationType/getRelation', {
			leftRecordType: entityId,
			relationType: 6
		}, function(data) {			
			var relationList = data.relationList;
			if (relationList.length == 0) {
                Dialog.notice("请在模型中添加<对一>的关系", "warning");
                $CPF.closeLoading();    
                return;
	          } 
			
			if (data.code == 400) {
				Dialog.notice("操作失败！刷新后重试", "warning");
				$CPF.closeLoading();		
				return;
			}
			
			var attrHtml = "<li class='add-attr clear-fix'>" +
            "<div class='icon-label attr' data-type='8' data-order='' data-id=''>" +
            "<i class='icon icon-attr'></i>" +
            "<span class='text'>关系属性</span>" +
            "</div>" +
            "<div class='label-bar rattr edit' data-type='8' data-order='' data-id=''>" +
            "<input type='text' class='edit-input text' value='"+relationList[0].name+"'>" +
            "<select class='abc-attr relationCode'>"            
	            for(var i=0; i<relationList.length; i++) {            	
	            	attrHtml += "<option data-id='"+relationList[i].typeCode+"' data-rightId='"+relationList[i].rightRecordType+"' value='"+relationList[i].name+"'>"+relationList[i].name+"</option>";                
	            }
			attrHtml += "</select>";
			
			var rightRecordType = relationList[0].rightRecordType;
			
		   Ajax.ajax('admin/dictionary/basicItem/getComm', {
			   entityId:rightRecordType
		   }, function(data){		    	
	            var data = data.commList;
				if (data.length == 0) {
	                Dialog.notice("请在模型中添加属性", "warning");
	                $CPF.closeLoading();    
	                return;
		          } 
				attrHtml +=  "<select class='abc-attr rattrType'>"            
	            for(var i=0; i<data.length; i++) {
	            	attrHtml += "<option item-data-type='"+data[i][2]+"' data-id='"+data[i][0]+"' value='"+data[i][1]+"'>"+data[i][1]+"</option>";                
	            }
				attrHtml += "</select>";
				
				attrHtml += "<select class='data-type attr-type'>";            
				   Ajax.ajax('admin/node/basicItemNode/getDataType', {
					   dataType:data[0][2]
				   }, function(data){		
					   
				    	var dataTypeList = data.dataType;
				    	for(var i=0; i<dataTypeList.length; i++) {
				    		attrHtml += "<option value='"+dataTypeList[i][0]+"'>"+dataTypeList[i][1]+"</option>"; 
			            };
			            attrHtml += "</select>";
				  
				attrHtml += "<select class='node-ops-type'>";	
				var nodePosType = nodePosTypeRATTRIBUTE;
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
		         "<i class='icon icon-trash-sm'></i>" +
		         "<i class='icon-simulate-trashsm'></i>" +
		         "</div>" +
		         "</div>" +
		         "</li>";
		         var $html = $(attrHtml).prependTo($content);
		         $html.find("select").css({"width":"13%","marginLeft":"16px"}).select2();		            
		         addUnfold(el);
		         $CPF.closeLoading();		
				 });
		    });
			
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
            "<i class='icon icon-trash-sm'></i>" +
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
            "<i class='icon icon-trash-sm'></i>" +
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
     * 添加过滤条件 filterGroup
      */
    function addFilterGroup(el) {
        var $content = $(el).closest(".collapse-header").siblings(".collapse-content");
        
       var entityId = $(el).closest(".collapse-header").attr("entityId");
       var source = $(el).closest(".collapse-header").attr("source");
        
        var dragWrapLen = $(".dragEdit-wrap").length + 1 ;
        $CPF.showLoading();
            var relativeHtml = "<li class='attr-relative'>" +
            "<div class='attr-relative-title collapse-header' source='"+source+"' entityId='"+entityId+"' data-order='' data-id=''>" +
            "<div class='icon-label attr-relative'>" +
            "<i class='icon icon-attr-relative'></i><span class='text'>Group</span>" +
            "</div>" +
            "<div class='label-bar filterGroup edit'>" +
            "<input type='text' class='edit-input text' value='filterGroup名称'>";
            relativeHtml += "<select class='node-ops-type'>";	
            var nodePosType = optFILTERS;
            for(var key in nodePosType) {
 		    	relativeHtml += "<option value='"+key+"'>"+nodePosType[key]+"</option>"; 
 	         }
	        relativeHtml += "</select>";
	        relativeHtml += "<div class='btn-wrap'>" +
            "<i class='icon icon-save'></i>" +  
            "<i class='icon icon-add-filter group'></i>" +
            "<i class='icon icon-trash-sm'></i>" +
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
     * 添加过滤条件 filter
      */
    function addFilter(el) {
        var $content = $(el).closest(".collapse-header").siblings(".collapse-content");
        
        var entityId = $(el).closest(".collapse-header").attr("entityId");
        var source = $(el).closest(".collapse-header").attr("source");
        
        var dragWrapLen = $(".dragEdit-wrap").length + 1 ;
        $CPF.showLoading();
            var relativeHtml = "<li class='attr-relative'>" +
            "<div class='attr-relative-title collapse-header' source='"+source+"' entityId='"+entityId+"' data-order='' data-id=''>" +
            "<div class='icon-label attr-relative'>" +
            "<i class='icon icon-attr-relative'></i><span class='text'>filter</span>" +
            "</div>" +
            "<div class='label-bar filter edit'>" +
            "<input type='text' class='edit-input filterName text' value='filter名称'>";
            
            relativeHtml += "<select class='abcattrCodeData'>";	
            //getCriteriaSymbol
    	    Ajax.ajax('admin/dictionary/basicItem/getComm', {
    	    	entityId:entityId
    	    }, function(data){		
    	    	var entityData = data.commList;
    	    	 Ajax.ajax('admin/dictionary/basicItem/getRepeatChild', {
    	    		 repeatId:entityId
    	    	    }, function(data){		
    	    	    	var repeatData = data.repeatChild;
    	    	if (source == "moreAttr") {
    	    		 for(var key in repeatData) {
    	 		    	relativeHtml += "<option value='"+repeatData[key].code+"'>"+repeatData[key].cnName+"</option>"; 
    	 	         };
    	    	} else {
    	    		 for(var key in entityData) {
    	 		    	relativeHtml += "<option value='"+entityData[key][0]+"'>"+entityData[key][1]+"</option>"; 
    	 	         };
    	    	}
		   
	        relativeHtml += "</select>";
            
            relativeHtml += "<select class='node-Symbol-type'>";	
            //getCriteriaSymbol
    	    Ajax.ajax('admin/node/binFilterBody/getCriteriaSymbol', {
    	    	dataType:12
    	    }, function(data){		
    	    	var nodeSymbolType = data.symbolMap;
		    for(var key in nodeSymbolType) {
		    	relativeHtml += "<option value='"+key+"'>"+nodeSymbolType[key]+"</option>"; 
	         };
	        relativeHtml += "</select>"+
	        "<input type='text' class='edit-input valueStr text' value='value'>";
            
            relativeHtml += "<select class='node-ops-type'>";	
            var nodePosType = optFILTERS;
            for(var key in nodePosType) {
 		    	relativeHtml += "<option value='"+key+"'>"+nodePosType[key]+"</option>"; 
 	         }
	        relativeHtml += "</select>";
	        relativeHtml += "<div class='btn-wrap'>" +
            "<i class='icon icon-save'></i>" +  
            /*"<i class='icon icon-add-filter group'></i>" +*/
            "<i class='icon icon-trash-sm'></i>" +
           /* "<i class='icon icon-arrow-sm'></i>" +*/
            "</div>" +
            "</div>" +
            "</div>" +            
            "<ul class='attr-relative-drag-wrap dragEdit-wrap collapse-content collapse-content-active' id='dragEdit-"+dragWrapLen+"'>" +
            "</ul>" +
            "</li>";         		    
		    var $html = $(relativeHtml).prependTo($content);
            $html.find("select").css({"width":"13%","marginLeft":"16px"}).select2();
            $html.find("select.node-ops-type").css({"width":"8%","marginLeft":"16px"}).select2();
            
            addUnfold(el);
		    drag($(".dragEdit-wrap").length);
		    $CPF.closeLoading();	
    	    })
    	    })
    	    })
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
            "<i class='icon icon-trash-sm'></i>" +
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
        var editBar = $("#twoLevelEdit").find(".label-bar.edit");
        var editEntity = $("#twoLevelEdit").find(".entity-edit-wrap.edit");
        if(editBar.length > 0 || editEntity.length > 0) {
            Dialog.notice("请先保存正在编辑的节点", "warning");
            return true;
        }
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
    
    //二级属性保存修改方法
    function twoLevelattrSave(el) {
    	var $attrBar = $(el).closest(".label-bar");
    	var type = 4;
    	var dataCode = $attrBar.attr("data-code");
    	var mappingId = $attrBar.attr("mappingid");
    	var enumId = $attrBar.children(".enumId").find("option:selected").val();
    	
    	var mlgroupcode = $attrBar.attr("mlgroupcode");
    	var name = $attrBar.children(".edit-input").val();    	
    	$CPF.showLoading();
    	Ajax.ajax('admin/modelItem/createModelItemAttr', {
    		'modelItem.code':dataCode,
    		'modelItem.name':name,
    		'modelItem.type':type,
    		'modelItem.parent':mlgroupcode,
    		'miTowlevel.code':dataCode,
    		'miTowlevel.mappingId':mappingId,
    		'miTowlevel.enumId':enumId
		 }, function(data) {
			 if(data.state == "400") {
				 Dialog.notice(data.msg, "warning");
				 $CPF.closeLoading();
				 return;
			 }
			 var modelItem = data.modelItem;
			 //设置当前节点order和id
			 var order = data.order;
			 $attrBar.attr("data-code",modelItem.code);
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
    
    //二级组保存修改方法
    function twoLevelGroupSave(el) {
    	var $twoGroupBar = $(el).closest(".label-bar");
    	
    	var id = $twoGroupBar.closest(".collapse-header").attr("data-id");
    	var name = $twoGroupBar.children(".edit-input").val();    	
    	var mlgroupcode = $twoGroupBar.closest(".collapse-header").attr("mlgroupcode");
    	var enumItemCode = $twoGroupBar.children(".enumItemCode").find("option:selected").val();
    	var valueItemCode = $twoGroupBar.children(".valueItemCode").find("option:selected").val();
    	
    	$CPF.showLoading();
    	Ajax.ajax('admin/modelItem/miTwoMappSaveOrUP', {
    		id: id,
    		name: name,
    		mlgroupCode: mlgroupcode,
    		enumItemCode: enumItemCode,
    		valueItemCode: valueItemCode
		 }, function(data) {
			 if(data.state == "400") {
				 Dialog.notice(data.msg, "warning");
				 $CPF.closeLoading();
				 return;
			 }
			 var miTwolevelMapping = data.miTwolevelMapping;
			 //设置当前节点id
			 var id = miTwolevelMapping.id;
			 $twoGroupBar.closest(".collapse-header")
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
	            "<i class='icon icon-trash-sm'></i>" +
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
    
    //二级属性，删除的请求方法
    function twoLevelattrDeleteAjax(miCode, callback) {
    	$CPF.showLoading();
    	Ajax.ajax('admin/modelItem/delModelItem', {			
    		miCode: miCode
		 }, function(data) {	
			 
			 if (data.code == 400) {
				 Dialog.notice(data.msg, "error");
				 return;
			 }
			 
			 callback();
			 removePop();
			 $CPF.closeLoading();
		});
    };
    
    //二级属性组，删除的请求方法
    function twoLevelGroupDeleteAjax(mappingId, callback) {
    	$CPF.showLoading();
    	Ajax.ajax('admin/modelItem/delTwoAttrMapping', {			
    		mappingId: mappingId
		 }, function(data) {
			 if (data.code == 400) {
				 Dialog.notice(data.msg, "error");
				 return;
			 }
			 
			 callback();
			 removePop();
			 $CPF.closeLoading();
		});
    };
    
    
    
    //删除Filters的请求方法
    function deleteAjaxFilters(id, callback) {
    	$CPF.showLoading();
    	Ajax.ajax('admin/node/binFilterBody/doDelete', {			
			 id: id
		 }, function(data) {
			 
			 if(data.code == "400") {
				 Dialog.notice(data.msg, "error");
				 $CPF.closeLoading();
				 return;
			 }
			 
			 callback();
			 removePop();
			 $CPF.closeLoading();
		});
    };
       
    
    //跟实体删除方法
    function entityDelete(el) {
    	var $entityTitle = $(el).closest(".entity-title");
    	var id = $entityTitle.attr("data-id");
    	var isDelChil = true;
    	var callback = function() {
    		$entityTitle.next(".collapse-content").html();
    		$entityTitle.parent(".entity-edit-wrap")
    			.removeClass("active")
    			.addClass("edit");
    	};
    	if($entityTitle.hasClass("al-save")) {
    		deleteAjax(id, isDelChil, callback);
    	}else {
    		callback();
    		removePop();
    	}
    }
    
    //二级属性组删除方法
    function twoLevelGroupDelete(el) {    	
    	var $attrBar = $(el).closest(".label-bar");  
    	debugger;
    	var mappingId = $attrBar.closest(".collapse-header").attr("data-id");
    	var callback = function() {
    		$attrBar.parent(".add-attr").remove();    		
    	};    	
    	if($attrBar.hasClass("al-save")) {
    		twoLevelGroupDeleteAjax(mappingId, callback);
    	}else {
    		callback();
    		removePop();
    	}    
    }
    
    //二级属性删除方法
    function twoLevelattrDelete(el) {    	
    	var $attrBar = $(el).closest(".label-bar");  
    	var code = $attrBar.attr("data-code");
    	var callback = function() {
    		$attrBar.parent(".add-attr").remove();    		
    	};    	
    	if($attrBar.hasClass("al-save")) {
    		twoLevelattrDeleteAjax(code, callback);
    	}else {
    		callback();
    		removePop();
    	}    
    }
    
    
    

    $page.on("click", function () {    	
        removePop();
    });
      

    //收缩事件绑定
    $("#twoLevelEdit").on("click", ".icon-arrow, .icon-arrow-sm", function (e) {
    	var $labelBar = $(this).closest(".label-bar");
    	
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
    	   var $labbar = $(this).closest(".label-bar");
    	   var twoLevelGroup =  $(this).closest(".label-bar").hasClass("twoLevelGroup");
    	   if (twoLevelGroup) {//这里是加载二级属性组的孩子
     		var mappingId =$labbar.closest(".collapse-header").attr("data-id");
     		var enumItemCode = $labbar.children(".enumItemCode").find("option:selected").val();
     		var $parent = $labbar.closest(".collapse-header").siblings(".collapse-content");
     		 Ajax.ajax('admin/modelItem/getTwoAttrByMappingId', {
     			mappingId: mappingId
     		}, function(data) {		
     			var twoAttrList = data.twoAttrList;
     			Ajax.ajax('admin/modelItem/getMiEnumChild', {
     				miCode: enumItemCode
     			}, function(data) {	
     				var casEnumChild = data.casEnumChild;
     				for ( var key in twoAttrList) {
         				initTwoAttrChild(twoAttrList[key], casEnumChild, $parent);
    				}
     				
     			});
     		 
     		});
     	  }
     	 $content.removeClass("need-ajax");
        }
    })

    //跟实体添加事件绑定
    $("#twoLevelEdit").on("click", ".icon-add, .icon-add-abc", function (e) {
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
    $("#twoLevelEdit").on("click", ".icon-add-sm", function (e) {
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
    $("#twoLevelEdit").on("click", ".icon-add-tag", function (e) {
        e.stopPropagation();
        $(this).closest(".label-bar.tag").addClass("edit");
        removePop();
        popTag(this);
        $(this).addClass("active");
    });
    
    //关系下标签添加
    $("#twoLevelEdit").on("click", ".icon-add-tag-relative", function (e) {
        e.stopPropagation();
        $(this).closest(".label-bar.tag").addClass("edit");
        removePop();        
        popRelativeTag(this);
        $(this).addClass("active");
    });
    
    //filterGroup 事件绑定
    $("#twoLevelEdit").on("click", ".icon-add-filterGroup", function (e) {
       
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
    $("#twoLevelEdit").on("click", ".icon-add-filter", function (e) {
       
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
    $("#twoLevelEdit").on("click", ".icon-trash, .icon-trash-sm", function (e) {
        e.stopPropagation();
        removePop();
        var $label = $(this).closest(".label-bar");
        
            popAttr(this);
        $(this).addClass("active")
    })

    //添加页中的事件绑定
    $("#twoLevelEdit").on("click", ".card>li.card-list", function (e) {
        e.stopPropagation();
        if ($("#twoLevelEdit").find(".icon-add.active").length > 0) {
            var el = $("#twoLevelEdit").find(".icon-add.active")[0];
        } else if ($("#twoLevelEdit").find(".icon-add-sm.active").length > 0) {
            var el = $("#twoLevelEdit").find(".icon-add-sm.active")[0];
        } else if ($("#twoLevelEdit").find(".icon-add-abc.active").length > 0) {
            var el = $("#twoLevelEdit").find(".icon-add-abc.active")[0];
        }else if ($("#twoLevelEdit").find(".icon-add-filterGroup.active").length > 0) {
            var el = $("#twoLevelEdit").find(".icon-add-filterGroup.active")[0];
        }else if ($("#twoLevelEdit").find(".icon-add-filter.active").length > 0) {
            var el = $("#twoLevelEdit").find(".icon-add-filter.active")[0];
        }
        if ($(this).hasClass("add-tag")) {
            addTag(el);
        } else if ($(this).hasClass("add-attr")) {        	
        	if($(el).closest(".label-bar").hasClass("more-attr")){        		
        		addAttrM(el);
        	}else {
        		addAttr(el);
        	}          
        } else if ($(this).hasClass("add-twoAttr")) {
        	
        	addTwoAttr(el);
        } else if ($(this).hasClass("add-twoAttr-group")) {
           // addMoreAttr(el);
        	
        	
        	 var mlgroupCode = $(el).closest(".collapse-header").attr("data-code");
           	
             Ajax.ajax('admin/modelItem/getMiEnumList', {
             	pmiCode:mlgroupCode
  		   }, function(data){		    	
  			 var miEnumList =  data.modelItemEnumList;
  			 Ajax.ajax('admin/modelItem/getMiNoEnumList', {
              	pmiCode:mlgroupCode
   		   }, function(data){		    	
   			 var miNoEnumList =  data.modelItemNoEnumList;
   			addTwoAttrGroup(el,mlgroupCode, miNoEnumList, miEnumList);
   		   });
  			  
  		   });
        	
        	
        	
        	
        } else if ($(this).hasClass("add-relative")) {
            addRelative(el);
        } else if ($(this).hasClass("add-cascade-attr")) {
        	addCascadeAttr(el);//添加级联属性方法
        } else if ($(this).hasClass("add-more-cascade-attr")) {
        	addMoreCascadeAttr(el);//添加多值级联属性方法
        } else if ($(this).hasClass("add-rattr-attr")) {
        	addRattr(el);//添加关系属性
        } else if ($(this).hasClass("add-filters")) {
        	 var filtersBar = $(el).closest(".label-bar").closest(".collapse-header").parent().find(".label-bar.filters");
        	 if(filtersBar.length > 0) {
                 Dialog.notice("只能添加一个filters", "warning");
                 return true;
             }
        	 
        	var source =  $(this).attr("source");
        	addFilters(el, source);
        } else if ($(this).hasClass("add-filterGroup")) {
        	addFilterGroup(el);
        } else if ($(this).hasClass("add-filter")) {
        	addFilter(el);
        } else if ($(this).hasClass("add-rFilter")) {
        	addrFilter(el);
        }  else if ($(this).hasClass("add-refattribute-attr")) {
        	addRefattributeAttr(el);//添加引用属性方法
        } else if ($(this).hasClass("add-rRefattribute-attr")) {
        	addrRefattributeAttr(el);//添加引用->引用属性方法
        }  
        
        removePop();
        $(el).removeClass("active");
    });

    //弹出页中的事件绑定添加标签
    $("#twoLevelEdit").on("click", ".tag-checkbox-input", function (e) {   
        e.stopPropagation();
        var el = $("#twoLevelEdit").find(".icon-add-tag.active");
        if(el.length == 0) {
        	el = $("#twoLevelEdit").find(".icon-add-tag-relative.active")[0];
        }else {
        	el = $("#twoLevelEdit").find(".icon-add-tag.active");
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
    $("#twoLevelEdit").on("click", ".icon-toleft", function () {
        if ($(this).hasClass("ban")) {
            return;
        }
        var $ul = $(this).next(".tag-content").children("ul");
        var marginLeft = parseFloat($ul.css("marginLeft"));
        var ulWidth = parseFloat($ul.css("width"));
        var wrapWidth = parseFloat($ul.parent(".tag-content").css("width"));
        $("#twoLevelEdit").find(".icon-toright").removeClass("ban");
        if (ulWidth - wrapWidth + marginLeft < 80) {
            marginLeft = wrapWidth - ulWidth;
            $(this).addClass("ban");
        } else {
            marginLeft = marginLeft - 80;
        }
        $ul.css("marginLeft", marginLeft);
    })

    //标签向右移动事件绑定
    $("#twoLevelEdit").on("click", ".icon-toright", function () {
        if ($(this).hasClass("ban")) {
            return;
        }
        var $ul = $(this).prev(".tag-content").children("ul");
        var marginLeft = parseFloat($ul.css("marginLeft"));
        $("#twoLevelEdit").find(".icon-toleft").removeClass("ban");
        if (marginLeft > -80) {
            marginLeft = 0;
            $(this).addClass("ban");
        } else {
            marginLeft = marginLeft + 80;
        }
        $ul.css("marginLeft", marginLeft);
    })
    
    //标签删除图标点击事件绑定
    $("#twoLevelEdit").on("click", ".tag-content .icon-delete", function(){
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
    $("#twoLevelEdit").on("dblclick", ".label-bar", function(){
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
    $("#twoLevelEdit").on("dblclick", ".entity-title", function(){ 
    	$(this).find(".edit-input").removeAttr("disabled");
    	$(this).find("select").removeAttr("disabled");
        $(this).addClass("edit");
    })
    
    //保存
    $("#twoLevelEdit").on("click", ".icon-save", function() {        
        var labelBar = $(this).closest(".label-bar");
        
       if(labelBar.hasClass("twoLevelattr")) {
        	twoLevelattrSave(this);
        }else if(labelBar.hasClass("twoLevelGroup")) {
        	//moreAttrSave(this);
        	twoLevelGroupSave(this);
        }
    });
    
    //删除-全部
    $("#twoLevelEdit").on("click", ".opera.confirm", function(e) {  
    	e.stopPropagation();    
        var entityTitle = $(".icon-trash.active").closest(".entity-title");
        var labelBar = $(".icon-trash-sm.active").closest(".label-bar");
       
        var el = $(".icon-trash-sm.active")[0];        
        if(labelBar.hasClass("twoLevelGroup")) {         	
        	//attrDelete(el);
        	twoLevelGroupDelete(el);
        }else if(labelBar.hasClass("twoLevelattr")) {
        	twoLevelattrDelete(el);
        }
    })
    
    //删除-仅组
    $("#twoLevelEdit").on("click", ".opera.only-group", function(e) { 
    	e.stopPropagation();
    	var el = $(".icon-trash-sm.active")[0];       
        var labelBar = $(".icon-trash-sm.active").closest(".label-bar");        
        if(labelBar.hasClass("more-attr")) {
        	moreAttrDelete(el, false);
        }else if(labelBar.hasClass("attr-group")) {
        	attrGroupDelete(el, false);
        }
    })
    
    //实体选择点击事件绑定
    $("#twoLevelEdit").on("click", ".entity_attr", function() {
    	var $attrArray = $(".entity_attr",$page);
    	for(var i=0; i<$attrArray.length; i++) {
    		if($($attrArray[i]).hasClass("active")) { //已经有选择过的了 就不能再点击选择了
    			return;
    		}
    	}    	
    	$(this).addClass("active");    		
    	getEntity(this);
    }) 
    
    //修改名称
    $("#twoLevelEdit").on("change", "select.abc-attr", function(){
    	var _value = $(this).find("option:selected").text();
    	var $input = $(this).siblings(".edit-input");
    	var _options = $(this).find("option");
    	$input.val(_value);
    	
    	//修改关系属性对应实体的值
    	var $rattr = $(this).siblings(".rattrType.select2-offscreen");
    	var rightEntityId = $(this).find("option:selected").attr("data-rightid");
    	
    	if ($rattr.hasClass("rattrType")) {
    	 Ajax.ajax('admin/dictionary/basicItem/getComm', {
			   entityId:rightEntityId
		   }, function(data){		    	
	            var data = data.commList;
	            var rattrHtml="";
				if (data.length == 0) {
	                Dialog.notice("请在模型中添加属性", "warning");
	                $CPF.closeLoading();    
	                return;
		          } 
	            for(var i=0; i<data.length; i++) {
	            	rattrHtml += "<option item-data-type='"+data[i][2]+"' data-id='"+data[i][0]+"' value='"+data[i][1]+"'>"+data[i][1]+"</option>";                
	            }
	            
	            $rattr.children().remove();
	            $rattr.append(rattrHtml); 
	            $rattr.css({"width":"15%","marginLeft":"16px"}).select2();
		   });
    	} else {
    		
    		//修改dataType的值
        	var $dataType = $(this).siblings(".data-type.select2-offscreen");
        	var itemDataType = $(this).find("option:selected").attr("item-data-type");
        	
        	if ($dataType.hasClass("attr-type")) {
        		Ajax.ajax('admin/node/basicItemNode/getDataType', {
                    dataType:itemDataType
                }, function(data){     
                     var dataTypeList = data.dataType;
                     var attrHtml="";
                     for(var i=0; i<dataTypeList.length; i++) {
                         if(i == 0) {
                             attrHtml += "<option value='"+dataTypeList[i][0]+"' selected>"+dataTypeList[i][1]+"</option>";  
                         }else {
                             attrHtml += "<option value='"+dataTypeList[i][0]+"'>"+dataTypeList[i][1]+"</option>"; 
                         }                           
                     };
                     $dataType.children().remove();
                     $dataType.append(attrHtml); 
                     $dataType.css({"width":"13%","marginLeft":"16px"}).select2();
                });
        	}
    		
    	}
    	 
    	
    	
    	/*var isOption = false;
    	var isDefault = false;    	
    	if($input.val() == "属性名" || $input.val() == "多值属性名称"){
    		isDefault = true;
    	}
    	for(var i=0; i<_options.length; i++) {    		    	    		    
    		if($(_options[i]).attr("value") == $input.val()){
    			isOption = true;
    			break;
    		}
    	}
    	if(isOption || isDefault) {
    		$input.val(_value);
    	}    	    	    	*/
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