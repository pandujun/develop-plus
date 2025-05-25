package io.github.pandujun.develop.plus.web.advice;

import io.github.pandujun.develop.plus.core.constant.HeaderIgnoreAutoPackageConstant;
import io.github.pandujun.develop.plus.core.result.BusinessException;
import io.github.pandujun.develop.plus.core.result.Result;
import io.github.pandujun.develop.plus.core.result.ResultEnums;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * 全局异常，数据封装
 */
@RestControllerAdvice
public class GlobalServerExceptionHandler {
    private final static Logger logger = LoggerFactory.getLogger(GlobalServerExceptionHandler.class);

    /**
     * 封装业务异常
     */
    @ExceptionHandler(value = BusinessException.class)
    public <E> Result<E> handleBusinessException(BusinessException ex,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response) {
        logger.error("GlobalServerExceptionHandler#BusinessException：", ex);
        exceptionExtend(request, response);
        return Result.error(ResultEnums.getMapCode(ex.getCode()), ex.getMsg());
    }

    /**
     * 参数校验异常
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public <E> Result<E> handleValidException(MethodArgumentNotValidException ex,
                                              HttpServletRequest request,
                                              HttpServletResponse response) {
        logger.error("GlobalServerExceptionHandler#MethodArgumentNotValidException：", ex);
        exceptionExtend(request, response);
        String errorMsg;
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        if (!CollectionUtils.isEmpty(errors)) {
            errorMsg = errors.get(0).getDefaultMessage();
        } else {
            errorMsg = ResultEnums.PARAM_ERROR.msg();
        }
        return Result.error(ResultEnums.PARAM_ERROR, errorMsg);
    }


    /**
     * 请求方式不支持异常
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public <E> Result<E> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex,
            HttpServletRequest request) throws HttpRequestMethodNotSupportedException {
        logger.error("GlobalServerExceptionHandler#HttpRequestMethodNotSupportedException URI：{}, EX：", request.getRequestURI(), ex);
        throw ex;
    }

    /**
     * 其他非业务异常
     */
    @ExceptionHandler(value = Exception.class)
    public <E> Result<E> handleException(Exception ex,
                                         HttpServletRequest request,
                                         HttpServletResponse response) {
        logger.error("GlobalServerExceptionHandler#Other Exception Message: ", ex);
        exceptionExtend(request, response);
        return Result.error(ResultEnums.INTERNAL_SERVER_ERROR);
    }

    static void exceptionExtend(HttpServletRequest request,
                                HttpServletResponse response) {
        //Feign服务间调用
        String feignCaller = request.getHeader(HeaderIgnoreAutoPackageConstant.FEIGN_TAG);
        if (StringUtils.hasText(feignCaller)) {
            response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
        }
    }
}
