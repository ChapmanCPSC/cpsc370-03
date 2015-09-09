package com.alecrichter.textalarm;

public class Alarm {

    private String id, title, type, contentO, contentF, activated, toneUri;

    Alarm(String id, String title, String type, String contentO, String contentF,
          String activated, String toneUri) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.contentO = contentO;
        this.contentF = contentF;
        this.activated = activated;
        this.toneUri = toneUri;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getContentO() {
        return contentO;
    }

    public String getContentF() {
        return contentF;
    }

    public String getActivated() {
        return activated;
    }

    public String getUri() {
        return toneUri;
    }

    public void toggleAlarm(String value) {
        activated = value;
    }

    public void setUri(String uri) {
        toneUri = uri;
    }

}
