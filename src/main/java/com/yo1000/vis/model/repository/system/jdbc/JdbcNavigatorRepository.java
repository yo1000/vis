package com.yo1000.vis.model.repository.system.jdbc;

import com.yo1000.vis.model.data.Navigator;
import com.yo1000.vis.model.repository.system.NavigatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.DigestUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

/**
 * Created by yoichi.kikuchi on 15/06/03.
 */
@Repository
public class JdbcNavigatorRepository implements NavigatorRepository {
    @Autowired
    @Qualifier("systemJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Override
    public int insert(Navigator navigator) {
        return this.getJdbcTemplate().update("INSERT INTO NAVIGATOR (" +
                        "  NAV_ID,   NAV_ROW, NAV_COL," +
                        "  NAV_NAME, NAV_URL, NAV_SEPARATOR " +
                        ") VALUES ( " +
                        "  ?, ?, ?, ?, ?, ? " +
                        ")",
                DigestUtils.md5DigestAsHex(UUID.randomUUID().toString().getBytes()),
                navigator.getRowIndex(),
                navigator.getColumnIndex(),
                navigator.getName(),
                navigator.getUrl(),
                (navigator.getSeparator() != null && navigator.getSeparator().equals("1")) ? "1" : "0"
        );

    }

    @Override
    public int updateById(Navigator navigator) {
        return this.getJdbcTemplate().update("UPDATE NAVIGATOR SET " +
                        "  NAV_NAME = ?, " +
                        "  NAV_URL  = ?, " +
                        "  NAV_SEPARATOR = ? " +
                        "WHERE " +
                        "  NAV_ID = ?",
                navigator.getName(),
                navigator.getUrl(),
                navigator.getSeparator() != null && navigator.getSeparator().equals("1") ? "1" : "0",
                navigator.getId()
        );
    }

    @Override
    public int updateByColumnRow(Navigator navigator) {
        return this.getJdbcTemplate().update("UPDATE NAVIGATOR SET " +
                        "  NAV_NAME = ?, " +
                        "  NAV_URL  = ?, " +
                        "  NAV_SEPARATOR = ? " +
                        "WHERE " +
                        "  NAV_COL = ? " +
                        "AND " +
                        "  NAV_ROW = ? ",
                navigator.getName(),
                navigator.getUrl(),
                navigator.getSeparator() != null && navigator.getSeparator().equals("1") ? "1" : "0",
                navigator.getColumnIndex(),
                navigator.getRowIndex()
        );
    }

    @Override
    public Navigator findByColumnRow(int columnIndex, int rowIndex) {
        List<Navigator> navs = this.getJdbcTemplate().query("SELECT " +
                        "  NAV_ID, " +
                        "  NAV_ROW, " +
                        "  NAV_COL, " +
                        "  NAV_NAME, " +
                        "  NAV_URL, " +
                        "  NAV_SEPARATOR " +
                        "FROM " +
                        "  NAVIGATOR " +
                        "WHERE " +
                        "  NAV_COL = ? " +
                        "AND " +
                        "  NAV_ROW = ? ",
                new RowMapper<Navigator>() {
                    @Override
                    public Navigator mapRow(ResultSet resultSet, int i) throws SQLException {
                        return new Navigator(
                                resultSet.getString("NAV_ID"),
                                resultSet.getString("NAV_NAME"),
                                resultSet.getString("NAV_URL"),
                                resultSet.getString("NAV_SEPARATOR"),
                                resultSet.getInt("NAV_COL"),
                                resultSet.getInt("NAV_ROW")
                        );
                    }
                },
                columnIndex,
                rowIndex
        );

        return navs.size() > 0 ? navs.get(0) : null;
    }

    @Override
    public List<Navigator> findHeaders() {
        return this.getJdbcTemplate().query("SELECT " +
                        "  NAV_ID, " +
                        "  NAV_ROW, " +
                        "  NAV_COL, " +
                        "  NAV_NAME, " +
                        "  NAV_URL, " +
                        "  NAV_SEPARATOR " +
                        "FROM " +
                        "  NAVIGATOR " +
                        "WHERE " +
                        "  NAV_ROW = 0 " +
                        "ORDER BY " +
                        "  NAV_COL",
                new RowMapper<Navigator>() {
                    @Override
                    public Navigator mapRow(ResultSet resultSet, int i) throws SQLException {
                        return new Navigator(
                                resultSet.getString("NAV_ID"),
                                resultSet.getString("NAV_NAME"),
                                resultSet.getString("NAV_URL"),
                                resultSet.getString("NAV_SEPARATOR"),
                                resultSet.getInt("NAV_COL"),
                                resultSet.getInt("NAV_ROW")
                        );
                    }
                }
        );
    }

    @Override
    public List<Navigator> findSubitemsByColumnIndex(int columnIndex) {
        return this.getJdbcTemplate().query("SELECT " +
                        "  NAV_ID, " +
                        "  NAV_ROW, " +
                        "  NAV_COL, " +
                        "  NAV_NAME, " +
                        "  NAV_URL, " +
                        "  NAV_SEPARATOR " +
                        "FROM " +
                        "  NAVIGATOR " +
                        "WHERE" +
                        "  NAV_ROW >= 1 " +
                        "AND" +
                        "  NAV_COL = ? " +
                        "ORDER BY " +
                        "  NAV_ROW",
                new RowMapper<Navigator>() {
                    @Override
                    public Navigator mapRow(ResultSet resultSet, int i) throws SQLException {
                        return new Navigator(
                                resultSet.getString("NAV_ID"),
                                resultSet.getString("NAV_NAME"),
                                resultSet.getString("NAV_URL"),
                                resultSet.getString("NAV_SEPARATOR"),
                                resultSet.getInt("NAV_COL"),
                                resultSet.getInt("NAV_ROW")
                        );
                    }
                },
                columnIndex
        );
    }

    protected JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}
