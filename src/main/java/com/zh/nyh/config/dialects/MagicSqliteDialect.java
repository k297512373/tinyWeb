package com.zh.nyh.config.dialects;


import org.springframework.stereotype.Component;
import org.ssssssss.magicapi.modules.db.BoundSql;
import org.ssssssss.magicapi.modules.db.dialect.Dialect;

@Component
public class MagicSqliteDialect implements Dialect{
	
	@Override
	public boolean match(String jdbcUrl){
		return jdbcUrl.contains(":sqlite:");
	}

	@Override
	public String getPageSql(String sql, BoundSql boundSql, long offset, long limit) {
		boundSql.addParameter(offset);
		boundSql.addParameter(limit);
		return sql + "\n limit ?,?";
	}

}
