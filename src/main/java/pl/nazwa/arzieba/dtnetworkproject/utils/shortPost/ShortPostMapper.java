package pl.nazwa.arzieba.dtnetworkproject.utils.shortPost;

import pl.nazwa.arzieba.dtnetworkproject.dao.DeviceDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.ShortPostDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.ShortPost;
import pl.nazwa.arzieba.dtnetworkproject.utils.calendar.CalendarUtil;
import java.util.Calendar;

public class ShortPostMapper {

    public static ShortPost map(ShortPostDTO dto, DeviceDAO dao){
        ShortPost post = new ShortPost();
        post.setAuthor(dto.getAuthor());
        post.setContent(dto.getContent());
        post.setDevice(dao.findByInventNumber(dto.getInventNumber()));
        Calendar calendar= CalendarUtil.string2cal(dto.getDate());
        post.setPostDate(calendar);
        post.setPostLevel(dto.getPostLevel());
        return post;
    }

    public static ShortPostDTO map(ShortPost post){
        ShortPostDTO dto = new ShortPostDTO();
        dto.setAuthor(post.getAuthor());
        dto.setContent(post.getContent());
        dto.setDate(CalendarUtil.cal2string(post.getPostDate()));
        dto.setInventNumber(post.getDevice().getInventNumber());
        dto.setPostLevel(post.getPostLevel());
        return dto;
    }
}
