package com.hepengju.mockdata.util;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import static java.net.URLEncoder.encode;

/**
 * Web工具类: 取IP地址,获取当前Request等
 *
 * @author he_pe 2018-03-05
 */
@Component
public class WebUtil {


    /**
     * 处理文件下载的通用方法
     */
    public static void handleFileDownload(String attachment) {
        String fileName = attachment;
        // 这种方式: 谷歌/火狐OK, IE/Edge不行
        //try {fileName = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");} catch (UnsupportedEncodingException e) {}

        // 这种方式: 谷歌/火狐/IE/Edge都可以
        try {
            fileName = encode(attachment, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
        }
        HttpServletResponse response = WebUtil.getHttpServletResponse();
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"; filename*=utf-8''" + fileName);

        // 保留原始的文件名, 便于前端解析 --> 需要使用; 两种都设置下
        // var filename = resp.headers['original-filename'];
        // filename = decodeURIComponent(escape(filename));
        String webDecodeName = new String(attachment.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        response.setHeader("Original-Filename", webDecodeName);

        // 以下两个也加入, 以便可能的使用
        response.setHeader("Original-Filename-Java", attachment);
        response.setHeader("Original-Filename-Encode", fileName);

        // 暴露响应头
        response.setHeader("Access-Control-Expose-Headers", "*");

    }


    public static void handleFileDownload(String attachment, byte[] byteArray) {
        handleFileDownload(attachment);
        if (byteArray != null) {
            try {
                ServletOutputStream outputStream = WebUtil.getHttpServletResponse().getOutputStream();
                outputStream.write(byteArray);
                outputStream.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static HttpServletResponse getHttpServletResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

}
