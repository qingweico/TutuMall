package cn.qingweico.utils;

import com.google.code.kaptcha.Constants;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author 周庆伟
 * @date 2020/09/24
 */

public class CodeUtil {
    /**
     * 检查验证码
     *
     * @param request HttpServletRequest
     * @return true or false
     */
    public static boolean checkVerificationCode(HttpServletRequest request) {
        String actualVerificationCode = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        String userInputVerificationCode = HttpServletRequestUtil.getString(request, "userInputVerificationCode");
        return actualVerificationCode.equals(userInputVerificationCode);
    }

    /**
     * 生成二维码的图片流
     *
     * @param content String
     * @param resp    HttpServletResponse
     * @return BitMatrix
     */
    public static BitMatrix generateQrCodeStream(String content, HttpServletResponse resp) {
        // 给响应添加头部信息, 主要是告诉浏览器返回的是图片流
        resp.setHeader("Cache-Control", "no-store");
        resp.setHeader("Pragma", "no-cache");
        resp.setDateHeader("Expires", 0);
        resp.setContentType("image/png");
        // 设置图片的文字编码以及内边框距
        Map<EncodeHintType, Object> hints = new HashMap<>(5);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 0);
        BitMatrix bitMatrix;
        try {
            // 参数顺序分别为:
            // 编码内容
            // 编码类型
            // 生成图片宽度
            // 生成图片高度
            // 设置参数
            bitMatrix = new MultiFormatWriter().encode(content,
                    BarcodeFormat.QR_CODE,
                    200,
                    200,
                    hints);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
        return bitMatrix;
    }

    public static void generateQrCode(HttpServletResponse response,
                                      String content,
                                      String urlPrefix,
                                      String authUrl,
                                      String urlMiddle,
                                      String urlSuffix) {
        try {
            String longUrl = urlPrefix
                    + authUrl
                    + urlMiddle
                    + URLEncoder.encode(content, "UTF-8")
                    + urlSuffix;
            String shortUrl = ShortNetAddressUtil.getShortAddress(longUrl);
            BitMatrix qR = CodeUtil.generateQrCodeStream(shortUrl, response);
            Objects.requireNonNull(qR);
            MatrixToImageWriter.writeToStream(qR, "png", response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
