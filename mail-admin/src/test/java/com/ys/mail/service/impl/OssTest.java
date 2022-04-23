package com.ys.mail.service.impl;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.model.Bucket;
import com.tencent.cloud.Response;
import com.ys.mail.MailAdminApplication;
import com.ys.mail.config.CosConfig;
import com.ys.mail.entity.PcReview;
import com.ys.mail.entity.PmsProduct;
import com.ys.mail.service.CosService;
import com.ys.mail.service.PcReviewService;
import com.ys.mail.service.PmsBrandService;
import com.ys.mail.service.PmsProductService;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.DateTool;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Desc
 * @Author CRH
 * @Create 2022-01-05 15:13
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MailAdminApplication.class})
public class OssTest {

    @Autowired
    private CosService cosService;

    @Autowired
    private CosConfig cosConfig;

    @Autowired
    private PmsProductService pmsProductService;

    @Autowired
    private PcReviewService pcReviewService;

    @Autowired
    private PmsBrandService pmsBrandService;

    @Test
    public void getListBucketsTest() {
        Response stsCredential = cosService.getStsCredential(null);
        System.out.println("tmpSecretId：" + stsCredential.credentials.tmpSecretId);
        System.out.println("tmpSecretKey：" + stsCredential.credentials.tmpSecretKey);
        System.out.println("sessionToken：" + stsCredential.credentials.sessionToken);
        // 使用临时凭证进行[查询存储桶列表]操作
        COSClient cosClient = cosService.getOssClient(stsCredential.credentials);
        List<Bucket> buckets = cosClient.listBuckets();
        for (Bucket bucketElement : buckets) {
            String bucketName = bucketElement.getName();
            String bucketLocation = bucketElement.getLocation();
            System.out.println(bucketName + "," + bucketLocation);
        }
        cosClient.shutdown();
    }

    @Test
    public void batchUploadProductToCOS() {
        // 读取所有商品出来
        LambdaQueryWrapper<PmsProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.lt(PmsProduct::getPromotionType, 7);
        wrapper.eq(PmsProduct::getProductId, "1469282324213534720");
        wrapper.last("LIMIT 1");
        List<PmsProduct> allList = pmsProductService.list(wrapper);
        List<String> picIds = new ArrayList<>();

        // 并获取商品的所有图片名称
        allList.forEach(item -> {
            // 添加主图
            String pic = item.getPic();
            if (BlankUtil.isNotEmpty(pic)) {
                picIds.add(getPicNameByURI(item.getPic()));
            }
            // 添加相册图
            String albumPics = item.getAlbumPics();
            if (BlankUtil.isNotEmpty(albumPics)) {
                String[] albumPicsArray = albumPics.split(",");
                for (String s : albumPicsArray) {
                    picIds.add(getPicNameByURI(s));
                }
            }
            // 添加详情图
            String detailDesc = item.getDetailDesc();
            if (BlankUtil.isNotEmpty(detailDesc)) {
                List<String> list = parsePicDesc(detailDesc);
                picIds.addAll(list);
            }
        });
        System.out.println(picIds.size());

        // 读取本地图片库
        File[] files = FileUtil.ls("C:\\Users\\Administrator\\Desktop\\img\\");
        picIds.forEach(item -> {
            boolean flag = false;
            for (File file : files) {
                // 获取图片名称
                String fileName = file.getName();
                // 对比图片名称
                if (item.equals(fileName)) {
                    // 名称相同，则上传到cos
                    flag = true;
                    break;
                }
            }
            assert flag;
        });

        // 更新详情图片

    }

    // 获取图片名称
    private static String getPicNameByURI(String uri) {
        return uri.substring(5);
    }

    // 解析图片详情，返回
    private static List<String> parsePicDesc(String picDesc) {
        // <p><img src="http://j.huwing.cn:8018/img/4927c5ab4e924439abe59f09b89acde5.jpg" style="height:auto; width:100%" /></p>
        // 先进行分组
        String[] split = picDesc.split("</p>");
        List<String> list = new ArrayList<>();
        for (String s : split) {
            int index = s.indexOf("/img/");
            if (index > -1) {
                index = index + 5;
                String substring = s.substring(index, index + 36);
                list.add(substring);
            }
        }
        return list;
    }

    @Test
    public void test() {
        String todayNow = DateTool.getTodayNow();
        // 获取当天所有待审核的审核记录，需要退还资金（查询）
//        QueryWrapper<PcReview> reviewQueryWrapper = new QueryWrapper<>();
//        reviewQueryWrapper.eq("DATE(create_time)", todayNow)
//                .eq("review_state", NumberUtils.INTEGER_ZERO);
//        List<PcReview> todayReviewList = pcReviewService.list(reviewQueryWrapper);

        List<PcReview> todayList = pcReviewService.getTodayList(todayNow);

        System.out.println();
    }

    @Test
    public void getListTest(){
        // 查找指定的对象键是否存在
        String key = cosConfig.getUploadFolder()+"/img/1471786150199955456-1.jpg";
        try {
            COSClient cosClient = cosService.getOssClient(null);
            boolean objectExists = cosClient.doesObjectExist(cosConfig.getBucket(), key);
            System.out.println(objectExists);
        } catch (CosClientException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String str = "/img/5ac41ac5e6ca47f59edec7569f631238.jpg,/img/a708ba396b7447adbf3c9cba1a7098be.jpg";
        String[] albumPicsArray = str.split(",");
        for (String s : albumPicsArray) {
            System.out.println(getPicNameByURI(s));
        }
    }

}
