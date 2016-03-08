package com.infor.retail.healthcheck.config;

import com.infor.retail.healthcheck.model.Service;
import com.infor.retail.healthcheck.service.HealthMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.util.ArrayList;

@PropertySource("classpath:endpoint.properties")
@Configuration
public class AppConfig {

    @Autowired
    private Environment environment;

    // Bean allows function to run automatically
    @Bean(name="serviceList")
    public ArrayList<Service> start() {

        ArrayList<Service> health_services = new ArrayList<>();
        String val = environment.getProperty("endpoints");
        HealthMonitorService hms = new HealthMonitorService();
        for (String i : val.split(",")) { // each service separated by ","
            String[] item = i.split("\\*"); // each name/url in properties file separated by "*"
            String serviceName = item[0];
            String serviceURL = item[1];
            int responseCode = hms.getStatusCode(serviceURL);
            String subservice = hms.getSubService();
            health_services.add(new Service(serviceName, responseCode, subservice));
        }
        return health_services;
    }
}