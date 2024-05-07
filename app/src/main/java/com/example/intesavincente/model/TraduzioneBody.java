package com.example.intesavincente.model;

public class TraduzioneBody {
    private String from;
    private String to;
    private String data;
    private String platform;

    public TraduzioneBody(String from, String to, String data, String platform) {
        this.from = from;
        this.to = to;
        this.data = data;
        this.platform = platform;
    }
    public TraduzioneBody(){

    }
    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @Override
    public String toString() {
        return
                "from:" + from +
                        " to:" + to +
                        " data: " + data +
                        " platform: " + platform;
    }
}
