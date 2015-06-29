package com.yo1000.vis.component.scheduler;

import com.yo1000.vis.model.data.Query;
import com.yo1000.vis.model.service.ChartService;
import com.yo1000.vis.model.service.QueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by yoichi.kikuchi on 2015/06/26.
 */
@Component
public class TaskScheduler {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskScheduler.class);

    @Autowired
    private ChartService chartService;

    @Autowired
    private QueryService queryService;

    @Scheduled(cron = "0 */20 8-23 * * 1-5")
    public void renewCache() {
        for (Query query : this.getQueryService().getQueries()) {
            try {
                this.runQuery(query);
            }
            catch (Exception e) {
                LOGGER.warn(e.getMessage(), e);
            }
        }
    }

    protected void runQuery(Query query) {
        if (query.getView().equals("snowcover")) {
            this.getChartService().getItemsForSnowCover(query.getKey(), null, null);
            return;
        }

        this.getChartService().getItems(query.getKey());
        this.getChartService().getItemsForSummary(query.getKey());
    }

    protected ChartService getChartService() {
        return chartService;
    }

    protected QueryService getQueryService() {
        return queryService;
    }
}
