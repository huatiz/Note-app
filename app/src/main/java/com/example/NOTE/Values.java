package com.example.NOTE;

public class Values {
    private Integer id;
    private String content;
    private String time;
    private String day;

    public Integer getId() { return id; }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public String getDay() { return day; }

    public void setId(Integer id) { this.id = id; }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTime(String time) { this.time = time; }

    public void setDay(String day) { this.day = day; }

    @Override
    public String toString() {
        return "Values{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", day='" + day + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}