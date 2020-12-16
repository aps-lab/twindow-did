package org.adorsys.adssi.twindow.utils;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.TypeRef;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;

import org.adorsys.adssi.twindow.data.DiD;
import org.adorsys.adssi.twindow.data.DiDSubject;
import org.adorsys.adssi.twindow.data.JcsBase64Ed25519Signature2020;

public class JsonPathUtils {

    private static TypeRef<List<JcsBase64Ed25519Signature2020>> proofList = new TypeRef<List<JcsBase64Ed25519Signature2020>>() {
    };

    static {
        Configuration.setDefaults(new Configuration.Defaults() {
            private final JsonProvider jsonProvider = new JacksonJsonProvider(JsonUtils.MAPPER);
            private final MappingProvider mappingProvider = new JacksonMappingProvider();

            @Override
            public JsonProvider jsonProvider() {
                return jsonProvider;
            }

            @Override
            public MappingProvider mappingProvider() {
                return mappingProvider;
            }

            @Override
            public Set<Option> options() {
                return EnumSet.noneOf(Option.class);
            }
        });
    }

    public static DocumentContext parse(String record) {
        return JsonPath.parse(record);
    }

    public static List<JcsBase64Ed25519Signature2020> proof(String record) {
        DocumentContext doc = parse(record);
        return doc.read("$.proof", proofList);
    }

    public static List<JcsBase64Ed25519Signature2020> proof(DocumentContext doc) {
        return doc.read("$.proof", proofList);
    }

    public static List<JcsBase64Ed25519Signature2020> proof(DocumentContext doc, String proofType) {
        return proof(doc).stream().filter(p -> p.getProofPurpose().contains(proofType)).collect(Collectors.toList());
    }

    public static DiDSubject diDSubject(String record) {
        DocumentContext doc = parse(record);
        return diDSubject(doc);
    }

    public static DiDSubject diDSubject(DocumentContext doc) {
        return doc.read("$.credentialSubject", DiDSubject.class);
    }

    public static DiD loadDidDocument(String record) {
        DocumentContext doc = parse(record);
        return doc.read("$.credentialSubject.did", DiD.class);
    }
}