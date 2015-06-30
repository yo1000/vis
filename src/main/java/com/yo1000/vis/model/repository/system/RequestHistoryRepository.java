package com.yo1000.vis.model.repository.system;

import com.yo1000.vis.model.data.RequestHistory;

import java.util.List;

/**
 * Created by yoichi.kikuchi on 15/06/08.
 */
public interface RequestHistoryRepository {
    public List<RequestHistory> find();
    public int insert(String url);
    public boolean exists(String url);
}
