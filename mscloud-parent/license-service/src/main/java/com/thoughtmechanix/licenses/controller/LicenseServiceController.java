package com.thoughtmechanix.licenses.controller;

import com.thoughtmechanix.licenses.model.License;
import com.thoughtmechanix.licenses.service.LicenseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value="/v1/organizations/{organizationId}/licenses")
public class LicenseServiceController {
    @Autowired
    private LicenseService licenseService;


    @Autowired
    private HttpServletRequest request;

    private static final Logger logger = LoggerFactory.getLogger(LicenseServiceController.class);

    @RequestMapping(value={"/",""},method = RequestMethod.GET)
    public List<License> getLicenses( @PathVariable("organizationId") String organizationId) {
        return licenseService.getLicensesByOrg(organizationId);
    }

    @RequestMapping(value="/{licenseId}",method = RequestMethod.GET)
    public License getLicenses( @PathVariable("organizationId") String organizationId,
                                @PathVariable("licenseId") String licenseId) {
        logger.debug("Entering the license-service-controller");
        logger.debug("Found tmx-correlation-id in license-service-controller: {} ", request.getHeader("tmx-correlation-id"));
        return licenseService.getLicense(organizationId, licenseId);
    }

    @RequestMapping(value="{licenseId}",method = RequestMethod.PUT)
    public void updateLicenses( @PathVariable("licenseId") String licenseId, @RequestBody License license) {
        licenseService.updateLicense(license);
    }

    @RequestMapping(value="/",method = RequestMethod.POST)
    @PreAuthorize("#oauth2.hasScope('license:save')")  //增加scope验证，需要再注册客户端时设置客户端可访问的scope
    public void saveLicenses(@RequestBody License license) {
        licenseService.saveLicense(license);
    }

    @RequestMapping(value="{licenseId}",method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLicenses( @PathVariable("licenseId") String licenseId, @RequestBody License license) {
         licenseService.deleteLicense(license);
    }
}
