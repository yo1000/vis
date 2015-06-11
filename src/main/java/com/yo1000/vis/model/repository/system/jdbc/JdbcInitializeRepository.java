package com.yo1000.vis.model.repository.system.jdbc;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Created by yoichi.kikuchi on 15/06/03.
 */
@Aspect
@Repository
public class JdbcInitializeRepository {
    @Autowired
    @Qualifier("systemJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    private boolean initialized = false;

    @Around("execution(* com.yo1000.vis.model.repository.system.jdbc.*.*(..)) " +
            "&& @within(org.springframework.stereotype.Repository) " +
            "&& not @within(org.aspectj.lang.annotation.Aspect)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        }
        catch (Exception e) {
            e.printStackTrace();
            this.triesInitialize(e);
            return joinPoint.proceed();
        }
    }

    protected void triesInitialize(Exception e) {
        if (this.isInitialized()) {
            throw new IllegalStateException(e);
        }

        this.createNavigatorTable();
        this.createQueryTable();

        this.setInitialized(true);
    }

    protected int createNavigatorTable() {
        return this.getJdbcTemplate().update("CREATE TABLE NAVIGATOR(" +
                "NAV_ID        VARCHAR(64)  NOT NULL, " +
                "NAV_ROW       INT          NOT NULL, " +
                "NAV_COL       INT          NOT NULL, " +
                "NAV_NAME      VARCHAR(64)  NOT NULL, " +
                "NAV_URL       VARCHAR(512), " +
                "NAV_SEPARATOR VARCHAR(1)   DEFAULT '0', " +
                "PRIMARY KEY(NAV_ID)" +
                ")"
        );
    }

    protected int createQueryTable() {
        int result = this.getJdbcTemplate().update("CREATE TABLE QUERY(" +
                "QRY_ID        VARCHAR(64)  NOT NULL, " +
                "QRY_KEY       VARCHAR(64)  NOT NULL, " +
                "QRY_NAME      VARCHAR(128) NOT NULL, " +
                "QRY_SQL       CLOB         NOT NULL, " +
                "QRY_VIEW      VARCHAR(64)  NOT NULL, " +
                "QRY_AUTHOR    VARCHAR(64)  DEFAULT '', " +
                "QRY_MODIFIED  BIGINT       NOT NULL," +
                "PRIMARY KEY(QRY_ID)" +
                ")"
        );

        result += this.getJdbcTemplate().update(
                "CREATE UNIQUE INDEX UNQ_QRY_KEY ON QUERY (QRY_KEY)"
        );

        return result;
    }

    protected boolean isInitialized() {
        return initialized;
    }

    protected void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    protected JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}
