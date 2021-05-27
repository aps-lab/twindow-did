package com.plooh.adssi.twindow.core;

import com.plooh.adssi.twindow.crypto.Ed25519VerificationKey2018Service;
import com.plooh.adssi.twindow.crypto.JcsBase64Ed25519Signature2020Service;
import com.plooh.adssi.twindow.utils.JsonPathUtils;
import com.plooh.adssi.twindow.utils.JsonUtils;
import com.plooh.adssi.twindow.utils.TwindowUtils;
import com.plooh.adssi.twindow.utils.UDFUtils;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.TypeRef;
import com.nimbusds.jose.jwk.OctetKeyPair;

import com.plooh.adssi.twindow.data.CredentialSubject;
import com.plooh.adssi.twindow.data.DiD;
import com.plooh.adssi.twindow.data.DiDSubject;
import com.plooh.adssi.twindow.data.Ed25519VerificationKey2018;
import com.plooh.adssi.twindow.data.JcsBase64Ed25519Signature2020;
import com.plooh.adssi.twindow.data.ProofPurpose;
import com.plooh.adssi.twindow.data.RecordType;
import com.plooh.adssi.twindow.data.ServiceEntry;
import com.plooh.adssi.twindow.data.ServiceType;
import com.plooh.adssi.twindow.data.VerifiableCred;
import com.plooh.adssi.twindow.data.X25519KeyAgreementKey2019;
import org.apache.commons.lang3.StringUtils;

public class DIDDocument {
        private static TypeRef<List<Ed25519VerificationKey2018>> publicKeyList = new TypeRef<List<Ed25519VerificationKey2018>>() {
        };
        private static TypeRef<List<X25519KeyAgreementKey2019>> encryptionKeyList = new TypeRef<List<X25519KeyAgreementKey2019>>() {
        };

        public static String make(Instant now, String subjectDid, List<Object> authentication,
                        List<Object> assertionMethod, List<X25519KeyAgreementKey2019> keyAgreement,
                        List<String> serviceEndpoints, String antecedantHash, String antecedentClosing,
                        List<SignatureEntryHolder> signatureEntryHolders, String issuerDid) {

                VerifiableCred vc = vc(RecordType.DIDDocument.name(), now, subjectDid, authentication, assertionMethod,
                                keyAgreement, serviceEndpoints, antecedantHash, antecedentClosing,
                                signatureEntryHolders, issuerDid);
                try {
                        String signatureInputString = JsonUtils.MAPPER.writeValueAsString(vc);
                        sign(signatureInputString, signatureEntryHolders);
                        return JsonUtils.MAPPER.writeValueAsString(vc);
                } catch (IOException ioe) {
                        throw new RuntimeException(ioe);
                }
        }

        public static String addProviderSignature(Instant now, String selfSignedRcord, String antecedantHash,
                        String antecedentClosing, List<SignatureEntryHolder> signatureEntryHolders, String issuerDid) {
                DocumentContext doc = JsonPathUtils.parse(selfSignedRcord);
                DiDSubject diDSubject = JsonPathUtils.diDSubject(doc);
                DiD did = diDSubject.getDid();
                String udf;
                try {
                        udf = udf(did);
                } catch (IOException e) {
                        throw new RuntimeException(e);
                }
                if (!udf.equals(diDSubject.getUdf())) {
                        throw new RuntimeException("Computed udf notmatching the one stored in document.");

                }
                List<JcsBase64Ed25519Signature2020> signatureEntryies = prepareProof(now, antecedantHash,
                                antecedentClosing, signatureEntryHolders, issuerDid);

                for (JcsBase64Ed25519Signature2020 signature2020 : signatureEntryies) {
                        doc = doc.add("$.proof", signature2020);
                }
                String jsonString = doc.jsonString();
                sign(jsonString, signatureEntryHolders);
                return doc.jsonString();
        }

