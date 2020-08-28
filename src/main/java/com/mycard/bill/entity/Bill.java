package com.mycard.bill.entity;

import com.mycard.bill.entity.id.BillId;
import lombok.Data;

import javax.persistence.*;
import java.time.Month;

@Data
@Entity
@IdClass(BillId.class)
@Table(name = "bill")
public class Bill {

    @Id
    @Column(name = "card_bin")
    private Long cardBin;

    @Id
    @Column(name = "card_number")
    private Long cardNumber;

    @Id
    @Column(
            name = "month",
            columnDefinition = "tinyint"
    )
    private Month month;

    @Id
    @Column(
            name = "year",
            columnDefinition = "smallint"
    )
    private short year;

    @Column(name = "value", nullable = false)
    private Double value;
}
