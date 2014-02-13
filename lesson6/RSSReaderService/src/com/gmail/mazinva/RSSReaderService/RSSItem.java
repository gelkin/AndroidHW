package com.gmail.mazinva.RSSReaderService;

import java.io.Serializable;

public class RSSItem implements Serializable {
    public String title;
    public String description;

    public int key = 0;

    public RSSItem() {}

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
}
