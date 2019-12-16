import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import xyz.haohao.sso.core.ReturnT;
import xyz.haohao.sso.core.sso.conf.SsoParamName;
import xyz.haohao.sso.core.sso.enums.SsoCallbackType;
import xyz.haohao.sso.core.security.HttpAuthorizationUtil;
import xyz.haohao.sso.dao.domain.SysApp;
import lombok.extern.log4j.Log4j2;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.springframework.scheduling.annotation.Async;

import java.io.IOException;
import java.util.*;

/**
 * @Author: zaishu.ye
 * @Date: 2019/12/3 11:54
 */
@Log4j2
public class EvictTest {


    @Test
    public void test(){
        evictUser();
    }

    @Async
    void evictUser() {
        SysApp sysApp = new SysApp();
        sysApp.setUrl("http://sample-web1.haohao.xyz:8381");
        sysApp.setAppName("sample-web1");
        sysApp.setAppKey("64fbc60bfacbfef24d76a68d15b85fb1");
        sysApp.setAppSecret("a57d43d82ab30e980800967694cc1728");
        sysApp.setCallbackPath("/sso/callback");

        //okhttpGet(sysApp, "3");

        httpcomponentget(sysApp, "3");
    }

    //@Async
    public void okhttpGet(SysApp sysApp, String uid) {
        StopWatch watch = new StopWatch();
        watch.start();

        log.info("evictUser from {}. uid=: {} ", sysApp.getAppName(), uid);
        OkHttpClient okHttpClient = new OkHttpClient();

        String date = HttpAuthorizationUtil.date();
        String authorization = HttpAuthorizationUtil.generateAuthorization(sysApp.getCallbackPath(), "GET", date, sysApp.getAppKey(), sysApp.getAppSecret());

        watch.split();
        log.info("1 - "+watch.getSplitTime());

        String url = sysApp.getUrl().concat(sysApp.getCallbackPath()).concat("?")
                .concat(SsoParamName.CALLBACK_TYPE).concat("=").concat(SsoCallbackType.AUTO_EVICT.name())
                .concat("&").concat(SsoParamName.USER_ID).concat("=").concat(uid);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Date", date)
                .addHeader("Authorization", authorization)
                .build();

        watch.split();
        log.info("2 - "+watch.getSplitTime());

        okhttp3.Call call = okHttpClient.newCall(request);

        watch.split();
        log.info("3 - "+watch.getSplitTime());

        try {
            Response response = call.execute();

            watch.split();
            log.info("4 - "+watch.getSplitTime());

            if (response.isSuccessful()) {
                String body = response.body().string();//body.string() 只能被获取一次
                ReturnT returnT = JSONObject.parseObject(body, new TypeReference<ReturnT>(){});
                log.info("evictUser result from {}: {}", sysApp.getAppName(), returnT);
            }

            watch.split();
            log.info("5 - "+watch.getSplitTime());
            return;

        } catch (IOException e) {
            log.error("call app auto logout exception.", e);
        }
    }



    public void httpcomponentget(SysApp sysApp, String uid) {
        StopWatch watch = new StopWatch();
        watch.start();

        log.info("evictUser from {}. uid=: {} ", sysApp.getAppName(), uid);

        String url = sysApp.getUrl().concat(sysApp.getCallbackPath());

        Map<String, String> headers = new HashMap<>();

        String date = HttpAuthorizationUtil.date();
        String authorization = HttpAuthorizationUtil.generateAuthorization(sysApp.getCallbackPath(), "GET", date, sysApp.getAppKey(), sysApp.getAppSecret());
        headers.put("Date", date);
        headers.put("Authorization", authorization);


        Map<String, String> params = new HashMap<>();
        params.put(SsoParamName.CALLBACK_TYPE, SsoCallbackType.AUTO_EVICT.name());
        params.put(SsoParamName.USER_ID, uid);


        String result = this.get(url, params, headers);


        watch.split();
        log.info("2 - "+watch.getSplitTime());

        log.info("result {}", result);

        watch.split();
        log.info("3 - "+watch.getSplitTime());

    }

    public String get(String url, Map<String, String> params, Map<String, String> headers){
        HttpGet httpGet = null;
        CloseableHttpClient httpClient = null;
        try{
            // httpGet config
            httpGet = new HttpGet(url);

            // headers
            if (headers!=null && headers.size()>0) {
                for (Map.Entry<String, String> headerItem: headers.entrySet()) {
                    httpGet.setHeader(headerItem.getKey(), headerItem.getValue());
                }
            }

            // params
            if (params != null && !params.isEmpty()) {
                if (!url.contains("?")) {
                    url = url.concat("?");
                }

                Iterator<String> it = params.keySet().iterator();
                while (it.hasNext()) {
                    String name = it.next();
                    url = url.concat("&").concat(name).concat("=").concat(params.get(name));
                }

            }


            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
            httpGet.setConfig(requestConfig);

            // httpClient = HttpClients.createDefault();	// default retry 3 times
            // httpClient = HttpClients.custom().setRetryHandler(new DefaultHttpRequestRetryHandler(3, true)).build();
            httpClient = HttpClients.custom().disableAutomaticRetries().build();

            // parse response
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                if (response.getStatusLine().getStatusCode() == 200) {
                    String responseMsg = EntityUtils.toString(entity, "UTF-8");
                    EntityUtils.consume(entity);
                    return responseMsg;
                }
                EntityUtils.consume(entity);
            }
            log.info("http statusCode error, statusCode:" + response.getStatusLine().getStatusCode());
            return null;
        } catch (Exception e) {
            e.printStackTrace();
			/*StringWriter out = new StringWriter();
			e.printStackTrace(new PrintWriter(out));
			callback.setMsg(out.toString());*/
            return e.getMessage();
        } finally{
            if (httpGet != null) {
                httpGet.releaseConnection();
            }
            if (httpClient!=null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
