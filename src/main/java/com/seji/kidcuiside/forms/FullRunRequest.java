package com.seji.kidcuiside.forms;

public class FullRunRequest {
    private String sessionId;
    private String name;
    private String path;
    private String language;
    private String code;

    public FullRunRequest(){}

    public FullRunRequest(PreRunRequest p) {
        name = p.getName();
        path = p.getPath();
        language = p.getLanguage();
        code = p.getCode();
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "FullRunRequest{" +
                "sessionId='" + sessionId + '\'' +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", language='" + language + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}