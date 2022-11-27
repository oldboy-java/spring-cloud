package com.thoughtmechanix.organization.services;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.thoughtmechanix.organization.mapper.OrganizationMapper;
import com.thoughtmechanix.organization.model.Organization;
import org.springframework.stereotype.Service;

@Service
public class OrganizationService extends ServiceImpl<OrganizationMapper, Organization> {

    public Organization getOrg(String organizationId) {
        return this.selectById(organizationId);
    }

    public void saveOrg(Organization org){
        this.insert(org);
    }

    public void updateOrg(Organization org){
        this.updateById(org);

    }

    public void deleteOrg(String orgId){
        this.deleteById(orgId);
    }
}
