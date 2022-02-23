package com.blockchain.fantom;

import java.time.Duration;

public class ResponseTransfer<T> {

    public ResponseTransfer() {
    }

    private Duration performance;
    private String message;
    private T content;

    public Duration getPerformance() {
        return performance;
    }

    public void setPerformance(Duration performance) {
        this.performance = performance;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}
