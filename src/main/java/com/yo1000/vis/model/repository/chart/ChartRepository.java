package com.yo1000.vis.model.repository.chart;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by yoichi.kikuchi on 15/06/09.
 */
public interface ChartRepository {
    public List<Map<String, Object>> getItems(String sql);
    public List<Map<String, Object>> getItems(String sql, Date target);

    public List<Object>  getItemsForSnowCover(String sql);
    public List<Object>  getItemsForSnowCover(String sql, Date target);
}
