
//表单通用配置 列表页 详情页都需要
layui.define(['jquery'],function(exports){
    var obj =
        {
            tableNameRest:"forders_rest",
            tableName:"forders",
            moduleName:"forders",//sys_module的moduleName
            formVerifyEditData:{//详情页提交时表单校验 使用方法参考layui官网 表单验证规则

            },
            form:{

            }
        }
    exports('forders_config',obj);
})
