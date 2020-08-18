package com.lzp.commonlibdemo.entity;

import com.fast.common.bean.BaseBean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Model extends BaseBean {
    private String title;
    private String content;
    private String imgUrl;

}