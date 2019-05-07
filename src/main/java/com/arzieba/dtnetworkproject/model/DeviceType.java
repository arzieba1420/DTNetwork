package com.arzieba.dtnetworkproject.model;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
public enum DeviceType {

    CRV("/images/crv.png"),
    HPM("/images/hpm.png"),
    CHILLER("/images/chiller.png"),
    UPS("/images/ups.png"),
    PCW("/images/pcw.jpg"),
    XDP("/images/xdp.png"),
    XDC("/images/xdc.png"),
    HIROSS("/images/hiross.png"),
    GENERATOR("/images/generator.png"),
    DRY_COOLER("/images/cooler.png"),
    OTHER("/images/cyfronet.png");
    
    private final String IMAGE;

     private DeviceType(String source) {
        this.IMAGE = source;
    }
}
