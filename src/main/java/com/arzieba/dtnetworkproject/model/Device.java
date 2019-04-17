package com.arzieba.dtnetworkproject.model;

import com.arzieba.dtnetworkproject.dto.DeviceDTO;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.context.annotation.Primary;


import javax.persistence.*;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Device {

    @Id
    private String inventNumber;

    @Column(nullable = false)
    private String deviceDescription;

    @Column(nullable = false)
    private Room room;

    @Column(nullable = false)
    private DeviceType deviceType;

    @OneToOne(mappedBy = "device")
    private DeviceCard deviceCard;

    @OneToMany(mappedBy = "device", fetch =FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Damage> damageList;

    @OneToMany(mappedBy = "device")
    private List<ShortPost> shortPosts;


}
