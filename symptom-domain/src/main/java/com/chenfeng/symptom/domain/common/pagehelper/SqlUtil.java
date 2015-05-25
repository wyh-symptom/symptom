package com.chenfeng.symptom.domain.common.pagehelper;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.builder.annotation.ProviderSqlSource;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.MixedSqlNode;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Method;
import java.util.*;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class SqlUtil {
	private static final ThreadLocal<CountablePage> LOCAL_PAGE = new ThreadLocal<CountablePage>();

	private boolean offsetAsPageNum = false;

	private boolean rowBoundsWithCount = false;

	private boolean pageSizeZero = false;

	private boolean reasonable = false;

	private static Map<String, String> PARAMS = new HashMap<String, String>(5);

	private static Boolean hasRequest;
	private static Class<?> requestClass;
	private static Method getParameterMap;

	static {
		try {
			requestClass = Class.forName("javax.servlet.ServletRequest");
			getParameterMap = requestClass.getMethod("getParameterMap",
					new Class[] {});
			hasRequest = true;
		} catch (Exception e) {
			hasRequest = false;
		}
	}

	private static final List<ResultMapping> EMPTY_RESULTMAPPING = new ArrayList<ResultMapping>(
			0);

	private static final String SUFFIX_PAGE = "_PageHelper";

	private static final String SUFFIX_COUNT = SUFFIX_PAGE + "_Count";

	private static final String PAGEPARAMETER_FIRST = "First" + SUFFIX_PAGE;

	private static final String PAGEPARAMETER_SECOND = "Second" + SUFFIX_PAGE;

	private static final String PROVIDER_OBJECT = "_provider_object";

	private static final String ORIGINAL_PARAMETER_OBJECT = "_ORIGINAL_PARAMETER_OBJECT";

	private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
	private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();

	private static MetaObject forObject(Object object) {
		return MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY,
				DEFAULT_OBJECT_WRAPPER_FACTORY);
	}

	private static final SqlParser sqlParser = new SqlParser();

	private Parser parser;

	private Dialect dialect;

	public enum Dialect {
		mysql, mariadb, sqlite, oracle, hsqldb, postgresql, sqlserver, db2
	}

	public static void setLocalPage(CountablePage page) {
		LOCAL_PAGE.set(page);
	}

	private static CountablePage getLocalPage() {
		return LOCAL_PAGE.get();
	}

	private static void clearLocalPage() {
		LOCAL_PAGE.remove();
	}

	public CountablePage getPage(Object params) {
		CountablePage page = getLocalPage();
		if (page == null) {
			if (params instanceof RowBounds) {
				RowBounds rowBounds = (RowBounds) params;
				if (offsetAsPageNum) {
					page = new CountablePage(rowBounds.getOffset(),
							rowBounds.getLimit(), rowBoundsWithCount);
				} else {
					page = new CountablePage(rowBounds, rowBoundsWithCount);
				}
			} else {
				page = getPageFromObject(params);
			}
			setLocalPage(page);
		}
		if (page.getReasonable() == null) {
			page.setReasonable(reasonable);
		}
		if (page.getPageSizeZero() == null) {
			page.setPageSizeZero(pageSizeZero);
		}
		return page;
	}

	public static CountablePage getPageFromObject(Object params) {
		int pageNum;
		int pageSize;
		MetaObject paramsObject = null;
		if (params == null) {
			throw new NullPointerException(
					"page query params can not be null !");
		}
		if (hasRequest && requestClass.isAssignableFrom(params.getClass())) {
			try {
				paramsObject = forObject(getParameterMap.invoke(params,
						new Object[] {}));
			} catch (Exception e) {

			}
		} else {
			paramsObject = forObject(params);
		}
		if (paramsObject == null) {
			throw new NullPointerException("fail to deal with params!");
		}
		try {
			pageNum = Integer.parseInt(String.valueOf(getParamValue(
					paramsObject, "pageNum", true)));
			pageSize = Integer.parseInt(String.valueOf(getParamValue(
					paramsObject, "pageSize", true)));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					"pageNum and pageSize must be number!");
		}
		Object _count = getParamValue(paramsObject, "count", false);
		boolean count = true;
		if (_count != null) {
			count = Boolean.valueOf(String.valueOf(_count));
		}
		CountablePage page = new CountablePage(pageNum, pageSize, count);
		Object reasonable = getParamValue(paramsObject, "reasonable", false);
		if (reasonable != null) {
			page.setReasonable(Boolean.valueOf(String.valueOf(reasonable)));
		}
		Object pageSizeZero = getParamValue(paramsObject, "pageSizeZero", false);
		if (pageSizeZero != null) {
			page.setPageSizeZero(Boolean.valueOf(String.valueOf(pageSizeZero)));
		}
		return page;
	}

	public static Object getParamValue(MetaObject paramsObject,
			String paramName, boolean required) {
		Object value = null;
		if (paramsObject.hasGetter(PARAMS.get(paramName))) {
			value = paramsObject.getValue(PARAMS.get(paramName));
		}
		if (required && value == null) {
			throw new RuntimeException("lack param:" + PARAMS.get(paramName));
		}
		return value;
	}

	public Object processPage(Invocation invocation) throws Throwable {
		try {
			final Object[] args = invocation.getArgs();
			MappedStatement ms = (MappedStatement) args[0];
			RowBounds rowBounds = (RowBounds) args[2];
			Object result = null;
			if (SqlUtil.getLocalPage() == null
					&& rowBounds == RowBounds.DEFAULT) {
				return invocation.proceed();
			} else if (ms.getId().contains("findEndlessPage")) {
				result = _processEndLessPage(invocation);
			} else if (ms.getId().contains("findPage")) {
				result = _processPage(invocation);
			} else {
				throw new UnsupportedOperationException();
			}
			clearLocalPage();
			return result;
		} finally {
			clearLocalPage();
		}
	}

	private RowBounds rowPlusOne(RowBounds rowBounds) {
		return new RowBounds(rowBounds.getOffset(), rowBounds.getLimit() + 1);
	}

	private Object _processEndLessPage(Invocation invocation) throws Throwable {
		final Object[] args = invocation.getArgs();
		MappedStatement ms = (MappedStatement) args[0];
		RowBounds rowBounds = (RowBounds) args[2];

		args[2] = RowBounds.DEFAULT;

		EndlessPage page = new EndlessPage();
		RowBounds newRowBounds = this.rowPlusOne(rowBounds);

		SqlSource sqlSource = ((MappedStatement) args[0]).getSqlSource();

		if (newRowBounds.getLimit() > 0 && rowBounds != RowBounds.DEFAULT) {

			processPageMappedStatement(ms, sqlSource, newRowBounds, args);

			List result = (List) invocation.proceed();

			if (result == null) {
				result = new ArrayList();
			}

			if (result.size() == newRowBounds.getLimit()) {
				result.remove(result.size() - 1);
				page.setLastPage(false);

			} else {
				page.setLastPage(true);
			}
			page.addAll(result);
		}

		return page;

	}

	private Object _processPage(Invocation invocation) throws Throwable {
		final Object[] args = invocation.getArgs();
		RowBounds rowBounds = (RowBounds) args[2];
		if (SqlUtil.getLocalPage() == null && rowBounds == RowBounds.DEFAULT) {
			return invocation.proceed();
		} else {
			MappedStatement ms = (MappedStatement) args[0];
			args[2] = RowBounds.DEFAULT;
			CountablePage page = getPage(rowBounds);
			if ((page.getPageSizeZero() != null && page.getPageSizeZero())
					&& page.getPageSize() == 0) {
				Object result = invocation.proceed();
				page.addAll((List) result);
				page.setPageNum(1);
				page.setPageSize(page.size());
				page.setTotal(page.size());

				return page;
			}
			SqlSource sqlSource = ((MappedStatement) args[0]).getSqlSource();

			if (page.isCount()) {

				processCountMappedStatement(ms, sqlSource, args);

				Object result = invocation.proceed();

				page.setTotal((Integer) ((List) result).get(0));
				if (page.getTotal() == 0) {
					return page;
				}
			}

			if (page.getPageSize() > 0
					&& ((rowBounds == RowBounds.DEFAULT && page.getPageNum() > 0) || rowBounds != RowBounds.DEFAULT)) {

				RowBounds newRowBounds = new RowBounds(page.getStartRow(),
						page.getPageSize());
				processPageMappedStatement(ms, sqlSource, newRowBounds, args);

				Object result = invocation.proceed();

				page.addAll((List) result);
			}

			return page;
		}
	}

	public void setProperties(Properties p) {

		String offsetAsPageNum = p.getProperty("offsetAsPageNum");
		this.offsetAsPageNum = Boolean.parseBoolean(offsetAsPageNum);

		String rowBoundsWithCount = p.getProperty("rowBoundsWithCount");
		this.rowBoundsWithCount = Boolean.parseBoolean(rowBoundsWithCount);

		String pageSizeZero = p.getProperty("pageSizeZero");
		this.pageSizeZero = Boolean.parseBoolean(pageSizeZero);

		String reasonable = p.getProperty("reasonable");
		this.reasonable = Boolean.parseBoolean(reasonable);

		PARAMS.put("pageNum", "pageNum");
		PARAMS.put("pageSize", "pageSize");
		PARAMS.put("count", "countSql");
		PARAMS.put("reasonable", "reasonable");
		PARAMS.put("pageSizeZero", "pageSizeZero");
		String params = p.getProperty("params");
		if (params != null && params.length() > 0) {
			String[] ps = params.split("[;|,|&]");
			for (String s : ps) {
				String[] ss = s.split("[=|:]");
				if (ss.length == 2) {
					PARAMS.put(ss[0], ss[1]);
				}
			}
		}
	}

	public SqlUtil(String strDialect) {
		if (strDialect == null || "".equals(strDialect)) {
			throw new IllegalArgumentException(
					"Mybatis can not gain dialect param!");
		}
		try {
			dialect = Dialect.valueOf(strDialect);
			parser = SimpleParser.newParser(dialect);
		} catch (IllegalArgumentException e) {
			String dialects = null;
			for (Dialect d : Dialect.values()) {
				if (dialects == null) {
					dialects = d.toString();
				} else {
					dialects += "," + d;
				}
			}
			throw new IllegalArgumentException(
					"Mybatis dialect error ，available values[" + dialects + "]");
		}
	}

	public Map setPageParameter(MappedStatement ms, Object parameterObject,
			RowBounds rowBounds) {
		BoundSql boundSql = ms.getBoundSql(parameterObject);
		return parser
				.setPageParameter(ms, parameterObject, boundSql, rowBounds);
	}

	public void processCountMappedStatement(MappedStatement ms,
			SqlSource sqlSource, Object[] args) {
		args[0] = getMappedStatement(ms, sqlSource, args[1], SUFFIX_COUNT);
	}

	public void processPageMappedStatement(MappedStatement ms,
			SqlSource sqlSource, RowBounds rowBounds, Object[] args) {
		args[0] = getMappedStatement(ms, sqlSource, args[1], SUFFIX_PAGE);

		args[1] = setPageParameter((MappedStatement) args[0], args[1],
				rowBounds);
	}

	public static interface Parser {

		boolean isSupportedMappedStatementCache();

		String getCountSql(String sql);

		String getPageSql(String sql);

		List<ParameterMapping> getPageParameterMapping(
				Configuration configuration, BoundSql boundSql);

		Map setPageParameter(MappedStatement ms, Object parameterObject,
				BoundSql boundSql, RowBounds rowBounds);
	}

	public static abstract class SimpleParser implements Parser {
		public static Parser newParser(Dialect dialect) {
			Parser parser = null;
			switch (dialect) {
			case mysql:
			case mariadb:
			case sqlite:
				parser = new MysqlParser();
				break;
			default:
				throw new UnsupportedOperationException();
			}
			return parser;
		}

		@Override
		public boolean isSupportedMappedStatementCache() {
			return true;
		}

		public String getCountSql(final String sql) {
			return sqlParser.getSmartCountSql(sql);
		}

		public abstract String getPageSql(String sql);

		public List<ParameterMapping> getPageParameterMapping(
				Configuration configuration, BoundSql boundSql) {
			List<ParameterMapping> newParameterMappings = new ArrayList<ParameterMapping>();
			newParameterMappings.addAll(boundSql.getParameterMappings());
			newParameterMappings.add(new ParameterMapping.Builder(
					configuration, PAGEPARAMETER_FIRST, Integer.class).build());
			newParameterMappings
					.add(new ParameterMapping.Builder(configuration,
							PAGEPARAMETER_SECOND, Integer.class).build());
			return newParameterMappings;
		}

		public Map setPageParameter(MappedStatement ms, Object parameterObject,
				BoundSql boundSql, RowBounds rowBounds) {
			Map paramMap = null;
			if (parameterObject == null) {
				paramMap = new HashMap();
			} else if (parameterObject instanceof Map) {
				paramMap = (Map) parameterObject;
			} else {
				paramMap = new HashMap();

				boolean hasTypeHandler = ms.getConfiguration()
						.getTypeHandlerRegistry()
						.hasTypeHandler(parameterObject.getClass());
				MetaObject metaObject = forObject(parameterObject);

				if (ms.getSqlSource() instanceof MyProviderSqlSource) {
					paramMap.put(PROVIDER_OBJECT, parameterObject);
				}
				if (!hasTypeHandler) {
					for (String name : metaObject.getGetterNames()) {
						paramMap.put(name, metaObject.getValue(name));
					}
				}

				if (boundSql.getParameterMappings() != null
						&& boundSql.getParameterMappings().size() > 0) {
					for (ParameterMapping parameterMapping : boundSql
							.getParameterMappings()) {
						String name = parameterMapping.getProperty();
						if (!name.equals(PAGEPARAMETER_FIRST)
								&& !name.equals(PAGEPARAMETER_SECOND)
								&& paramMap.get(name) == null) {
							if (hasTypeHandler
									|| parameterMapping.getJavaType().equals(
											parameterObject.getClass())) {
								paramMap.put(name, parameterObject);
								break;
							}
						}
					}
				}
			}

			paramMap.put(ORIGINAL_PARAMETER_OBJECT, parameterObject);
			return paramMap;
		}
	}

	// Mysql
	private static class MysqlParser extends SimpleParser {
		@Override
		public String getPageSql(String sql) {
			StringBuilder sqlBuilder = new StringBuilder(sql.length() + 14);
			sqlBuilder.append(sql);
			sqlBuilder.append(" limit ?,?");
			return sqlBuilder.toString();
		}

		@Override
		public Map setPageParameter(MappedStatement ms, Object parameterObject,
				BoundSql boundSql, RowBounds rowBounds) {
			Map paramMap = super.setPageParameter(ms, parameterObject,
					boundSql, rowBounds);
			paramMap.put(PAGEPARAMETER_FIRST, rowBounds.getOffset());
			paramMap.put(PAGEPARAMETER_SECOND, rowBounds.getLimit());
			return paramMap;
		}

	}

	private class MyDynamicSqlSource implements SqlSource {
		private Configuration configuration;
		private SqlNode rootSqlNode;
		private Boolean count;

		public MyDynamicSqlSource(Configuration configuration,
				SqlNode rootSqlNode, Boolean count) {
			this.configuration = configuration;
			this.rootSqlNode = rootSqlNode;
			this.count = count;
		}

		public BoundSql getBoundSql(Object parameterObject) {
			DynamicContext context;

			if (parameterObject != null
					&& parameterObject instanceof Map
					&& ((Map) parameterObject)
							.containsKey(ORIGINAL_PARAMETER_OBJECT)) {
				context = new DynamicContext(configuration,
						((Map) parameterObject).get(ORIGINAL_PARAMETER_OBJECT));
			} else {
				context = new DynamicContext(configuration, parameterObject);
			}
			rootSqlNode.apply(context);
			SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(
					configuration);
			Class<?> parameterType = parameterObject == null ? Object.class
					: parameterObject.getClass();
			SqlSource sqlSource = sqlSourceParser.parse(context.getSql(),
					parameterType, context.getBindings());
			if (count) {
				sqlSource = getCountSqlSource(configuration, sqlSource,
						parameterObject);
			} else {
				sqlSource = getPageSqlSource(configuration, sqlSource,
						parameterObject);
			}
			BoundSql boundSql = sqlSource.getBoundSql(parameterObject);

			for (Map.Entry<String, Object> entry : context.getBindings()
					.entrySet()) {
				boundSql.setAdditionalParameter(entry.getKey(),
						entry.getValue());
			}
			return boundSql;
		}
	}

	private class MyProviderSqlSource implements SqlSource {
		private Configuration configuration;
		private ProviderSqlSource providerSqlSource;

		private Boolean count;

		private MyProviderSqlSource(Configuration configuration,
				ProviderSqlSource providerSqlSource, Boolean count) {
			this.configuration = configuration;
			this.providerSqlSource = providerSqlSource;
			this.count = count;
		}

		@Override
		public BoundSql getBoundSql(Object parameterObject) {
			BoundSql boundSql = null;
			if (parameterObject instanceof Map
					&& ((Map) parameterObject).containsKey(PROVIDER_OBJECT)) {
				boundSql = providerSqlSource
						.getBoundSql(((Map) parameterObject)
								.get(PROVIDER_OBJECT));
			} else {
				boundSql = providerSqlSource.getBoundSql(parameterObject);
			}
			if (count) {
				return new BoundSql(configuration, parser.getCountSql(boundSql
						.getSql()), boundSql.getParameterMappings(),
						parameterObject);
			} else {
				return new BoundSql(configuration, parser.getPageSql(boundSql
						.getSql()), parser.getPageParameterMapping(
						configuration, boundSql), parameterObject);
			}
		}
	}

	private MappedStatement getMappedStatement(MappedStatement ms,
			SqlSource sqlSource, Object parameterObject, String suffix) {
		MappedStatement qs = null;
		if (parser.isSupportedMappedStatementCache()) {
			try {
				qs = ms.getConfiguration().getMappedStatement(
						ms.getId() + suffix);
			} catch (Exception e) {
				// ignore
			}
		}
		if (qs == null) {

			qs = newMappedStatement(ms,
					getsqlSource(ms, sqlSource, parameterObject, suffix),
					suffix);
			if (parser.isSupportedMappedStatementCache()) {
				try {
					ms.getConfiguration().addMappedStatement(qs);
				} catch (Exception e) {
					// ignore
				}
			}
		}
		return qs;
	}

	private MappedStatement newMappedStatement(MappedStatement ms,
			SqlSource sqlSource, String suffix) {
		String id = ms.getId() + suffix;
		MappedStatement.Builder builder = new MappedStatement.Builder(
				ms.getConfiguration(), id, sqlSource, ms.getSqlCommandType());
		builder.resource(ms.getResource());
		builder.fetchSize(ms.getFetchSize());
		builder.statementType(ms.getStatementType());
		builder.keyGenerator(ms.getKeyGenerator());
		if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
			StringBuilder keyProperties = new StringBuilder();
			for (String keyProperty : ms.getKeyProperties()) {
				keyProperties.append(keyProperty).append(",");
			}
			keyProperties.delete(keyProperties.length() - 1,
					keyProperties.length());
			builder.keyProperty(keyProperties.toString());
		}
		builder.timeout(ms.getTimeout());
		builder.parameterMap(ms.getParameterMap());
		if (suffix == SUFFIX_PAGE) {
			builder.resultMaps(ms.getResultMaps());
		} else {

			List<ResultMap> resultMaps = new ArrayList<ResultMap>();
			ResultMap resultMap = new ResultMap.Builder(ms.getConfiguration(),
					id, int.class, EMPTY_RESULTMAPPING).build();
			resultMaps.add(resultMap);
			builder.resultMaps(resultMaps);
		}
		builder.resultSetType(ms.getResultSetType());
		builder.cache(ms.getCache());
		builder.flushCacheRequired(ms.isFlushCacheRequired());
		builder.useCache(ms.isUseCache());

		return builder.build();
	}

	public boolean isDynamic(MappedStatement ms) {
		return ms.getSqlSource() instanceof DynamicSqlSource;
	}

	private SqlSource getsqlSource(MappedStatement ms, SqlSource sqlSource,
			Object parameterObject, String suffix) {

		if (isDynamic(ms)) {
			MetaObject msObject = forObject(ms);
			SqlNode sqlNode = (SqlNode) msObject
					.getValue("sqlSource.rootSqlNode");
			MixedSqlNode mixedSqlNode = null;
			if (sqlNode instanceof MixedSqlNode) {
				mixedSqlNode = (MixedSqlNode) sqlNode;
			} else {
				List<SqlNode> contents = new ArrayList<SqlNode>(1);
				contents.add(sqlNode);
				mixedSqlNode = new MixedSqlNode(contents);
			}
			return new MyDynamicSqlSource(ms.getConfiguration(), mixedSqlNode,
					suffix == SUFFIX_COUNT);
		} else if (sqlSource instanceof ProviderSqlSource) {
			return new MyProviderSqlSource(ms.getConfiguration(),
					(ProviderSqlSource) sqlSource, suffix == SUFFIX_COUNT);
		}

		else if (suffix == SUFFIX_PAGE) {

			return getPageSqlSource(ms.getConfiguration(), sqlSource,
					parameterObject);
		}

		else {
			return getCountSqlSource(ms.getConfiguration(), sqlSource,
					parameterObject);
		}
	}

	private SqlSource getPageSqlSource(Configuration configuration,
			SqlSource sqlSource, Object parameterObject) {
		BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
		return new StaticSqlSource(configuration, parser.getPageSql(boundSql
				.getSql()), parser.getPageParameterMapping(configuration,
				boundSql));
	}

	private SqlSource getCountSqlSource(Configuration configuration,
			SqlSource sqlSource, Object parameterObject) {
		BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
		return new StaticSqlSource(configuration, parser.getCountSql(boundSql
				.getSql()), boundSql.getParameterMappings());
	}

	public static void testSql(String dialet, String originalSql) {
		SqlUtil sqlUtil = new SqlUtil(dialet);
		if (sqlUtil.dialect == Dialect.sqlserver) {
			setLocalPage(new CountablePage(1, 10));
		}
		String countSql = sqlUtil.parser.getCountSql(originalSql);
		System.out.println(countSql);
		String pageSql = sqlUtil.parser.getPageSql(originalSql);
		System.out.println(pageSql);
	}
}