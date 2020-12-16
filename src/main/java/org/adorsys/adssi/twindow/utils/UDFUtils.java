package org.adorsys.adssi.twindow.utils;

import java.io.IOException;

import com.nimbusds.jose.util.StandardCharset;

import org.adorsys.udf.UDF;

import net.minidev.json.JSONObject;

public class UDFUtils {

    public static String udf(JSONObject didDoc) throws IOException {
        String json = didDoc.toJSONString();
        return udf(json);
    }

    public static String udf(String json) throws IOException {
        String canonicalString = JCSUtils.encode(json);
        return UDF.contentDigestOfDataString(canonicalString.getBytes(StandardCharset.UTF_8), "application/did+json",
                1000, null, null);
    }
}