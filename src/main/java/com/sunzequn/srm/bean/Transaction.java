package com.sunzequn.srm.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sloriac on 2016/11/21.
 */
public class Transaction {

    private List<String> items = new ArrayList<>();

    public void addItem(String item) {
        items.add(item);
    }

    public Transaction(List<String> items) {
        this.items = items;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "items=" + items +
                '}';
    }
}
