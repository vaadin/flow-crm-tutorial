package com.example.application.data.entity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;

@Entity
public class PushSubscription extends AbstractEntity {

    @NotEmpty
    private String userName;
    @NotEmpty
    private String endpoint;
    @NotEmpty
    private String p256dh;
    @NotEmpty
    private String auth;

    public PushSubscription() {

    }

    public PushSubscription(String user, String endpoint, String p256dh, String auth) {
        this.userName = user;
        this.endpoint = endpoint;
        this.p256dh = p256dh;
        this.auth = auth;
    }

    public String getUserName() {
        return userName;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getP256dh() {
        return p256dh;
    }

    public String getAuth() {
        return auth;
    }
}
