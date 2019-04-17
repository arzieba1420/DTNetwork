package com.arzieba.dtnetworkproject.utils.shortPost;

import com.arzieba.dtnetworkproject.dao.DeviceDAO;
import com.arzieba.dtnetworkproject.dto.ShortPostDTO;
import com.arzieba.dtnetworkproject.model.ShortPost;
import com.arzieba.dtnetworkproject.utils.calendar.CalendarUtil;

import java.util.Calendar;

public class ShortPostMapper {

    public static ShortPost map(ShortPostDTO dto, DeviceDAO dao){
        ShortPost post = new ShortPost();
        post.setAuthor(dto.getAuthor());
        post.setContent(dto.getContent());
        post.setDevice(dao.findByInventNumber(dto.getInventNumber()));
        Calendar calendar= CalendarUtil.string2cal(dto.getDate());
        post.setPostDate(calendar);
        post.setDate(calendar.getTime());
        return post;
    }

    public static ShortPostDTO map(ShortPost post){
        ShortPostDTO dto = new ShortPostDTO();
        dto.setAuthor(post.getAuthor());
        dto.setContent(post.getContent());
        dto.setDate(CalendarUtil.cal2string(post.getPostDate()));
        dto.setInventNumber(post.getDevice().getInventNumber());
        return dto;
    }

}
