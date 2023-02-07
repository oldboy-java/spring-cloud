package com.thoughtmechanix.authentication.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;


@TableName(value = "t_user_orgs")
@Data
public class UserOrganization implements Serializable {
    @TableField(value = "organization_id")
    String organizationId;

    @TableField(value = "user_id" )
    Long userId;

}
