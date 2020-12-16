package org.adorsys.adssi.twindow.data;

import java.util.Arrays;
import java.util.List;

public enum ProofPurpose {
    assertionMethod, documentation, authentication;

    public static final List<String> AUTH_ASSERT_DOC = Arrays.asList(authentication.name(), assertionMethod.name(),
                                                                     documentation.name());
    public static final List<String> ASSERT_DOC = Arrays.asList(assertionMethod.name(), documentation.name());
    public static final List<String> DOC = Arrays.asList(documentation.name());
    public static final List<String> AUTH = Arrays.asList(authentication.name());
    public static final List<String> ASSERT = Arrays.asList(assertionMethod.name());
}