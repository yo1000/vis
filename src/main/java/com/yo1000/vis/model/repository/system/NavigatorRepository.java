package com.yo1000.vis.model.repository.system;

import com.yo1000.vis.model.data.Navigator;

import java.util.List;

/**
 * Created by yoichi.kikuchi on 15/06/02.
 */
public interface NavigatorRepository {
    public int insert(Navigator navigator);
    public int updateById(Navigator navigator);
    public int updateByColumnRow(Navigator navigator);
    public Navigator findByColumnRow(int columnIndex, int rowIndex);
    public List<Navigator> findHeaders();
    public List<Navigator> findSubitemsByColumnIndex(int columnIndex);
}
