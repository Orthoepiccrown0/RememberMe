package com.armor.rememberme;


public class Note {
    private String id;
    private String content;
    private String header;
    private String content_color;
    private String header_color;
    private String back_color;

    public Note(String id, String content, String header, String content_color, String header_color, String back_color) {
        this.id = id;
        this.content = content;
        this.header = header;
        this.content_color = content_color;
        this.header_color = header_color;
        this.back_color = back_color;
    }

    public String getnId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getHeader() {
        return header;
    }

    public String getContent_color() {
        return content_color;
    }

    public String getHeader_color() {
        return header_color;
    }

    public String getBack_color() {
        return back_color;
    }
}
