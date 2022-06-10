package com.ys.mail.plugins;

import com.ys.mail.annotation.EnumDocumentValid;
import com.ys.mail.constant.StringConstant;
import com.ys.mail.constant.WarningsConstant;
import com.ys.mail.enums.IPairs;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.EnumTool;
import com.ys.mail.util.ListMapUtil;
import springfox.documentation.builders.ParameterBuilder;

/**
 * 枚举文档处理工具
 *
 * @author CRH
 * @date 2022-06-09 16:20
 * @since 1.0
 */
public class EnumDocumentUtil {

    /**
     * 统一处理文档
     *
     * @param enumDocument     枚举文档注解（该注解中的枚举类必须实现IPairs接口）
     * @param parameterBuilder 参数构建器
     * @param originalValue    原有值
     */
    @SuppressWarnings(WarningsConstant.UNCHECKED)
    public static void handlerDocument(EnumDocumentValid enumDocument, ParameterBuilder parameterBuilder, String paramName, String originalValue) {
        // 将枚举文档注解中的枚举类型尝试转换
        Class<? extends IPairs<?, ?, ?>> iPairsClass = (Class<? extends IPairs<?, ?, ?>>) enumDocument.enumClass()
                                                                                                      .asSubclass(IPairs.class);

        // 获取注解属性值
        String coverValue = enumDocument.coverValue();
        String jointMark = enumDocument.jointMark();
        String delimiter = enumDocument.delimiter();
        int[] include = enumDocument.include();
        int[] exclude = enumDocument.exclude();
        boolean document = enumDocument.isDocument();

        if (document) {
            // 获取枚举值列表拼接后的字符串
            String enumList = EnumTool.values(iPairsClass, ListMapUtil.valueOf(include), ListMapUtil.valueOf(exclude), jointMark, delimiter);
            // 修改 context->ParameterBuilder中的description值：该值由 原有值+枚举列表
            if (BlankUtil.isEmpty(coverValue)) {
                if (BlankUtil.isEmpty(originalValue)) {
                    originalValue = paramName;
                }
                if (BlankUtil.isNotEmpty(enumList)) {
                    coverValue = String.format("%s%s%s", originalValue, StringConstant.ZH_COLON, enumList);
                } else {
                    coverValue = originalValue;
                }
            }
            parameterBuilder.description(coverValue);
        }
    }
}
