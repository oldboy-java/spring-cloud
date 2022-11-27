package com.thoughtmechanix.authentication.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.thoughtmechanix.authentication.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {

    @Select("SELECT c.`code` FROM  t_role_permission a  INNER JOIN t_user_role b " +
            "ON a.role_id = b.role_id INNER JOIN t_permission c ON c.id = a.permission_id" +
            " WHERE b.user_id = #{userId}")
    List<String> listPermissionsByUserId(Long userId);
}
