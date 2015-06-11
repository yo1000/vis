package com.yo1000.vis.model.repository.system.jdbc;

import com.yo1000.vis.model.data.Query;
import com.yo1000.vis.model.repository.system.QueryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.DigestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by yoichi.kikuchi on 15/06/08.
 */
@Repository
public class JdbcQueryRepository implements QueryRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcQueryRepository.class);

    @Autowired
    @Qualifier("systemJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Query> find() {
        return this.getJdbcTemplate().query("SELECT " +
                        "  QRY_ID," +
                        "  QRY_KEY," +
                        "  QRY_NAME," +
                        "  QRY_SQL," +
                        "  QRY_VIEW," +
                        "  QRY_AUTHOR," +
                        "  QRY_MODIFIED " +
                        "FROM " +
                        "  QUERY " +
                        "ORDER BY " +
                        "  QRY_KEY",
                new RowMapper<Query>() {
                    @Override
                    public Query mapRow(ResultSet resultSet, int i) throws SQLException {
                        StringBuilder qrySqlBuilder = new StringBuilder();

                        try (BufferedReader reader = new BufferedReader(
                                resultSet.getClob("QRY_SQL").getCharacterStream())) {
                            String line = null;
                            while ((line = reader.readLine()) != null) {
                                qrySqlBuilder.append(line).append(" \n");
                            }
                        } catch (IOException e) {
                            throw new IllegalStateException(e);
                        }

                        return new Query(
                                resultSet.getString("QRY_ID"),
                                resultSet.getString("QRY_KEY"),
                                resultSet.getString("QRY_NAME"),
                                qrySqlBuilder.toString(),
                                resultSet.getString("QRY_VIEW"),
                                resultSet.getString("QRY_AUTHOR"),
                                new Date(resultSet.getLong("QRY_MODIFIED"))
                        );
                    }
                }
        );
    }

    @Override
    public Query find(String id) {
        return this.getJdbcTemplate().queryForObject("SELECT " +
                        "  QRY_ID," +
                        "  QRY_KEY," +
                        "  QRY_NAME," +
                        "  QRY_SQL," +
                        "  QRY_VIEW," +
                        "  QRY_AUTHOR," +
                        "  QRY_MODIFIED " +
                        "FROM " +
                        "  QUERY " +
                        "WHERE " +
                        "  QRY_ID = ?",
                new RowMapper<Query>() {
                    @Override
                    public Query mapRow(ResultSet resultSet, int i) throws SQLException {
                        StringBuilder qrySqlBuilder = new StringBuilder();

                        try (BufferedReader reader = new BufferedReader(
                                resultSet.getClob("QRY_SQL").getCharacterStream())) {
                            String line = null;
                            while ((line = reader.readLine()) != null) {
                                qrySqlBuilder.append(line).append(" \n");
                            }
                        } catch (IOException e) {
                            throw new IllegalStateException(e);
                        }

                        return new Query(
                                resultSet.getString("QRY_ID"),
                                resultSet.getString("QRY_KEY"),
                                resultSet.getString("QRY_NAME"),
                                qrySqlBuilder.toString(),
                                resultSet.getString("QRY_VIEW"),
                                resultSet.getString("QRY_AUTHOR"),
                                new Date(resultSet.getLong("QRY_MODIFIED"))
                        );
                    }
                },
                id
        );
    }

    @Override
    public Query findByKey(String key) {
        return this.getJdbcTemplate().queryForObject("SELECT " +
                        "  QRY_ID," +
                        "  QRY_KEY," +
                        "  QRY_NAME," +
                        "  QRY_SQL," +
                        "  QRY_VIEW," +
                        "  QRY_AUTHOR," +
                        "  QRY_MODIFIED " +
                        "FROM " +
                        "  QUERY " +
                        "WHERE " +
                        "  QRY_KEY = ?",
                new RowMapper<Query>() {
                    @Override
                    public Query mapRow(ResultSet resultSet, int i) throws SQLException {
                        StringBuilder qrySqlBuilder = new StringBuilder();

                        try (BufferedReader reader = new BufferedReader(
                                resultSet.getClob("QRY_SQL").getCharacterStream())) {
                            String line = null;
                            while ((line = reader.readLine()) != null) {
                                qrySqlBuilder.append(line).append(" \n");
                            }
                        }
                        catch (IOException e) {
                            throw new IllegalStateException(e);
                        }

                        return new Query(
                                resultSet.getString("QRY_ID"),
                                resultSet.getString("QRY_KEY"),
                                resultSet.getString("QRY_NAME"),
                                qrySqlBuilder.toString(),
                                resultSet.getString("QRY_VIEW"),
                                resultSet.getString("QRY_AUTHOR"),
                                new Date(resultSet.getLong("QRY_MODIFIED"))
                        );
                    }
                },
                key
        );
    }

    @Override
    public int insert(Query query) {
        return this.getJdbcTemplate().update("INSERT INTO QUERY( " +
                        "  QRY_ID , QRY_KEY , QRY_NAME  , " +
                        "  QRY_SQL, QRY_VIEW, QRY_AUTHOR, " +
                        "  QRY_MODIFIED " +
                        ") VALUES ( " +
                        "  ?, ?, ?, ?, ?, ?, ? " +
                        ")",
                DigestUtils.md5DigestAsHex(UUID.randomUUID().toString().getBytes()),
                query.getKey(),
                query.getName(),
                query.getSql(),
                query.getView(),
                query.getAuthor(),
                System.currentTimeMillis()
        );
    }


    @Override
    public int update(Query query) {
        return this.getJdbcTemplate().update("UPDATE QUERY SET " +
                        "  QRY_KEY = ?, " +
                        "  QRY_NAME  = ?, " +
                        "  QRY_SQL = ?, " +
                        "  QRY_VIEW = ?, " +
                        "  QRY_AUTHOR = ?, " +
                        "  QRY_MODIFIED = ? " +
                        "WHERE " +
                        "  QRY_ID = ? ",
                query.getKey(),
                query.getName(),
                query.getSql(),
                query.getView(),
                query.getAuthor(),
                System.currentTimeMillis(),
                query.getId()
        );
    }
        /*
                "QRY_ID        VARCHAR(64)  NOT NULL, " +
                "QRY_KEY       VARCHAR(64)  NOT NULL UNIQUE, " +
                "QRY_NAME      VARCHAR(128) NOT NULL, " +
                "QRY_SQL       CLOB         NOT NULL, " +
                "QRY_VIEW      VARCHAR(64)  NOT NULL, " +
                "QRY_AUTHOR    VARCHAR(64)  DEFAULT '', " +
                "QRY_MODIFIED  INT,         NOT NULL," +

     */


    protected JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}
