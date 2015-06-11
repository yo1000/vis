package com.yo1000.vis.model.data;

import java.util.Date;

/**
 * Created by yoichi.kikuchi on 15/06/08.
 */
public class Query {
    private String id;
    private String key;
    private String name;
    private String sql;
    private String view;
    private String author;
    private Date modified;

    public Query() {}

    public Query(String id, String key, String name, String sql, String view, String author, Date modified) {
        this.setId(id);
        this.setKey(key);
        this.setName(name);
        this.setSql(sql);
        this.setView(view);
        this.setAuthor(author);
        this.setModified(modified);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }
}
