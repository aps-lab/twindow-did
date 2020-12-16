package org.adorsys.adssi.twindow.crypto;

import java.util.List;
import java.util.stream.Collectors;

import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.OctetKeyPair;

import org.adorsys.adssi.twindow.data.Ed25519VerificationKey2018;

public class Ed25519VerificationKey2018Service {
    public static final String KEY_TYPE = "Ed25519VerificationKey2018";
    private static final Common25519Service ed25519 = new Common25519Service(Curve.Ed25519);

    public static OctetKeyPair generateKeyPair(String keyID) {
        return ed25519.genKeyPair(keyID);
    }

    public static String publicKeyBase58(OctetKeyPair publicJWK) {
        return ed25519.publicKeyBase58(publicJWK);
    }

    public static OctetKeyPair publicKeyFromBase58(String publicKeyBase58, String keyID) {
        return ed25519.publicKeyFromBase58(publicKeyBase58, keyID, KeyUse.SIGNATURE);
    }

    public static Ed25519VerificationKey2018 publicKeyEntry(OctetKeyPair keyPair, String controller) {
        String publicKeyBase58 = publicKeyBase58(keyPair);
        return new Ed25519VerificationKey2018().id(keyPair.getKeyID()).publicKeyBase58(publicKeyBase58).type(KEY_TYPE)
                .controller(controller);
    }

    public static List<OctetKeyPair> keyPairs(int qty, String did) {
        return ed25519.keyPairs(qty, did, "#key-Ed25519-");
    }

    public static List<Ed25519VerificationKey2018> keyEntries(List<OctetKeyPair> keys, String controller) {
        return keys.stream().map(k -> publicKeyEntry(k, controller)).collect(Collectors.toList());
    }
}