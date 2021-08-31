package ExerMate.ExerMate.Biz.Controller.Params;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import ExerMate.ExerMate.Base.Annotation.Required;
import ExerMate.ExerMate.Base.Error.ExerMateWarn;
import ExerMate.ExerMate.Frame.Util.ParseUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

/**
 * @描述 일반매개변수，주로 통용적인 조작을 담당한다
 **/
public abstract class CommonParams {

    protected abstract void beforeTransfer();


    public JSONObject toJsonObject() throws Exception {
        beforeTransfer();
        JSONObject jsonObject = new JSONObject();
        Field[] fields = ParseUtil.getAllFields(getClass());
        for (Field field : fields) {
            field.setAccessible(true);
            jsonObject.put(field.getName(), field.get(this));
        }
        return jsonObject;
    }


    public static Object transferJsonArr(JSONArray srcArr, Class type) {
        Object fillArr = Array.newInstance(type, srcArr.size());
        for (int i = 0; i < srcArr.size(); ++i) {
            Object subObj = srcArr.get(i);
            if (srcArr.get(i) instanceof JSONArray)
                Array.set(fillArr, i, transferJsonArr((JSONArray) subObj, type.getComponentType()));
            else {
                try {
                    Array.set(fillArr, i, subObj);
                } catch (Exception e) {
                    Array.set(fillArr, i, JSON.parseObject(subObj.toString(), type));
                }
            }
        }
        return fillArr;
    }


    public void fromJsonObject(JSONObject paramJson) throws Exception {
        Field[] fields = ParseUtil.getAllFields(getClass());
        for (Field field : fields) {
            if (paramJson.containsKey(field.getName())) {
                field.setAccessible(true);
                Object obj = paramJson.get(field.getName());

                if (field.getType().isArray()) {
                    Class subCls = field.getType().getComponentType();
                    if (obj instanceof JSONArray) {
                        JSONArray arr = (JSONArray) obj;
                        field.set(this, transferJsonArr(arr, field.getType().getComponentType()));
                    } else {
                        Object fillArr = Array.newInstance(subCls, 1);
                        Array.set(fillArr, 0, obj);
                        field.set(this, fillArr);
                    }
                } else {
                    try {
                        field.set(this, obj);
                    } catch (Exception e) {
                        field.set(this, JSON.parseObject(obj.toString(), field.getType()));
                    }
                }
            } else if (field.isAnnotationPresent(Required.class)) {
                throw new ExerMateWarn("default", field.getName() + "을 채워주세요");
            }
        }
    }


    @Override
    public String toString() {
        try {
            return ParseUtil.getJSONString(toJsonObject());
        } catch (Exception e) {
            return null;
        }
    }
}

