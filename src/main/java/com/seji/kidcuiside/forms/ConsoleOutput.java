package com.seji.kidcuiside.forms;

public class ConsoleOutput {
    private Boolean eof;
    private String output;
    private String error;

    public Boolean getEof() {
        return eof;
    }

    public void setEof(Boolean eof) {
        this.eof = eof;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "ConsoleOutput{" +
                "eof=" + eof +
                ", output='" + output + '\'' +
                ", error='" + error + '\'' +
                '}';
    }
}
