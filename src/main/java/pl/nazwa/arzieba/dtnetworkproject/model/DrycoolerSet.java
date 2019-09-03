package pl.nazwa.arzieba.dtnetworkproject.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table
@Getter
@Setter
@ToString
public class DrycoolerSet {

        @Id
        @GeneratedValue
        Integer drycoolerSetId;

        @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "Device_INVENT")
        @OnDelete(action = OnDeleteAction.CASCADE)
        private Device device;


        private Double actualSetPoint_AmbL; //temp zadana drycoolera zewn
        private Double actualSetPoint_CWL; //temp zadana drycoolera zadana
        private Double actualSetPoint_AmbR; //temp zadana drycoolera zewn
        private Double actualSetPoint_CWR; //temp zadana drycoolera zadana
        @Temporal(TemporalType.DATE)
        private Calendar setDate;
        private Author author;

        private Double previousSetPoint_AmbL; //temp zadana drycoolera zewn
        private Double previousSetPoint_CWL; //temp zadana drycoolera zadana
        private Double previousSetPoint_AmbR; //temp zadana drycoolera zewn
        private Double previousSetPoint_CWR; //temp zadana drycoolera zadana
        @Temporal(TemporalType.DATE)
        private Calendar previousSetDate;
        private Author previousAuthor;


    }
