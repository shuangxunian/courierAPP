package com.my.kuaidi.admin.api.dict;

import com.my.kuaidi.core.service.CommonRestController;
import org.springframework.beans.factory.InitializingBean;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.math.*;
import java.util.List;
import java.util.Map;
import com.my.kuaidi.core.common.constant.PageConstant;
import com.my.kuaidi.core.common.exception.BusinessException;
import com.my.kuaidi.core.serialize.ResponseMsg;
import com.my.kuaidi.model.Dict;
import com.my.kuaidi.service.dict.service.DictService;

import com.my.kuaidi.core.utils.ListUtil;
import com.my.kuaidi.core.utils.MapUtil;
import com.my.kuaidi.core.utils.StringUtil;




@RequestMapping("/admin/dict_rest/")
@RestController
public class AdminDictRestController extends CommonRestController<Dict,Long> implements InitializingBean
{

    @Resource
    private DictService dictService;

    @RequestMapping(value = "search")
    public ResponseMsg search(
        @RequestParam(required = false) String uniqueField,
        @RequestParam(required = false) Long uniqueValue,
        @RequestParam(required = false,defaultValue = "20") Integer limit,
        @RequestParam(required = false) String keyword
    ){
        limit = Math.min(PageConstant.MAX_LIMIT,limit);
        List<Dict> list = null;
        Map<String,Object> query = new HashedMap();
        query.put("limit",limit);
        query.put("notSafeOrderBy","order_no asc");
        if(uniqueValue!=null){//说明是来初始化的
            query.put(uniqueField,uniqueValue);
            list = dictService.getModelList(query);
        }else {//正常搜索
            if(ListUtil.isBlank(list)){
                query.put("codeFirst",keyword);
                list = dictService.getModelList(query);
                query.remove("codeFirst");
            }
            if(ListUtil.isBlank(list)){
                query.put("nameFirst",keyword);
                list = dictService.getModelList(query);
                query.remove("nameFirst");
            }
        }
        return new ResponseMsg(list);
    }
    //分页查询
    @RequestMapping(value={"page"}, method={RequestMethod.GET})
    public ResponseMsg page(
        @RequestParam(required = false,value ="codeFirst")                            String codeFirst ,
        @RequestParam(required = false,value ="nameFirst")                            String nameFirst ,
        @RequestParam(required = false,value ="parentIdFirst")                            Long parentIdFirst ,
        @RequestParam int page,@RequestParam int limit,@RequestParam(required = false) String safeOrderBy)
    {
        limit = Math.min(limit, PageConstant.MAX_LIMIT);
        int start = (page - 1) * limit;
        Map<String,Object> query = new HashedMap();
        query.put("codeFirst",coverBlankToNull(codeFirst));
        query.put("nameFirst",coverBlankToNull(nameFirst));
        query.put("parentIdFirst",parentIdFirst);
        Integer count = dictService.getModelListCount(query);
        query.put("start",start);
        query.put("limit",limit);
        if(StringUtil.isBlank(safeOrderBy)){
            query.put("notSafeOrderBy","order_no asc");
        }else{
            query.put("safeOrderBy",safeOrderBy);
        }
        return new ResponseMsg(count,dictService.getModelList(query));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.commonService = dictService;
        super.primaryKey = "id";//硬编码此实体的主键名称
    }
}
