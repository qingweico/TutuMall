package cn.qingweico.dto;

import java.io.InputStream;

/**
 * 生成图片辅助工具类
 *
 * @author 周庆伟
 * @date 2020/09/10
 */
public class ImageHolder {
    private String imageForm;
    private InputStream image;
    private String imageName;

    public ImageHolder(InputStream image, String imageName) {
        this.image = image;
        this.imageName = imageName;
    }

    public ImageHolder() {
    }

    public String getImageForm() {
        return imageForm;
    }

    public void setImageForm(String imageForm) {
        this.imageForm = imageForm;
    }

    public InputStream getImage() {
        return image;
    }

    public void setImage(InputStream image) {
        this.image = image;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
