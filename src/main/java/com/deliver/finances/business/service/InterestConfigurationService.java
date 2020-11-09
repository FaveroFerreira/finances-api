package com.deliver.finances.business.service;

import com.deliver.finances.exception.DataNotFoundException;
import com.deliver.finances.model.entity.InterestConfiguration;
import com.deliver.finances.repository.InterestConfigurationRepository;
import org.springframework.stereotype.Service;

@Service
public class InterestConfigurationService {

    private final InterestConfigurationRepository interestConfigurationRepository;

    public InterestConfigurationService(InterestConfigurationRepository interestConfigurationRepository) {
        this.interestConfigurationRepository = interestConfigurationRepository;
    }

    public InterestConfiguration findInterestConfigurationRangeByNumberOfDelayedDays(Integer delayedDays) {
        return interestConfigurationRepository
                .findMatchingRangeForDelayedDays(delayedDays)
                .orElseThrow(DataNotFoundException::new);
    }
}
