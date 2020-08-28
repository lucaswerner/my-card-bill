package com.mycard.bill.service;

import com.mycard.bill.dto.CardDTO;

public interface CardService {

    CardDTO checkUserHasCardAndGet(Long bin, Long number, Long userId);

}
