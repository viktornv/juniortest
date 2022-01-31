package org.haulmont.testtask.Services;


import org.haulmont.testtask.Entities.Credit;
import org.haulmont.testtask.Repositories.CreditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreditService {
    @Autowired
    CreditRepository creditRepository;
    public CreditService() { }

    public List<Credit> findAll() {
        return creditRepository.findAll();
    }

    public void delete(Credit credit) {
        creditRepository.delete(credit);
    }
    public void save(Credit credit)  {
        if (credit == null) {
            System.out.println("Credit is null!!");
            return;
        }
        creditRepository.save(credit);
    }

}

