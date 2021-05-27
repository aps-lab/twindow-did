package com.plooh.adssi.twindow.core;

import java.util.List;

import com.nimbusds.jose.jwk.OctetKeyPair;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import com.plooh.adssi.twindow.data.JcsBase64Ed25519Signature2020;

@RequiredArgsConstructor
@Getter
@Setter
public class SignatureEntryHolder {
    private JcsBase64Ed25519Signature2020 signatureEntry;
    private String signatureValue;
    private final OctetKeyPair keyPair;
    private final List<String> purposes;
}