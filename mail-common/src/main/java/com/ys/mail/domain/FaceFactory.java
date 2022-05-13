package com.ys.mail.domain;

import com.ys.mail.constant.FigureConstant;
import org.springframework.stereotype.Component;

/**
 * 第三方人脸识别抽象工厂
 * @author ghdhj
 */
@Component
public class FaceFactory {

    /**
     * 第三方人脸识别实例获取,0->腾讯云,1->其它第三方等
     * @param type 类型
     * @return 返回值
     */
    public FaceProvider create(int type){
        FaceProvider faceProvider = null;
        switch (type){
            default:
            case FigureConstant.INT_ZERO:
                faceProvider = new TencentFaceIdInfo();
                break;
            case FigureConstant.INT_ONE:
                break;
        }
        return faceProvider;
    }
}
