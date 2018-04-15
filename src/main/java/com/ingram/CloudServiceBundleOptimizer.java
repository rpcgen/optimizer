package com.ingram;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CloudServiceBundleOptimizer {


    public List<CloudServiceBundle> optimize(List<CloudService> services, int serviceslen) {

        Map<CloudService, Integer> groupedServices = services.stream()
                .map(service -> Collections.singletonMap(service, 1))
                .reduce(new HashMap<>(), (a, b) -> {
                    b.forEach((key, value) -> {
                        if (a.containsKey(key))
                            a.put(key, a.get(key) + value);
                        else
                            a.put(key, value);
                    });
                    return a;
                });

        int groupslen = groupedServices.values().stream()
                .max(Integer::compareTo)
                .orElse(0);

        List<CloudServiceBundle> groups = new ArrayList<>(groupslen);

        for (int i = 0; i < groupslen; i++)
            groups.add(new CloudServiceBundle());

        if (groupslen == 0)
            return groups;
        else
            return optimize(
                    services.stream()
                            .sorted(Comparator.comparing(groupedServices::get).reversed())
                            .collect(Collectors.toList()), serviceslen, groups, groupslen);
    }

    private List<CloudServiceBundle> optimize(
            List<CloudService> services,
            int serviceslen,
            List<CloudServiceBundle> groups,
            int groupslen) {

        if (serviceslen == 0)
            return groups;
        else
            return min(services.get(serviceslen-1), optimize(services, serviceslen - 1, groups, groupslen), groupslen);
    }

    private List<CloudServiceBundle> min(CloudService cloudService, List<CloudServiceBundle> groups, int groupslen) {

        groups.get(0).push(cloudService);

        int        winnerIndex = 0;
        BigDecimal winnerCost  = cost(groups, groupslen);

        groups.get(0).pop();

        for (int i = 1; i < groupslen; i++) {

            groups.get(i).push(cloudService);

            BigDecimal indexCost = cost(groups, groupslen);
            if (indexCost.compareTo(winnerCost) < 0) {
                winnerIndex = 1;
                winnerCost  = indexCost;
            } else

            if (indexCost.compareTo(winnerCost) == 0 && groups.get(winnerIndex).contains(cloudService)) {
                winnerIndex = 1;
                winnerCost  = indexCost;
            }

            groups.get(i).pop();
        }

        groups.get(winnerIndex).push(cloudService);
        return groups;
    }

    private BigDecimal cost(List<CloudServiceBundle> groups, int groupslen) {
        if (groupslen == 0)
            return new BigDecimal(0);
        else
            return cost(groups.get(groupslen-1)).add(cost(groups, groupslen-1));
    }

    private BigDecimal cost(CloudServiceBundle bundle) {
        return new CloudServiceBundlePriceCalculator().getPrice(bundle);
    }

}
