package com.yo1000.vis.model.service;

import com.yo1000.vis.model.data.Navigator;
import com.yo1000.vis.model.data.NavigatorGroup;
import com.yo1000.vis.model.repository.system.NavigatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoichi.kikuchi on 15/06/03.
 */
@Service
public class NavigatorService {
    @Autowired
    private NavigatorRepository navigatorRepository;

    private List<NavigatorGroup> cache;

    private List<NavigatorGroup> trimmedCache;

    public void setNavigator(Navigator navigator) {
        Navigator nav = this.getNavigatorRepository().findByColumnRow(
                navigator.getColumnIndex(), navigator.getRowIndex());

        if (nav == null) {
            this.getNavigatorRepository().insert(navigator);
        }
        else {
            this.getNavigatorRepository().updateById(navigator);
        }

        this.clearCache();
    }

    public List<NavigatorGroup> getTrimmedNavigatorGroups() {
        if (this.getTrimmedCache() != null && !this.getTrimmedCache().isEmpty()) {
            return this.getTrimmedCache();
        }

        List<NavigatorGroup> navGroups = new ArrayList<NavigatorGroup>();
        List<Navigator> navHeads = this.getNavigatorRepository().findHeaders();

        for (Navigator navHead : navHeads) {
            navGroups.add(new NavigatorGroup(navHead, this.getNavigatorRepository()
                    .findSubitemsByColumnIndex(navHead.getColumnIndex())));
        }

        this.setTrimmedCache(navGroups);
        return navGroups;
    }

    public List<NavigatorGroup> getNavigatorGroups() {
        if (this.getCache() != null && !this.getCache().isEmpty()) {
            return this.getCache();
        }

        List<NavigatorGroup> navGroups = new ArrayList<NavigatorGroup>();

        final int beginColIndex = 0;
        final int endColIndex = 6;

        List<Navigator> navHeads = this.getNavigatorRepository().findHeaders();
        int navHeadsNextCol = (navHeads.size() > 0) ? navHeads.get(0).getColumnIndex() : 0;
        int navHeadsIndex = 0;

        for (int i = beginColIndex; i < endColIndex; i++) {
            Navigator navHead = null;

            if (i < navHeadsNextCol || navHeadsNextCol < 0 || navHeads.size() <= 0
                    || navHead == navHeads.get(navHeadsIndex)) {
                navHead = new Navigator("", "(empty)", "", "0", i, 0);
            }
            else {
                navHeadsNextCol = navHeadsIndex + 1 < navHeads.size()
                        ? navHeads.get(navHeadsIndex).getColumnIndex() : -1;
                navHead = navHeads.get(navHeadsIndex);
                navHeadsIndex++;
            }

            navGroups.add(new NavigatorGroup(navHead, this.getNavigatorRows(i)));
        }

        this.setCache(navGroups);
        return navGroups;
    }

    protected List<Navigator> getNavigatorRows(int columnIndex) {
        List<Navigator> items = this.getNavigatorRepository()
                    .findSubitemsByColumnIndex(columnIndex);

        int beginRowIndex = 0;
        int endRowIndex = 10;

        List<Navigator> navItems = new ArrayList<Navigator>(items);

        for (Navigator item : items) {
            for (int j = beginRowIndex; j < item.getRowIndex(); j++) {
                navItems.add(j, new Navigator("", "(empty)", "", "0",  columnIndex, j));
            }
            beginRowIndex = item.getRowIndex() + 1;
        }

        for (int j = navItems.size(); j < endRowIndex; j++) {
            navItems.add(j, new Navigator("", "(empty)", "", "0", columnIndex, j));
        }

        navItems.remove(0);
        return navItems;
    }

    protected void clearCache() {
        this.getCache().clear();
        this.getTrimmedCache().clear();
    }

    protected NavigatorRepository getNavigatorRepository() {
        return navigatorRepository;
    }

    protected List<NavigatorGroup> getCache() {
        return cache;
    }

    protected void setCache(List<NavigatorGroup> cache) {
        this.cache = cache;
    }

    protected List<NavigatorGroup> getTrimmedCache() {
        return trimmedCache;
    }

    protected void setTrimmedCache(List<NavigatorGroup> trimmedCache) {
        this.trimmedCache = trimmedCache;
    }
}
