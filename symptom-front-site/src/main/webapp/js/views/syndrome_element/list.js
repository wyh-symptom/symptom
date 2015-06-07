(function($) {

	var constant = {
		MENU_CSS : '.bind-menu-syndrome-element',
		PAGINATOR : '#paginator'
	};

	var viewModel = {
		syndromeElements : ko.observableArray([]),
		syndromeElementStart: ko.observable(''),
		syndromeElementEnd: ko.observable(''),
		search : function() {
			bindEvent.getData(0, this.syndromeElementStart(), this.syndromeElementEnd());
		}
	};

	var format = {
		formatInit : function(syndromeElements) {
			$.each(syndromeElements, function(i, n) {
				n.deleteItem = function(n) {
					$.ajax({
						type : 'DELETE',
						url : $.SPM.context + '/syndrome/element/delete/' + n.id(),
						success : function() {
							viewModel.syndromeElements.remove(n);
						}
					});
				};
			});
		}
	};
	
	var bindEvent = {
		bindMenuCss : function() {
			$(".list-group-item-success")
					.removeClass("list-group-item-success");
			$(constant.MENU_CSS).addClass("list-group-item-success");
		},
		bindPaginator : function(currentPage, totalPages) {
			var options = {
				size:'normal',
				currentPage: currentPage,
				totalPages: totalPages,
				numberOfPages: 10,
				bootstrapMajorVersion:3,
				alignment:'center',
				tooltipTitles: function (type, page, current) {
	                switch (type) {
	                case "first":
	                    return "首页";
	                case "prev":
	                    return "上一页";
	                case "next":
	                    return "下一页";
	                case "last":
	                    return "尾页";
	                case "page":
	                    return "第" + page + "页";
	                }
	            },
	            onPageClicked: function(e,originalEvent,type,page){
	            	bindEvent.getData(page - 1, viewModel.syndromeElementStart(), viewModel.syndromeElementEnd());
	            }
			}
			
			$(constant.PAGINATOR).bootstrapPaginator(options);
		},
		getData : function(page, syndromeElementStart, syndromeElementEnd) {
			$.ajax({
				type : 'POST',
				url : $.SPM.context + '/syndrome/element/list',
				dataType : 'JSON',
				data : {page: page, syndromeElementStart: syndromeElementStart, syndromeElementEnd: syndromeElementEnd},
				success : function(data) {
					var temp = ko.mapping.fromJS(data.content);
					if(data.totalPages > 0)
						bindEvent.bindPaginator(data.currentPage + 1, data.totalPages);
					 else {
							$(constant.PAGINATOR).empty();
					}
					format.formatInit(temp());
					viewModel.syndromeElements(temp());
				}
			});
		}
	};

	var search = {
		init : function() {
			ko.applyBindings(viewModel);
			bindEvent.bindMenuCss();
			bindEvent.getData(0, '', '');
		}
	};

	$(function() {
		search.init();
	});
})(jQuery);