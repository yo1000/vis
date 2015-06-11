package com.yo1000.vis.model.service;

import com.yo1000.vis.model.data.Query;
import com.yo1000.vis.model.repository.system.QueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yoichi.kikuchi on 15/06/08.
 */
@Service
public class QueryService {
    @Autowired
    private QueryRepository queryRepository;

    public void setQuery(Query query) {
        if (query.getId() == null || query.getId().isEmpty()) {
            this.getQueryRepository().insert(query);
            return;
        }

        this.getQueryRepository().update(query);
    }

    public Query getQuery(String id) {
        return this.getQueryRepository().find(id);
    }

    public Query getQueryByKey(String key) {
        return this.getQueryRepository().findByKey(key);
    }

    public List<Query> getQueries() {
        return this.getQueryRepository().find();
    }

    protected QueryRepository getQueryRepository() {
        return queryRepository;
    }
}
