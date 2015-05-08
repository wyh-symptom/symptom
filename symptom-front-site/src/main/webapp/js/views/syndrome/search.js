(function($) {

	var constant = {
		MENU_CSS : '.bind-menu-syndrome-search',
		SUBMIT_BUTTON : '.bind-search-submit-button',
		SEARCH_FROM : '.bind-search-from',
		SEARCH_SYMPTOM_NAME : '.bind-search-symptom-name',
		SEARCH_SYMPTOM_NAME_SELECT : '.bind-search-symptom-name-select'
	};

	var viewModel = {
		keys : ko.observableArray([]),
		values : ko.observableArray([]),
		symptoms : ko.observableArray([]),
		selectSymptomNames : ko.observableArray([]),
		selectSymptomNameChange : function(item) {
			var symptomNameSelect = $(constant.SEARCH_SYMPTOM_NAME_SELECT).val();
			$.each(item.keys(), function(i, n) {
				if (n.symptomName == symptomNameSelect) {
					n.onClick();
				}
			});
		},
	};

	var common = {
		switchCss : function(array, item) {
			$.each(array, function(index, n) {
				n.checkedCss('list-group-item');
			});
			item.checkedCss('list-group-item list-group-item-info');
		}
	};
	
	var format = {
		formatKeyValue : function(data) {
			var formatValues = function(values) {
				$.each(values, function(i, n) {
					n.onClick = function() {
						common.switchCss(values, n);
						viewModel.symptoms.remove(n);
						viewModel.symptoms.push(n); 
					};
					n.onDelete = function() {
						if (confirm("你确认要删除该条件！")) {
							viewModel.symptoms.remove(n);
						}
					};
					n.checkedCss = ko.observable('list-group-item');
				});
			};
			
			$.each(data, function(i, n) {
				n.onClick = function() {
					formatValues(n.syndromes);
					viewModel.values(n.syndromes);
					common.switchCss(data, n);
				};
				n.checkedCss = ko.observable('list-group-item');
			});
			
		}
	};
	
	var bindEvent = {
		bindMenuCss : function() {
			$('.list-group-item-success')
					.removeClass('list-group-item-success');
			$(constant.MENU_CSS).addClass('list-group-item-success');
		},
		bindSubmit : function() {
			$(constant.SUBMIT_BUTTON).on('click', function() {
				if(!viewModel.symptoms().length) {
					alert('你没有选择任何条件！');
					return;
				}
				$(constant.SEARCH_FROM).submit();
			});
		},
		bindInputSymptomNameChange : function() {
			$(constant.SEARCH_SYMPTOM_NAME).on('change', function() {
				var temp = $(constant.SEARCH_SYMPTOM_NAME).val();
				viewModel.selectSymptomNames([]);
				$.each(viewModel.keys(), function(i, item) {
					if (item.symptomName.indexOf(temp) >= 0) {
						viewModel.selectSymptomNames.push(item);
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
					format.formatKeyValue(data);
					viewModel.keys(data);
				}
			});
		}
	};

	var search = {
		init : function() {
			ko.applyBindings(viewModel);
			bindEvent.bindMenuCss();
			bindEvent.bindSubmit();
			bindEvent.bindInputSymptomNameChange();
			bindEvent.initData();
		}
	};

	$(function() {
		search.init();
	});
})(jQuery);