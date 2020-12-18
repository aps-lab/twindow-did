package org.adorsys.adssi.twindow.crypto;

import java.util.List;
import java.util.stream.Collectors;

import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.OctetKeyPair;

import org.adorsys.adssi.twindow.data.X25519KeyAgreementKey2019;

public class X25519KeyAgreementKey2019Service {

    public static final String KEY_TYPE = "X25519KeyAgreementKey2019";

    private static final Common25519Service x25519 = new Common25519Service(Curve.X25519);

    public static OctetKeyPair generateKeyPair(String keyID) {
        return x25519.genKeyPair(keyID);
    }

    public static String publicKeyBase58(OctetKeyPair publicJWK) {
        return x25519.publicKeyBase58(publicJWK);
    }

    public static OctetKeyPair publicKeyFromBase58(String publicKeyBase58, String keyID) {
        return x25519.publicKeyFromBase58(publicKeyBase58, keyID, KeyUse.ENCRYPTION);
    }

    public static X25519KeyAgreementKey2019 publicKeyEntry(OctetKeyPair keyPair, String controller) {
        String publicKeyBase58 = publicKeyBase58(keyPair);
        return new X25519KeyAgreementKey2019().id(keyPair.getKeyID()).publicKeyBase58(publicKeyBase58).type(KEY_TYPE)
                .controller(controller);
    }

    public static List<OctetKeyPair> keyPairs(int qty, String did) {
        return keyPairs(qty, 0, did);
    }

    public static List<OctetKeyPair> keyPairs(int qty, int startIndex, String did) {
        return x25519.keyPairs(qty, startIndex, did, "#key-X25519-");
    }

    public static List<X25519KeyAgreementKey2019> keyEntries(List<OctetKeyPair> keys, String controller) {
        return keys.stream().map(k -> publicKeyEntry(k, controller)).collect(Collectors.toList());
    }
}