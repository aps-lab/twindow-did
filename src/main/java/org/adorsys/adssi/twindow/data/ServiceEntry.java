package org.adorsys.adssi.twindow.data;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ServiceEntry {
    private String id;
    private String type;
    private String serviceEndpoint;

    public ServiceEntry id(String id) {
        this.id = id;
        return this;
    }

    public ServiceEntry type(String type) {
        this.type = type;
        return this;
    }

    public ServiceEntry serviceEndpoint(String serviceEndpoint) {
        this.serviceEndpoint = serviceEndpoint;
        return this;
    }
}