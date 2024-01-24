package dev.com.kupreychik.model;

public class ErrorMessage {
    private String reason;

    public ErrorMessage(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}