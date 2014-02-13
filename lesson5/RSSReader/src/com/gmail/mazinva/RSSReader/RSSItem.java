package com.gmail.mazinva.RSSReader;

import java.io.Serializable;

public class RSSItem implements Serializable {
    private String title;
    private String description;

    private int key = 0;

    public RSSItem() {}

    public void setKey(int value) {
        key = value;
    }

    public void set(String value) {
        switch (key) {
            case 1:
                title = value;
                break;
            case 2:
                description = value;
                break;
            default:
        }
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

}
