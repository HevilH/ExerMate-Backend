package ExerMate.ExerMate.Frame.Util;

import com.alibaba.fastjson.JSONObject;
import ExerMate.ExerMate.Base.Constant.GlobalConstant;
import ExerMate.ExerMate.Base.Constant.NameConstant;
import ExerMate.ExerMate.Base.Error.CourseWarn;
import io.netty.handler.codec.http.multipart.FileUpload;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class ParseUtil {


    public static String getWarnStackInfo(CourseWarn courseWarn) {
        if (courseWarn == null)
            return null;
        String stackInfo = null;
        StackTraceElement[] stackTrace = courseWarn.getStackTrace();
        for (StackTraceElement stackTraceElement:stackTrace) {
            String className = stackTraceElement.getClassName();

            if (className.startsWith(NameConstant.PACKAGE_NAME + ".Biz.Controller")) {
                stackInfo = stackTraceElement.getClassName() + GlobalConstant.ARG_SPLIT
                        + stackTraceElement.getMethodName() + GlobalConstant.ARG_SPLIT
                        + stackTraceElement.getLineNumber() + GlobalConstant.STACK_SPLIT;
                break;
            }
        }
        if (stackInfo == null) {

            for (StackTraceElement stackTraceElement:stackTrace) {
                String className = stackTraceElement.getClassName();
                if (className.startsWith(NameConstant.PACKAGE_NAME)) {
                    stackInfo = stackTraceElement.getClassName() + GlobalConstant.ARG_SPLIT
                            + stackTraceElement.getMethodName() + GlobalConstant.ARG_SPLIT
                            + stackTraceElement.getLineNumber() + GlobalConstant.STACK_SPLIT;
                    break;
                }
            }
        }
        return stackInfo;
    }


    public static String getErrorStackInfo(Throwable recordError) {
        if (recordError == null)
            return null;
        StringBuilder stackInfo = new StringBuilder();
        StackTraceElement[] stackTrace = recordError.getStackTrace();
        for (StackTraceElement stackTraceElement:stackTrace) {
            String className = stackTraceElement.getClassName();
            if (className.startsWith(NameConstant.PACKAGE_NAME)) {
                stackInfo.append(stackTraceElement.getClassName())
                        .append(GlobalConstant.ARG_SPLIT)
                        .append(stackTraceElement.getMethodName())
                        .append(GlobalConstant.ARG_SPLIT)
                        .append(stackTraceElement.getLineNumber())
                        .append(GlobalConstant.STACK_SPLIT);
            }
        }
        return stackInfo.toString();
    }


    public static String getJSONString(JSONObject jsonObject) {
        for (String key:jsonObject.keySet()) {
            Object val = jsonObject.get(key);
            if (val == null)
                continue;
            if (val instanceof FileUpload) {
                FileUpload fileUpload = (FileUpload)val;
                jsonObject.put(key, "file-" + fileUpload.getFilename());
            } else {
                jsonObject.put(key, val);
            }
        }
        return jsonObject.toString();
    }


    public static Field[] getAllFields(final Class<?> cls) {
        final List<Field> allFieldsList = getAllFieldsList(cls);
        return allFieldsList.toArray(new Field[allFieldsList.size()]);
    }


    public static List<Field> getAllFieldsList(final Class<?> cls) {
        final List<Field> allFields = new ArrayList<Field>();
        Class<?> currentClass = cls;
        while (currentClass != null) {
            final Field[] declaredFields = currentClass.getDeclaredFields();
            for (final Field field : declaredFields) {
                allFields.add(field);
            }
            currentClass = currentClass.getSuperclass();
        }
        return allFields;
    }
}
