package org.haulmont.testtask.Services;

import org.haulmont.testtask.Entities.Bank;
import org.haulmont.testtask.Entities.Client;
import org.haulmont.testtask.Entities.Credit;
import org.haulmont.testtask.Repositories.BankRepository;
import org.haulmont.testtask.Repositories.ClientRepository;
import org.haulmont.testtask.Repositories.CreditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankService {
    @Autowired
    private final BankRepository bankRepository;
    @Autowired
    private final ClientRepository clientRepository;
    @Autowired
    private final CreditRepository creditRepository;

    public BankService( BankRepository bankRepository, ClientRepository clientRepository, CreditRepository creditRepository) {
        this.bankRepository = bankRepository;
        this.clientRepository = clientRepository;
        this.creditRepository = creditRepository;
    }

    public List<Bank> findAll(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return bankRepository.findAll();
        }
        else {
             return bankRepository.search(stringFilter);
            //return bankRepository.findAll();
        }
    }

    public void save(Bank bank) {
        if (bank == null) {
            System.err.println("Contact is null");
            return;
        }
        bankRepository.save(bank);
    }

    public void delete(Bank bank) {
        bankRepository.delete(bank);
    }

    public List<Client> findAllClients() {
        return clientRepository.findAll();
    }

    public List<Credit> findAllCredits(){
        return creditRepository.findAll();
    }
}
