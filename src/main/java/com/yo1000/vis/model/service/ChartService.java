package com.yo1000.vis.model.service;

import com.yo1000.vis.aop.Cache;
import com.yo1000.vis.model.data.Query;
import com.yo1000.vis.model.repository.chart.ChartRepository;
import com.yo1000.vis.model.repository.system.QueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by yoichi.kikuchi on 15/06/09.
 */
@Service
public class ChartService {
    @Autowired
    private ChartRepository chartRepository;

    @Autowired
    private QueryRepository queryRepository;

    @Cache
    public List<Map<String, Object>> getItems(String key) {
        Query query = this.getQueryRepository().findByKey(key);
        List<Map<String, Object>> items = this.getChartRepository().getItems(query.getSql());

        return items;
    }

    @Cache
    public List<Map<String, Object>> getItemsForSummary(String key) {
        Query query = this.getQueryRepository().findByKey(key);
        List<Map<String, Object>> items = this.getChartRepository().getItems(query.getSql());

        for (int i = 1; i < items.size(); i++) {
            for (Map.Entry<String, Object> data : items.get(i).entrySet()) {
                if (Number.class.isAssignableFrom(data.getValue().getClass())) {
                    data.setValue(((Number) data.getValue()).doubleValue() + ((Number) items.get(i - 1).get(data.getKey())).doubleValue());
                }
            }
        }

        return items;
    }

    @Cache
    public List<List<Object>> getItemsForSnowCover(String key, Date start, Date end) {
        Query query = this.getQueryRepository().findByKey(key);

        List<List<Object>> items = new ArrayList<List<Object>>();
        Date work = new Date(start.getTime());
        Calendar calendar = Calendar.getInstance();

        while (!work.after(end)) {
            items.add(this.getChartRepository().getItemsForSnowCover(query.getSql(), work));

            calendar.setTime(work);
            calendar.add(Calendar.MONTH, 1);
            work = calendar.getTime();
        }

        int maxSize = 0;

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).size() > maxSize) {
                maxSize = items.get(i).size();
            }
        }

        for (List<Object> item : items) {
            while (item.size() < maxSize) {
                item.add(1, 0);
            }
        }

        return items;
    }

    protected ChartRepository getChartRepository() {
        return chartRepository;
    }

    protected QueryRepository getQueryRepository() {
        return queryRepository;
    }
}
