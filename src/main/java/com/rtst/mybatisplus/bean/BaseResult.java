package com.rtst.mybatisplus.bean;


import java.util.HashMap;
import java.util.Map;

public class BaseResult extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public BaseResult() {
        put("code", 200);
        put("msg", "操作成功");
    }

    public static BaseResult error() {
        return error(500, "操作失败");
    }

    public static BaseResult operate(boolean b){
        if(b){
            return BaseResult.ok();
        }
        return BaseResult.error();
    }

    public static BaseResult error(String msg) {
        return error(500, msg);
    }

    public static BaseResult error(int code, String msg) {
        BaseResult r = new BaseResult();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static BaseResult ok(String msg) {
        BaseResult r = new BaseResult();
        r.put("msg", msg);
        return r;
    }

    public static BaseResult ok(Map<String, Object> map) {
        BaseResult r = new BaseResult();
        r.putAll(map);
        return r;
    }

    public static BaseResult ok() {
        return new BaseResult();
    }

    public static BaseResult error401() {
        return error(401, "你还没有登录");
    }

    public static BaseResult error403() {
        return error(403, "你没有访问权限");
    }

    public static BaseResult data(Object data){
        return BaseResult.ok().put("data",data);
    }

    public static BaseResult page(Object page){
        return BaseResult.ok().put("page",page);
    }

    @Override
    public BaseResult put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
