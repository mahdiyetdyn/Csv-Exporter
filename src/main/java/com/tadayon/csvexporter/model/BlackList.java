package com.tadayon.csvexporter.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "black_list")
public class BlackList {
    @Id
    @Column(name = "card_id")
    private String cardId;
    @Column(name = "operation_date")
    private LocalDateTime operationDate;
}
