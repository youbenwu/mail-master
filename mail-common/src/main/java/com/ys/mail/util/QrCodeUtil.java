package com.ys.mail.util;

import cn.hutool.extra.qrcode.BufferedImageLuminanceSource;
import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.ys.mail.model.image.QrCode;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.util.Hashtable;

/**
 * 二维码生成工具
 *
 * @author 007
 * @date 2022-04-19 15:19
 * @since 1.0
 */
public class QrCodeUtil {

    private static final String CHARSET = "utf-8";
    private static final String FORMAT_NAME = "PNG";
    /**
     * 默认，二维码尺寸
     */
    private static final int QRCODE_SIZE = 400;
    /**
     * 默认，LOGO压缩宽度
     */
    private static final int WIDTH = 100;
    /**
     * 默认，LOGO压缩高度
     */
    private static final int HEIGHT = 100;

    public static void main(String[] args) throws Exception {
        // 存放在二维码中的内容
        String text = "http://cos.jitiger.cn/files/apk/qingchuangying_v1.02_202205131845.apk?RHseck6wxfYzZd4WFGjbEwaF3jCTJxS2";
        // 嵌入二维码的图片路径
        // String imgPath = "C:\\Users\\Administrator\\AppData\\Local\\Temp\\temp-download-899320843966558724-logo-qcy-144.png";
        String imgPath = "C:\\Users\\Administrator\\Desktop\\图片\\轻创营项目\\Logo\\qcy-144.png";
        // String imgPath = "C:\\Users\\Administrator\\Desktop\\图片\\001.png";
        // 生成的二维码的路径及名称
        String destPath = "C:\\Users\\Administrator\\AppData\\Local\\Temp\\temp-qrcode-qcy.png";
        //生成二维码
        QrCodeUtil.encode(text, imgPath, destPath, false);
        // 解析二维码
        String str = QrCodeUtil.decode(destPath);
        // 打印出解析出的内容
        System.out.println(str);
    }

    private static BufferedImage createImage(QrCode qrCode) throws Exception {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        // 设置空白边距
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(qrCode.getContent(), BarcodeFormat.QR_CODE, qrCode.getQrcodeSize(), qrCode.getQrcodeSize(), hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        if (BlankUtil.isNotEmpty(qrCode.getLogoPath())) {
            // 插入图片
            QrCodeUtil.insertImage(image, qrCode);
        }
        return image;
    }

    /**
     * 创建二维码
     *
     * @param content      二维码内容
     * @param imgPath      嵌入图片路径
     * @param needCompress 是否需要压缩
     * @return 二维码图片对象
     */
    private static BufferedImage createImage(String content, String imgPath, boolean needCompress) throws Exception {
        QrCode qrCode = QrCode.builder().content(content).needCompress(needCompress).qrcodeSize(QRCODE_SIZE)
                              .logoPath(imgPath).logoWidth(WIDTH).logoHeight(HEIGHT).build();
        return createImage(qrCode);
    }

    /**
     * 插入图片
     *
     * @param source 二维码图片源
     */
    private static void insertImage(BufferedImage source, QrCode qrCode) throws Exception {
        String logoPath = qrCode.getLogoPath();
        int logoWidth = qrCode.getLogoWidth();
        int logoHeight = qrCode.getLogoHeight();
        int qrcodeSize = qrCode.getQrcodeSize();
        File file = new File(logoPath);
        if (!file.exists()) {
            return;
        }
        Image src = ImageIO.read(new File(logoPath));
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        // 压缩LOGO
        if (qrCode.isNeedCompress()) {
            if (width > logoWidth) {
                width = logoWidth;
            }
            if (height > logoHeight) {
                height = logoHeight;
            }
            Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            // 绘制缩小后的图
            g.drawImage(image, 0, 0, null);
            g.dispose();
            src = image;
        }
        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (qrcodeSize - width) / 2;
        int y = (qrcodeSize - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }

    /**
     * 创建目录
     *
     * @param destPath 目录
     */
    private static void mkdirs(String destPath) {
        File file = new File(destPath);
        // 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
        if (!file.exists() && !file.isDirectory()) {
            boolean mkdirs = file.mkdirs();
        }
    }

    /**
     * 生成二维码
     */
    public static void encode(String content, String imgPath, String destPath, boolean needCompress) throws Exception {
        BufferedImage image = QrCodeUtil.createImage(content, imgPath, needCompress);
        mkdirs(destPath);
        ImageIO.write(image, FORMAT_NAME, new File(destPath));
    }

    public static BufferedImage encode(String content, String imgPath, boolean needCompress) throws Exception {
        return QrCodeUtil.createImage(content, imgPath, needCompress);
    }

    public static void encode(String content, String imgPath, String destPath) throws Exception {
        QrCodeUtil.encode(content, imgPath, destPath, false);
    }

    public static void encode(String content, File imgFile, File destFile) throws Exception {
        QrCodeUtil.encode(content, imgFile.getAbsolutePath(), destFile.getAbsolutePath(), false);
    }

    public static void encode(String content, File imgFile, File destFile, boolean needCompress) throws Exception {
        QrCodeUtil.encode(content, imgFile.getAbsolutePath(), destFile.getAbsolutePath(), needCompress);
    }

    public static Boolean encode(String content, File destFile) throws Exception {
        BufferedImage image = QrCodeUtil.createImage(content, null, false);
        return ImageIO.write(image, FORMAT_NAME, destFile);
    }

    public static void encode(String content, String destPath) throws Exception {
        QrCodeUtil.encode(content, null, destPath, false);
    }

    public static void encode(QrCode qrCode) throws Exception {
        BufferedImage image = QrCodeUtil.createImage(qrCode);
        String qrPath = qrCode.getQrPath();
        mkdirs(qrPath);
        ImageIO.write(image, FORMAT_NAME, new File(qrPath));
    }

    public static void encode(String content, String imgPath, OutputStream output, boolean needCompress) throws Exception {
        BufferedImage image = QrCodeUtil.createImage(content, imgPath, needCompress);
        ImageIO.write(image, FORMAT_NAME, output);
    }

    public static void encode(String content, OutputStream output) throws Exception {
        QrCodeUtil.encode(content, null, output, false);
    }

    /**
     * 解析二维码
     *
     * @param file 二维码文件
     * @return 解析内容
     */
    public static String decode(File file) throws Exception {
        BufferedImage image;
        image = ImageIO.read(file);
        if (image == null) {
            return null;
        }
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result;
        Hashtable hints = new Hashtable();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        result = new MultiFormatReader().decode(bitmap, hints);
        String resultStr = result.getText();
        return resultStr;
    }

    public static String decode(String path) throws Exception {
        return QrCodeUtil.decode(new File(path));
    }


}