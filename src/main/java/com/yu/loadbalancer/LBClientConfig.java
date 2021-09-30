package com.yu.loadbalancer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClientSpecification;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties("load-balancer-custom-config")
public class LBClientConfig {

    private String[] services;

    public void setServices(String[] services) {
        this.services = services;
    }

    private static final Logger logger = LoggerFactory.getLogger(LBClientConfig.class);

    @Bean
    public LoadBalancerClientFactory loadBalancerClientFactory() {
        LoadBalancerClientFactory clientFactory = new LoadBalancerClientFactory();
        List<LoadBalancerClientSpecification> lbConfigList = new ArrayList<>();
        if (services == null || services.length == 0) {
            logger.info("no services is registered to use Custom config class.");
        } else {
            for (String serviceName : services) {
                logger.info("use Custom config class for service: [{}].", serviceName);
                lbConfigList.add(
                        new LoadBalancerClientSpecification(serviceName,
                                new Class[]{CustomLoadBalancerConfiguration.class})
                );
            }
        }
        clientFactory.setConfigurations(lbConfigList);
        return clientFactory;
    }

}
