package com.deliver.finances.repository;

import com.deliver.finances.model.entity.InterestConfiguration;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestConfigurationRepository extends JpaRepository<InterestConfiguration, Long> {

    @Query(" SELECT r " +
            " FROM InterestConfiguration r " +
            " WHERE :delayedDays  <= r.endRange" +
            " AND :delayedDays >= r.startRange")
    Optional<InterestConfiguration> findMatchingRangeForDelayedDays(@Param("delayedDays") Integer delayedDays);
}
