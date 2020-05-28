package com.gsh.system.domain;

import java.util.List;

/**
 * 结构视图节点
 */
public class DeptUserNode {
    private String id;
    private String name;
    private String open;
    private String html;
    private List<DeptUserNode> child;

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

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public List<DeptUserNode> getChild() {
        return child;
    }

    public void setChild(List<DeptUserNode> child) {
        this.child = child;
    }

    public DeptUserNode(String id, String name, String open, String html, List<DeptUserNode> child) {
        this.id = id;
        this.name = name;
        this.open = open;
        this.html = html;
        this.child = child;
    }

    public DeptUserNode() {
    }
}
