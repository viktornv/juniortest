package org.haulmont.testtask.Services;

import com.vaadin.flow.component.notification.Notification;
import org.haulmont.testtask.Entities.*;
import org.haulmont.testtask.Repositories.PaymentScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentScheduleService {

    @Autowired
    PaymentScheduleRepository paymentScheduleRepository;

    BankService bankService;
    CreditOfferService creditOfferService;

    public PaymentScheduleService(BankService bankService, CreditOfferService creditOfferService) {
        this.bankService = bankService;
        this.creditOfferService = creditOfferService;

    }

    public void saveCredit(Client client, Credit credit, double creditAmount, int creditPeriod ){
        try {
            Bank bank = new Bank(client, credit);
            bankService.save(bank);

            LocalDateTime localDateTime = LocalDateTime.now();
            Date date = Date.valueOf(localDateTime.toLocalDate());
            double scale = Math.pow(10, 2);

            double ostatok = creditAmount * 0.8;
            double procent = credit.getCreditProcent();
            int period = creditPeriod * 12;
            double paymentBody = Math.ceil(ostatok / period * scale) / scale;

            for (int i = 0; i < creditPeriod * 12; i++) {
                double paymentProcent = Math.ceil(((ostatok * (procent / 100)) / period) * scale) / scale;
                double paymentPerMonth = Math.ceil((paymentBody + paymentProcent) * scale) / scale;
                if(paymentProcent<0) paymentProcent=0;
                PaymentSchedule schedule = new PaymentSchedule(date, paymentPerMonth, paymentBody, paymentProcent);
                ostatok -= paymentPerMonth;
                localDateTime = localDateTime.plusMonths(1);
                date = Date.valueOf(localDateTime.toLocalDate());
                //System.out.println("------------  "+schedule);
                save(schedule);
                CreditOffer creditOffer = new CreditOffer(client, credit, creditAmount, schedule, bank.getId());
                //System.out.println("============  "+creditOffer);
                creditOfferService.save(creditOffer);
            }
            Notification.show("Операция завершена успешно!");


        } catch (Exception e) {
            Notification.show("Не удалось завершить операцию, попробуйте снова!");

            e.printStackTrace();
        }
    }



    public void delete(PaymentSchedule schedule) {
        paymentScheduleRepository.delete(schedule);
    }

    public List<PaymentSchedule> findAll() {
        return paymentScheduleRepository.findAll();
    }

    public void save(PaymentSchedule schedule) {
        paymentScheduleRepository.save(schedule);
    }
}

