package com.applica.applicaandroid.models;

public enum LoginSignUpFormType {
    SIGN_UP(0),
    LOGIN(1),;
    
    private final int id;
    LoginSignUpFormType(final int id) {
        this.id = id;
    }
    
    int id() {
        return id;
    }

}
