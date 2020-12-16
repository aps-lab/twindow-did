package org.adorsys.adssi.twindow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nimbusds.jose.jwk.OctetKeyPair;

import org.adorsys.adssi.twindow.log.KeyStorage;

public class KeyStorageMock implements KeyStorage {
    private final Map<String, OctetKeyPair> keyPairs = new HashMap<>();
    private String did;

    @Override
    public OctetKeyPair get(String keyId) {
        return keyPairs.get(keyId);
    }

    @Override
    public void update(List<OctetKeyPair> keys) {
        for (OctetKeyPair kp : keys) {
            keyPairs.put(kp.getKeyID(), kp);
        }
    }

    @Override
    public String getDid() {
        return did;
    }

    @Override
    public void setDid(String did) {
        this.did = did;
    }
}