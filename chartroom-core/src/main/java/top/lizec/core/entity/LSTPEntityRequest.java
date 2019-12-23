package top.lizec.core.entity;

/*
GET /login LZTP/1.0
HOST: chartroom.lizec.top
Connect: keep-alive
User-Agent: LzcClient/1.0(Windows NT 10; Win64)

{"username":"GGBoy","password":"g85fdjf"}

*
* */
public class LSTPEntityRequest {
    private String method;
    private String path;
    private String protocol;
    private String host;
    private String userAgent;
    private String body;

    private LSTPEntityRequest() {
    }

    private LSTPEntityRequest(String method, String path, String body) {
        this.method = method;
        this.path = path;
        this.protocol = "LZTP/1.0";
        this.host = "chartroom.lizec.top";
        this.userAgent = "LzcClient/1.0(Windows NT 10; Win64)";
        this.body = body;
    }

    public static LSTPEntityRequest parseFrom(String request) {
        LSTPEntityRequest lstpEntityRequest = new LSTPEntityRequest();
        String[] entity = request.split("\n\n");
        if (entity.length != 2) {
            System.err.println(request);
            throw new IllegalArgumentException("LSTP请求格式错误");
        }

        String header = entity[0];
        lstpEntityRequest.body = entity[1];

        String[] lines = header.split("\n");
        String[] arg = lines[0].split(" ");

        if (arg.length != 3) {
            System.err.println(request);
            throw new IllegalArgumentException("LSTP请求格式错误");
        }

        lstpEntityRequest.method = arg[0];
        lstpEntityRequest.path = arg[1];
        lstpEntityRequest.protocol = arg[2];
        for (int i = 1; i < lines.length; i++) {
            String[] info = lines[i].split(":");
            if (info.length != 2) {
                System.err.println(request);
                throw new IllegalArgumentException("LSTP请求格式错误");
            }

            switch (info[0]) {
                case "HOST":
                    lstpEntityRequest.host = info[1].trim();
                    break;
                case "User-Agent":
                    lstpEntityRequest.userAgent = info[1].trim();
                    break;
            }
        }
        return lstpEntityRequest;
    }

    public static LSTPEntityRequest createGetWith(String path, String body) {
        return new LSTPEntityRequest("POST", path, body);
    }

    public static void main(String[] args) {
        String request = "GET /login LZTP/1.0\n" +
                "HOST: chartroom.lizec.top\n" +
                "Connect: keep-alive\n" +
                "User-Agent: LzcClient/1.0(Windows NT 10; Win64)\n" +
                "\n" +
                "{\"username\":\"GGBoy\",\"password\":\"g85fdjf\"}";

        LSTPEntityRequest enetity = LSTPEntityRequest.parseFrom(request);
        System.out.println(enetity);
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHost() {
        return host;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return method + " " + path + " " + protocol + "\n" +
                "HOST:" + host + "\n" +
                "User-Agent:" + userAgent + "\n" +
                "\n" +
                body;
    }
}


