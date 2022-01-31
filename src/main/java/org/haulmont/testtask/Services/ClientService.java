package org.haulmont.testtask.Services;

import org.haulmont.testtask.Entities.Client;
import org.haulmont.testtask.Repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {
    @Autowired
    ClientRepository clientRepository;
    public ClientService() { }

    public List<Client> findAllClients(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return clientRepository.findAll();
        } else {
            return clientRepository.search(stringFilter);
        }
    }

    public void deleteContact(Client contact) {
        clientRepository.delete(contact);
    }

    public void saveClient(Client client) {
        if (client == null) {
            return;
        }
        clientRepository.save(client);
    }

}
