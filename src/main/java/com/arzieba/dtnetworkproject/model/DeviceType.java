package com.arzieba.dtnetworkproject.model;

import lombok.Getter;

@Getter
public enum DeviceType {

    CRV("/images/crv.png"),
    HPM("/images/hpm.png"),
    CHILLER("/images/chiller.png"),
    UPS("/images/ups.png"),
    PCW("/images/pcw.jpg"),
    XDP("/images/xdp.png"),
    XDC("/images/xdc.png"),
    HIROSS("/images/hiross.jpg"),
    GENERATOR("/images/generator.jpg"),
    DRY_COOLER("/images/cooler.png"),
    OTHER("/images/cyfronet.jpg");
    
    private final String IMAGE;

     private DeviceType(String source) {
        this.IMAGE = source;
    }
}
