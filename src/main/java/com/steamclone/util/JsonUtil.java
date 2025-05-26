package com.steamclone.util;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class JsonUtil {
    private static final Gson gson = new Gson();

    // 将对象转换为JSON并写入响应
    public static void writeJsonResponse(HttpServletResponse response, Object data) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String json = gson.toJson(data);

        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }
}