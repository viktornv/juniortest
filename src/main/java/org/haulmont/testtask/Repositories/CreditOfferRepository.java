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
    @Query("select p from CreditOffer p where p.bank_id =:bankID")
    List<CreditOffer> findAllOffersForClient(@Param("bankID") long bankID);

    @Transactional
    @Modifying
    @Query("delete from CreditOffer p where p.bank_id =:bankID")
    void deleteAllOffersForClient(@Param("bankID") long bankID);
}