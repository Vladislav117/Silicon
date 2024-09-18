package ru.vladislav117.silicon.economy;

import ru.vladislav117.silicon.Silicon;
import ru.vladislav117.silicon.dataManager.SiDataManager;
import ru.vladislav117.silicon.node.SiNode;

/**
 * Действия для локальной валюты (балансы хранятся на локальном диске).
 */
public class SiLocalCurrencyActions implements SiCurrencyActions {
    static LocalCurrencyDataManager dataManager = new LocalCurrencyDataManager();

    @Override
    public int getBalance(SiCurrency currency, String account) {
        return dataManager.get(account).getInteger(currency.getName(), 0);
    }

    @Override
    public void setBalance(SiCurrency currency, String account, int value) {
        dataManager.get(account).set(currency.getName(), value);
        dataManager.save(account);
    }

    @Override
    public void addBalance(SiCurrency currency, String account, int value) {
        SiNode balances = dataManager.get(account);
        balances.set(currency.getName(), balances.getInteger(currency.getName(), 0) + value);
        dataManager.save(account);
    }

    @Override
    public boolean makeTransaction(SiCurrency currency, String sender, String receiver, int value) {
        SiNode senderBalances = dataManager.get(sender);
        int senderBalance = senderBalances.getInteger(currency.getName(), 0);
        if (value > senderBalance) return false;
        SiNode receiverBalances = dataManager.get(receiver);
        senderBalances.set(currency.getName(), senderBalances.getInteger(currency.getName(), 0) - value);
        receiverBalances.set(currency.getName(), receiverBalances.getInteger(currency.getName(), 0) + value);
        dataManager.save(sender);
        dataManager.save(receiver);
        return true;
    }

    /**
     * Менеджер данных для локальных валют.
     */
    static class LocalCurrencyDataManager extends SiDataManager {
        /**
         * Создание нового менеджера данных локальных валют.
         */
        public LocalCurrencyDataManager() {
            super(Silicon.getDirectory().getChild("economy_balances"));
        }

        @Override
        public SiNode getDefault(String name) {
            return SiNode.emptyMap();
        }
    }
}
