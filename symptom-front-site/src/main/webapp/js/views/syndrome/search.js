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
		symptomNames : ko.observableArray([]),
		selectSymptomNames : ko.observableArray([]),
		selectSymptomNameChange : function(item) {
			var symptomNameSelect = $(constant.SEARCH_SYMPTOM_NAME_SELECT).val();
			$.each(item.keys(), function(i, n) {
				$.each(n.syndromeNames, function(j, sn) {
					if (sn.symptomName == symptomNameSelect) {
						n.onClick();
						sn.onClick();
					}
				});
			});
		},
	};

	var format = {
		formatKeyValue : function(data) {
			var formatValues = function(values) {
				$.each(values, function(i, n) {
					n.onClick = function() {
						
						if (n.isChecked()) {
							viewModel.symptoms.remove(n);
							n.isChecked(false);
							n.checkedCss("list-group-item");
						} else {
							viewModel.symptoms.push(n); 
							n.isChecked(true);
				            n.checkedCss('list-group-item list-group-item-info');
						}
						
						viewModel.symptomNames([]);
						$.each(viewModel.symptoms(), function(i, syn) {
							if (viewModel.symptomNames.indexOf(syn.symptomName+"") == -1) {
								viewModel.symptomNames.push(syn.symptomName+"");
							}
						});
					};
				});
			};
			
			$.each(data, function(i, n) {
				$.each(n.syndromeNames, function(j, item) {
					$.each(item.syndromes, function(k, s) {
						s.checkedCss = ko.observable('list-group-item');
						s.isChecked = ko.observable(false);
					});
				});
				
			});
			
			$.each(data, function(i, n) {
				
				if (i == 0) {
					n.treegrid = i + 1;
				} else {
					n.treegrid = data[i -1].length + i + 1;
				}
				n.treegrCss = ko.observable('treegrid-' + n.treegrid + ' treegrid-collapsed');
				n.treeIconCss = ko.observable('treegrid-expander glyphicon glyphicon-plus');
				n.isVisible = ko.observable(false);
				n.onClick = function() {
					var isVisible = n.isVisible();
					$.each(data, function(k, sn) {
						sn.isVisible(false);
						sn.treegrCss('treegrid-' + n.treegrid + ' treegrid-collapsed');
						sn.treeIconCss('treegrid-expander glyphicon glyphicon-plus');
					});
					if (isVisible) {
						n.isVisible(false);
						n.treegrCss('treegrid-' + n.treegrid + ' treegrid-collapsed');
						n.treeIconCss('treegrid-expander glyphicon glyphicon-plus');
					} else {
						n.isVisible(true);
						n.treegrCss('treegrid-' + n.treegrid + ' treegrid-expanded');
						n.treeIconCss('treegrid-expander glyphicon glyphicon-minus');
					}
				}
				
				$.each(n.syndromeNames, function(j, item) {
					item.treegrid = n.treegrid + j + 1;
					item.treegrCss = ko.observable('treegrid-' + item.treegrid+' treegrid-parent-' + n.treegrid);
					
					item.onClick = function() {
						formatValues(item.syndromes);
						viewModel.values(item.syndromes);
					};
				});
				
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
					$.each(item.syndromeNames, function(j, n) {
						
						if (n.symptomName.indexOf(temp) >= 0) {
							viewModel.selectSymptomNames.push(n);
						}
					});
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