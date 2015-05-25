package com.chenfeng.symptom.domain.common.pagehelper;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SqlParser {
	private static final List<SelectItem> COUNT_ITEM;
	private static final Alias TABLE_ALIAS;

	static {
		COUNT_ITEM = new ArrayList<SelectItem>();
		COUNT_ITEM.add(new SelectExpressionItem(new Column("count(*)")));

		TABLE_ALIAS = new Alias("table_count");
		TABLE_ALIAS.setUseAs(false);
	}

	private Map<String, String> CACHE = new ConcurrentHashMap<String, String>();

	public void isSupportedSql(String sql) {
		if (sql.trim().toUpperCase().endsWith("FOR UPDATE")) {
			throw new RuntimeException("not support for update sql");
		}
	}

	public String getSmartCountSql(String sql) {

		isSupportedSql(sql);
		if (CACHE.get(sql) != null) {
			return CACHE.get(sql);
		}

		Statement stmt = null;
		try {
			stmt = CCJSqlParserUtil.parse(sql);
		} catch (JSQLParserException e) {

			String countSql = getSimpleCountSql(sql);
			CACHE.put(sql, countSql);
			return countSql;
		}
		Select select = (Select) stmt;
		SelectBody selectBody = select.getSelectBody();

		processSelectBody(selectBody);

		processWithItemsList(select.getWithItemsList());

		sqlToCount(select);
		String result = select.toString();
		CACHE.put(sql, result);
		return result;
	}

	public String getSimpleCountSql(final String sql) {
		isSupportedSql(sql);
		StringBuilder stringBuilder = new StringBuilder(sql.length() + 40);
		stringBuilder.append("select count(*) from (");
		stringBuilder.append(sql);
		stringBuilder.append(") tmp_count");
		return stringBuilder.toString();
	}

	public void sqlToCount(Select select) {
		SelectBody selectBody = select.getSelectBody();

		if (selectBody instanceof PlainSelect
				&& !selectItemsHashParameters(((PlainSelect) selectBody)
						.getSelectItems())
				&& ((PlainSelect) selectBody).getGroupByColumnReferences() == null) {
			((PlainSelect) selectBody).setSelectItems(COUNT_ITEM);
		} else {
			PlainSelect plainSelect = new PlainSelect();
			SubSelect subSelect = new SubSelect();
			subSelect.setSelectBody(selectBody);
			subSelect.setAlias(TABLE_ALIAS);
			plainSelect.setFromItem(subSelect);
			plainSelect.setSelectItems(COUNT_ITEM);
			select.setSelectBody(plainSelect);
		}
	}

	public void processSelectBody(SelectBody selectBody) {
		if (selectBody instanceof PlainSelect) {
			processPlainSelect((PlainSelect) selectBody);
		} else if (selectBody instanceof WithItem) {
			WithItem withItem = (WithItem) selectBody;
			if (withItem.getSelectBody() != null) {
				processSelectBody(withItem.getSelectBody());
			}
		} else {
			SetOperationList operationList = (SetOperationList) selectBody;
			if (operationList.getPlainSelects() != null
					&& operationList.getPlainSelects().size() > 0) {
				List<PlainSelect> plainSelects = operationList
						.getPlainSelects();
				for (PlainSelect plainSelect : plainSelects) {
					processPlainSelect(plainSelect);
				}
			}
			if (!orderByHashParameters(operationList.getOrderByElements())) {
				operationList.setOrderByElements(null);
			}
		}
	}

	public void processPlainSelect(PlainSelect plainSelect) {
		if (!orderByHashParameters(plainSelect.getOrderByElements())) {
			plainSelect.setOrderByElements(null);
		}
		if (plainSelect.getFromItem() != null) {
			processFromItem(plainSelect.getFromItem());
		}
		if (plainSelect.getJoins() != null && plainSelect.getJoins().size() > 0) {
			List<Join> joins = plainSelect.getJoins();
			for (Join join : joins) {
				if (join.getRightItem() != null) {
					processFromItem(join.getRightItem());
				}
			}
		}
	}

	public void processWithItemsList(List<WithItem> withItemsList) {
		if (withItemsList != null && withItemsList.size() > 0) {
			for (WithItem item : withItemsList) {
				processSelectBody(item.getSelectBody());
			}
		}
	}

	public void processFromItem(FromItem fromItem) {
		if (fromItem instanceof SubJoin) {
			SubJoin subJoin = (SubJoin) fromItem;
			if (subJoin.getJoin() != null) {
				if (subJoin.getJoin().getRightItem() != null) {
					processFromItem(subJoin.getJoin().getRightItem());
				}
			}
			if (subJoin.getLeft() != null) {
				processFromItem(subJoin.getLeft());
			}
		} else if (fromItem instanceof SubSelect) {
			SubSelect subSelect = (SubSelect) fromItem;
			if (subSelect.getSelectBody() != null) {
				processSelectBody(subSelect.getSelectBody());
			}
		} else if (fromItem instanceof ValuesList) {

		} else if (fromItem instanceof LateralSubSelect) {
			LateralSubSelect lateralSubSelect = (LateralSubSelect) fromItem;
			if (lateralSubSelect.getSubSelect() != null) {
				SubSelect subSelect = lateralSubSelect.getSubSelect();
				if (subSelect.getSelectBody() != null) {
					processSelectBody(subSelect.getSelectBody());
				}
			}
		}

	}

	public boolean orderByHashParameters(List<OrderByElement> orderByElements) {
		if (orderByElements == null) {
			return false;
		}
		for (OrderByElement orderByElement : orderByElements) {
			if (orderByElement.toString().contains("?")) {
				return true;
			}
		}
		return false;
	}

	public boolean selectItemsHashParameters(List<SelectItem> selectItems) {
		if (selectItems == null) {
			return false;
		}
		for (SelectItem selectItem : selectItems) {
			if (selectItem.toString().contains("?")) {
				return true;
			}
		}
		return false;
	}
}
