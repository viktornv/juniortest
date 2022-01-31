package org.haulmont.testtask.Repositories;

import org.haulmont.testtask.Entities.PaymentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentScheduleRepository extends JpaRepository<PaymentSchedule, Long> {
}