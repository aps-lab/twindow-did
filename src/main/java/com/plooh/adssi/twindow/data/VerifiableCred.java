package com.plooh.adssi.twindow.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class VerifiableCred {
    @JsonProperty("@context")
    private final List<String> context = Arrays.asList("https://www.w3.org/2018/credentials/v1",
            "https://secure-wallet.org/vc/v1");

    private String issuanceDate;
    private String issuer;
    private List<String> type;
    // A document might contain many signature entries. Among other, each document
    // must have only one signature entry with the proofPurpose=documentation. This
    // one will be used to produce the transprency logs.
    private List<JcsBase64Ed25519Signature2020> proof;
    private CredentialSubject credentialSubject;

    public JcsBase64Ed25519Signature2020 getLastProofOfType(String signatureType) {
        List<JcsBase64Ed25519Signature2020> list = this.proof.stream()
                .filter(p -> p.getProofPurpose().contains(signatureType)).collect(Collectors.toList());
        // Return last element.
        return list.get(list.size() - 1);
    }

    public VerifiableCred issuanceDate(String issuanceDate) {
        this.issuanceDate = issuanceDate;
        return this;
    }

    public VerifiableCred issuer(String issuer) {
        this.issuer = issuer;
        return this;
    }

    public VerifiableCred type(List<String> type) {
        this.type = type;
        return this;
    }

    public VerifiableCred proof(List<JcsBase64Ed25519Signature2020> proof) {
        this.proof = new ArrayList<>(proof);
        Collections.sort(this.proof);
        return this;
    }

    public VerifiableCred credentialSubject(CredentialSubject credentialSubject) {
        this.credentialSubject = credentialSubject;
        return this;
    }
}