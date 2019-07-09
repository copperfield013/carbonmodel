
seajs.use(['dialog','utils', 'ajax', '$CPF'], function(Dialog, Utils, Ajax, $CPF){
	
	var $page = $('#filterEdit');	
	
	//获取本页面belongModel code
	//var moreLineCode = $page.attr("data-code");
	
	var filterId = $page.attr("data-filterId");
	var belongModel = $page.attr("data-belongModel");
	
	 $(function(){
		  //  $CPF.showLoading();
		    drag($(".dragEdit-wrap", $page).length);   
		    
		    //初始化， 过滤条件组
		    initFilterGroup(filterId, belongModel);
		    
		    $(".label-bar", $page).addClass("al-save");
		  //  $CPF.closeLoading();
	  });
	 
	 
	 /**
	  * 保存树形结构
	  */
	 $($page).on("click", "#btn-save", function (e) {
		 
		var belongModel = $page.attr("data-belongModel");
		var miCode = $page.attr("data-miCode");
		var type = $page.attr("data-type");
		
		var filterId = $(".rootFilterNode").children("li").find(".collapse-header").attr("data-id");
		if (filterId == undefined) {
			 Dialog.notice("请添加过滤组！", "warning");
			return;
		}
		 
		 Ajax.ajax('admin/expressionAndFilter/saveFilter',{
			 miCode:miCode,
			 type:type,
			 filterId:filterId
		 }, function(data) {	
			 if (data.code == 200) {
				 Dialog.notice("操作成功！", "success"); 
			 }
		 });
		 
	 });
	 
	       //初始化  本模型的group 即 本模型的孩子
    function initFilterGroup(fid, belongModel) {
		 
    	
    	if (fid == "") {
    		return;
    	}
    	
		//获取本模型有哪些组
			 Ajax.ajax('admin/expressionAndFilter/getMiFilterGroup', {
				 filterId: fid
				}, function(data) {		
					
					if (data.code == "400") {
						 Dialog.notice(data.msg, "error");
						 $CPF.closeLoading();
					} else{
						var miFilterGroup = data.miFilterGroup;
						var $parent = $(".rootFilterNode");	
						if (miFilterGroup != null) {
							
							if (miFilterGroup.type==1) {
								// 初始化普通组
								initCommGroup($parent, miFilterGroup, belongModel);
							} else if (miFilterGroup.type==2) {
								//初始化关系组
								
							}
						}
						
					}
					
					$CPF.closeLoading();
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
	
    
	function addEntityOPT() {
		var $select = $("#filterEdit .entity-title").find(".node-ops-type");	
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
    
    //abc初始化方法
    
    
    
    //关系属性初始化方法
	
    //多值属性下的属性初始化方法
    
    
	/**
     * 获取实体信息方法 示例     
     */
	function getEntity(entity) {		
		var cnName = $(entity).attr("data-cnname");		
		$("#filterEdit .entity-title>.edit-input").val(cnName);
		$("#filterEdit .entity-title>.entity-only-title").html(cnName);
		$("#filterEdit .entity-edit-wrap").addClass("active");
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
            html +=  "<li class='card-list add-comm-group'>" +
                "<i class='icon icon-card-attr-group'></i>" +
                "<span class='text'>过滤普通组</span>" +
                "</li>" +
                "<li class='card-list add-rela-group'>" +
                "<i class='icon icon-card-attr-group'></i>" +
                "<span class='text'>过滤关系组</span>" +
                "</li>" +
                "</ul>";
        

        var wrap = $("#filterEdit");
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
       
        var html = "<ul class='card'>";
            html += "<li class='card-list add-filter-criterion'>" +
                "<i class='icon icon-card-attr'></i>" +
                "<span class='text'>添加过滤条件</span>" +
                "</li>" +
                "</ul>";

        var wrap = $("#filterEdit");
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
        var wrap = $("#filterEdit");
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
        var wrap = $("#filterEdit");
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
			var wrap = $("#filterEdit");
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
    
    $("#filterEdit").on("click", ".tag-search", function (e) {
        e.stopPropagation();        
    });
    
    //搜索标签点击事件绑定
    $("#filterEdit").on("input propertychange", ".tag-search", function (e) {
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
			var wrap = $("#filterEdit");
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

        var wrap = $("#filterEdit");
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

        var wrap = $("#filterEdit");
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
			enumCode: enumItemCode
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
    
    function initTwoAttrChild(twoAttr, enumItemCode, $content) {
    	
        $CPF.showLoading();
		Ajax.ajax('admin/modelItem/getMiEnumChild', {
			enumCode: enumItemCode
		}, function(data) {		
			var casEnumChild = data.casEnumChild;
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
	    });	
    }
    
    /**
     * 添加过滤表达式
      */
    function addCriterion(el, belongmodel, modelItemList, operatorMap, groupId) {
        var $content = $(el).closest(".collapse-header").siblings(".collapse-content");
        var dragWrapLen = $(".dragEdit-wrap").length + 1 ;
        $CPF.showLoading();
        
            var moreAttrHtml = "<li class='add-attr clear-fix'>" +
            "<div class='attr-group-title collapse-header' data-id='' data-groupId='"+groupId+"'>" +
            "<div class='icon-label attr'>" +
            "<i class='icon icon-attr'></i>" +
            "<span class='text'>表达式</span>" +
            "</div>" +
            "<div class='label-bar filterCriterion edit'>" +
            "<input type='text' class='edit-input name text' value='表达式名称'>" +
            "<select class='abc-attr itemCode'>"
            	for ( var key in modelItemList) {
            		moreAttrHtml += "<option value='"+modelItemList[key].code+"'>"+modelItemList[key].name+"</option>";  
				}
            moreAttrHtml += "</select>";  
            
            moreAttrHtml +=  "<select class='abc-attr operator'>"
        	
            	for ( var mapKey in operatorMap) {
            		 moreAttrHtml += "<option value='"+mapKey+"'>"+operatorMap[mapKey]+"</option>";  
				}
            
            moreAttrHtml += "</select>"; 
            
            
            moreAttrHtml +=  "<input type='text' class='value edit-input text' value='值'>";
            
		    moreAttrHtml += "<div class='btn-wrap'>" +
		    "<i class='icon icon-save'></i>" +
		    "<i class='icon icon-trash-sm'></i>" +
		    "</div>" +
		    "</div>" +
		    "</div>" +
		    "<ul class='attr-group-drag-wrap dragEdit-wrap collapse-content collapse-content-active' id='dragEdit-"+dragWrapLen+"'>" +
		    "</ul>" +
		    "</li>"		            
		    var $html = $(moreAttrHtml).prependTo($content);
		    $html.find("select").css({"width":"15%","marginLeft":"16px"}).select2();
		    drag($(".dragEdit-wrap").length);
		    addUnfold(el);
		    $CPF.closeLoading();			 		    		  
    };
    
    
    function initCriterion($parent, belongmodel, modelItemList, operatorMap, miFilterCriterion){
    	var attrGroupHtml = getCriterion(belongmodel, modelItemList, operatorMap, miFilterCriterion);
	    var $html = $(attrGroupHtml).prependTo($($parent));
	    $html.find("select").css({"width":"15%","marginLeft":"16px"}).select2();
        drag($(".dragEdit-wrap").length);
    }
    
    /**
     * 初始化过滤表达式
     */
    function getCriterion(belongmodel, modelItemList, operatorMap, miFilterCriterion) {
        var dragWrapLen = $(".dragEdit-wrap").length + 1 ;
        
            var moreAttrHtml = "<li class='add-attr clear-fix'>" +
            "<div class='attr-group-title collapse-header' data-id='"+miFilterCriterion.id+"' data-groupId='"+miFilterCriterion.groupId+"'>" +
            "<div class='icon-label attr'>" +
            "<i class='icon icon-attr'></i>" +
            "<span class='text'>表达式</span>" +
            "</div>" +
            "<div class='label-bar filterCriterion al-save'>" +
            "<input type='text' class='edit-input name text' value='"+miFilterCriterion.name+"'>" +
            "<select class='abc-attr itemCode'>"
            	for ( var key in modelItemList) {
            		if (miFilterCriterion.itemCode == modelItemList[key].code) {
            			moreAttrHtml += "<option selected value='"+modelItemList[key].code+"'>"+modelItemList[key].name+"</option>";  
            		} else {
            			moreAttrHtml += "<option value='"+modelItemList[key].code+"'>"+modelItemList[key].name+"</option>";  
            		}
				}
            moreAttrHtml += "</select>";  
            
            moreAttrHtml +=  "<select class='abc-attr operator'>"
        	
            	for ( var mapKey in operatorMap) {
            		
            		if (miFilterCriterion.operator == mapKey) {
            			moreAttrHtml += "<option selected value='"+mapKey+"'>"+operatorMap[mapKey]+"</option>";  
            		} else {
            			moreAttrHtml += "<option value='"+mapKey+"'>"+operatorMap[mapKey]+"</option>";  
            		}
            		
				}
            
            moreAttrHtml += "</select>"; 
            
            moreAttrHtml +=  "<input type='text' class='value edit-input text' value='值'>";
            
		    moreAttrHtml += "<div class='btn-wrap'>" +
		    "<i class='icon icon-save'></i>" +
		    "<i class='icon icon-trash-sm'></i>" +
		    "</div>" +
		    "</div>" +
		    "</div>" +
		    "<ul class='attr-group-drag-wrap dragEdit-wrap collapse-content collapse-content-inactive' id='dragEdit-"+dragWrapLen+"'>" +
		    "</ul>" +
		    "</li>";
		    
		    return moreAttrHtml;
    };
    

    /**
     * 添加普通组的方法
      */
    function addCommGroup(el, belongmodel) {
        var $content = $(el).closest(".collapse-header").siblings(".collapse-content");
                
        var dragWrapLen = $(".dragEdit-wrap").length + 1 ;
        $CPF.showLoading();
        
            var moreAttrHtml = "<li class='add-attr clear-fix'>" +
            "<div class='more-attr-title collapse-header' data-belongmodel='"+belongmodel+"' data-type='1' data-order='' data-id='' data-pid=''>" +
            "<div class='icon-label more-attr'>" +
            "<i class='icon icon-more-attr'></i>" +
            "<span class='text'>普通组</span>" +
            "</div>" +
            "<div class='label-bar commGroup edit'>" +
            "<input type='text' class='edit-input text' value='组名称'>" +
            "<select class='abc-attr logicalOperator'>"
            	
           /* moreAttrHtml += "<option value='1'>OR</option>";   */
            moreAttrHtml += "<option value='2'>AND</option>";  
            
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
    
    //单行组初始化方法
    function initCommGroup($parent, miFilterGroup, belongmodel){
    	var attrGroupHtml = getCommGroup(miFilterGroup, belongmodel);
	    var $html = $(attrGroupHtml).prependTo($($parent));
	   /* $html.find("select").css({"width":"7%","marginLeft":"2"}).select2();*/
        drag($(".dragEdit-wrap").length);
    }
    
    /**
     * 普通组初始化方法
     */
    function getCommGroup(miFilterGroup, belongmodel) {
    	
        var dragWrapLen = $(".dragEdit-wrap").length + 1 ;
        $CPF.showLoading();
            var moreAttrHtml = "<li class='add-attr clear-fix'>" +
            "<div class='more-attr-title collapse-header' data-belongmodel='"+belongmodel+"' data-type='"+miFilterGroup.type+"' data-order='' data-id='"+miFilterGroup.id+"' data-pid='"+miFilterGroup.pid+"'>" +
            "<div class='icon-label more-attr'>" +
            "<i class='icon icon-more-attr'></i>" +
            "<span class='text'>普通组</span>" +
            "</div>" +
            "<div class='label-bar commGroup al-save'>" +
            "<input type='text' class='edit-input text' value='"+miFilterGroup.name+"'>" +
            "<select  class='abc-attr logicalOperator'>";
            	
           /* moreAttrHtml += "<option value='1'>OR</option>";   */
            
            if (miFilterGroup.logicalOperator == 2) {
            	 moreAttrHtml += "<option value='2' selected='selected'>AND</option>";  
            } else {
            	  /* moreAttrHtml += "<option value='1'>OR</option>";   */
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
		    "<ul class='more-attr-drag-wrap dragEdit-wrap collapse-content collapse-content-inactive need-ajax' id='dragEdit-"+dragWrapLen+"'>" +
		    "</ul>" +
		    "</li>";
		    
		    return moreAttrHtml;
    };
    
    
    //提醒有未保存的节点
    function judgeSave() {    	
        var editBar = $("#filterEdit").find(".label-bar.edit");
        var editEntity = $("#filterEdit").find(".entity-edit-wrap.edit");
        if(editBar.length > 0 || editEntity.length > 0) {
            Dialog.notice("请先保存正在编辑的节点", "warning");
            return true;
        }
    }
    
 // 过滤普通组保存方法
    function filterSave(el) {
    	var $attrBar = $(el).closest(".label-bar");
    	var  $header = $attrBar.closest(".collapse-header");
    	
    	var type = 1;
    	var id = $header.attr("data-id");
    	var name = $attrBar.children(".edit-input").val();  
    	var logicalOperator = $attrBar.children(".logicalOperator").find("option:selected").val();
    	var pid = $header.attr("data-pid");
    	
    	if (pid == "undefined") {
    		pid="";
    	}
    	
    	return;
    	
    	$CPF.showLoading();
    	Ajax.ajax('admin/expressionAndFilter/createFilterCommGroup', {
    		'id':id,
    		'name':name,
    		'type':type,
    		'logicalOperator':logicalOperator,
    		'pid':pid
		 }, function(data) {
			 if(data.state == "400") {
				 Dialog.notice(data.msg, "warning");
				 $CPF.closeLoading();
				 return;
			 }
			 var miFilterGroup = data.miFilterGroup;
			 //设置当前节点order和id
			 $header.attr("data-id",miFilterGroup.id);
			 $header.attr("data-pid",miFilterGroup.pid);
			 saveSuccess(el)
			 $CPF.closeLoading();
		});
    };
    
    
    
    // 过滤普通组保存方法
    function commGroupSave(el) {
    	var $attrBar = $(el).closest(".label-bar");
    	var  $header = $attrBar.closest(".collapse-header");
    	
    	var type = 1;
    	var id = $header.attr("data-id");
    	var name = $attrBar.children(".edit-input").val();  
    	var logicalOperator = $attrBar.children(".logicalOperator").find("option:selected").val();
    	var pid = $header.attr("data-pid");
    	
    	if (pid == "undefined") {
    		pid="";
    	}
    	
    	$CPF.showLoading();
    	Ajax.ajax('admin/expressionAndFilter/createFilterCommGroup', {
    		'id':id,
    		'name':name,
    		'type':type,
    		'logicalOperator':logicalOperator,
    		'pid':pid
		 }, function(data) {
			 if(data.state == "400") {
				 Dialog.notice(data.msg, "warning");
				 $CPF.closeLoading();
				 return;
			 }
			 var miFilterGroup = data.miFilterGroup;
			 //设置当前节点order和id
			 $header.attr("data-id",miFilterGroup.id);
			 $header.attr("data-pid",miFilterGroup.pid);
			 saveSuccess(el)
			 $CPF.closeLoading();
		});
    };
    
    
    /**
     * 保存表达式
     */
    function filterCriterionSave(el) {
    	var $attrBar = $(el).closest(".label-bar");
    	var  $header = $attrBar.closest(".collapse-header");
    	
    	var id = $header.attr("data-id");
    	var groupId = $header.attr("data-groupid");
    	var name = $attrBar.children(".name").val();  
    	var itemCode = $attrBar.children(".itemCode").find("option:selected").val();
    	var operator = $attrBar.children(".operator").find("option:selected").val();
    	var value = $attrBar.children(".value").val();
    	
    	$CPF.showLoading();
    	Ajax.ajax('admin/expressionAndFilter/createFilterCriterion', {
    		'id':id,
    		'name':name,
    		'itemCode':itemCode,
    		'operator':operator,
    		'value':value,
    		'groupId':groupId
		 }, function(data) {
			 if(data.state == "400") {
				 Dialog.notice(data.msg, "warning");
				 $CPF.closeLoading();
				 return;
			 }
			 var miFilterCriterion = data.miFilterCriterion;
			 //设置当前节点order和id
			 $header.attr("data-id",miFilterCriterion.id);
			 $header.attr("data-groupid",miFilterCriterion.groupId);
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
    
    
    /**
     * 普通分组删除
     */
    function commGroupDelete(el) {    	
    	var $attrBar = $(el).closest(".label-bar");  
    	var $header = $attrBar.closest(".collapse-header");
    	var id = $header.attr("data-id");
    	
    	var isDelChil = false;
    	var callback = function() {
    		$attrBar.closest(".add-attr").remove();    		
    	};    	
    	if($attrBar.hasClass("al-save")) {
    		commGroupDeleteAjax(id, callback);
    	}else {
    		callback();
    		removePop();
    	}    
    }
    
  //删除普通分组的请求方法
    function commGroupDeleteAjax(id, callback) {
    	$CPF.showLoading();
    	Ajax.ajax('admin/expressionAndFilter/delMiFiltergroup', {			
    		groupId: id
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
    
    //表达式删除方法
    function filterCriterionDelete(el) {    	
    	var $attrBar = $(el).closest(".label-bar");  
    	var $header = $attrBar.closest(".collapse-header");
    	var id = $header.attr("data-id");
    	
    	var isDelChil = false;
    	var callback = function() {
    		$attrBar.closest(".add-attr").remove();    		
    	};    	
    	if($attrBar.hasClass("al-save")) {
    		filterCriterionDeleteAjax(id, callback);
    	}else {
    		callback();
    		removePop();
    	}    
    }
    
  //删除表达式的请求方法
    function filterCriterionDeleteAjax(id, callback) {
    	$CPF.showLoading();
    	Ajax.ajax('admin/expressionAndFilter/delMiFilterCriterion', {			
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

    $page.on("click", function () {    	
        removePop();
    });
      
    //收缩事件绑定
    $("#filterEdit").on("click", ".icon-arrow, .icon-arrow-sm", function (e) {
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
     	
    	   // 普通分组的孩子
    	   if($labbar.hasClass("commGroup")) {
    		   
    		  var $header = $labbar.closest(".collapse-header");
    		  var belongmodel=  $header.attr("data-belongmodel");
    		  var groupId =  $header.attr("data-id");
    		  
    		  // 获取分组的孩子， 并遍历
    		  // 分组的孩子有表达式， 还有分组
    		  Ajax.ajax('admin/expressionAndFilter/getMiFiltergroupChild',{
    			  groupId:groupId
          		}, function(data){	
          			var miFilterCriterionList = data.miFilterCriterionList;
          			
          			Ajax.ajax('admin/modelItem/getFilOperator',{
                		
                	}, function(data1){	
                		
                		var operatorMap = data1.operatorMap;
        	 			Ajax.ajax('admin/modelItem/getShowCode', {
        	        		miCode:belongmodel
        	 		   }, function(data2){	
        		 			var modelItemList = data2.modelItemList;
        		 			var $parent = $header.siblings(".collapse-content");
        		 			
    	          			for ( var key in miFilterCriterionList) {
    	          				var miFilterCriterion = miFilterCriterionList[key];
    	          				
    	          				//初始化表达式
    	              			initCriterion($parent, belongmodel, modelItemList, operatorMap, miFilterCriterion);
    						}
        	 			   
        	 		   });
         			   
         		   });
          			
          		});
    	   }
    	   
     	 $content.removeClass("need-ajax");
        }
    })

    //跟实体添加事件绑定
    $("#filterEdit").on("click", ".icon-add, .icon-add-abc", function (e) {
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
    $("#filterEdit").on("click", ".icon-add-sm", function (e) {
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
    $("#filterEdit").on("click", ".icon-add-tag", function (e) {
        e.stopPropagation();
        $(this).closest(".label-bar.tag").addClass("edit");
        removePop();
        popTag(this);
        $(this).addClass("active");
    });
    
    //关系下标签添加
    $("#filterEdit").on("click", ".icon-add-tag-relative", function (e) {
        e.stopPropagation();
        $(this).closest(".label-bar.tag").addClass("edit");
        removePop();        
        popRelativeTag(this);
        $(this).addClass("active");
    });
    
    //filterGroup 事件绑定
    $("#filterEdit").on("click", ".icon-add-filterGroup", function (e) {
       
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
    $("#filterEdit").on("click", ".icon-add-filter", function (e) {
       
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
    $("#filterEdit").on("click", ".icon-trash, .icon-trash-sm", function (e) {
        e.stopPropagation();
        removePop();
        var $header = $(this).closest(".label-bar").hasClass("attr-group");
        
        if ($header) { //delete-list-c
            popGroupAttr(this);
        } else { //delete-list
            popAttr(this);
        }
        $(this).addClass("active")
    })

    //添加页中的事件绑定
    $("#filterEdit").on("click", ".card>li.card-list", function (e) {
        e.stopPropagation();
        if ($("#filterEdit").find(".icon-add.active").length > 0) {
            var el = $("#filterEdit").find(".icon-add.active")[0];
        } else if ($("#filterEdit").find(".icon-add-sm.active").length > 0) {
            var el = $("#filterEdit").find(".icon-add-sm.active")[0];
        } else if ($("#filterEdit").find(".icon-add-abc.active").length > 0) {
            var el = $("#filterEdit").find(".icon-add-abc.active")[0];
        }else if ($("#filterEdit").find(".icon-add-filterGroup.active").length > 0) {
            var el = $("#filterEdit").find(".icon-add-filterGroup.active")[0];
        }else if ($("#filterEdit").find(".icon-add-filter.active").length > 0) {
            var el = $("#filterEdit").find(".icon-add-filter.active")[0];
        }
        
        
        var $header = $(el).closest(".collapse-header");
        var belongmodel = $header.attr("data-belongmodel");
      if ($(this).hasClass("add-comm-group")) {
    	 debugger;
    	  if ($header.hasClass("entity-title")) {
    		  var $content = $header.siblings(".collapse-content");
        	  var len = $content.children("li").length;
        	  if (len<1) {
        		  addCommGroup(el, belongmodel);
        	  } else {
        		  Dialog.notice("只能添加一个组", "warning");
        	  }
    	  } else {
    		  addCommGroup(el, belongmodel);
    	  }
    	  
        } else if ($(this).hasClass("add-relative")) {
            addRelative(el);
        } else if ($(this).hasClass("add-filter-criterion")) {
        	
        	var groupId = $header.attr("data-id");
        	
        	//  这里要获取   belongmodel 的普通属性
        	Ajax.ajax('admin/modelItem/getFilOperator',{
        		
        	}, function(data1){	
        		
        		var operatorMap = data1.operatorMap;
	 			Ajax.ajax('admin/modelItem/getShowCode', {
	        		miCode:belongmodel
	 		   }, function(data){	
		 			var modelItemList = data.modelItemList;
		 		
		 			if (modelItemList.length >1) {
		 				addCriterion(el, belongmodel, modelItemList, operatorMap, groupId);
		 			} else {
		 				Dialog.notice("请先在模型中添加普通属性！", "warning");
		 			}
	 			   
	 		   });
 			   
 		   });
        	// 这里获取操作符号
        	
        }
        
        removePop();
        $(el).removeClass("active");
    });

    //弹出页中的事件绑定添加标签
    $("#filterEdit").on("click", ".tag-checkbox-input", function (e) {   
        e.stopPropagation();
        var el = $("#filterEdit").find(".icon-add-tag.active");
        if(el.length == 0) {
        	el = $("#filterEdit").find(".icon-add-tag-relative.active")[0];
        }else {
        	el = $("#filterEdit").find(".icon-add-tag.active");
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
    $("#filterEdit").on("click", ".icon-toleft", function () {
        if ($(this).hasClass("ban")) {
            return;
        }
        var $ul = $(this).next(".tag-content").children("ul");
        var marginLeft = parseFloat($ul.css("marginLeft"));
        var ulWidth = parseFloat($ul.css("width"));
        var wrapWidth = parseFloat($ul.parent(".tag-content").css("width"));
        $("#filterEdit").find(".icon-toright").removeClass("ban");
        if (ulWidth - wrapWidth + marginLeft < 80) {
            marginLeft = wrapWidth - ulWidth;
            $(this).addClass("ban");
        } else {
            marginLeft = marginLeft - 80;
        }
        $ul.css("marginLeft", marginLeft);
    })

    //标签向右移动事件绑定
    $("#filterEdit").on("click", ".icon-toright", function () {
        if ($(this).hasClass("ban")) {
            return;
        }
        var $ul = $(this).prev(".tag-content").children("ul");
        var marginLeft = parseFloat($ul.css("marginLeft"));
        $("#filterEdit").find(".icon-toleft").removeClass("ban");
        if (marginLeft > -80) {
            marginLeft = 0;
            $(this).addClass("ban");
        } else {
            marginLeft = marginLeft + 80;
        }
        $ul.css("marginLeft", marginLeft);
    })
    
    //标签删除图标点击事件绑定
    $("#filterEdit").on("click", ".tag-content .icon-delete", function(){
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
    $("#filterEdit").on("dblclick", ".label-bar", function(){
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
    $("#filterEdit").on("dblclick", ".entity-title", function(){ 
    	$(this).find(".edit-input").removeAttr("disabled");
    	$(this).find("select").removeAttr("disabled");
        $(this).addClass("edit");
    })
    
    //保存
    $("#filterEdit").on("click", ".icon-save", function() {        
        
    	var  $header = $(this).closest(".collapse-header");
    	if ($header.hasClass("entity-title")) {
    		//filterSave(this);
    		return;
    	}
    	
    	
    	
    	var labelBar = $(this).closest(".label-bar");
        
       if(labelBar.hasClass("commGroup")) {
        	commGroupSave(this);
        }else if(labelBar.hasClass("filterCriterion")) {
        	filterCriterionSave(this);
        }
    });
    
    //删除-全部
    $("#filterEdit").on("click", ".opera.confirm", function(e) {  
    	e.stopPropagation();    
        var entityTitle = $(".icon-trash.active").closest(".entity-title");
        var labelBar = $(".icon-trash-sm.active").closest(".label-bar");
        if(entityTitle.length > 0) {
        	var el = $(".icon-trash.active")[0];
        	entityDelete(el);
        	return;
        }
        
        
        var el = $(".icon-trash-sm.active")[0];        
        if(labelBar.hasClass("filterCriterion")) { 
        	//删除表达式
        	filterCriterionDelete(el);        	
        }else if(labelBar.hasClass("commGroup")) {
        	commGroupDelete(el);
        }
        
        
    })
    
    //实体选择点击事件绑定
    $("#filterEdit").on("click", ".entity_attr", function() {
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
    $("#filterEdit").on("change", "select.abc-attr", function(){
    	var _value = $(this).find("option:selected").val();
    	var $input = $(this).siblings(".edit-input");
    	var _options = $(this).find("option");
    	/*$input.val(_value);*/
    	
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