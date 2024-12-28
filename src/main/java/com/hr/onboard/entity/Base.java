package com.hr.onboard.entity;

import com.hr.onboard.model.generator.UUIDv7Generator;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@MappedSuperclass
@Data
public class Base {
    @Id
    @GeneratedValue(generator = "UUIDv7")
    @GenericGenerator(name = "UUIDv7", type = UUIDv7Generator.class)
    @Column(unique = true, updatable = false, nullable = false)
    protected UUID id;
}
