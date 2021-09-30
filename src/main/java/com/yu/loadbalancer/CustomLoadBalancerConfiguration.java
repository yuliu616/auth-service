package com.yu.loadbalancer;

import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

public class CustomLoadBalancerConfiguration {

    @Bean
    public ServiceInstanceListSupplier discoveryClientServiceInstanceListSupplier(
            ConfigurableApplicationContext context)
    {
        return ServiceInstanceListSupplier.builder()
                .withDiscoveryClient()
//                .withSameInstancePreference() // add this if you like
//                .withZonePreference() // add this if you like
//                .withCaching()
                .withHealthChecks() // add this if you like
                .build(context);
    }

}
