package com.bank.spring.jpa.saldo;

import com.bank.spring.jpa.client.model.Client;
import com.bank.spring.jpa.currency.Currency;
import com.bank.spring.jpa.saldo.model.Saldo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Stream;

@Service
public class SaldoService {

    @Autowired
    private SaldoRepository saldoRepository;

    public SaldoService(SaldoRepository saldoRepository) {
        this.saldoRepository = saldoRepository;
    }

    public void generateSaldo(Client client, Currency currency) {
        Saldo saldo = new Saldo(client, currency.name(), 0);
        saldoRepository.save(saldo);
    }

    public boolean doesCurrencyExits(String currencyName) {
        for (Currency currency : Currency.values()) {
            if (currency.name().equalsIgnoreCase(currencyName)) {
                return true;
            }
        }
        return false;
    }

    public double getSaldo(long clientId, String currency) {
        if (!doesCurrencyExits(currency)) {
            throw new IllegalArgumentException("This currency does not exist!");
        }
        List<Saldo> saldoList = findClientAllSaldos(clientId);
        double amount = 0;
        for (Saldo saldo : saldoList) {
            if (saldo.getCurrency().equalsIgnoreCase(currency)) {
                amount = saldo.getAmount();
            }
        }
        return amount;
    }

    public void generateAllSaldos(Client client) {
        EnumSet.allOf(Currency.class).forEach(currency -> generateSaldo(client, currency));
    }

    public List<Saldo> findClientAllSaldos(long clientId) {
        return saldoRepository.findSaldoByClientId(clientId);
    }

}
