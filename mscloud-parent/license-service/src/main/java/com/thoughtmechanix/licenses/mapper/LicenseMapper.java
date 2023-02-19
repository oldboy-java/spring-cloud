package com.thoughtmechanix.licenses.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.thoughtmechanix.licenses.model.License;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LicenseMapper extends BaseMapper<License> {

}
