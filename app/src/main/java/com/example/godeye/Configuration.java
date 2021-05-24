package com.example.godeye;

public class Configuration {
    private static final String app_host = "xx.xx.xx.xx";
    private static final String domain_path = "https://" + app_host + "/";
    private static final String app_auth = domain_path + "/receiver.php";

    public static String getApp_host() {
        return app_host;
    }

    public static String getDomain_path() {
        return domain_path;
    }

    public static String getApp_auth() {
        return app_auth;
    }
}
