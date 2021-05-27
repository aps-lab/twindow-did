package com.plooh.adssi.twindow.data;

import lombok.Getter;

@Getter
public abstract class CredentialSubject {
    private String id;
    private String type;

    public CredentialSubject id(String id) {
        this.id = id;
        return this;
    }

    public CredentialSubject type(String type) {
        this.type = type;
        return this;
    }
}