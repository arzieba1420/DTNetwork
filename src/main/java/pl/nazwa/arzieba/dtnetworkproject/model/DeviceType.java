package pl.nazwa.arzieba.dtnetworkproject.model;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
public enum DeviceType {

    CRV("/images/crv.png"), //0
    HPM("/images/hpm.png"), //1
    CHILLER("/images/chiller.png"),//2
    UPS("/images/ups.png"),//3
    PCW("/images/pcw.png"),//4
    XDP("/images/xdp.png"),//5
    XDC("/images/xdc.png"),//6
    HIROSS("/images/hiross.png"),//7
    GENERATOR("/images/generator.png"),//8
    DRY_COOLER("/images/cooler.png"),//9
    OTHER("/images/cyfronet.png"),//10
    XDH("/images/xdh.png"),//11
    PDX("/images/pdx.png");

    private final String IMAGE;

     private DeviceType(String source) {
        this.IMAGE = source;
    }
}
