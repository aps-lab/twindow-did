
package com.plooh.adssi.twindow.data;

import com.plooh.adssi.twindow.utils.JsonUtils;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class DiD {
    @JsonProperty("@context")
    private final String context = "https://www.w3.org/ns/did/v1";
    private String id;
    private List<Object> authentication;
    private List<Object> assertionMethod;
    private List<X25519KeyAgreementKey2019> keyAgreement;
    private List<ServiceEntry> service;

    public DiD id(String id) {
        this.id = id;
        return this;
    }

    public DiD authentication(List<Object> authentication) {
        this.authentication = authentication;
        return this;
    }

    public DiD assertionMethod(List<Object> assertionMethod) {
        this.assertionMethod = assertionMethod;
        return this;
    }

    public DiD keyAgreement(List<X25519KeyAgreementKey2019> keyAgreement) {
        this.keyAgreement = keyAgreement;
        return this;
    }

    public DiD service(List<ServiceEntry> service) {
        this.service = service;
        return this;
    }

    public String randomAssersionKeyId() {
        if (assertionMethod == null)
            return null;
        int randomElementIndex = ThreadLocalRandom.current().nextInt(assertionMethod.size()) % assertionMethod.size();
        Object object = assertionMethod.get(randomElementIndex);
        if (object instanceof String)
            return (String) object;

        if (object instanceof Ed25519VerificationKey2018) {
            Ed25519VerificationKey2018 pk = (Ed25519VerificationKey2018) object;
            return pk.getId();
        }
        Ed25519VerificationKey2018 pk = JsonUtils.MAPPER.convertValue(object, Ed25519VerificationKey2018.class);
        return pk.getId();
    }
}