package com.my.kuaidi.admin.rmp.common;

import com.my.kuaidi.admin.rmp.util.HttpReqUtil;
import com.my.kuaidi.core.utils.StringUtil;

import java.util.Map;

/**
 * 返回参数排过序的url
 */
public class SortUrlBuilder {

    private String servletPath;
    private Map<String,String[]> reqMap;

    public SortUrlBuilder(String servletPath, Map<String, String[]> reqMap) {
        this.servletPath = servletPath;
        this.reqMap = reqMap;
    }

    public String buildSortUrl(){
        String queryString = HttpReqUtil.sortParams(reqMap,null);
        if(StringUtil.isBlank(queryString)){
            return servletPath;
        }
        return new StringBuilder(servletPath).append("?").append(queryString).toString();
    }
}
