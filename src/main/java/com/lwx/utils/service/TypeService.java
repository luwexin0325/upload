package com.lwx.utils.service;

import com.lwx.utils.dto.TypeDto;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author luwenxin
 * @create 2020/1/16
 */
public interface TypeService {
    Map<String, String> add(TypeDto typeDto);
}
