package io.github.pandujun.develop.plus.web.utils;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.pandujun.develop.plus.core.constant.CommonSymbolConstant;
import io.github.pandujun.develop.plus.core.result.ResultEnums;
import io.github.pandujun.develop.plus.core.utils.GsonUtils;
import io.github.pandujun.develop.plus.web.bean.RedisNoPrefixClient;
import io.github.pandujun.develop.plus.web.bo.ProxyInfoBO;
import io.github.pandujun.develop.plus.web.configuration.RequestConfig;
import io.github.pandujun.develop.plus.web.enums.TrustCertOkHttpSingleton;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class HttpUtils {
    private final static Logger logger = LoggerFactory.getLogger(HttpUtils.class);
    private static RedisNoPrefixClient redisNoPrefixClient;
    private static RedissonClient redissonClient;
    private static volatile boolean initialized = false;

    // 代理ip和端口获取锁
    private static final String PROXY_IP_PORT_GET_LOCK = "PROXY_IP_PORT_GET_LOCK";
    // 爬虫代理ip和端口
    private static final String REPTILE_PROXY_IP_PORT = "REPTILE_PROXY_IP_PORT:";
    // 爬虫代理池
    private static final String REPTILE_PROXY_POOL = "REPTILE_PROXY_POOL";
    // 爬虫代理ip和端口有效期(15s内不在使用)
    private static final Long REDUNDANT_TIME = 15L;

    private static final MediaType MEDIA_TYPE_JSON = MediaType.get("application/json; charset=utf-8");
    private static final MediaType MEDIA_TYPE_FORM = MediaType.get("application/x-www-form-urlencoded; charset=utf-8");

    private static final OkHttpClient defaultOkHttpClient = TrustCertOkHttpSingleton.INSTANCE.getOkHttpClient();

    public static final String ACCEPT = "Accept";
    public static final String ACCEPT_CONTENT = "application/json, text/plain, */*";
    public static final String ACCEPT_LANGUAGE = "Accept-Language";
    public static final String ACCEPT_LANGUAGE_CONTENT = "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6";
    public static final String CONNECTION = "Connection";
    public static final String CONNECTION_CONTENT = "keep-alive";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_TYPE_CONTENT_JSON = "application/json";
    public static final String Origin = "Origin";
    public static final String Referer = "Referer";
    public static final String SecFetchDest = "Sec-Fetch-Dest";
    public static final String SecFetchMode = "Sec-Fetch-Mode";
    public static final String SecFetchSite = "Sec-Fetch-Site";
    public static final String UserAgent = "User-Agent";
    public static final String SecChUa = "Sec-Ch-Ua";
    public static final String SecChUaMobile = "Sec-Ch-Ua-Mobile";
    public static final String SecChUaPlatform = "Sec-Ch-Ua-Platform";

    /**
     * post请求
     *
     * @param url 请求地址
     * @param requestJson 请求参数
     * @param headers 请求头
     * @param returnClazz 返回参数类型
     * @return 请求结果
     * @param <T> 返回参数类型
     */
    public static <T> T postJson(String url, String requestJson, Map<String, String> headers, Class<T> returnClazz) {
        return requestAndProxy(url, requestJson, null, headers, returnClazz, false, MEDIA_TYPE_JSON);
    }

    public static <T> T postJso(String url, String requestJson, Map<String, String> headers, Class<T> returnClazz, boolean proxyFlag) {
        return requestAndProxy(url, requestJson, null, headers, returnClazz, proxyFlag, MEDIA_TYPE_JSON);
    }

    public static <T> T postJson(String url, String requestJson, Map<String, String> headers, Type type) {
        return requestAndProxy(url, requestJson, null, headers, type, false, MEDIA_TYPE_JSON);
    }

    public static <T> T postJson(String url, String requestJson, Map<String, String> headers, Type type, boolean proxyFlag) {
        return requestAndProxy(url, requestJson, null, headers, type, proxyFlag, MEDIA_TYPE_JSON);
    }

    /**
     * get请求
     *
     * @param url 请求地址
     * @param headers 请求头
     * @param returnClazz 返回参数类型
     * @return 请求结果
     * @param <T> 返回参数类型
     */
    public static <T> T getParam(String url, Map<String, String> headers, Class<T> returnClazz) {
        return requestAndProxy(url, null, null, headers, returnClazz, false, null);
    }

    public static <T> T getParam(String url, Map<String, String> headers, Class<T> returnClazz, boolean proxyFlag) {
        return requestAndProxy(url, null, null, headers, returnClazz, proxyFlag, null);
    }

    public static <T> T getParam(String url, Map<String, String> headers, Type type) {
        return requestAndProxy(url, null, null, headers, type, false, null);
    }

    public static <T> T getParam(String url, Map<String, String> headers, Type type, boolean proxyFlag) {
        return requestAndProxy(url, null, null, headers, type, proxyFlag, null);
    }

    /**
     * post请求（表单）
     *
     * @param url 请求地址
     * @param headers 请求头
     * @param returnClazz 返回参数类型
     * @return 请求结果
     * @param <T> 返回参数类型
     */
    public static <T> T postForm(String url, Map<String, String> headers, Map<String, Object> formData, Class<T> returnClazz) {
        return requestAndProxy(url, null, formData, headers, returnClazz, false, null);
    }

    public static <T> T postForm(String url, Map<String, String> headers, Map<String, Object> formData, Class<T> returnClazz, boolean proxyFlag) {
        return requestAndProxy(url, null, formData, headers, returnClazz, proxyFlag, null);
    }

    public static <T> T postForm(String url, Map<String, String> headers, Map<String, Object> formData, Type type) {
        return requestAndProxy(url, null, formData, headers, type, false, null);
    }

    public static <T> T postForm(String url, Map<String, String> headers, Map<String, Object> formData, Type type, boolean proxyFlag) {
        return requestAndProxy(url, null, formData, headers, type, proxyFlag, null);
    }

    public static InputStream getFile(String url, String requestJson, Map<String, String> headers) {
        return requestAndProxy(url, requestJson, null, headers, InputStream.class, false, MEDIA_TYPE_JSON);
    }

    public static InputStream getFile(String url, String requestJson, Map<String, String> headers, boolean proxyFlag) {
        return requestAndProxy(url, requestJson, null, headers, InputStream.class, proxyFlag, MEDIA_TYPE_JSON);
    }


    /**
     * 请求(可代理)
     *
     * @param url 请求地址
     * @param requestJson 请求参数（请求参数和表单参数二选一）
     * @param formData 表单数据（请求参数和表单参数二选一）
     * @param headers 请求头
     * @param returnType 响应参数类型
     * @param proxyFlag 是否代理
     * @return 响应结果
     * @param <T> 响应参数类型
     */
    private static <T> T requestAndProxy(
            String url,
            String requestJson,
            Map<String, Object> formData,
            Map<String, String> headers,
            Object returnType,
            boolean proxyFlag, MediaType mediaType) {
        String requestId = UUID.randomUUID().toString();
        logger.info("requestId:{}, url:{}, requestJson:{}", requestId, url, requestJson);
        Request.Builder requestBuilder = new Request.Builder()
                .url(url);
        // 设置请求体
        if (Objects.nonNull(requestJson)) {
            requestBuilder.post(RequestBody.Companion.create(requestJson, mediaType));
        } else if (Objects.nonNull(formData)) {
            // 构建表单数据
            StringBuilder formBody = new StringBuilder();
            if (!CollectionUtils.isEmpty(formData)) {
                formData.forEach((key, value) -> {
                    if (!formBody.isEmpty()) {
                        formBody.append("&");
                    }
                    formBody.append(key).append("=").append(value);
                });
            }
            requestBuilder.post(RequestBody.create(formBody.toString(), MEDIA_TYPE_FORM));
        }else {
            requestBuilder.get();
        }

        // 设置请求头
        if (!CollectionUtils.isEmpty(headers)) {
            headers.forEach(requestBuilder::addHeader);
        }

        // 判断是否使用代理client
        OkHttpClient okHttpClient = getOkHttpClientProxy(proxyFlag);

        try (Response response = okHttpClient.newCall(requestBuilder.build()).execute()) {
            if (response.code() == HttpStatus.OK.value()) {
                ResponseBody responseBody = response.body();
                if (Objects.nonNull(responseBody)) {
                    if (returnType == InputStream.class) {
                        byte[] bytes = responseBody.bytes();
                        return (T) new ByteArrayInputStream(bytes);
                    }
                    String responseJson = responseBody.string();
                    logger.info("postJson requestId:{}, url:{}, requestJson:{}, headers:{}, response:{}",
                            requestId, url, requestJson, GsonUtils.getGson().toJson(headers),
                            StringUtils.hasText(responseJson) ? responseJson.substring(0, Math.min(responseJson.length(), 2000)) : "");
                    if (Objects.nonNull(returnType)) {
                        if (returnType == String.class) {
                            return (T) responseJson;
                        }

                        if (returnType instanceof Class) {
                            return GsonUtils.getGson().fromJson(responseJson, (Class<T>) returnType);
                        } else if (returnType instanceof Type) {
                            return GsonUtils.getGson().fromJson(responseJson, (Type) returnType);
                        }
                        throw ResultEnums.PARAM_ERROR.getException("未知返回类型");
                    }
                }
            }
            logger.info("postJson return null requestId:{}, url:{}, code:{} requestJson:{}, headers:{}",
                    requestId, url, response.code(), requestJson, GsonUtils.getGson().toJson(headers));
        } catch (Exception ex) {
            logger.error("do postJson requestId:{}, error: { }", requestId, ex);
        }

        return null;
    }

    /**
     * 获取client(可代理）
     *
     * @param proxyFlag 代理ip和端口 中间用:分隔符隔开
     * @return OkHttpClient
     */
    private static OkHttpClient getOkHttpClientProxy(Boolean proxyFlag) {
        if (proxyFlag) {
            String proxyHostAndPortColon = getProxyIpAndPortDynamic(null);
            try {
                return TrustCertOkHttpSingleton.INSTANCE.getHttpClient(proxyHostAndPortColon);
            } catch (Exception e) {
                return new OkHttpClient()
                        .newBuilder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .build();
            }

        } else {
            return defaultOkHttpClient;
        }
    }

    /**
     * 获取代理ip和端口（动态）
     *
     * @param oldProxyIpAndPort 旧代理ip和端口
     * @return 新代理ip和端口
     */
    public static String getProxyIpAndPortDynamic(String oldProxyIpAndPort) {
        ensureInitialized();
        if (StringUtils.hasText(oldProxyIpAndPort)) {
            Long export = redisNoPrefixClient.getExpire(REPTILE_PROXY_IP_PORT + oldProxyIpAndPort);
            if (Objects.nonNull(export) && export > REDUNDANT_TIME) {
                logger.info("爬取 ==》代理IP未过期，可继续使用：{}", oldProxyIpAndPort);
                return null;
            }
        }

        //从代理池中获取
        Set<String> proxyIpPortSet = redisNoPrefixClient.sMembers(REPTILE_PROXY_POOL);
        if (!CollectionUtils.isEmpty(proxyIpPortSet)) {
            for (String ipAndPort : proxyIpPortSet) {
                Long export = redisNoPrefixClient.getExpire(REPTILE_PROXY_IP_PORT + ipAndPort);
                if (Objects.nonNull(export) && export > REDUNDANT_TIME) {
                    logger.info("爬取 ==》代理IP未过期，可继续使用：{}", ipAndPort);
                    return ipAndPort;
                } else {
                    redisNoPrefixClient.sRemove(REPTILE_PROXY_POOL, ipAndPort);
                }
            }
        }

        RLock lock = redissonClient.getLock(PROXY_IP_PORT_GET_LOCK);
        boolean tryLock;

        try {
            tryLock = lock.tryLock(30, 60, TimeUnit.SECONDS);
            if (tryLock) {
                //调用接口获取
                String proxyIpAndPort = getProxyInfo(redisNoPrefixClient);
                logger.info("爬取 ==》代理IP已过期，关闭旧的连接并使用新代理IP：{}", proxyIpAndPort);
                return proxyIpAndPort;
            } else {
                throw ResultEnums.INTERNAL_SERVER_ERROR.getException("获取代理ip 锁 失败");
            }
        } catch (InterruptedException e) {
            logger.error("获取代理ip 锁 失败：", e);
            throw ResultEnums.INTERNAL_SERVER_ERROR.getException("获取代理ip 锁 失败");
        } finally {
            // 确保锁被正确释放
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    /**
     * 获取代理ip
     *
     * @param redisNoPrefixClient redisNoPrefixClient
     * @return String
     */
    private static String getProxyInfo(RedisNoPrefixClient redisNoPrefixClient) {
        RequestConfig requestConfig = SpringUtils.getBean(RequestConfig.class);
        String proxyGetUrl = requestConfig.getProxyGetDynamicUrl();
        if (!StringUtils.hasText(proxyGetUrl)) {
            throw ResultEnums.PARAM_ERROR.getException("代理获取地址未配置，请在request.proxyGetUrl参数中配置相关地址 ");
        }
        //再次获取

        Set<String> proxyIpPortSet = redisNoPrefixClient.sMembers(REPTILE_PROXY_POOL);
        if (!CollectionUtils.isEmpty(proxyIpPortSet)) {
            for (String ipAndPort : proxyIpPortSet) {
                Long export = redisNoPrefixClient.getExpire(REPTILE_PROXY_IP_PORT + ipAndPort);
                if (Objects.nonNull(export) && export > REDUNDANT_TIME) {
                    return ipAndPort;
                } else {
                    redisNoPrefixClient.sRemove(REPTILE_PROXY_POOL, ipAndPort);
                }
            }
        }

        //获取代理ip
        JsonObject jsonObject = HttpUtils.getParam(proxyGetUrl, null, JsonObject.class);
        if (Objects.nonNull(jsonObject) && 1000 == jsonObject.get("code").getAsInt()) {
            JsonArray jsonArray = jsonObject.getAsJsonArray("data");
            JsonObject proxyObject = jsonArray.get(0).getAsJsonObject();
            ProxyInfoBO proxyInfoBO = GsonUtils.getGson().fromJson(proxyObject.toString(), ProxyInfoBO.class);
            String ipPortStr = proxyInfoBO.getIp() + CommonSymbolConstant.COLON_E + proxyInfoBO.getPort();
            redisNoPrefixClient.set(REPTILE_PROXY_IP_PORT + ipPortStr, proxyObject.toString(), 175L);
            redisNoPrefixClient.sAdd(REPTILE_PROXY_POOL, ipPortStr);
            return ipPortStr;
        } else {
            logger.error("获取代理ip失败: {}", GsonUtils.getGson().toJson(jsonObject));
            throw ResultEnums.PARAM_ERROR.getException("获取代理IP失败");
        }
    }

    // 初始化方法，使用双重检查锁确保线程安全
    private static void ensureInitialized() {
        if (!initialized) {
            synchronized (HttpUtils.class) {
                if (!initialized) {
                    try {
                        // 使用 SpringUtils 获取 Bean
                        redisNoPrefixClient = SpringUtils.getBean(RedisNoPrefixClient.class);
                        redissonClient = SpringUtils.getBean(RedissonClient.class);
                        initialized = true;
                        logger.info("HttpUtils 懒加载初始化完成");
                    } catch (Exception e) {
                        logger.error("HttpUtils 初始化失败", e);
                        throw new RuntimeException("HttpUtils 初始化失败", e);
                    }
                }
            }
        }
    }

}
