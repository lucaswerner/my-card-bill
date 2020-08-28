package com.mycard.bill.controller;

import com.mycard.bill.dto.BillDTO;
import com.mycard.bill.dto.PostBillDTO;
import com.mycard.bill.dto.PrincipalDTO;
import com.mycard.bill.entity.id.BillId;
import com.mycard.bill.service.BillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.time.Month;

@RestController
@RequestMapping("/bills")
@Api(tags = "bill")
public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @GetMapping("/all/{cardBin}/{cardNumber}")
    @ApiOperation(value = "GetBillPage")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong")
    })
    public ResponseEntity<Page<BillDTO>> getBillPage(
            @PathVariable("cardBin") Long cardBin,
            @PathVariable("cardNumber") Long cardNumber,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("pageNumber") Integer pageNumber
    ) {
        return ResponseEntity
                .ok()
                .body(billService.getBillDTOPageByCard(cardBin, cardNumber, PageRequest.of(pageNumber, pageSize)));
    }

    @GetMapping("/all/{cardBin}/{cardNumber}/{month}/{year}")
    @ApiOperation(value = "GetBill")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong")
    })
    public ResponseEntity<BillDTO> getBill(
            @PathVariable("cardBin") Long cardBin,
            @PathVariable("cardNumber") Long cardNumber,
            @PathVariable("month") byte month,
            @PathVariable("year") short year
    ) {
        return billService.getBillDTOById(new BillId(cardBin, cardNumber, Month.of(month), year))
                .map(bill -> ResponseEntity.ok().body(bill))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping
    @ApiOperation(value = "PostBill")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong")
    })
    public ResponseEntity<BillDTO> saveBill(
            @Valid @RequestBody PostBillDTO postBillDTO,
            HttpServletRequest request
    ) {
        final BillDTO billDTO = billService.saveBill(postBillDTO);
        final URI location = URI.create(
                String.format(
                        request.getRequestURI() + "/%s/%s/%s/%s",
                        billDTO.getCardBin(),
                        billDTO.getCardNumber(),
                        billDTO.getMonth(),
                        billDTO.getYear()
                )
        );

        return ResponseEntity.created(location).body(billDTO);
    }

    @GetMapping("/{cardBin}/{cardNumber}")
    @ApiOperation(value = "GetUserBillPage")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong")
    })
    public ResponseEntity<Page<BillDTO>> getUserBillPage(
            @PathVariable("cardBin") Long cardBin,
            @PathVariable("cardNumber") Long cardNumber,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("pageNumber") Integer pageNumber,
            Authentication authentication
    ) {
        return ResponseEntity
                .ok()
                .body(
                        billService.getBillDTOPageByCardAndUser(
                                cardBin,
                                cardNumber,
                                PageRequest.of(pageNumber, pageSize),
                                ((PrincipalDTO) authentication.getPrincipal()).getId()
                        )
                );
    }

    @GetMapping("/{cardBin}/{cardNumber}/{month}/{year}")
    @ApiOperation(value = "GetBill")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong")
    })
    public ResponseEntity<BillDTO> getBill(
            @PathVariable("cardBin") Long cardBin,
            @PathVariable("cardNumber") Long cardNumber,
            @PathVariable("month") byte month,
            @PathVariable("year") short year,
            Authentication authentication
    ) {
        return billService.getBillDTOByIdAndUser(
                new BillId(cardBin, cardNumber, Month.of(month), year),
                ((PrincipalDTO) authentication.getPrincipal()).getId()
        )
                .map(bill -> ResponseEntity.ok().body(bill))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
}
