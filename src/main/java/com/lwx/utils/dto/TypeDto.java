package com.lwx.utils.dto;

import com.lwx.utils.entity.Type;
import lombok.Data;

/**
 * @author luwenxin
 * @create 2020/1/16
 */
@Data
public class TypeDto extends Type {
    private String id;
    private String type;
    private String carNo;
}
