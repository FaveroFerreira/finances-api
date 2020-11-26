package com.deliver.finances.controller;

import com.deliver.finances.business.facade.CreateBillFacade;
import com.deliver.finances.config.flags.FeatureFlags;
import com.deliver.finances.model.entity.Bill;
import com.deliver.finances.model.vo.CreateBillVO;
import com.deliver.finances.model.vo.DisplayBillVO;
import com.deliver.finances.util.UriUtils;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bills")
public class BillController {

    private final CreateBillFacade createBillFacade;

    public BillController(CreateBillFacade createBillFacade) {
        this.createBillFacade = createBillFacade;
    }

    @PostMapping
    public ResponseEntity<Bill> createBill(@Valid @RequestBody CreateBillVO createBillVO) {
        Bill bill = createBillFacade.createBill(createBillVO);
        URI locationUri = UriUtils.getLocationUri(bill);
        return ResponseEntity.created(locationUri).body(bill);
    }

    @GetMapping
    public ResponseEntity<List<DisplayBillVO>> listCreatedBills() {
        List<DisplayBillVO> displayBillVOS = createBillFacade.listCreatedBills();
        return ResponseEntity.ok(displayBillVOS);
    }

    @GetMapping("/configs")
    public boolean configs() {
        return FeatureFlags.CALCULATE_COMPOSED_INTEREST.isActive();
    }

}
