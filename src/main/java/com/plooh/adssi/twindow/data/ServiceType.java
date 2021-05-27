package com.plooh.adssi.twindow.data;

public enum ServiceType {
    EndpointDiscocveryService("discovery-");

    private final String suffix;

    private ServiceType(String suffix) {
        this.suffix = suffix;
    }

    public String suffix() {
        return suffix;
    }
}