package com.plooh.adssi.twindow.log;

import java.util.List;

import com.nimbusds.jose.jwk.OctetKeyPair;

public interface KeyStorage {

    OctetKeyPair get(String keyId);

    void update(List<OctetKeyPair> keyPairs);

    String getDid();

    void setDid(String did);
}