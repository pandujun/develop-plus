package io.github.pandujun.develop.plus.web.configuration;

import okhttp3.OkHttpClient;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public enum TrustCertOkHttpSingleton {
    INSTANCE;

    private final OkHttpClient okHttpClient;

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    TrustCertOkHttpSingleton() {

        OkHttpClient createOkHttpClient;
        try {
            createOkHttpClient = getHttpClient(null);

        } catch (Exception ex) {
            createOkHttpClient = new OkHttpClient()
                    .newBuilder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build();
        }
        okHttpClient = createOkHttpClient;
    }

    public OkHttpClient getHttpClient(String proxyHostAndPortColon) throws Exception {
        //TrustManagerFactory 不是Okhttp的API内容，是JDK本身对秘钥管理的支持类
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init((KeyStore) null);
        TrustManager[] trustManagersDefault = trustManagerFactory.getTrustManagers();
        if (trustManagersDefault.length != 1 || !(trustManagersDefault[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagersDefault));
        }
        X509TrustManager trustManager = (X509TrustManager) trustManagersDefault[0];
        //初始化一个管理器的数组，用于初始化后面的上下文，这里只提供了一个X509管理器。 X509是一个秘钥格式的标准。
        final TrustManager[] trustManagers = {
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                }
        };

        //初始化SSL的上下文
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustManagers, null);
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, trustManager)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS);

        if (StringUtils.hasText(proxyHostAndPortColon)) {
            clientBuilder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHostAndPortColon.split(":")[0], Integer.parseInt(proxyHostAndPortColon.split(":")[1]))));

        }
        //永远返回true，对所有的host都信任
        clientBuilder.setHostnameVerifier$okhttp((hosts, sslSession) -> {
            System.err.println("https hosts :" + hosts);
            return true;
        });
        return clientBuilder.build();
    }

}

