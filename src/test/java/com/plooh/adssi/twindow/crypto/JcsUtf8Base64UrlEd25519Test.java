package com.plooh.adssi.twindow.crypto;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import com.nimbusds.jose.jwk.OctetKeyPair;
import com.nimbusds.jose.util.IOUtils;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

public class JcsUtf8Base64UrlEd25519Test {
    @Test
    public void testSignVerify() throws IOException {
        String keyID = "sampleKey";
        OctetKeyPair keyPair = Ed25519VerificationKey2018Service.generateKeyPair(keyID);
        InputStream is = this.getClass().getResourceAsStream("/did001.json");
        String data = IOUtils.readInputStreamToString(is, StandardCharsets.UTF_8);
        String jws = JcsBase64Ed25519Signature2020Service.sign(data, keyPair);
        String signatureBase64Url = StringUtils.substringAfterLast(jws, ".");

        String publicKeyBase58 = Ed25519VerificationKey2018Service.publicKeyBase58(keyPair);

        // Verify
        OctetKeyPair publicJWK = Ed25519VerificationKey2018Service.publicKeyFromBase58(publicKeyBase58, keyID);
        boolean verified = JcsBase64Ed25519Signature2020Service.verify(data, publicJWK, signatureBase64Url);
        assertTrue(verified);
    }
}