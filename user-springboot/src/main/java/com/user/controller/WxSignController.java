package com.user.controller;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Arrays;


/**
 * 微信组件相关内容
 */
@RestController
public class WxSignController {


    private static final String token = "123456";


    @RequestMapping(value = "/download",method = RequestMethod.GET)
    public void downloadFile(@RequestParam String fileUrl, String fileName, HttpServletResponse response) throws Exception {
        URL url = new URL(fileUrl);
        if(StringUtils.isEmpty(fileName)){
            fileName = fileUrl.substring(fileUrl.lastIndexOf(47) + 1);
        }
        URLConnection conn = url.openConnection();
        InputStream inputStream = conn.getInputStream();
        response.reset();
        response.setContentType("application/octet-stream");
        //纯下载方式 文件名应该编码成UTF-8
        response.setHeader("Content-Disposition",
                "attachment; filename=" + URLEncoder.encode(fileName.toString(), "UTF-8"));
        byte[] buffer = new byte[1024];
        int len;
        OutputStream outputStream = response.getOutputStream();
        while ((len = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, len);
        }
        inputStream.close();
    }


    @RequestMapping(value = "/api/wx", method = RequestMethod.GET)
    public String getFromWx(String signature, String timestamp, String nonce, String echostr) {
        if (checkSignature(signature, timestamp, nonce)) {
            return echostr;
        }
        return "";
    }


    public boolean checkSignature(String signature, String timestamp, String nonce) {
        String[] arr = new String[]{token, timestamp, nonce};
        if(arr == null && arr.length == 0){
            return false;
        }
        //排序
        Arrays.sort(arr);
        //生成字符串
        StringBuffer content = new StringBuffer();
        for (int i = 0; i < arr.length; i++) {
            content.append(arr[i]);
        }
        //sha1加密
        String temp = getSha1(content.toString());
        return temp.equals(signature);
    }

    /**
     * Sha1加密方法
     *
     * @param str
     * @return
     */
    private String getSha1(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            return null;
        }
    }

}
