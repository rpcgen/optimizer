package com.ingram;

import java.util.Collection;
import java.util.Stack;

public class CloudServiceBundle extends Stack<CloudService> {

    public CloudServiceBundle() {
    }

    public CloudServiceBundle(Collection<CloudService> c) {
        c.forEach(this::push);
    }
}
