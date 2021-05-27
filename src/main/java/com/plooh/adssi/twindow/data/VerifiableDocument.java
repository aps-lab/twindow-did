package com.plooh.adssi.twindow.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class VerifiableDocument {
    String vc;
    String signatureValue;

    public VerifiableDocument vc(String vc) {
        this.vc = vc;
        return this;
    }

    public VerifiableDocument signatureValue(String signatureValue) {
        this.signatureValue = signatureValue;
        return this;
    }
}