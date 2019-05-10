package pl.nazwa.arzieba.dtnetworkproject.utils.deviceCard;

import pl.nazwa.arzieba.dtnetworkproject.dao.DeviceDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.DeviceCardDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.DeviceCard;
import pl.nazwa.arzieba.dtnetworkproject.utils.calendar.CalendarUtil;

public class DeviceCardMapper {

    public static DeviceCard map(DeviceCardDTO dto, DeviceDAO dao){
        DeviceCard card = new DeviceCard();
        card.setDevice(dao.findByInventNumber(dto.getInventNumber()));
        card.setAddress(dto.getParameters());
        card.setAttachementsIDs(dto.getAttachementsIDs());
        card.setBuildTime(dto.getBuildTime());
        card.setCreationTime(CalendarUtil.string2cal(dto.getCreationTime()));
        card.setCreatorOfDeviceCard(dto.getCreatorOfDeviceCard());
        card.setDeliverer(dto.getDeliverer());
        card.setDeliveryDate(CalendarUtil.string2cal(dto.getDeliveryDate()));
        card.setDeliveryDocumentID(dto.getDeliveryDocumentID());
        card.setDeviceName(dto.getDeviceName());
        card.setDeviceType(dto.getDeviceType());
        card.setDeviceValue(dto.getDeviceValue());
        card.setFabricalID(dto.getFabricalID());
        card.setFinanceSource(dto.getFinanceSource());
        card.setInstallPlace(dto.getInstallPlace());
        card.setKeeperData(dto.getKeeperData());
        card.setProducer(dto.getProducer());
        card.setSignatureNumber(dto.getSignatureNumber());
        card.setStartDate(CalendarUtil.string2cal(dto.getStartDate()));
        return card;
    }

    public static DeviceCardDTO map(DeviceCard card){
        DeviceCardDTO dto = new DeviceCardDTO();
        dto.setInventNumber(card.getDevice().getInventNumber());
        dto.setParameters(card.getAddress());
        dto.setAttachementsIDs(card.getAttachementsIDs());
        dto.setBuildTime(card.getBuildTime());
        dto.setCreationTime(CalendarUtil.cal2string(card.getCreationTime()));
        dto.setCreatorOfDeviceCard(card.getCreatorOfDeviceCard());
        dto.setDeliverer(card.getDeliverer());
        dto.setDeliveryDate(CalendarUtil.cal2string(card.getDeliveryDate()));
        dto.setDeliveryDocumentID(card.getDeliveryDocumentID());
        dto.setDeviceName(card.getDeviceName());
        dto.setDeviceType(card.getDeviceType());
        dto.setDeviceValue(card.getDeviceValue());
        dto.setFabricalID(card.getFabricalID());
        dto.setFinanceSource(card.getFinanceSource());
        dto.setInstallPlace(card.getInstallPlace());
        dto.setKeeperData(card.getKeeperData());
        dto.setProducer(card.getProducer());
        dto.setSignatureNumber(card.getSignatureNumber());
        dto.setStartDate(CalendarUtil.cal2string(card.getStartDate()));
        return dto;
    }

}
