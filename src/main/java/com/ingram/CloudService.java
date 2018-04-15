package com.ingram;

import java.math.BigDecimal;

public enum CloudService {
    A, B, C, D, E, F, G;

    BigDecimal getPrice() {
        return new BigDecimal(8);
    }
}