        public static void sign(String signatureInputString, List<SignatureEntryHolder> signatureEntryHolders) {
                // signatureInputString must be the samefor all
                for (SignatureEntryHolder signatureEntryHolder : signatureEntryHolders) {
                        String jws = JcsBase64Ed25519Signature2020Service.sign(signatureInputString,
                                        signatureEntryHolder.getKeyPair());
                        String signatureValue = StringUtils.substringAfterLast(jws, ".");
                        signatureEntryHolder.getSignatureEntry().signatureValue(signatureValue);
                }
        }

        private static VerifiableCred vc(String didType, Instant now, String subjectDid, List<Object> authentication,
                        List<Object> assertionMethod, List<X25519KeyAgreementKey2019> keyAgreement,
                        List<String> serviceEndpoints, String antecedantHash, String antecedentClosing,
                        List<SignatureEntryHolder> signatureEntryHolders, String issuerDid) {
                try {
                        List<ServiceEntry> serviceEntries = makeServiceEntries(subjectDid, serviceEndpoints);
                        DiD did = new DiD().id(subjectDid).assertionMethod(assertionMethod).keyAgreement(keyAgreement)
                                        .authentication(authentication).service(serviceEntries);
                        String udf = udf(did);
                        CredentialSubject didSubject = new DiDSubject().did(did).udf(udf).id(subjectDid);

                        return vc(didType, now, didSubject, antecedantHash, antecedentClosing, signatureEntryHolders,
                                        issuerDid);

                } catch (IOException e) {
                        throw new RuntimeException(e);
                }
        }

        private static String udf(DiD did) throws JsonProcessingException, IOException {
                String didDoc = JsonUtils.MAPPER.writeValueAsString(did);
                return UDFUtils.udf(didDoc);
        }

        public static VerifiableCred vc(String didType, Instant now, CredentialSubject didSubject,
                        String antecedantHash, String antecedentClosing,
                        final List<SignatureEntryHolder> signatureEntryHolders, String issuerDid) {
                List<JcsBase64Ed25519Signature2020> signatureEntryies = prepareProof(now, antecedantHash,
                                antecedentClosing, signatureEntryHolders, issuerDid);

                return new VerifiableCred().credentialSubject(didSubject).issuanceDate(TwindowUtils.DTF.format(now))
                                .issuer(issuerDid).type(Arrays.asList(RecordType.VerifiableCredential.name(), didType))
                                .proof(signatureEntryies);
        }

        private static List<JcsBase64Ed25519Signature2020> prepareProof(Instant now, String antecedantHash,
                        String antecedentClosing, final List<SignatureEntryHolder> signatureEntryHolders,
                        String issuerDid) {
                List<JcsBase64Ed25519Signature2020> signatureEntryies = new ArrayList<>();
                for (SignatureEntryHolder holder : signatureEntryHolders) {
                        JcsBase64Ed25519Signature2020 signatureEntry = new JcsBase64Ed25519Signature2020()
                                        .created(TwindowUtils.DTF.format(now)).nonce(UUID.randomUUID().toString())
                                        .type(JcsBase64Ed25519Signature2020Service.SIGNATURE_TYPE).issuer(issuerDid)
                                        .verificationMethod(holder.getKeyPair().getKeyID())
                                        .proofPurpose(holder.getPurposes());
                        if (holder.getPurposes().contains(ProofPurpose.documentation.name())) {
                                signatureEntry.twindow(TwindowUtils.twindow(now, antecedantHash, antecedentClosing));
                        }
                        signatureEntryies.add(signatureEntry);
                        holder.setSignatureEntry(signatureEntry);
                }
                return signatureEntryies;
        }

        private static List<ServiceEntry> makeServiceEntries(String subjectDid, List<String> serviceEndpoints) {
                List<ServiceEntry> serviceEntries = new ArrayList<>();
                for (int i = 0; i < serviceEndpoints.size(); i++) {
                        ServiceEntry se = new ServiceEntry()
                                        .id(subjectDid + "#" + ServiceType.EndpointDiscocveryService.suffix() + i)
                                        .serviceEndpoint(serviceEndpoints.get(i))
                                        .type(ServiceType.EndpointDiscocveryService.name());
                        serviceEntries.add(se);
                }
                return serviceEntries;
        }

