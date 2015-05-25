(function($) {

	var constant = {
		MENU_CSS : '.bind-menu-syndrome',
		PAGINATOR : '#paginator',
	};

	var viewModel = {
		syndromes : ko.observableArray([])
	};

	var format = {
		formatInit : function(syndromes) {
			$.each(syndromes, function(i, n) {
				n.deleteItem = function(n) {
					$.ajax({
						type : 'DELETE',
						url : $.SPM.context + '/syndrome/delete/' + n.id(),
						success : function() {
							viewModel.syndromes.remove(n);
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
		bindPaginator : function() {
			var options = {
				size:'normal',
				alignment:'center',
				bootstrapMajorVersion:3,
				currentPage: 3,
				numberOfPages: 10,
				totalPages:11,
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
	            pageUrl: function(type, page, current){

	                return "http://example.com/list/page/"+page;

	            }
			}
			
			$(constant.PAGINATOR).bootstrapPaginator(options);
		},
		initData : function() {
			$.ajax({
				type : 'POST',
				url : $.SPM.context + '/syndrome/list',
				dataType : 'JSON',
				success : function(data) {
					var temp = ko.mapping.fromJS(data);
					format.formatInit(temp());
					viewModel.syndromes(temp());
				}
			});
		}
	};

	var search = {
		init : function() {
			ko.applyBindings(viewModel);
			bindEvent.bindMenuCss();
			bindEvent.bindPaginator();
			bindEvent.initData();
		}
	};

	$(function() {
		search.init();
	});
})(jQuery);