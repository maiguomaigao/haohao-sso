package xyz.haohao.sso.client.sso.test;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import xyz.haohao.sso.core.ReturnT;
import xyz.haohao.sso.core.sso.ticket.AppTicket;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: zaishu.ye
 * @Date: 2019/11/26 13:41
 */
@Log4j2
public class JsonConvertTest {

    @Test
    public void testToString(){

        List list = Arrays.asList("a , b ,c ".split(","));
        log.info(list);
    }

    @Test
    public void testToObject(){
        String dataJson = "{\"version\":\"65c71ec40390470090c86f109581b217\",\"expireMinutes\":15,\"freshTime\":1574746845844,\"userId\":\"3\",\"loginName\":\"haohao\",\"userName\":\"haohao\",\"loginType\":null}";
        AppTicket session = JSONObject.parseObject(dataJson, AppTicket.class);
        log.info(session);

        String returnTJson = "{\"code\":200,\"msg\":null,\"data\":{\"version\":\"65c71ec40390470090c86f109581b217\",\"expireMinutes\":15,\"freshTime\":1574746845844,\"userId\":\"3\",\"loginName\":\"haohao\",\"userName\":\"haohao\",\"loginType\":null},\"fail\":false,\"success\":true}";

        ReturnT<AppTicket> returnT = JSONObject.parseObject(returnTJson, new TypeReference<ReturnT<AppTicket>>(){});

        log.info(returnT);


        returnTJson = "{\"code\":2000,\"msg\":\"sys app auth failed\",\"data\":null,\"fail\":true,\"success\":false}";

        returnT = JSONObject.parseObject(returnTJson, new TypeReference<ReturnT<AppTicket>>(){});

        log.info(returnT);
    }
}
