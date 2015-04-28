(function($) {

	var constant = {
			MENU_CSS : '.bind-menu-syndrome-element'
	};

	var viewModel = {
		syndromeElements : ko.observableArray([])
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
		initData : function() {
			$.ajax({
				type : 'POST',
				url : $.SPM.context + '/syndrome/element/list',
				dataType : 'JSON',
				success : function(data) {
					var temp = ko.mapping.fromJS(data);
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
			bindEvent.initData();
		}
	};

	$(function() {
		search.init();
	});
})(jQuery);