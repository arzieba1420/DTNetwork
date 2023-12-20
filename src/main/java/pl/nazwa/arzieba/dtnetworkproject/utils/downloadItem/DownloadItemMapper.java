package pl.nazwa.arzieba.dtnetworkproject.utils.downloadItem;

import pl.nazwa.arzieba.dtnetworkproject.dao.DeviceDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.DamageDTO;
import pl.nazwa.arzieba.dtnetworkproject.dto.DownloadItemDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.Damage;
import pl.nazwa.arzieba.dtnetworkproject.model.DownloadItem;
import pl.nazwa.arzieba.dtnetworkproject.utils.calendar.CalendarUtil;

public class DownloadItemMapper {
    public static DownloadItemDTO map(DownloadItem downloadItem){
        DownloadItemDTO dto = new DownloadItemDTO();
        dto.setId(downloadItem.getId());
        dto.setDescription(downloadItem.getDescription());
        dto.setName(downloadItem.getName());
        dto.setType(downloadItem.getType());
        return dto;
    }

    public static DownloadItem map(DownloadItemDTO dto){
        return DownloadItem.builder()
                .id(dto.getId())
                .description(dto.getDescription())
                .name(dto.getName())
                .type(dto.getType())
                .build();
    }
}
