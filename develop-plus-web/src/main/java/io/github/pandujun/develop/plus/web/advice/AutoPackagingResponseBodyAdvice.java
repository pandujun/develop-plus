package io.github.pandujun.develop.plus.web.advice;

import io.github.pandujun.develop.plus.core.constant.CommonPathConstant;
import io.github.pandujun.develop.plus.core.constant.HeaderIgnoreAutoPackageConstant;
import io.github.pandujun.develop.plus.core.result.Result;
import io.github.pandujun.develop.plus.core.utils.GsonUtils;
import io.github.pandujun.develop.plus.web.annotation.AutoPackaging;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

/**
 * 返回参数封装
 * <p>
 * &#064;Author  pandujun
 * <p>
 * &#064;Date  2023/10/30 10:45
 */
@RestControllerAdvice
public class AutoPackagingResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(@NonNull MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object returnValue, @NonNull MethodParameter returnType, @NonNull MediaType selectedContentType, @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, @NonNull ServerHttpResponse response) {
        //swagger相关接口不处理
        String path = request.getURI().getPath();
        if (path.contains(CommonPathConstant.SWAGGER_PATH_CONTAINS) || path.contains(CommonPathConstant.SWAGGER_RESOURCES_CONTAINS)) {
            return returnValue;
        }
        //feign之间调用
        if (request.getHeaders().containsKey(HeaderIgnoreAutoPackageConstant.FEIGN_TAG)) {
            return returnValue;
        }
        //配置了自动封装的注解
        AutoPackaging autoPackaging = returnType.getMethodAnnotation(AutoPackaging.class);
        if (Objects.nonNull(autoPackaging) && !autoPackaging.value()) {
            return returnValue;
        }

        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        //获取返回值类型
        String returnClassType = returnType.getParameterType().getName();
        Class<?> superclass = returnType.getParameterType().getSuperclass();

        //如果返回值类型为void以及返回值为null，则默认返回成功
        if (Void.TYPE.getName().equals(returnClassType) ||
                (Objects.isNull(returnValue) && !String.class.getName().equals(returnClassType))) {
            return Result.success();
        } else if (Result.class.getName().equals(returnClassType) ||
                (Objects.nonNull(superclass) && Result.class.getName().equals(superclass.getName()))) {
            return returnValue;
        } else if (String.class.getName().equals(returnClassType)) {
            //返回类型为String封装后必须再次转为json格式的String
            //不转的话StringHttpMessageConverter出抛出ClassCastException异常
            //异常原因:
            //      步骤1: Spring加载所有可用的HttpMessageConverter
            //            此时StringHttpMessageConverter已被加载并且标记supports==true可用状态
            //      步骤2: 调用beforeBodyWrite进行统一的返回封装，此时返回的String类型被改为Result类
            //      步骤3: 调用StringHttpMessageConverter中的getContentLength()方法,
            //             此时返回类型已更改，类转换异常并抛出ClassCastException
            //解决原理:
            //      当返回类型为String时，将步骤2中得到的Result做toJSONString处理,
            //      使返回类型重新更改为String

            return GsonUtils.getGson().toJson(Result.success(returnValue));
        } else {
            return Result.success(returnValue);
        }
    }
}
