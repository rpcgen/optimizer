package com.ingram

import spock.lang.Specification
import spock.lang.Unroll

import java.math.RoundingMode

import static com.ingram.CloudService.*

class CloudServiceBundlePriceCalculatorTest extends Specification {

    @Unroll
    def 'Service must apply properly the discount'() {

        given:
        def service = new CloudServiceBundlePriceCalculator()

        when:
        BigDecimal price = service.getPrice(set)

        then:
        price.doubleValue() == withScale(expectation).doubleValue()

        where:
        set                             | expectation
        new CloudServiceBundle([A])     | new BigDecimal( 8.0)
        new CloudServiceBundle([A,A])   | new BigDecimal(16.0)
        new CloudServiceBundle([A,B])   | new BigDecimal(15.2)
        new CloudServiceBundle([A,B,B]) | new BigDecimal(23.2)
        new CloudServiceBundle([A,B,C]) | new BigDecimal(21.6)
    }

    static def withScale(BigDecimal bigDecimal) {
        bigDecimal.setScale(2, RoundingMode.HALF_EVEN)
        return bigDecimal
    }
}
