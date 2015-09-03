package com.applica.applicaandroid.models;

public enum LoginResult {
    SUCCESS(0),
    FAIL(1),;
    
    private final int id;
    LoginResult(final int id) {
        this.id = id;
    }
    
    int id() {
        return id;
    }

}
