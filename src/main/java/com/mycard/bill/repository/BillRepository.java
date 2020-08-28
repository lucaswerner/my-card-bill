package com.mycard.bill.repository;

import com.mycard.bill.entity.Bill;
import com.mycard.bill.entity.id.BillId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends JpaRepository<Bill, BillId> {

    Page<Bill> findAllByCardBinAndCardNumberOrderByYearDescMonthDesc(Long cardBin, Long cardNumber, Pageable pageable);

}
