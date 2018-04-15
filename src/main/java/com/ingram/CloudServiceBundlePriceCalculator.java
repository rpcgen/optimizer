package com.ingram;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CloudServiceBundlePriceCalculator {

    public BigDecimal getPrice(CloudServiceBundle bundle) {

        Set<CloudService>  uniqueElements   = new HashSet<>(bundle);
        List<CloudService> repeatedElements = new ArrayList<>(bundle);
        uniqueElements.stream()
                .map    (repeatedElements::indexOf)
                .forEach(x -> repeatedElements.remove(x.intValue()));

        BigDecimal elementsWithNoDiscountPrice = repeatedElements.stream()
                .map(CloudService::getPrice)
                .reduce(BigDecimal::add)
                .orElse(new BigDecimal(0));

        BigDecimal elementsWithDiscountPrice = uniqueElements.stream()
                .map(CloudService::getPrice)
                .reduce(BigDecimal::add)
                .orElse(new BigDecimal(0));

        BigDecimal result = elementsWithDiscountPrice.multiply(new BigDecimal(1).subtract(getDiscount(bundle)))
                .add(elementsWithNoDiscountPrice);

        return result.setScale(2, RoundingMode.HALF_EVEN);
    }

    private BigDecimal getDiscount(CloudServiceBundle bundle) {
        switch ((int) bundle.stream().distinct().count()) {
            case 1 : return new BigDecimal(0);
            case 2 : return new BigDecimal(0.05);
            case 3 : return new BigDecimal(0.10);
            case 4 : return new BigDecimal(0.15);
            case 5 : return new BigDecimal(0.25);
            case 6 : return new BigDecimal(0.30);
            default: return new BigDecimal(0.35);
        }
    }
}
