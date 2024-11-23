package com.repairstation.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
public class DBUtils {
    @Value("${project-config.database-backup-path}")
    private String DBBackupPath;

    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.data.mongodb.uri}")
    private String uri;

    public ProcessBuilder backupMySQL() {
        String[] command = {
                "mysqldump",
                "-u", username,
                "-p" + password,
                "-P", "3306",
                "repair_station"
        };


        File DBFile = new File(DBBackupPath + File.separator + "MySQL"
                + File.separator + getFormattedDate() + "备份");
        if (!DBFile.exists()) {
            boolean flag = DBFile.mkdirs();
            log.info("mysql文件夹创建成功：{}", flag);
        }
        // 指定输出文件
        File outputFile = new File(DBFile, "repair_station.sql");

        // 创建 ProcessBuilder 实例
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectOutput(outputFile); // 将输出重定向到文件
        return processBuilder;
    }

    public ProcessBuilder backupMongoDB() {
        File DBFile = new File(DBBackupPath + File.separator + "MongoDB"
                + File.separator + getFormattedDate() + "备份");
        if (!DBFile.exists()) {
            boolean flag = DBFile.mkdirs();
            log.info("MongoDB文件夹创建成功：{}", flag);
        }


        String[] command = {
                "mongodump",
                "--uri=" + uri,
                "--out=" + DBFile.getAbsolutePath() // 指定输出目录
        };


        // 创建 ProcessBuilder 实例
        return new ProcessBuilder(command);
    }

    private String getFormattedDate() {
        // 创建 SimpleDateFormat 实例
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        // 获取当前日期
        Date date = new Date();
        // 格式化日期
        return dateFormat.format(date);
    }
}
