package xyz.haohao.sso.client.sso.test;

import xyz.haohao.sso.core.security.HttpAuthorizationUtil;
import xyz.haohao.sso.core.util.URLUTF8Encoder;
import lombok.extern.log4j.Log4j2;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Test;

import java.io.IOException;

/**
 * @Author: zaishu.ye
 * @Date: 2019/11/25 17:48
 */
@Log4j2
public class OkHttp3Client {
    @Test
    public void verify() throws Exception {
        OkHttpClient okHttpClient = new OkHttpClient();
        String server = "http://localhost:8380";
        String method = "GET";
        String path = "/api/ticket/verifyGrantTicket";
        String globalTicketId = "3_3c09c1e8a5354c09be7cfa5c7093fa28";

        String key="64fbc60bfacbfef24d76a68d15b85fb2", secret = "a57d43d82ab30e980800967694cc1729";

        String date = HttpAuthorizationUtil.date();
        String authorization = HttpAuthorizationUtil.generateAuthorization("/api/ticket/verify", method, date, key, secret);

        String url = server.concat(path).concat("?globalTid=").concat(globalTicketId).concat("&Date=").concat(URLUTF8Encoder.encode(date)).concat("&Authorization=").concat(authorization);
        Request request = new Request.Builder()
                .get()
                .url(url)
                .addHeader("Date", date)
                .addHeader("Authorization", authorization)
                .build();

        log.info("request: {}" , request.url());

        okhttp3.Call call = okHttpClient.newCall(request);
        try {
            log.info("start call");
            Response response = call.execute();
            log.info(response.body().string());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void evict() throws Exception {
        OkHttpClient okHttpClient = new OkHttpClient();
        String server = "http://localhost:8380";
        String method = "GET";
        String path = "/api/ticket/verifyGrantTicket";
        String globalTicketId = "3_3c09c1e8a5354c09be7cfa5c7093fa28";

        String key="64fbc60bfacbfef24d76a68d15b85fb2", secret = "a57d43d82ab30e980800967694cc1729";

        String date = HttpAuthorizationUtil.date();
        String authorization = HttpAuthorizationUtil.generateAuthorization("/api/ticket/verify", method, date, key, secret);

        String url = server.concat(path).concat("?globalTid=").concat(globalTicketId).concat("&Date=").concat(URLUTF8Encoder.encode(date)).concat("&Authorization=").concat(authorization);
        Request request = new Request.Builder()
                .get()
                .url(url)
                .addHeader("Date", date)
                .addHeader("Authorization", authorization)
                .build();

        log.info("request: {}" , request.url());

        okhttp3.Call call = okHttpClient.newCall(request);
        try {
            log.info("start call");
            Response response = call.execute();
            log.info(response.body().string());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
