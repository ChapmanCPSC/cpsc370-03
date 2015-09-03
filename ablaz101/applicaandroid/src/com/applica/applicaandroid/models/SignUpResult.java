package com.applica.applicaandroid.models;

public enum SignUpResult {
    SUCCESS(0),
    BAD_EMAIL(1),
    BAD_PASSWORD(2),
    EXISTING_EMAIL(3),
    FAIL(4),;
    
    private final int id;
    SignUpResult(final int id) {
        this.id = id;
    }
    
    int id() {
        return id;
    }
    
}
