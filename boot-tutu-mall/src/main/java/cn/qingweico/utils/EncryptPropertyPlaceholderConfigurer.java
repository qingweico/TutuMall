package cn.qingweico.utils;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * 数据库信息加密
 *
 * @author 周庆伟
 * @date 2020/11/16
 */
public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
    /**
     * 需要加密的字段数组
     */
    private final String[] encryptPropNames = {
            "spring.datasource.username",
            "spring.datasource.password"
    };

    /**
     * 对关键的属性进行转换
     */
    @NonNull
    @Override
    protected String convertProperty(@NonNull String propertyName, @NonNull String propertyValue) {
        if (isEncryptProp(propertyName)) {
            // 对已加密的字段进行解密工作
            return DesUtil.getDecryptString(propertyValue);
        } else {
            return propertyValue;
        }
    }

    /**
     * 该属性是否已加密
     *
     * @param propertyName propertyName
     * @return boolean
     */
    private boolean isEncryptProp(String propertyName) {
        // 若等于需要加密的field, 则进行加密
        for (String encryptPropertyName : encryptPropNames) {
            if (propertyName.equals(encryptPropertyName)) {
                return true;
            }
        }
        return false;
    }


}
