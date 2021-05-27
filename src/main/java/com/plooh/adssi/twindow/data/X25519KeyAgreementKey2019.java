package com.plooh.adssi.twindow.data;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class X25519KeyAgreementKey2019 {
    private String id;
    private String publicKeyBase58;
    private String type;
    private String controller;

    public X25519KeyAgreementKey2019 id(String id) {
        this.id = id;
        return this;
    }

    public X25519KeyAgreementKey2019 publicKeyBase58(String publicKeyBase58) {
        this.publicKeyBase58 = publicKeyBase58;
        return this;
    }

    public X25519KeyAgreementKey2019 type(String type) {
        this.type = type;
        return this;
    }

    public X25519KeyAgreementKey2019 controller(String controller) {
        this.controller = controller;
        return this;
    }
}