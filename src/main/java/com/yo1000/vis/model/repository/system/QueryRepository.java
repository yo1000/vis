package com.yo1000.vis.model.repository.system;

import com.yo1000.vis.model.data.Query;

import java.util.List;

/**
 * Created by yoichi.kikuchi on 15/06/08.
 */
public interface QueryRepository {
    public List<Query> find();
    public Query find(String id);
    public Query findByKey(String key);
    public int insert(Query query);
    public int update(Query query);
}
