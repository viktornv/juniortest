package org.haulmont.testtask.Services;


import org.haulmont.testtask.Entities.CreditOffer;
import org.haulmont.testtask.Repositories.CreditOfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreditOfferService {
    @Autowired
    CreditOfferRepository creditOfferRepository;

    public CreditOfferService() {
    }

    public void delete(CreditOffer creditOffer) {
        creditOfferRepository.delete(creditOffer);
    }

    public List<CreditOffer> findAll() {
        return creditOfferRepository.findAll();
    }

    public List<CreditOffer> findAllOffersForClient(long bankID) {
        return creditOfferRepository.findByBank(bankID);
    }

    public void deleteAllOffersForClient(long bankID) {
        creditOfferRepository.deleteByBank(bankID);
    }

    public void save(CreditOffer creditOffer) {
        creditOfferRepository.save(creditOffer);
    }

}
