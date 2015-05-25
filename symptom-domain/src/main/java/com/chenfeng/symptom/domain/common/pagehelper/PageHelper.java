package com.chenfeng.symptom.domain.common.pagehelper;

import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

@Intercepts(@Signature(type = Executor.class, method = "query", args = {
		MappedStatement.class, Object.class, RowBounds.class,
		ResultHandler.class }))
public class PageHelper implements Interceptor {

	private SqlUtil sqlUtil;


	public Object intercept(Invocation invocation) throws Throwable {
		return sqlUtil.processPage(invocation);
	}

	public Object plugin(Object target) {
		if (target instanceof Executor) {
			return Plugin.wrap(target, this);
		} else {
			return target;
		}
	}

	public void setProperties(Properties p) {

		String dialect = p.getProperty("dialect");
		sqlUtil = new SqlUtil(dialect);
		sqlUtil.setProperties(p);
	}
}
