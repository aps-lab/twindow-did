package com.plooh.adssi.twindow.data;

import lombok.Getter;

@Getter
public class DiDSubject extends CredentialSubject {
    private DiD did;
    private String udf;

    public DiDSubject() {
        type(RecordType.DIDDocument.name());
    }

    public DiDSubject did(DiD did) {
        this.did = did;
        return this;
    }

    public CredentialSubject udf(String udf) {
        this.udf = udf;
        return this;
    }
}