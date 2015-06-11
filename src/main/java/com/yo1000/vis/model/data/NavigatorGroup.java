package com.yo1000.vis.model.data;

import java.util.List;

/**
 * Created by yoichi.kikuchi on 15/06/08.
 */
public class NavigatorGroup {
    private Navigator header;
    private List<Navigator> subitems;

    public NavigatorGroup() {}

    public NavigatorGroup(Navigator header, List<Navigator> subitems) {
        this.header = header;
        this.subitems = subitems;
    }

    public Navigator getHeader() {
        return header;
    }

    public void setHeader(Navigator header) {
        this.header = header;
    }

    public List<Navigator> getSubitems() {
        return subitems;
    }

    public void setSubitems(List<Navigator> subitems) {
        this.subitems = subitems;
    }
}
