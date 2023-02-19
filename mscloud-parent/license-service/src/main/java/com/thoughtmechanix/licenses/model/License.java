package com.thoughtmechanix.licenses.model;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

@Data
@TableName(value = "licenses")
public class License{

  @TableId(value = "license_id", type = IdType.UUID)
  private String licenseId;

  @TableField(value = "organization_id")
  private String organizationId;

  @TableField(exist = false)
  private String organizationName ="";
  @TableField(exist = false)
  private String contactName ="";
  @TableField(exist = false)
  private String contactPhone ="";
  @TableField(exist = false)
  private String contactEmail ="";
  @TableField(exist = false)
  private String productName;

  @TableField(value = "license_type")
  private String licenseType;

  @TableField(value = "license_max")
  private Integer licenseMax;

  @TableField(value = "license_allocated")
  private Integer licenseAllocated;

  @TableField(value="comment")
  private String comment;


  public License withId(String id){
    this.setLicenseId(id);
    return this;
  }

  public License withOrganizationId(String organizationId){
    this.setOrganizationId(organizationId);
    return this;
  }

  public License withProductName(String productName){
    this.setProductName(productName);
    return this;
  }

  public License withLicenseType(String licenseType){
    this.setLicenseType(licenseType);
    return this;
  }

  public License withLicenseMax(Integer licenseMax){
    this.setLicenseMax(licenseMax);
    return this;
  }

  public License withLicenseAllocated(Integer licenseAllocated){
    this.setLicenseAllocated(licenseAllocated);
    return this;
  }

  public License withComment(String comment){
    this.setComment(comment);
    return this;
  }

  public License withOrganizationName(String organizationName){
    this.setOrganizationName(organizationName);
    return this;
  }

  public License withContactName(String contactName){
    this.setContactName(contactName);
    return this;
  }

  public License withContactPhone(String contactPhone){
    this.setContactPhone(contactPhone);
    return this;
  }

  public License withContactEmail(String contactEmail){
    this.setContactEmail(contactEmail);
    return this;
  }




}
