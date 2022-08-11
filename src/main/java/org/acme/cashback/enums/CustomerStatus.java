package org.acme.cashback.enums;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum CustomerStatus {
    SILVER("silver", new BigDecimal(1)),
    GOLD("gold", new BigDecimal(2)),
    PLATINUM("platinum", new BigDecimal(4));

    private static final Map<String,CustomerStatus> CUSTOMER_STATUS_MAP;

    private final String status;
    private final BigDecimal cashbackPercentage;

    CustomerStatus(String name, BigDecimal cashbackPercentage) {
        this.status = name;
        this.cashbackPercentage = cashbackPercentage;
    }

    static {
        Map<String,CustomerStatus> map = new ConcurrentHashMap<String, CustomerStatus>();
        for (CustomerStatus instance : CustomerStatus.values()) {
            map.put(instance.getStatus().toLowerCase(),instance);
        }
        CUSTOMER_STATUS_MAP = Collections.unmodifiableMap(map);
    }

    public static CustomerStatus get(String name) {
        return CUSTOMER_STATUS_MAP.get(name.toLowerCase());
    }

    public String getStatus() {
        return status;
    }

    public BigDecimal getCashbackPercentage() {
        return cashbackPercentage.movePointLeft(2);
    }


}
