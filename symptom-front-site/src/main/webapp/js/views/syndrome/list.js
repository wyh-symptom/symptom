(function($) {

	var constant = {
			MENU_CSS : '.bind-menu-syndrome'
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
			bindEvent.initData();
		}
	};

	$(function() {
		search.init();
	});
})(jQuery);