package com.yo1000.vis.component.scheduler;

import com.yo1000.vis.model.data.RequestHistory;
import com.yo1000.vis.model.service.RequestHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Created by yoichi.kikuchi on 2015/06/26.
 */
@Component
public class TaskScheduler {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskScheduler.class);

    @Autowired
    private RequestHistoryService requestHistoryService;

    @Autowired
    private RestTemplate restTemplate;

    @Scheduled(cron = "0 */20 8-23 * * 1-5")
    public void renewCache() {
        for (RequestHistory history : this.getRequestHistoryService().getHistories()) {
            try {
                this.getRestTemplate().getForObject(history.getUrl(), Void.class);
            }
            catch (Exception e) {
                LOGGER.warn(e.getMessage(), e);
            }
        }
    }

    protected RequestHistoryService getRequestHistoryService() {
        return requestHistoryService;
    }

    protected RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
