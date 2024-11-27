package com.repairstation.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import com.repairstation.common.R;
import com.repairstation.domain.po.Orders;
import com.repairstation.domain.po.Staff;
import com.repairstation.domain.vo.FileVO;
import com.repairstation.service.OrdersService;
import com.repairstation.service.StaffService;
import com.repairstation.utils.DBUtils;
import com.repairstation.utils.DownloadUtil;
import com.repairstation.utils.JWTUtils;
import com.repairstation.utils.ScheduledUtils;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {
    @Value("${project-config.photo-path}")
    private String photoPath;

    @Value("${project-config.database-backup-path}")
    private String DBBackupPath;



    @Autowired
    private OrdersService ordersService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private DBUtils dbUtils;

    @Cacheable(value = "commonCache", key = "'check-dir'")
    @PostMapping("/check-dir")
    public R<List<FileVO>> checkDir() {
        List<FileVO> list = new ArrayList<>();
        File directory = new File(photoPath);

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                // 根据文件修改时间进行排序
                Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());

                for (File file : files) {
                    FileVO fileVO = new FileVO();
                    fileVO.setName(file.getName());

                    Instant instant = Instant.ofEpochMilli(file.lastModified());
                    fileVO.setDate(instant.atZone(ZoneId.systemDefault()).toLocalDateTime());
                    list.add(fileVO);
                }
            }
        }

        return R.success(list);
    }

    @CacheEvict(value = "commonCache", key = "'check-dir'")
    @DeleteMapping("/del")
    public R<String> del(@RequestParam String name) {
        File directory = new File(photoPath, name);
        try {
            deleteDirectory(directory);
            String id = name.split("-")[0];
            Orders order = ordersService.getById(id);
            if (order.getStatus() != 2) {
                order.setStatus(2);
                ordersService.updateById(order);
            }

            return R.success("删除成功");
        } catch (IOException e) {
            log.error(e.getMessage());
            return R.error("删除失败: " + e.getMessage());
        }
    }

    @CacheEvict(value = "commonCache", key = "'check-dir'")
    @PostMapping("/uploads/{id}")
    public R<String> uploads(@RequestParam("files")MultipartFile[] files, @PathVariable String id) {
        if (files == null || files.length == 0) {
            log.error("没有接收到任何文件");
            return R.error("上传失败：没有接收到任何文件");
        }
        log.info("{}", files.length);
        // 创建上传目录
        Orders order = ordersService.getById(id);
        Staff staff = staffService.getById(order.getStaffId());
        //给文件夹取名
        String fileName = photoPath + File.separator + id + "-" + staff.getName() + "(上传)";
        File uploadDirFile = new File(fileName);
        if (!uploadDirFile.exists()) {
            boolean flag = uploadDirFile.mkdirs();
            log.info("uploads方法创建文件：{}", flag);
        }

        ExecutorService executorService = Executors.newFixedThreadPool(20); // 设置线程池
        List<Future<R<String>>> futures = new ArrayList<>();

        for (MultipartFile file : files) {
            futures.add(executorService.submit(() ->
                    upload(file, fileName))); // 提交任务

        }

        // 处理结果
        for (Future<R<String>> future : futures) {
            try {
                R<String> result = future.get(); // 获取结果
                if (result.getCode() == 0) {
                    executorService.shutdown(); // 关闭线程池
                    return result;
                }
            } catch (Exception e) {
                log.error("上传文件处理出错: {}", e.getMessage());
            }
        }

        executorService.shutdown(); // 关闭线程池
        return R.success("上传成功: ");
    }

    @GetMapping("/download-photo")
    public void download(@RequestParam String name, HttpServletResponse response) {
        name = File.separator + name.replace("*", File.separator);
        //log.info("{}", name);
        try {
            File file = new File(photoPath, name);
            FileInputStream fileInputStream = new FileInputStream(file);
            ServletOutputStream outputStream = response.getOutputStream();

            // 根据文件扩展名设置响应的内容类型
            String fileExtension = getFileExtension(file.getName()).toLowerCase();
            switch (fileExtension) {
                case "jpg":
                case "jpeg":
                    response.setContentType("image/jpeg");
                    break;
                case "png":
                    response.setContentType("image/png");
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unsupported file type"); // 不支持的类型
                    return;
            }

            int len;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }

            outputStream.close();
            fileInputStream.close();

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @GetMapping("/download-zip")
    public void downloadZip(HttpServletResponse response, @RequestParam String status) {
        DownloadUtil.download(response, DBBackupPath + ".zip", status.equals("1") ? "数据库原有数据.zip" : "已处理好的数据库.zip");
    }


    @GetMapping("/getPhotoList")
    public R<List<String>> getAllPhotos(@RequestParam String dirName) {
        List<String> photoUrls = new ArrayList<>();
        File folder = new File(photoPath, dirName); // 设置文件夹路径

        // 读取文件夹中的所有文件
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".jpeg"));

        if (files != null) {
            for (File file : files) {
                // 构建完整的 URL 并添加到列表中
                photoUrls.add(dirName + "*" + file.getName());
            }
        }
        return R.success(photoUrls); // 返回照片的 URL 列表
    }

    @GetMapping("/backup-db")
    public R<String> backupDB(HttpServletRequest req, @RequestParam String status) {
        String id = JWTUtils.getIdByRequest(req);

        if (!id.equals("20240704")) return R.error("不好意思啊，你不是管理员，这次不行哦！");

        File dbFile = new File(DBBackupPath);
        if (dbFile.exists()) {
            FileUtil.del(dbFile);
        }

        ProcessBuilder mysql = dbUtils.backupMySQL();
        ProcessBuilder mongodb = dbUtils.backupMongoDB();
        try {
            // 启动进程
            Process process = mysql.start();
            Process process1 = mongodb.start();
            // 等待进程结束
            int exitCode = process.waitFor();
            int exitCode1 = process1.waitFor();
            if (exitCode == 0 && exitCode1 == 0) {
                //复制图片进文件夹，顺便备份了
                if (status.equals("1"))
                    FileUtil.copy(photoPath, DBBackupPath, true);
                ZipUtil.zip(DBBackupPath);

                //设置时限，一小时自动删除
                ScheduledUtils.delFile1Hour(DBBackupPath);
                ScheduledUtils.delFile1Hour(DBBackupPath + ".zip");
            } else {
                log.error("等待失败");
            }
        } catch (InterruptedException | IOException e) {
            log.error(e.getMessage());
        }


        return R.success("备份成功！");
    }


    private R<String> upload(MultipartFile file, String fileName) {
        log.info("文件名是：{}", file.getOriginalFilename());

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            log.error("文件名为空");
            return R.error("上传失败");
        }



        File destFile = new File(fileName, file.getOriginalFilename());
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("存储文件失败: {}", e.getMessage());
            return R.error("上传失败");
        }


        return R.success(fileName);
    }
    private void deleteDirectory(File directory) throws IOException {
        if (directory.exists()) {
            // 如果是目录，则递归删除所有文件
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file); // 递归删除子目录
                    } else {
                        Files.delete(file.toPath()); // 删除文件
                    }
                }
            }
            // 删除空目录
            Files.delete(directory.toPath());
        } else {
            throw new IOException("目录不存在: " + directory.getAbsolutePath());
        }
    }
    private String getFileExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf('.');
        return (lastIndexOfDot == -1) ? "" : fileName.substring(lastIndexOfDot + 1);
    }

}
