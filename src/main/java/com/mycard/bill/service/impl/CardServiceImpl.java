package com.mycard.bill.service.impl;

import com.mycard.bill.client.CardClient;
import com.mycard.bill.dto.CardDTO;
import com.mycard.bill.service.CardService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CardServiceImpl implements CardService {

    private final CardClient cardClient;

    public CardServiceImpl(CardClient cardClient) {
        this.cardClient = cardClient;
    }

    public CardDTO checkUserHasCardAndGet(Long bin, Long number, Long userId) {
        final Optional<CardDTO> optionalCard = cardClient.getCard(bin, number);

        if (optionalCard.isEmpty() || !userId.equals(optionalCard.get().getUserId())) {
            throw new RuntimeException("Unknown card!");
        }

        return optionalCard.get();
    }
}
