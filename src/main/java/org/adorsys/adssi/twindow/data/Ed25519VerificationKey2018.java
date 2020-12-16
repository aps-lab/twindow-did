package org.adorsys.adssi.twindow.data;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Ed25519VerificationKey2018 {
    private String id;
    private String publicKeyBase58;
    private String type;
    private String controller;

    public Ed25519VerificationKey2018 id(String id) {
        this.id = id;
        return this;
    }

    public Ed25519VerificationKey2018 publicKeyBase58(String publicKeyBase58) {
        this.publicKeyBase58 = publicKeyBase58;
        return this;
    }

    public Ed25519VerificationKey2018 type(String type) {
        this.type = type;
        return this;
    }

    public Ed25519VerificationKey2018 controller(String controller) {
        this.controller = controller;
        return this;
    }
}