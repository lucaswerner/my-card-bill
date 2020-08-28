package com.mycard.bill.service;

import com.mycard.bill.dto.BillDTO;
import com.mycard.bill.dto.PostBillDTO;
import com.mycard.bill.entity.Bill;
import com.mycard.bill.entity.id.BillId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BillService {
    Bill saveBill(Bill bill);

    Optional<Bill> getBillById(BillId billId);

    Page<Bill> getBillPageByCard(Long cardBin, Long cardNumber, Pageable pageable);

    Optional<Bill> getBillByIdAndUser(BillId billId, Long userId);

    Page<Bill> getBillPageByCardAndUser(Long cardBin, Long cardNumber, Pageable pageable, Long userId);

    BillDTO saveBill(PostBillDTO postBillDTO);

    Optional<BillDTO> getBillDTOById(BillId billId);

    Page<BillDTO> getBillDTOPageByCard(Long cardBin, Long cardNumber, Pageable pageable);

    Optional<BillDTO> getBillDTOByIdAndUser(BillId billId, Long userId);

    Page<BillDTO> getBillDTOPageByCardAndUser(Long cardBin, Long cardNumber, Pageable pageable, Long userId);
}
