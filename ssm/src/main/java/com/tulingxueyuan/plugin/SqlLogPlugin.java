package com.tulingxueyuan.plugin;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Statement;
import java.util.Properties;

@Component
@Intercepts({
        @Signature(
                type = StatementHandler.class,
                method = "query",
                args = {Statement.class, ResultHandler.class})
})
public class SqlLogPlugin implements Interceptor {
    public static final Logger log = LoggerFactory.getLogger(SqlLogPlugin.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 开始
        Long b = System.currentTimeMillis();
        // 目标sql
        String sql = ((StatementHandler) invocation.getTarget()).getBoundSql().getSql();
        // 执行数据库操作
        Object obj = invocation.proceed();
        // 计算用时
        log.info(sql + " cost " + (System.currentTimeMillis() - b) + "ms");
        // if()
        // 告警
        return obj;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}