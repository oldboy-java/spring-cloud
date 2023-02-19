package com.thoughtmechanix.licenses.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.thoughtmechanix.licenses.clients.OrganizationFeignClient;
import com.thoughtmechanix.licenses.clients.OrganizationRestTemplateClient;
import com.thoughtmechanix.licenses.mapper.LicenseMapper;
import com.thoughtmechanix.licenses.model.License;
import com.thoughtmechanix.licenses.model.Organization;
import com.thoughtmechanix.licenses.service.LicenseService;
import com.thoughtmechanix.licenses.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
public class LicenseServiceImpl extends ServiceImpl<LicenseMapper, License> implements LicenseService {
    @Autowired
    private LicenseMapper licenseMapper;
    @Autowired
    OrganizationRestTemplateClient organizationRestClient;
    @Autowired
    private OrganizationFeignClient organizationFeignClient;
    @Autowired
    HttpServletRequest request;

    @Override
    public License getLicense(String organizationId,String licenseId) {
        log.info("token={}", request.getHeader(UserContext.AUTH_TOKEN));
        EntityWrapper<License> wrapper = new EntityWrapper<>();
        wrapper.eq("license_id", licenseId);
        wrapper.eq("organization_id", organizationId);
        License license = selectOne(wrapper);
        Organization org = getOrganization(organizationId);
        return license
                .withOrganizationName( org.getName())
                .withContactName( org.getContactName())
                .withContactEmail( org.getContactEmail() )
                .withContactPhone( org.getContactPhone() );
    }

//    @HystrixCommand
    private Organization getOrganization(String organizationId) {
//        return organizationRestClient.getOrganization(organizationId);
        return organizationFeignClient.getOrganization(organizationId);
    }

    private void randomlyRunLong(){
        Random rand = new Random();
        int randomNum = rand.nextInt((3 - 1) + 1) + 1;
        if (randomNum==3) {
            sleep();
        }
    }

    private void sleep(){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @HystrixCommand(fallbackMethod = "buildFallbackLicenseList",
            threadPoolKey = "licenseByOrgThreadPool",
            threadPoolProperties =
                    {@HystrixProperty(name = "coreSize",value="30"),
                            @HystrixProperty(name="maxQueueSize", value="10"),
                    },
            commandProperties={
                    @HystrixProperty(name="circuitBreaker.requestVolumeThreshold", value="10"),
                    @HystrixProperty(name="circuitBreaker.errorThresholdPercentage", value="75"),
                    @HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds", value="7000"),
                    @HystrixProperty(name="metrics.rollingStats.timeInMilliseconds", value="15000"),
                    @HystrixProperty(name="metrics.rollingStats.numBuckets", value="5")}
    )
    @Override
    public List<License> getLicensesByOrg(String organizationId){
        randomlyRunLong();
        EntityWrapper<License> wrapper = new EntityWrapper<>();
        wrapper.eq("organization_id", organizationId);
         return selectList(wrapper);
    }

    private List<License> buildFallbackLicenseList(String organizationId){
        List<License> fallbackList = new ArrayList<>();
        License license = new License()
                .withId("0000000-00-00000")
                .withOrganizationId( organizationId )
                .withProductName("Sorry no licensing information currently available");

        fallbackList.add(license);
        return fallbackList;
    }

    @Override
    public void saveLicense(License license){
        license.withId( UUID.randomUUID().toString());
        licenseMapper.insert(license);
    }

    @Override
    public void updateLicense(License license){
        licenseMapper.updateById(license);
    }

    @Override
    public void deleteLicense(License license){
        licenseMapper.deleteById( license.getLicenseId());
    }

}
