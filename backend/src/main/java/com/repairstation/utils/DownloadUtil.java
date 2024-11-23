package com.repairstation.utils;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
public class DownloadUtil {
    public static void download(HttpServletResponse response, String path, String lastName) {
        File file = new File(path);
        if (!file.exists()) {
            log.error("文件未找到: {}", file.getAbsolutePath());
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return; // 直接返回，不需要继续执行
        }

        lastName = URLEncoder.encode(lastName, StandardCharsets.UTF_8);

        // 设置响应头
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment; filename=\"" + lastName + "\"");
        response.setContentLengthLong(file.length());

        try (InputStream inputStream = new FileInputStream(file);
             OutputStream outputStream = response.getOutputStream()) {

            byte[] buffer = new byte[1024 * 1024 * 10]; // 1MB 缓冲区
            int bytesRead;

            // 读取文件并写入响应
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.flush(); // 确保所有数据都被写入

        } catch (IOException e) {
            log.error("IO异常: {}", e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
