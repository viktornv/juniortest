package org.haulmont.testtask.Repositories;

import org.haulmont.testtask.Entities.CreditOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CreditOfferRepository extends JpaRepository<CreditOffer, Long> {

    List<CreditOffer> findByBank(long bankID);

    @Transactional
    @Modifying
    void deleteByBank(long bankID);
}