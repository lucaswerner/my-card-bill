package com.mycard.bill.entity.id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Month;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillId implements Serializable {
    private static final long serialVersionUID = -7263061679660131479L;
    private Long cardBin;
    private Long cardNumber;
    private Month month;
    private short year;

}
