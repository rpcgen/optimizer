package com.ingram

import spock.lang.Specification

import static com.ingram.CloudService.*

class OptimumTest extends Specification {

    def 'service must provide an optimum response'() {

        def set = [A, B, C, C, D, D, E, E, F, F]

        def result = new CloudServiceBundleOptimizer().optimize(set, set.size())

        expect:

        result.size() == 2
        result[0].size() == 5
        result[1].size() == 5
    }

}