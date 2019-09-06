<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="media/admin/plugins/ace/ace.js"></script>
<script type="text/javascript" src="media/admin/plugins/ace/ext-language_tools.js"></script>
  <!--  添加自定义的关键字 -->
    <!-- <script src="./myAce.js" type="text/javascript"></script> -->
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>

<head>
	 <style type="text/css">
    * {
        margin: 0;
        padding: 0;
    }

    @media screen and (min-width: 960px) {
        .editorSet {
            width: 100%;
        }
        .editorSet .editor {
            height: 300px;
            width: 100%;
            overflow-y: hidden;
            background-color: rgb(44, 52, 55);
            /* float: left; */
        }
    }
    
     @media screen and (min-width: 960px) {
        .editorSet {
            width: 100%;
        }
        .editorSet .editor1 {
            height: 100px;
            width: 100%;
            overflow-y: hidden;
            background-color: rgb(44, 52, 55);
            /* float: left; */
        }
    }


    .NightTheme .editorSet .editor {
        -webkit-box-shadow: inset 0 1px 3px rgb(22, 26, 27);
        -moz-box-shadow: inset 0 1px 3px rgb(22, 26, 27);
        box-shadow: inset 0 1px 3px rgb(22, 26, 27);
    }

    .label {
        background: rgba(230, 230, 230, 0.5);
        height: 20px;
        padding: 0 6px;
        line-height: 20px;
        z-index: 999;
        text-align: center;
        font-size: 12px;
        color: #BBB;
        border-radius: 3px;
    }

    .editor iframe {
        border: 0 !important;
        min-height: 100px;
        min-width: 100px;
        height: 100%;
        width: 100%;
    }

    .submit-btn {
        color: #fff;
        background-color: #67c23a;
        display: inline-block;
        line-height: 1;
        white-space: nowrap;
        cursor: pointer;
        border: 1px solid #67c23a;
        -webkit-appearance: none;
        text-align: center;
        box-sizing: border-box;
        outline: none;
        margin: 0;
        transition: .1s;
        -moz-user-select: none;
        -webkit-user-select: none;
        -ms-user-select: none;
        padding: 12px 20px;
        font-size: 14px;
        border-radius: 4px;
        margin-bottom: 2px;
    }

    .edit-area {
        height: 95%;
    }
    
    </style>
</head>

<div id="sqlModelSqlAdd" modelItemCode="${miCode }" sqlTxt="${miModelSql.sqlTxt }">
	
	<div class="page-header">
		<div class="header-title">
			<h1>sql语句</h1>
		</div>
		<div class="header-buttons">
			<a class="export btn-toggle" title="保存" id="btn-save" href="javascript:;">
				<i class="glyphicon glyphicon-check"></i>
			</a>
			 <a id="btn-operation" title="运行" href="javascript:;">
                   <i class="glyphicon glyphicon-play"></i>
             </a>
		</div>
	</div>
	<div class="page-body">
		<div class="row">
			<div class="col-lg-12">
				 <div class="NightTheme">
     
     
        <div id="main_container">
            <div id="core" class="core core_margin1">
                <div class="editorSet clearfix">
                    <div>
                      
                       <div class="editor">
                            <span class="label">sql语句</span>
                            <pre id="editor2" class="edit-area"></pre>
                        </div>
                      
                       <div class="editor1">
                            <span class="label">执行结果</span>
                            <pre id="editor1" class="edit-area"></pre>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
			</div>
		</div>
	</div>
</div>

   
    <script type="text/javascript">

    seajs.use(['dialog','utils', 'ajax', '$CPF'], function(Dialog, Utils, Ajax, $CPF){

    	var $page = $('#sqlModelSqlAdd');	
    		
	    (function() {
	         var editor1 = ace.edit("editor1", {
	            theme: "ace/theme/monokai",
	            mode: "ace/mode/mysql",
	            wrap: true,
	            autoScrollEditorIntoView: true,
	            enableBasicAutocompletion: true,
	            enableSnippets: true,
	            enableLiveAutocompletion: true
	        });
	        var editor2 = ace.edit("editor2", {
	            theme: "ace/theme/monokai",
	            mode: "ace/mode/mysql",
	            autoScrollEditorIntoView: true,
	            enableBasicAutocompletion: true,
	            enableSnippets: true,
	            enableLiveAutocompletion: true
	        });
	        
	    	/*  var editorValue='';
			editor1.setValue(editorValue); */
	
			//获取编辑内容
			/* editor1.getValue(); */

			editor2.setValue($page.attr("sqlTxt"))
			
			var submit = document.querySelector('#btn-save');
       		submit.addEventListener('click', function() {

       			var nameValue = editor1.getValue();
       			var sqlValue = editor2.getValue();
       			var modelItemCode = $page.attr("modelItemCode");

       		 Ajax.ajax('admin/expressionAndFilter/saveSqlModelSql',{
       			sqlTxt:sqlValue,
       			modelItemCode:modelItemCode
           		 }, function(data) {			
     			/* if (data.code == "400") {
     				 Dialog.notice(data.msg, "error");
     				 $CPF.closeLoading();
     			} else{
     				Dialog.notice(data.msg, "success");
     			
     			} */
     			$CPF.closeLoading();
     		}); 
       			
       			
            });
       		
       		// 测试运行
       		var submitOperation = document.querySelector('#btn-operation');
       		submitOperation.addEventListener('click', function() {

       			var sqlValue = editor2.getValue();
       			var modelItemCode = $page.attr("modelItemCode");

       		 Ajax.ajax('admin/expressionAndFilter/executeSql',{
       			sqlTxt:sqlValue
           		 }, function(data) {			
           			editor1.setValue(data.msg);
     			$CPF.closeLoading();
     		}); 
       			
       			
            });
       		
       		
	    })()


	   
    
    });
 </script>


