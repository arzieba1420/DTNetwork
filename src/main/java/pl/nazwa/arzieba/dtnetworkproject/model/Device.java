package pl.nazwa.arzieba.dtnetworkproject.model;

import lombok.*;
import javax.persistence.*;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Device {

    //-----------------------------------------------------------------------MODEL FIELDS-----------------------------------------------------------------------------------------

    @Id
    @Column(unique = true)
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

    @OneToMany(mappedBy = "device")
    private List<GeneratorTest> tests;

    @OneToOne(mappedBy = "device")
    private  ChillerSet chillerSet;

    @OneToOne (mappedBy = "device")
    private DrycoolerSet drycoolerSet;
}
