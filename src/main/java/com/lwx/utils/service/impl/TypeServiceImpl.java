package com.lwx.utils.service.impl;

import com.lwx.utils.dao.ITypeDao;
import com.lwx.utils.dto.TypeDto;
import org.springframework.beans.factory.annotation.Autowired;
import com.lwx.utils.service.TypeService;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author luwenxin
 * @create 2020/1/16
 */
@Service
public class TypeServiceImpl implements TypeService {
    @Autowired
    private ITypeDao iTypeDao;

    @Override
    public Map<String, String> add(TypeDto typeDto) {
        Map<String, String> map = new LinkedHashMap<>();
        iTypeDao.add(typeDto);
        map.put("200", "ok");
        return map;
    }
}
