package com.yo1000.vis.model.repository.system;

import com.yo1000.vis.model.data.Query;
import com.yo1000.vis.model.data.Widget;

import java.util.List;
import java.util.Map;

/**
 * Created by yoichi.kikuchi on 15/06/12.
 */
public interface WidgetRepository {
    public int insert(Widget widget);
    public int updateById(Widget widget);
    public int deleteById(String id);
    public Map<Widget, Query> findById(String id);
    public List<Map<Widget, Query>> findByTypeIsMessage();
    public List<Map<Widget, Query>> findByTypeIsChart();
}
