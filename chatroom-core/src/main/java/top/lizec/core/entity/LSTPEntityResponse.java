package top.lizec.core.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LSTPEntityResponse {
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH-mm-ss");

    private String protocol;
    private String code;
    private String server;
    private String date;
    private String contentType;
    private String body;

    private LSTPEntityResponse() {
    }

    public LSTPEntityResponse(String code, String body) {
        this.protocol = "LZTP/1.0";
        this.code = code;
        this.server = "LzcServer";
        this.date = formatter.format(new Date());
        this.contentType = "text/json";
        this.body = body;
    }

    public static LSTPEntityResponse parseFrom(String response) {
        LSTPEntityResponse lstpEntityResponse = new LSTPEntityResponse();
        String[] entity = response.split("\n\n");
        if (entity.length != 2) {
            throw new IllegalArgumentException("LSTP请求格式错误");
        }

        String header = entity[0];
        lstpEntityResponse.body = entity[1];

        String[] lines = header.split("\n");
        String[] arg = lines[0].split(" ");

        if (arg.length != 2) {
            System.out.println(response);
            throw new IllegalArgumentException("LSTP请求格式错误");
        }
        lstpEntityResponse.protocol = arg[0];
        lstpEntityResponse.contentType = arg[1];

        for (int i = 1; i < lines.length; i++) {
            String[] info = lines[i].split(":");
            if (info.length != 2) {
                System.out.println(response);
                throw new IllegalArgumentException("LSTP请求格式错误");
            }

            switch (info[0]) {
                case "Server":
                    lstpEntityResponse.server = info[1].trim();
                    break;
                case "Content-Type":
                    lstpEntityResponse.contentType = info[1].trim();
                    break;
                case "Date":
                    lstpEntityResponse.date = info[1].trim();
            }
        }
        return lstpEntityResponse;
    }

    @Override
    public String toString() {
        return protocol + " " + code + "\n" +
                "Server:" + server + "\n" +
                "Date:" + date + "\n" +
                "Content-Type:" + contentType + "\n" +
                "\n" +
                body;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getCode() {
        return code;
    }

    public String getServer() {
        return server;
    }

    public String getDate() {
        return date;
    }

    public String getContentType() {
        return contentType;
    }

    public String getBody() {
        return body;
    }
}
