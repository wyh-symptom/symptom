<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查询</title>
<%@include file="../common/resource.jsp"%>
<script src="${SPM_CONTEXT}/js/views/jmgraph/jmgraph.debug.js"></script>
<script type="text/javascript" src="${SPM_CONTEXT}/js/views/jmgraph/images.js" ></script>
<style>
html,body,div{
	width:100%;
	height:100%;
	margin:0;
	padding:0;
}

table td.first {
	width:150px;
	vertical-align:top;
	background-color: #ccc;
}
#components {
	list-style: none;
	padding:0;
	margin: 0;	
}
#components li img {
	max-height:22px;
}
#container {
/*background-color: #000;*/
width:100%;
height:100%;
display:block;
overflow: auto;
}
#container {	
	margin:0 auto;
	border:1px solid #000;
	background-color: #fff;
	background: url('images/grid.png');
}

#expimagearea {
	background-color: #303030;
	color:blue;
	border:1px solid #ccc;
	width:600px;
	height:400px;
	overflow: auto;
	position:absolute;
	left:25%;
	top:150px;
}

div.editor-menu {
			position:absolute;
			padding: 4px;
			background-color: #fff;
			width:auto;
			height:auto;
			display: inline;
			min-width: 80px;
			border: solid 1px #bbd6ec;
		    border-radius: 3px;
		    -webkit-border-radius: 3px;
		    box-shadow: inset 0 0 2px #fff;
		     -webkit-box-shadow: inset 0 0 2px #fff;
		    -moz-box-shadow: inset 0 0 2px #fff;
		}
		div.editor-menu ul {
			list-style: none;	
			padding:0;
			margin:0;		
		}
		div.editor-menu ul li {
			cursor:pointer;	
			color:blue;	
			padding: 2px;	
		}
		div.editor-menu ul li:hover {
			color:green;
			background-color: #ccc;
		}
</style>
</head>
<body>
	<%@ include file="../common/header.jsp"%>
	<div class="container-fluid">
		<div class="row">
			<%@ include file="../common/menu.jsp"%>
			<div class="col-sm-10">

		<table>
			<tr>
				<td>
					
					<button id="btnZoomOut" >缩小</button>
					<button id="btnZoomIn" >放大</button>
					<button id="btnExpImage" >导出为图片</button>
				</td>
				<td></td>
			</tr>
			<tr>
				<td>
			<div id="container">				
			</div>
				</td>
				<td style="vertical-align:top;">
					<label for="txtname">名称:</label><input id="txtname" name="txtname" type="text" />
					<br />
					<label for="txtdes">说明:</label><input id="txtdes" name="txtdes" type="text" />					
				</td>
			</tr>
		</table>
		<div id="json"></div>
		<div id="expimagearea" style="display:none;">
			<div style="position:relative; height:60px;">
			<a href="#" style="position:fixed;color:red;" onclick="javascript:document.getElementById('expimagearea').style.display='none';">关闭</a>
