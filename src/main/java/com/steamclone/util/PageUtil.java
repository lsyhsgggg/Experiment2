package com.steamclone.util;

public class PageUtil {

    // 计算总页数
    public static int calculateTotalPages(int totalItems, int pageSize) {
        if (pageSize <= 0) {
            return 0;
        }
        return (totalItems + pageSize - 1) / pageSize;
    }
}