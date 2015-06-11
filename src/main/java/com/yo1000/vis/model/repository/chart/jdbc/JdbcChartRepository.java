package com.yo1000.vis.model.repository.chart.jdbc;

import com.yo1000.vis.model.repository.chart.ChartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by yoichi.kikuchi on 15/06/09.
 */
@Repository
public class JdbcChartRepository implements ChartRepository {
    @Autowired
    @Qualifier("chartJdbcTemplate")
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> getItems(String sql) {
        return this.getJdbcTemplate().query(sql, new ChartRowMapper());
    }

    @Override
    public List<Map<String, Object>> getItems(String sql, final Date target) {
        return this.getJdbcTemplate().query(sql, new HashMap<String, Object>() {
            {
                this.put("date", target);
            }
        }, new ChartRowMapper());
    }

    @Override
    public List<Object> getItemsForSnowCover(String sql) {
        return this.getJdbcTemplate().query(sql, new SnowCoverRowMapper());
    }

    @Override
    public List<Object> getItemsForSnowCover(String sql, final Date target) {
        SnowCoverRowMapper mapper = new SnowCoverRowMapper();
        List<Object> items = this.getJdbcTemplate().query(sql, new HashMap<String, Object>() {
            {
                this.put("date", target);
            }
        }, mapper);

        if (!mapper.getColumns().isEmpty()) {
            items.add(0, mapper.getColumns().get(0));
        }

        return items;
    }

    protected NamedParameterJdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    protected static class ChartRowMapper implements RowMapper<Map<String, Object>> {
        ResultSetMetaData meta = null;

        @Override
        public Map<String, Object> mapRow(ResultSet resultSet, int i) throws SQLException {
            if (this.meta == null) {
                this.meta = resultSet.getMetaData();
            }

            Map<String, Object> item = new LinkedHashMap<String, Object>();

            for (int j = 1; j <= meta.getColumnCount(); j++) {
                item.put(meta.getColumnLabel(j), resultSet.getObject(j));
            }

            return item;
        }

//        private List<String> columns = new ArrayList<String>();
//
//        @Override
//        public Map<String, Object> mapRow(ResultSet resultSet, int i) throws SQLException {
//            if (this.getColumns().isEmpty()) {
//                ResultSetMetaData meta = resultSet.getMetaData();
//
//                for (int j = 1; j < meta.getColumnCount(); j++) {
//                    this.getColumns().add(meta.getColumnLabel(j));
//                }
//            }
//
//            Map<String, Object> row = new LinkedHashMap<String, Object>();
//
//            for (int j = 1; j <= this.getColumns().size(); j++) {
//                row.put(this.getColumns().get(j - 1), resultSet.getObject(j));
//            }
//
//            return row;
//        }
//
//        protected List<String> getColumns() {
//            return columns;
//        }
    }

    protected static class SnowCoverRowMapper implements RowMapper<Object> {
        private List<Object> columns = new ArrayList<Object>();

        @Override
        public Object mapRow(ResultSet resultSet, int i) throws SQLException {
            if (resultSet.getMetaData().getColumnCount() < 2) {
                throw new SQLException("SELECT query item that requires two or more columns.");
            }

            Object o = resultSet.getObject(1);
            if (o instanceof String) {
                String s = (String) o;
                this.getColumns().add(s.matches("^-?[0-9]+$") ? Long.valueOf(s)
                        : s.matches("^-?[0-9]+\\.[0-9]+$") ? Double.valueOf(s)
                        : o);
                return resultSet.getObject(2);
            }

            this.getColumns().add(o);
            return resultSet.getObject(2);
        }

        public List<Object> getColumns() {
            return columns;
        }
    }
}
