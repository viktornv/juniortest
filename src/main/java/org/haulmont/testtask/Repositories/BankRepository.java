package org.haulmont.testtask.Repositories;

import org.haulmont.testtask.Entities.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {
    @Query("select c from Bank c " +
            "where lower(c.client.firstName) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(c.client.lastName) like lower(concat('%', :searchTerm, '%'))" +
            "or lower(c.client.middleName) like lower(concat('%', :searchTerm, '%'))")
    List<Bank> search(@Param("searchTerm") String searchTerm);
}