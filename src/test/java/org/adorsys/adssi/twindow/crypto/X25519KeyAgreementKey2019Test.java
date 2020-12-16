package org.adorsys.adssi.twindow.crypto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.X25519Decrypter;
import com.nimbusds.jose.crypto.X25519Encrypter;
import com.nimbusds.jose.jwk.OctetKeyPair;
import com.nimbusds.jose.util.IOUtils;

import org.junit.jupiter.api.Test;

public class X25519KeyAgreementKey2019Test {
    @Test
    public void testEncryptDecrypt() throws IOException, JOSEException, ParseException {
        String keyID = "sampleEncryptionKey";
        OctetKeyPair keyPair = X25519KeyAgreementKey2019Service.generateKeyPair(keyID);
        InputStream is = this.getClass().getResourceAsStream("/did001.json");
        String data = IOUtils.readInputStreamToString(is, StandardCharsets.UTF_8);
        JWEHeader header = new JWEHeader.Builder(JWEAlgorithm.ECDH_ES, EncryptionMethod.A128CBC_HS256)
                .keyID(keyPair.getKeyID()).build();
        JWEObject jwe = new JWEObject(header, new Payload(data));
        OctetKeyPair publicJWK = keyPair.toPublicJWK();
        jwe.encrypt(new X25519Encrypter(publicJWK));
        String jweString = jwe.serialize();

        JWEObject jwe2 = JWEObject.parse(jweString);
        jwe2.decrypt(new X25519Decrypter(keyPair));
        String decrypted = jwe2.getPayload().toString();
        assertEquals(data, decrypted);
    }

}