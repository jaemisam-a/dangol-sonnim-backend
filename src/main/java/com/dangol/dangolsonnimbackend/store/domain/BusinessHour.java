package com.dangol.dangolsonnimbackend.store.domain;

import com.dangol.dangolsonnimbackend.store.dto.BusinessHourRequestDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tb_business_hour")
public class BusinessHour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String weeks;
    private String hours;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    @JsonIgnore
    private Store store;

    public BusinessHour(BusinessHourRequestDTO businessHourRequestDTO, Store store) {
        this.hours = businessHourRequestDTO.getHours();
        this.weeks = businessHourRequestDTO.getWeeks();
        this.store = store;
    }
}
