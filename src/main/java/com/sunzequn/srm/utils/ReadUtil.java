package com.sunzequn.srm.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.*;
import java.util.List;

/**
 * Created by sloriac on 16-11-17.
 * <p>
 * 自己封装的文件读取工具类,包含一些基本的文件读取方法
 */
public class ReadUtil {


    private File file;
    private LineIterator lineIterator;

    public ReadUtil(File file) {
        this.file = file;
        try {
            lineIterator = FileUtils.lineIterator(file, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ReadUtil(String filePath) {
        file = new File(filePath);
        try {
            lineIterator = FileUtils.lineIterator(file, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 统计文件行数
     *
     * @return 文件行数
     */
    public long countLines() {
        if (lineIterator == null) {
            return 0;
        }
        long res = 0;
        while (lineIterator.hasNext()) {
            lineIterator.nextLine();
            res++;
        }
        return res;
    }

    /**
     * 按行读取文件
     *
     * @return 由文件每行字符串组成的List
     */
    public List<String> readByLine() {
        try {
            return FileUtils.readLines(file, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void close() {
        lineIterator.close();
    }

}
