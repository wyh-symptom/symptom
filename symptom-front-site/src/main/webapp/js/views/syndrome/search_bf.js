(function($) {

	var constant = {
		MENU_CSS : '.bind-menu-syndrome-search',
		ADD_BUTTON : '.bind-search-add-button',
		REMOVE_BUTTON : '.bind-search-remove-button',
		SYMPTOM_NAME_SELECT : '.bind-search-symptom-name-select'
	};

	var viewModel = {
		keys : ko.observableArray([]),
		itemCount : ko.observable(1)
	};

	var bindEvent = {
		bindAddElement : function() {
			$(constant.ADD_BUTTON).on('click', function() {
				$(this).closest('div .control-group').append(
						$(this).closest('div .row').clone(true));

				viewModel.itemCount(viewModel.itemCount() + 1);
				if (viewModel.itemCount() > 1) {
					$(constant.REMOVE_BUTTON).show();
				} else {
					$(constant.REMOVE_BUTTON).hide();
				}
			});
		},
		bindRemoveElement : function() {
			$(constant.REMOVE_BUTTON).on('click', function() {
				$(this).closest('div .row').remove();
				viewModel.itemCount(viewModel.itemCount() - 1);
				if (viewModel.itemCount() > 1) {
					$(constant.REMOVE_BUTTON).show();
				} else {
					$(constant.REMOVE_BUTTON).hide();
				}
			});
		},
		bindMenuCss : function() {
			$('.list-group-item-info')
					.removeClass('list-group-item-info');
			$(constant.MENU_CSS).addClass('list-group-item-info');
		},
		bindKeyChange : function() {
			$(constant.SYMPTOM_NAME_SELECT).on('change', function() {
				var $self = $(this);
				var sn = $self.val();
				$.each(viewModel.keys(), function(i, item) {
					if (item.symptomName === sn) {
						var $desc = $self.closest('div .form-group').find('select[name="description"]');
						$desc.empty();
						$desc.append(jQuery("<option></option>").attr({'value':"-1"}).text("请选择"));
						$.each(item.syndromes , function(i, n) {
							$desc.append(jQuery("<option></option>").attr({'value':n.syndromeElementStart + "__" + n.syndromeElementEnd}).text(n.description));
						});
					}
				});
			});
		},
		initData : function() {
			$.ajax({
				type : 'POST',
				url : $.SPM.context + '/syndrome/init',
				dataType : 'JSON',
				success : function(data) {
					viewModel.keys(data);
				}
			});
		}
	};

	var search = {
		init : function() {
			ko.applyBindings(viewModel);
			bindEvent.bindMenuCss();
			bindEvent.bindAddElement();
			bindEvent.bindRemoveElement();
			bindEvent.bindKeyChange();
			bindEvent.initData();
		}
	};

	$(function() {
		search.init();
	});
})(jQuery);