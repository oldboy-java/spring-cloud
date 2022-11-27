package com.thoughtmechanix.organization.model;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;


@TableName(value = "organizations")
@Data
public class Organization {
    @TableId(value = "organization_id",type = IdType.ID_WORKER)
    String id;

    @TableField(value = "name")
    String name;

    @TableField(value = "contact_name")
    String contactName;

    @TableField(value = "contact_email")
    String contactEmail;

    @TableField(value = "contact_phone")
    String contactPhone;

}
