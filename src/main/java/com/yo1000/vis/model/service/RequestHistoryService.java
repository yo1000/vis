package com.yo1000.vis.model.service;

import com.yo1000.vis.model.data.RequestHistory;
import com.yo1000.vis.model.repository.system.RequestHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yoichi.kikuchi on 15/06/08.
 */
@Service
public class RequestHistoryService {
    @Autowired
    private RequestHistoryRepository requestHistoryRepository;

    public void setHistory(String url) {
        if (!this.getRequestHistoryRepository().exists(url)) {
            this.getRequestHistoryRepository().insert(url);
        }
    }

    public List<RequestHistory> getHistories() {
        return this.getRequestHistoryRepository().find();
    }

    protected RequestHistoryRepository getRequestHistoryRepository() {
        return requestHistoryRepository;
    }
}
