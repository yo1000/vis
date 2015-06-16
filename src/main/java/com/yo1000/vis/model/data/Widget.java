package com.yo1000.vis.model.data;

/**
 * Created by yoichi.kikuchi on 15/06/12.
 */
public class Widget {
    private String id;
    private String type;
    private int position;
    private String queryId;

    public Widget() {}

    public Widget(String id, String type, int position, String queryId) {
        this.setId(id);
        this.setType(type);
        this.setPosition(position);
        this.setQueryId(queryId);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }
}
