package com.mycard.bill.service.impl;

import com.mycard.bill.dto.BillDTO;
import com.mycard.bill.dto.CardDTO;
import com.mycard.bill.dto.PostBillDTO;
import com.mycard.bill.entity.Bill;
import com.mycard.bill.entity.id.BillId;
import com.mycard.bill.repository.BillRepository;
import com.mycard.bill.service.BillService;
import com.mycard.bill.service.CardService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@CacheConfig(cacheNames = "BillService")
public class BillServiceImpl implements BillService {

    private final ModelMapper modelMapper;
    private final CardService cardService;
    private final BillRepository billRepository;

    public BillServiceImpl(ModelMapper modelMapper, CardService cardService, BillRepository billRepository) {
        this.modelMapper = modelMapper;
        this.cardService = cardService;
        this.billRepository = billRepository;
    }

    public Bill saveBill(Bill bill) {
        return billRepository.save(bill);
    }

    public Optional<Bill> getBillById(BillId billId) {
        return billRepository.findById(billId);
    }

    public Page<Bill> getBillPageByCard(Long cardBin, Long cardNumber, Pageable pageable) {
        return billRepository.findAllByCardBinAndCardNumberOrderByYearDescMonthDesc(cardBin, cardNumber, pageable);
    }

    public Optional<Bill> getBillByIdAndUser(BillId billId, Long userId) {
        final CardDTO cardDTO = cardService.checkUserHasCardAndGet(billId.getCardBin(), billId.getCardNumber(), userId);
        return getBillById(new BillId(cardDTO.getBin(), cardDTO.getNumber(), billId.getMonth(), billId.getYear()));
    }

    public Page<Bill> getBillPageByCardAndUser(Long cardBin, Long cardNumber, Pageable pageable, Long userId) {
        final CardDTO cardDTO = cardService.checkUserHasCardAndGet(cardBin, cardNumber, userId);
        return getBillPageByCard(cardDTO.getBin(), cardDTO.getNumber(), pageable);
    }

    @HystrixCommand(threadPoolKey = "saveBillThreadPool")
    public BillDTO saveBill(PostBillDTO postBillDTO) {
        return transformBillToBillDTO(saveBill(modelMapper.map(postBillDTO, Bill.class)));
    }

    @Cacheable(key = "{#billId.cardBin, #billId.cardNumber, #billId.month, #billId.year}")
    @HystrixCommand(threadPoolKey = "getBillDTOByIdThreadPool")
    public Optional<BillDTO> getBillDTOById(BillId billId) {
        return getBillById(billId)
                .map(this::transformBillToBillDTO);
    }

    @HystrixCommand(threadPoolKey = "getBillDTOPageByCardThreadPool")
    public Page<BillDTO> getBillDTOPageByCard(Long cardBin, Long cardNumber, Pageable pageable) {
        return getBillPageByCard(cardBin, cardNumber, pageable)
                .map(this::transformBillToBillDTO);
    }

    @Cacheable(key = "{#billId.cardBin, #billId.cardNumber, #billId.month, #billId.year}")
    @HystrixCommand(threadPoolKey = "getBillDTOByIdAndUserThreadPool")
    public Optional<BillDTO> getBillDTOByIdAndUser(BillId billId, Long userId) {
        return getBillByIdAndUser(billId, userId)
                .map(this::transformBillToBillDTO);
    }

    @HystrixCommand(threadPoolKey = "getBillDTOPageByCardAndUserThreadPool")
    public Page<BillDTO> getBillDTOPageByCardAndUser(Long cardBin, Long cardNumber, Pageable pageable, Long userId) {
        return getBillPageByCardAndUser(cardBin, cardNumber, pageable, userId)
                .map(this::transformBillToBillDTO);
    }

    private BillDTO transformBillToBillDTO(Bill bill) {
        return modelMapper.map(bill, BillDTO.class);
    }
}
