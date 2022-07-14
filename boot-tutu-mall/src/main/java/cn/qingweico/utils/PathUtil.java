package cn.qingweico.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 周庆伟
 * @date 2020/09/21
 */
@Component
@ConfigurationProperties(prefix = "base")
public class PathUtil {
    private static final String SEPARATOR = System.getProperty("file.separator");

    public static final String OS = "Windows 10";

    private static String windowBasePath;

    private static String linuxBasePath;

    private static String shopPath;

    private static String headLinePath;

    private static String shopCategoryPath;

    private static String userPath;

    public void setWindowBasePath(String windowBasePath) {
        PathUtil.windowBasePath = windowBasePath;
    }

    public void setLinuxBasePath(String linuxBasePath) {
        PathUtil.linuxBasePath = linuxBasePath;
    }

    public void setShopPath(String shopPath) {
        PathUtil.shopPath = shopPath;
    }

    public void setHeadLinePath(String headLinePath) {
        PathUtil.headLinePath = headLinePath;
    }

    public void setShopCategoryPath(String shopCategoryPath) {
        PathUtil.shopCategoryPath = shopCategoryPath;
    }

    public void setUserPath(String userPath) {
        PathUtil.userPath = userPath;
    }

    public static String getImageBasePath() {
        String os = System.getProperty("os.name");
        String path;
        if (OS.equals(os)) {
            path = windowBasePath;
        } else {
            path = linuxBasePath;
        }
        path = path.replace("/", SEPARATOR);
        return path;
    }

    public static String getShopImagePath(Long shopId) {
        String imagePath = shopPath + shopId + SEPARATOR;
        return imagePath.replace("/", SEPARATOR);
    }

    public static String getHeadLineImagePath() {
        return headLinePath.replace("/", SEPARATOR);
    }

    public static String getShopCategoryPath() {
        return shopCategoryPath.replace("/", SEPARATOR);
    }

    public static String getUserPath() {
        return userPath.replace("/", SEPARATOR);
    }
}
