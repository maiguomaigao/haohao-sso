package xyz.haohao.sso.client.sso.net;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import xyz.haohao.sso.client.sso.conf.SsoConf;
import xyz.haohao.sso.core.ReturnT;
import xyz.haohao.sso.core.sso.conf.SsoParamName;
import xyz.haohao.sso.core.security.HttpAuthorizationUtil;
import xyz.haohao.sso.core.sso.ticket.AppTicket;
import lombok.extern.log4j.Log4j2;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 *
 * @Author: yezaishu@qq.com
 * @Date: 2019/12/12 12:12
 */
@Component
@Log4j2
public class ServerApiCaller {

    private static final String REFRESH_PATH = "/api/ticket/fresh";
    private static final String REFRESH_METHOD = "GET";

    private static final String GRANT_TICKET_VERIFY_PATH = "/api/ticket/verifyGrantTicket";
    private static final String GRANT_TICKET_VERIFY_METHOD = "GET";

    @Autowired
    private SsoConf ssoConf;

    public ReturnT<AppTicket> verifyGrantTicket(String grantTid) {
        log.info("verify GrantTicket...grantTid={}", grantTid);
        OkHttpClient okHttpClient = new OkHttpClient();

        String date = HttpAuthorizationUtil.date();
        String authorization = HttpAuthorizationUtil.generateAuthorization(GRANT_TICKET_VERIFY_PATH, GRANT_TICKET_VERIFY_METHOD, date, ssoConf.getAppKey(), ssoConf.getAppSecret());

        String url = ssoConf.getSsoServer().concat(GRANT_TICKET_VERIFY_PATH).concat("?").concat(SsoParamName.GRANT_TICKET_ID).concat("=").concat(grantTid);
        Request request = new Request.Builder()
                .get()
                .url(url)
                .addHeader("Date", date)
                .addHeader("Authorization", authorization)
                .build();

        okhttp3.Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            log.info("verify response: {}", response);
            if (response.isSuccessful()) {
                String body = response.body().string();//body.string() 只能被获取一次
                log.info("verify response body: {}", body);
                ReturnT<AppTicket> returnT = JSONObject.parseObject(body, new TypeReference<ReturnT<AppTicket>>(){});
                log.info("verify Result: {}", returnT);

                return returnT;
            } else {
                log.warn("verify GrantTicket fail. code={}, message={}", response.code(), response.message());
            }
        } catch (IOException e) {
            log.error("call server sso verify api exception.", e);
        }

        return ReturnT.fail("verify grant ticket failed. grantTid="+grantTid);
    }

    /**
     * call ticket fresh server api
     * @param tid AppTicket.tid
     */
    @Async
    public void refresh(String tid) {
        OkHttpClient okHttpClient = new OkHttpClient();

        String date = HttpAuthorizationUtil.date();
        String authorization = HttpAuthorizationUtil.generateAuthorization(REFRESH_PATH, REFRESH_METHOD, date, ssoConf.getAppKey(), ssoConf.getAppSecret());

        String url = ssoConf.getSsoServer().concat(REFRESH_PATH).concat("?").concat(SsoParamName.APP_TICKET_ID).concat(tid);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Date", date)
                .addHeader("Authorization", authorization)
                .build();

        log.info("request: {}" , request.url());

        okhttp3.Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            log.info("refresh response: {}, body={}", response, response.body().string());

            if (response.isSuccessful()) {
                log.info("refresh success");
                return;
            }
        } catch (IOException e) {
            log.error("call remote sso renewwal api exception.", e);
        }
    }

/*
    public ReturnT<GlobalTicket> verifyGlobalTicket(String globalTicketId){
        OkHttpClient okHttpClient = new OkHttpClient();

        String date = HttpAuthorizationUtil.isoOffSetDateTime();
        String authorization = HttpAuthorizationUtil.generateAuthorization(VERIFY_PATH, VERIFY_METHOD, date, ssoConfig.getAppKey(), ssoConfig.getAppSecret());

        String url = ssoConfig.getSsoServer().concat(VERIFY_PATH).concat("?").concat(SsoParamName.GLOBAL_TICKET_ID).concat("=").concat(globalTicketId);
        Request request = new Request.Builder()
                .get()
                .url(url)
                .addHeader("Date", date)
                .addHeader("Authorization", authorization)
                .build();

        log.info("request: {}" , request.url());

        okhttp3.Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            log.info("verify response: {}", response);
            if (response.isSuccessful()) {
                String body = response.body().string();//body.string()只能被请求一次
                log.info("verify response body: {}", body);
                ReturnT<GlobalTicket> returnT = JSONObject.parseObject(body, new TypeReference<ReturnT<GlobalTicket>>(){});
                log.info("parsed returnT:{}", returnT);

                return returnT;
            }
        } catch (IOException e) {
            log.error("call remote sso verify api exception.", e);
        }

        return ReturnT.fail("verify globalTicketId failed");
    }

    */
    /**
     * call token remove server api
     * @param globalTicketId
     *//*
    @Async
    public void removeGlobalTicket(String globalTicketId) {
        OkHttpClient okHttpClient = new OkHttpClient();

        String date = HttpAuthorizationUtil.isoOffSetDateTime();
        String authorization = HttpAuthorizationUtil.generateAuthorization(REMOVE_PATH, REMOVE_METHOD, date, ssoConfig.getAppKey(), ssoConfig.getAppSecret());

        String url = ssoConfig.getSsoServer().concat(VERIFY_PATH).concat("?").concat(SsoParamName.GLOBAL_TICKET_ID).concat(globalTicketId);
        Request request = new Request.Builder()
                .delete()
                .url(url)
                .addHeader("Date", date)
                .addHeader("Authorization", authorization)
                .build();

        log.info("request: {}" , request.url());

        okhttp3.Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            log.info("remove response: {}, body={}", response, response.body().string());

            if (response.isSuccessful()) {
                log.info("renmoveGlobalTicket success");
                return;
            }
        } catch (IOException e) {
            log.error("call remote sso remove api exception.", e);
        }
    }*/
}
