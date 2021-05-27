package com.plooh.adssi.twindow.data;

import lombok.Getter;

@Getter
public abstract class AbstractRecord extends CredentialSubject {
    // This is the count of records
    private int count;
    // This is the hash of those records
    private String hash;

    public AbstractRecord hash(String hash) {
        this.hash = hash;
        return this;
    }

    public AbstractRecord count(int count) {
        this.count = count;
        return this;
    }
}