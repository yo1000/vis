package com.yo1000.vis.model.repository.system.jdbc;

import com.yo1000.vis.model.data.RequestHistory;
import com.yo1000.vis.model.repository.system.RequestHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * Created by yoichi.kikuchi on 15/06/08.
 */
@Repository
public class JdbcRequestHistoryRepository implements RequestHistoryRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcRequestHistoryRepository.class);

    @Autowired
    @Qualifier("systemJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<RequestHistory> find() {
        return this.getJdbcTemplate().query("SELECT " +
                        "  HST_ID," +
                        "  HST_URL " +
                        "FROM " +
                        "  REQUEST_HISTORY " +
                        "ORDER BY " +
                        "  HST_URL",
                new RowMapper<RequestHistory>() {
                    @Override
                    public RequestHistory mapRow(ResultSet resultSet, int i) throws SQLException {
                        return new RequestHistory(
                                resultSet.getString("HST_ID"),
                                resultSet.getString("HST_URL")
                        );
                    }
                }
        );
    }

    @Override
    public boolean exists(String url) {
        return this.getJdbcTemplate().queryForObject("SELECT " +
                        "  COUNT(HST_ID) AS HST_CNT " +
                        "FROM " +
                        "  REQUEST_HISTORY " +
                        "WHERE " +
                        "  HST_URL = ?",
                new RowMapper<Integer>() {
                    @Override
                    public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                        return resultSet.getInt("HST_CNT");
                    }
                },
                url
        ) > 0;
    }

    @Override
    public int insert(String url) {
        return this.getJdbcTemplate().update("INSERT INTO REQUEST_HISTORY( " +
                        "  HST_ID, HST_URL " +
                        ") VALUES ( " +
                        "  ?, ? " +
                        ")",
                DigestUtils.md5DigestAsHex(UUID.randomUUID().toString().getBytes()),
                url);
    }

    protected JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}