        public static OctetKeyPair readKey(String keyId, String logEntry) {
                return readKey(keyId, allKeys(logEntry));
        }

        public static OctetKeyPair readKey(String keyId, List<Ed25519VerificationKey2018> entries) {
                Optional<Ed25519VerificationKey2018> ke = entries.stream().filter(k -> k.getId().equals(keyId))
                                .findFirst();
                if (ke.isEmpty())
                        return null;
                Ed25519VerificationKey2018 pke = ke.get();
                return Ed25519VerificationKey2018Service.publicKeyFromBase58(pke.getPublicKeyBase58(), pke.getId());
        }

        public static List<Ed25519VerificationKey2018> allKeys(DocumentContext doc) {
                List<Ed25519VerificationKey2018> authentication = doc
                                .read("$.credentialSubject.did.authentication[?(@.id)]", publicKeyList);
                List<Ed25519VerificationKey2018> assertionMethod = doc
                                .read("$.credentialSubject.did.assertionMethod[?(@.id)]", publicKeyList);
                List<Ed25519VerificationKey2018> result = new ArrayList<>(authentication);
                result.addAll(assertionMethod);
                return result;
        }

        public static List<X25519KeyAgreementKey2019> encKeys(DocumentContext doc) {
                List<X25519KeyAgreementKey2019> keyAgreement = doc.read("$.credentialSubject.did.keyAgreement[?(@.id)]",
                                encryptionKeyList);
                return new ArrayList<>(keyAgreement);
        }

        public static List<Ed25519VerificationKey2018> allKeys(String logEntry) {
                DocumentContext doc = JsonPathUtils.parse(logEntry);
                return allKeys(doc);
        }

        public static List<X25519KeyAgreementKey2019> encKeys(String logEntry) {
                DocumentContext doc = JsonPathUtils.parse(logEntry);
                return encKeys(doc);
        }

        public static X25519KeyAgreementKey2019 encKey(String keyId, String logEntry) {
                return encKey(keyId, encKeys(logEntry));
        }

        public static X25519KeyAgreementKey2019 encKey(String keyId, DocumentContext doc) {
                return encKey(keyId, encKeys(doc));
        }

        public static X25519KeyAgreementKey2019 encKey(String keyId, List<X25519KeyAgreementKey2019> entries) {
                return entries.stream().filter(k -> k.getId().equals(keyId)).findFirst().orElse(null);
        }

        public static Ed25519VerificationKey2018 sigKey(String keyId, String logEntry) {
                return sigKey(keyId, allKeys(logEntry));
        }

        public static Ed25519VerificationKey2018 sigKey(String keyId, DocumentContext doc) {
                return sigKey(keyId, allKeys(doc));
        }

        public static Ed25519VerificationKey2018 sigKey(String keyId, List<Ed25519VerificationKey2018> entries) {
                return entries.stream().filter(k -> k.getId().equals(keyId)).findFirst().orElse(null);
        }

        public static Ed25519VerificationKey2018 sigKeyByPub58(String pub58, String logEntry) {
                return sigKeyByPub58(pub58, allKeys(logEntry));
        }

        public static Ed25519VerificationKey2018 sigKeyByPub58(String pub58, DocumentContext doc) {
                return sigKeyByPub58(pub58, allKeys(doc));
        }

        public static Ed25519VerificationKey2018 sigKeyByPub58(String pub58, List<Ed25519VerificationKey2018> entries) {
                return entries.stream().filter(k -> k.getPublicKeyBase58().equals(pub58)).findFirst().orElse(null);
        }

        public static DocumentContext parse(String record) {
                return JsonPathUtils.parse(record);
        }
}