package com.plooh.adssi.twindow.crypto;

import com.plooh.adssi.twindow.utils.JCSUtils;
import java.io.IOException;
import java.text.ParseException;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.Ed25519Signer;
import com.nimbusds.jose.crypto.Ed25519Verifier;
import com.nimbusds.jose.jwk.OctetKeyPair;
import com.nimbusds.jose.util.Base64URL;

public class JcsBase64Ed25519Signature2020Service {

    // private static final JWSHeader JWS_HEADER = new
    // JWSHeader.Builder(JWSAlgorithm.EdDSA).build();
    // private static Base64URL JWS_HEADER_BASE64_URL = JWS_HEADER.toBase64URL();
    public static final String SIGNATURE_TYPE = "JcsBase64Ed25519Signature2020";

    public static String sign(String data, OctetKeyPair keyPair) {
        return sign(data, keyPair, true);
    }

    public static boolean verify(String data, OctetKeyPair publicJWK, String signatureBase64Url) {
        try {
            return verifyInternal(data, publicJWK, signatureBase64Url);
        } catch (JOSEException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String verify(String data, OctetKeyPair publicJWK) {
        try {
            return verifyInternal(data, publicJWK);
        } catch (JOSEException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String sign(String data, OctetKeyPair keyPair, boolean detachedPayload) {
        try {
            return signInternal(data, keyPair, detachedPayload);
        } catch (IOException | JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    private static String signInternal(String data, OctetKeyPair keyPair, boolean detachedPayload)
            throws IOException, JOSEException {
        // header payload
        JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.EdDSA).keyID(keyPair.getKeyID()).build();
        JWSObject jwsObject = new JWSObject(jwsHeader, new Payload(jcs_utf8_base64url(data)));

        // Ed25519 signature
        jwsObject.sign(new Ed25519Signer(keyPair));
        return jwsObject.serialize(detachedPayload);
    }

    private static boolean verifyInternal(String data, OctetKeyPair publicJWK, String signatureBe64URL)
            throws JOSEException, ParseException {

        JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.EdDSA).keyID(publicJWK.getKeyID()).build();
        JWSObject jwsObject = new JWSObject(jwsHeader.toBase64URL(), jcs_utf8_base64url(data),
                Base64URL.from(signatureBe64URL));
        return jwsObject.verify(new Ed25519Verifier(publicJWK));
    }

    private static String verifyInternal(String jws, OctetKeyPair publicJWK) throws JOSEException, ParseException {

        JWSObject jwsObject = JWSObject.parse(jws);
        if (jwsObject.verify(new Ed25519Verifier(publicJWK))) {
            String payLoadString = jwsObject.getPayload().toJSONObject().toString();
            return payLoadString;
        } else {
            throw new RuntimeException("Can not verify data");
        }
    }

    private static Base64URL jcs_utf8_base64url(final String input) {
        // Cannonicalize
        String canonicalString = JCSUtils.encode(input);

        // Assumes String is utf8 meanscanonicalString.getBytes(StandardCharset.UTF_8)
        Base64URL jcs_utf8_base64url = Base64URL.encode(canonicalString);

        return jcs_utf8_base64url;
    }
}