<span style="color:red;float:right;">请右健另存为: </span>
</div>
<img id="expimage" />
		</div>
	



		<script type="text/javascript">

		
			
				//初始化编辑器
				var editor = new jmEditor({
					container:'container',
					width:800,
					height:600,
					enabled : true,//可以编辑
					connectable:true,//可以连线
					movable:true,//可以移动元素
					resizable:true//可以改变元素大小
				});

				//if(editor.graph.isSupportedBrowser() == false) {
				//	alert('不支持的浏览器');
				//	return;
				//} 
				
				//生成渐变背景
				var bg = editor.graph.createLinearGradient(0,0,0,'100%');
				bg.addColorStop(0,'#43F589');
				bg.addColorStop(0.5,'#0FA248');
				bg.addColorStop(1,'#43F589');
				var style = {
					stroke:'#000',
					fill : bg,//指定默认背景为渐变
					lineWidth:1,
					close:true,
					lineJoin:'round',
					radius:6,
					resizable : true,//默认元素可以放大缩小
					zIndex:1000
				};
				//设定连线样式
				style.connectLine = {
					stroke:'black',
					normalStroke:'black',
					overStroke:'red',
					selectedStroke:'red',
					lineWidth:1,
					zIndex:100,
					//连线上文件的颜色
					fontStyle : {						
						fill:'green',
						textAlign:'center'
					}
				};
				//设定边框样式，用于放大缩小拖放框
				style.borderStyle = {
					stroke:'#3BDB05',
					//fill:'transparent',
					rectStroke:'#3BDB05',//放大缩小小方块颜色
					close:true
				};
				//指定阴影
				style.shadow = editor.graph.createShadow(0,0,10,'#000');
				
				
				//字体样式
				style.fontStyle = {
					fill:'blue',
					textAlign:'center',
					textBaseline:'middle',//垂直居中
					font:'14px 微软雅黑'
				};

				//状态小图标样式
				style.overlay = {
					margin:{
						left:10,
						top:10
					}
				};

				//指定进程图为一个圆
				style.shape = 'arc';
				editor.regStyle('process',style);
				//复制一个进程选择样式
				var processselectedstyle = jmUtils.clone(style);
				//注册选择状态
				processselectedstyle.fill = 'red';
				editor.regStyle('process_selected',processselectedstyle);
				
				//指定条件图形为一个棱形
				style = jmUtils.clone(style);
				style.shape = 'prismatic';
				editor.regStyle('switch',style);
				//没有改变选中状态样式
				editor.regStyle('switch_selected',style);

				//指定fork元素为方块。背景为:#000
				style = jmUtils.clone(style);
				style.fill = '#000';
				style.resizable = false;//锁定其不让改变大小
				style.shape = 'rect';
				editor.regStyle('fork',style);
				editor.regStyle('fork_selected',style);
				//指定join为一个椭圆,样式续承自fork
				style = jmUtils.clone(style);
				style.shape = 'arc';
				editor.regStyle('join',style);
				editor.regStyle('join_selected',style);

				//指定子流程为方块。背景为90EE90,可以改变大小
				style = jmUtils.clone(style);
				style.fill = '#90EE90';
				//style.stroke = 'rgb(248,195,60)';
				style.resizable = true;
				style.shape = 'rect';
				editor.regStyle('subflow',style);
				editor.regStyle('subflow_selected',style);

				//上传文件样式为一张图片
		        style = jmUtils.clone(style);
		        style.resizable = true;
		        style.shape = 'img';
		        style.width = 120;
		        style.height = 80;
		        style.image = images.upload;
		        editor.regStyle('upload', style);
		        editor.regStyle('upload-selected', style);
		         //下载文件
		        style = jmUtils.clone(style);
		        style.image = images.download;
		        editor.regStyle('download', style);
		        editor.regStyle('download-selected', style);

		        //开始样式,不可改变大小，背景透明
				var startstyle = jmUtils.clone(style);
				//startstyle.fill = 'transparent';
				startstyle.resizable = false;
				startstyle.shape = 'arc';
				editor.regStyle('start',startstyle);
				editor.regStyle('start_selected',startstyle);

				//结束样式
				var endstyle = jmUtils.clone(startstyle);
				endstyle.close = true;
				endstyle.lineWidth = 1;
				endstyle.connectable = false;
				endstyle.fill = '#64D381';
				endstyle.shape = 'harc';
				endstyle.maxRadius=30;
				endstyle.minRadius=20;
				editor.regStyle('end',endstyle);
				editor.regStyle('end_selected',endstyle);

				


				//编辑器组件
				/*
				var components = document.getElementById('components');

				var startimg = editor.regComponent(images.start,{width:60,height:60,style:'start',value:'开始'});
				var li = document.createElement('li');
				li.appendChild(startimg);
				components.appendChild(li);

				var processimg = editor.regComponent(images.process,{width:100,height:80,style:'process',value:'process'});
				li = document.createElement('li');
				li.appendChild(processimg);
				components.appendChild(li);

				var subflowimg = editor.regComponent(images.subflow,{width:100,height:80,style:'subflow'});
				li = document.createElement('li');
				li.appendChild(subflowimg);
				components.appendChild(li);

				var forkimg = editor.regComponent(images.fork,{width:100,height:40,style:'fork'});
				li = document.createElement('li');
				li.appendChild(forkimg);
				components.appendChild(li);

				var joinimg = editor.regComponent(images.join,{width:100,height:50,style:'join'});
				li = document.createElement('li');
				li.appendChild(joinimg);
				components.appendChild(li);

				var switchimg = editor.regComponent(images['switch'],{width:100,height:50,style:'switch'});
				li = document.createElement('li');
				li.appendChild(switchimg);
				components.appendChild(li);

				var upimg = editor.regComponent(images['upload'],{width:100,height:50,style:'upload'});
				li = document.createElement('li');
				li.appendChild(upimg);
				components.appendChild(li);

				var downimg = editor.regComponent(images['download'],{width:100,height:50,style:'download'});
				li = document.createElement('li');
				li.appendChild(downimg);
				components.appendChild(li);

				var endimg = editor.regComponent(images.end,{width:60,height:60,style:'end',value:'结束'});
				li = document.createElement('li');
				li.appendChild(endimg);
				components.appendChild(li);
*/
				

				//工具按钮事件
				var btn = document.getElementById('btnZoomOut');
				btn.onclick = function() {
					editor.execute('zoomOut');
				}
				btn = document.getElementById('btnZoomIn');
				btn.onclick = function() {
					editor.execute('zoomIn');
				}
				

				btn = document.getElementById('btnExpImage');
				btn.onclick = function() {
					document.getElementById('expimage').src = editor.toImage();
					document.getElementById('expimagearea').style.display = 'inline';
					document.getElementById('json').innerHTML = editor.toJSON().toString();
				}

				

				var jsonData = {"relate":{"AB":1,"AC":1,"CD":1,"DF":1,"EH":1},"element":["A","B","C","E","F","D","H"]};
				var elementArr = jsonData.element;
				var relate = jsonData.relate;
				var x = 0;y = 0;width = 60,height = 60;
				for(var i = 0; i < elementArr.length; i++) {
					cell = editor.addCell({position :{x:x,y:y},width:width,height:height,style:'process'});
					cell.value(elementArr[i]);
					x += 100;
					y+= 100;
				}
				var cells = editor.getCells();
				for (var i = 0; i < cells.length; i++) {
					for (var j = 0; j < cells.length; j++) {
						if (cells[i].value() == cells[j].value()) {
							continue;
						}
						var relateShip = relate[cells[i].value() + cells[j].value()];
						if (relateShip == 1) {
							cells[i].connect(cells[j]);
						}
					}
				}
				//var cell3 = editor.addCell({position:{x:600,y:250},width:100,height:100,style:'subflow'});	
				//设置图标标识			
				//cell3.setOverlay('images/running.gif',30,30,'运行中...');

				editor.graph.refresh();

				//把画图信息转为json对象便于保存
				//var json = editor.toJSON();
				//从json数据恢复原画
				//editor.fromJSON('{"cells":[{"outs":[{"to":6,"from":1}],"id":1,"value":"test cell","position":{"x":135.65216064453125,"y":155.6521759033203},"width":100,"height":60,"style":"process"},{"outs":[{"to":5,"from":2},{"to":1,"from":2}],"id":2,"position":{"x":406.95654296875,"y":73.04345703125},"width":100,"height":100,"style":"subflow"},{"outs":[{"to":2,"from":3},{"to":6,"from":3}],"id":3,"position":{"x":502.60870361328125,"y":270},"width":100,"height":100,"style":"subflow"},{"outs":[{"to":3,"from":4}],"id":4,"position":{"x":269.0869903564453,"y":342.9739246368408},"width":60,"height":60,"style":"end"},{"outs":[{"to":1,"from":5}],"id":5,"position":{"x":156.04348754882812,"y":37.75652503967285},"width":60,"height":60,"style":"start"},{"outs":[{"to":4,"from":6}],"id":6,"position":{"x":123,"y":313.4087085723877},"width":60,"height":60,"style":"end"}]}');

				//元素上的右健菜单
				editor.graph.bind('mouseup',function(evt) {
					var menus = editor.menus();
					//如果右击在元素上
					if(evt.button == 2 && evt.target && evt.target.findParent) {
						////右击在元素上或其子元素上,向上查询其父容器jmcell
						var cell = evt.target.type == 'jmCell' ?evt.target: evt.target.findParent('jmCell');
						if(cell) {							
							menus.add('属性',function() {
								
							}).show();
							return false;
						}	
						//击中空白处
						else {
							menus.add('属性',function() {
								
							}).show();
							return false;//阻断冒泡
						}					
					}
					menus.hide();				
				});

				//监听选择事件
				editor.on('select',function(cell,selected) {
					if(cell.is('jmCell')) {
						//当元素被选择后，更改其样式
						var stylename = cell.styleName;
						var _index = stylename.indexOf('_');
						if(_index > 0) {
							stylename = stylename.substring(0,_index);
						}
						cell.setStyle(selected?stylename + '_selected':stylename);

						if(selected) {
							var cells = editor.getSelectedCells();//获取所有选择的元素
							//如果只选中一个
							if(cells.length == 1) {
								document.getElementById('txtname').value = cell.value();
							}
						}						
					}	
					else if(cell.is(jmConnectLine)) {
						if(selected) {
							var cns = editor.getSelectedConnects();//获取所有选择的连线
							//如果只选中一个
							if(cns.length == 1) {
								document.getElementById('txtname').value = cell.value();
							}
						}
					}				
				});


		        //检查元素是否可以添加
		        editor.validCell = function(cell) {		           
		            var cells = this.getCells();
		            for(var i=0;i< cells.length;i++) {
		                var c = cells[i];		                
		                if(cell.styleName == 'start' && c.styleName == 'start') {
		                    alert('开始元素有且只能有一个');
		                    return false;
		                }
		                else if(cell.styleName == 'end' && c.styleName == 'end') {
		                    alert('结束元素有且只能有一个');
		                    return false;
		                }
		            }
		            return true;
		        };
		        //检查元素是否可以相连
		        editor.validConnect = function(from,to) {
		            if(from.styleName == 'start') {
		                if(from.connects.count() > 0) {
		                    alert('开始元素有且只能有一个出口');
		                    return false;
		                }
		            } 
		            else if(from.styleName == 'join') {
		                //如果fork已存在出口
		                if(from.connects.count(function(con) {
		                        return con.from = from;
		                    }) > 0) {
		                    alert('join元素有且只能有一个出口');
		                    return false;
		                }
		            }              
		            return true;
		        };

		        //绑定名称改变事件，将值绑定到被选 中的元素上
				jmUtils.bindEvent(document.getElementById('txtname'),'change',function() {
					var cells = editor.getSelectedCells();//获取所有选择的元素
					for(var i in cells) {
						cells[i].value(this.value);
					}
					var cns = editor.getSelectedConnects();
					for(var i in cns) {
						cns[i].value(this.value);
					}
					editor.graph.refresh();//刷新画布
				});
			
			
		</script>
				
			</div>
		</div>
	</div>
	
	

</body>
</html>