package com.tp.bsserver;


import com.google.gson.JsonObject;
import com.tp.bsserver.common.Common;
import com.tp.bsserver.model.FormatType;
import com.tp.bsserver.model.SdkHttpResult;
import com.tp.bsserver.util.ApiHttpClient;
import com.tp.bsserver.util.ConvertTime;

public class Example {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String key = "bmdehs6pdlais";
        String secret = "oVd5GApLUmOcV1";
//
//        SdkHttpResult result = null;
//
        SdkHttpResult result = null;
//                ApiHttpClient.getToken(key, secret, "tp", "tp",
//                "http://qq.com/a.png", FormatType.json);
        result = ApiHttpClient.checkOnline(Common.APP_KEY, Common.SECRET, "tp", FormatType.json);
        JsonObject object;
        System.out.println(result);
//        List<String> userids = new ArrayList<String>();
//        userids.add("22");
//        result = ApiHttpClient.createGroup(key, secret, userids, "tp", "tp", FormatType.json);
//        System.out.println("token =" + result);


        String time = "2015-11-23 16:13:51.0";
//        String a[] = time.split("-");
//        String y = a[0];
//        String m = a[1];
//        String d = a[2];
//
//        System.out.println("  " + y + "\n" + m + "月" + d + "号");
        System.out.println(ConvertTime.convert(time));
    }
}
