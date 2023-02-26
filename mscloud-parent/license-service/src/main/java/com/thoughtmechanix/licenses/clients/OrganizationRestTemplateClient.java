package com.thoughtmechanix.licenses.clients;

import com.thoughtmechanix.licenses.model.Organization;
import com.thoughtmechanix.licenses.context.UserContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OrganizationRestTemplateClient {
    private static final Logger logger = LoggerFactory.getLogger(OrganizationRestTemplateClient.class);
    @Autowired
    private RestTemplate restTemplate;

    public Organization getOrganization(String organizationId){
        logger.debug("In Licensing Service.getOrganization,tmx-correlation-id= {}", UserContextHolder.getContext().getCorrelationId());

        ResponseEntity<Organization> restExchange =
                restTemplate.exchange(
                        "http://localhost:8090/api/organization/v1/organizations/{organizationId}",
                        HttpMethod.GET,
                        null, Organization.class, organizationId);

        return restExchange.getBody();
    }
}
