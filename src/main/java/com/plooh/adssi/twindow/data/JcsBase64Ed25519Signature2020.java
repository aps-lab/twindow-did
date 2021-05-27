package com.plooh.adssi.twindow.data;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class JcsBase64Ed25519Signature2020 implements Comparable<JcsBase64Ed25519Signature2020> {
    private String issuer;
    private String created;
    private List<String> proofPurpose;
    private String type;
    private String verificationMethod;
    private TwindowEntry twindow;
    private String signatureValue;
    private String nonce;

    public JcsBase64Ed25519Signature2020 issuer(String issuer) {
        this.issuer = issuer;
        return this;
    }

    public JcsBase64Ed25519Signature2020 created(String created) {
        this.created = created;
        return this;
    }

    public JcsBase64Ed25519Signature2020 proofPurpose(List<String> proofPurpose) {
        this.proofPurpose = proofPurpose;
        return this;
    }

    public JcsBase64Ed25519Signature2020 type(String type) {
        this.type = type;
        return this;
    }

    public JcsBase64Ed25519Signature2020 verificationMethod(String verificationMethod) {
        this.verificationMethod = verificationMethod;
        return this;
    }

    public JcsBase64Ed25519Signature2020 twindow(TwindowEntry twindow) {
        this.twindow = twindow;
        return this;
    }

    public JcsBase64Ed25519Signature2020 signatureValue(String signatureValue) {
        this.signatureValue = signatureValue;
        return this;
    }

    public JcsBase64Ed25519Signature2020 nonce(String nonce) {
        this.nonce = nonce;
        return this;
    }

    @Override
    public int compareTo(JcsBase64Ed25519Signature2020 o) {
        return created.compareTo(o.created);
    }
}