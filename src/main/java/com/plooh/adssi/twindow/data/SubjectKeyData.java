package com.plooh.adssi.twindow.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nimbusds.jose.jwk.OctetKeyPair;

import com.plooh.adssi.twindow.crypto.Ed25519VerificationKey2018Service;

public class SubjectKeyData {

    private final Map<String, OctetKeyPair> keyPairs = new HashMap<>();
    private final Map<String, Ed25519VerificationKey2018> publicKeys = new HashMap<>();
    private final String did;

    public SubjectKeyData(List<OctetKeyPair> keys, String did) {
        this.did = did;
        for (OctetKeyPair kp : keys) {
            keyPairs.put(kp.getKeyID(), kp);
            publicKeys.put(kp.getKeyID(), Ed25519VerificationKey2018Service.publicKeyEntry(kp, did));
        }
    }

    public OctetKeyPair getOctetKeyPairPair(String keyId) {
        return keyPairs.get(keyId);
    }

    public Ed25519VerificationKey2018 getPublicKeyEntry(String keyId) {
        return publicKeys.get(keyId);
    }

    public List<Ed25519VerificationKey2018> publicKeys() {
        return new ArrayList<>(publicKeys.values());
    }

    public String getDid() {
        return this.did;
    }
}
