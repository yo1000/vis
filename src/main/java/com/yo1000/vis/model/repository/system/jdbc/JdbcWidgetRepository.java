package com.yo1000.vis.model.repository.system.jdbc;

import com.yo1000.vis.model.data.Query;
import com.yo1000.vis.model.data.Widget;
import com.yo1000.vis.model.repository.system.WidgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.DigestUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by yoichi.kikuchi on 15/06/03.
 */
@Repository
public class JdbcWidgetRepository implements WidgetRepository {
    @Autowired
    @Qualifier("systemJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Override
    public int insert(Widget widget) {
        return this.getJdbcTemplate().update("INSERT INTO WIDGET (" +
                        "  WGT_ID,  WGT_TYPE,  WGT_POSITION,  QRY_ID " +
                        ") VALUES ( " +
                        "  ?, ?, ?, ? " +
                        ") ",
                DigestUtils.md5DigestAsHex(UUID.randomUUID().toString().getBytes()),
                widget.getType(),
                widget.getPosition(),
                widget.getQueryId()
        );
    }

    @Override
    public int updateById(Widget widget) {
        return this.getJdbcTemplate().update("UPDATE WIDGET SET " +
                        "  WGT_TYPE = ?, " +
                        "  WGT_POSITION = ?, " +
                        "  QRY_ID  = ? " +
                        "WHERE " +
                        "  WGT_ID = ? ",
                widget.getType(),
                widget.getPosition(),
                widget.getQueryId(),
                widget.getId()
        );
    }

    @Override
    public Map<Widget, Query> findById(String id) {
        List<Map<Widget, Query>> navs = this.getJdbcTemplate().query("SELECT " +
                        "  WGT_ID, " +
                        "  WGT_TYPE, " +
                        "  WGT_POSITION, " +
                        "  WIDGET.QRY_ID, " +
                        "  QRY_KEY, " +
                        "  QRY_NAME, " +
                        "  QRY_SQL, " +
                        "  QRY_VIEW, " +
                        "  QRY_AUTHOR, " +
                        "  QRY_MODIFIED " +
                        "FROM " +
                        "  WIDGET " +
                        "INNER JOIN " +
                        "  QUERY " +
                        "  ON WIDGET.QRY_ID = QUERY.QRY_ID " +
                        "WHERE " +
                        "  WGT_ID = ? " +
                        "ORDER BY " +
                        "  WGT_POSITION ",
                new RowMapper<Map<Widget, Query>>() {
                    @Override
                    public Map<Widget, Query> mapRow(ResultSet resultSet, int i) throws SQLException {
                        Map<Widget, Query> widgetQueryMap = new HashMap<Widget, Query>();

                        Widget widget = new Widget(
                                resultSet.getString("WGT_ID"),
                                resultSet.getString("WGT_TYPE"),
                                resultSet.getInt("WGT_POSITION"),
                                resultSet.getString("QRY_ID")
                        );

                        Query query = new Query(
                                resultSet.getString("QRY_ID"),
                                resultSet.getString("QRY_KEY"),
                                resultSet.getString("QRY_NAME"),
                                "",
                                resultSet.getString("QRY_VIEW"),
                                resultSet.getString("QRY_AUTHOR"),
                                new Date(resultSet.getLong("QRY_MODIFIED"))
                        );

                        widgetQueryMap.put(widget, query);
                        return  widgetQueryMap;
                    }
                },
                id
        );

        return navs.size() > 0 ? navs.get(0) : null;
    }

    @Override
    public List<Map<Widget, Query>> findByTypeIsMessage() {
        return this.getJdbcTemplate().query("SELECT " +
                        "  WGT_ID, " +
                        "  WGT_TYPE, " +
                        "  WGT_POSITION, " +
                        "  WIDGET.QRY_ID, " +
                        "  QRY_KEY, " +
                        "  QRY_NAME, " +
                        "  QRY_SQL, " +
                        "  QRY_VIEW, " +
                        "  QRY_AUTHOR, " +
                        "  QRY_MODIFIED " +
                        "FROM " +
                        "  WIDGET " +
                        "INNER JOIN " +
                        "  QUERY " +
                        "  ON WIDGET.QRY_ID = QUERY.QRY_ID " +
                        "WHERE " +
                        "  WGT_TYPE = 'message' " +
                        "ORDER BY " +
                        "  WGT_POSITION ",
                new RowMapper<Map<Widget, Query>>() {
                    @Override
                    public Map<Widget, Query> mapRow(ResultSet resultSet, int i) throws SQLException {
                        Map<Widget, Query> widgetQueryMap = new HashMap<Widget, Query>();

                        Widget widget = new Widget(
                                resultSet.getString("WGT_ID"),
                                resultSet.getString("WGT_TYPE"),
                                resultSet.getInt("WGT_POSITION"),
                                resultSet.getString("QRY_ID")
                        );

                        Query query = new Query(
                                resultSet.getString("QRY_ID"),
                                resultSet.getString("QRY_KEY"),
                                resultSet.getString("QRY_NAME"),
                                "",
                                resultSet.getString("QRY_VIEW"),
                                resultSet.getString("QRY_AUTHOR"),
                                new Date(resultSet.getLong("QRY_MODIFIED"))
                        );

                        widgetQueryMap.put(widget, query);
                        return widgetQueryMap;
                    }
                }
        );
    }

    @Override
    public List<Map<Widget, Query>> findByTypeIsChart() {
        return this.getJdbcTemplate().query("SELECT " +
                        "  WGT_ID, " +
                        "  WGT_TYPE, " +
                        "  WGT_POSITION, " +
                        "  WIDGET.QRY_ID, " +
                        "  QRY_KEY, " +
                        "  QRY_NAME, " +
                        "  QRY_SQL, " +
                        "  QRY_VIEW, " +
                        "  QRY_AUTHOR, " +
                        "  QRY_MODIFIED " +
                        "FROM " +
                        "  WIDGET " +
                        "INNER JOIN " +
                        "  QUERY " +
                        "  ON WIDGET.QRY_ID = QUERY.QRY_ID " +
                        "WHERE " +
                        "  WGT_TYPE = 'chart' " +
                        "ORDER BY " +
                        "  WGT_POSITION ",
                new RowMapper<Map<Widget, Query>>() {
                    @Override
                    public Map<Widget, Query> mapRow(ResultSet resultSet, int i) throws SQLException {
                        Map<Widget, Query> widgetQueryMap = new HashMap<Widget, Query>();

                        Widget widget = new Widget(
                                resultSet.getString("WGT_ID"),
                                resultSet.getString("WGT_TYPE"),
                                resultSet.getInt("WGT_POSITION"),
                                resultSet.getString("QRY_ID")
                        );

                        Query query = new Query(
                                resultSet.getString("QRY_ID"),
                                resultSet.getString("QRY_KEY"),
                                resultSet.getString("QRY_NAME"),
                                "",
                                resultSet.getString("QRY_VIEW"),
                                resultSet.getString("QRY_AUTHOR"),
                                new Date(resultSet.getLong("QRY_MODIFIED"))
                        );

                        widgetQueryMap.put(widget, query);
                        return  widgetQueryMap;
                    }
                }
        );
    }

    protected JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}
