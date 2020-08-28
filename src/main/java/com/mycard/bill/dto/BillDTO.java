package com.mycard.bill.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.Month;

@Data
public class BillDTO implements Serializable {
    private static final long serialVersionUID = 6420860690185253435L;
    private Long cardBin;
    private Long cardNumber;
    private Month month;
    private short year;
    private Double value;
}
