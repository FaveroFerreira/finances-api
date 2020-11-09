package com.deliver.finances.business.service;

import com.deliver.finances.model.entity.Bill;
import com.deliver.finances.repository.BillRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BillService {

    private final BillRepository billRepository;

    public BillService(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    public Bill save(Bill bill) {
        return billRepository.save(bill);
    }

    public List<Bill> listBills() {
        return billRepository.findAll();
    }
}
