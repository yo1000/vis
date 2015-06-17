package com.yo1000.vis.model.service;

import com.yo1000.vis.model.data.Query;
import com.yo1000.vis.model.data.Widget;
import com.yo1000.vis.model.repository.system.WidgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by yoichi.kikuchi on 15/06/12.
 */
@Service
public class WidgetService {
    @Autowired
    private WidgetRepository widgetRepository;

    public Map<Widget, Query> get(String id) {
        return this.getWidgetRepository().findById(id);
    }

    public Map<Widget, Query> getMessageWidgets() {
        List<Map<Widget, Query>> results = this.getWidgetRepository().findByTypeIsMessage();

        if (results == null || results.isEmpty()) {
            return new LinkedHashMap<Widget, Query>() {
                {
                    this.put(new Widget("", "message", 0, 0, ""), new Query());
                }
            };
        }

        return results.get(0);
    }

    public Map<Widget, Query> getChartWidgets() {
        List<Map<Widget, Query>> results = this.getWidgetRepository().findByTypeIsChart();
        final int itemSize = 16;

        if (results == null || results.isEmpty()) {
            Map<Widget, Query> items = new LinkedHashMap<Widget, Query>();

            for (int i = 0; i < itemSize; i++) {
                items.put(new Widget("", "chart", i + 1, 2, ""), new Query());
            }
            return items;
        }

        Map<Widget, Query> items = new LinkedHashMap<Widget, Query>();

        for (int i = 0; i < itemSize && i < results.size(); i++) {
            Map.Entry<Widget, Query> result = results.get(i).entrySet().iterator().next();

            for (int j = items.size(); j < result.getKey().getPosition() - 1; j++) {
                items.put(new Widget("", "chart", j + 1, 2, ""), new Query());
            }

            items.put(result.getKey(), result.getValue());
        }

        for (int i = items.size(); i < itemSize; i++) {
            items.put(new Widget("", "chart", i + 1, 2, ""), new Query());
        }

        return items;
    }

    public void set(Widget widget) {
        if (widget.getId() == null || widget.getId().isEmpty()) {
            this.getWidgetRepository().insert(widget);
            return;
        }

        this.getWidgetRepository().updateById(widget);
    }

    public void remove(String id) {
        if (id == null || id.isEmpty()) {
            return;
        }

        this.getWidgetRepository().deleteById(id);
    }

    protected WidgetRepository getWidgetRepository() {
        return widgetRepository;
    }
}
