package com.yo1000.vis.model.data;

/**
 * Created by yoichi.kikuchi on 15/06/03.
 */
public class Navigator {
    private String id;
    private String name;
    private String url;
    private String separator;
    private int columnIndex;
    private int rowIndex;

    public Navigator() {}

    public Navigator(String id, String name, String url, String separator, int columnIndex, int rowIndex) {
        this.setId(id);
        this.setName(name);
        this.setUrl(url);
        this.setSeparator(separator);
        this.setColumnIndex(columnIndex);
        this.setRowIndex(rowIndex);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }
}
