package com.ys.mail.constant;

import java.io.Serializable;

/**
 * 支付宝提现一些常量
 *
 * @author DT
 * @version 1.0
 * @date 2021-12-03 17:56
 */

public class AlipayConstant implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * gateway:支付宝网关（固定）https://openapi.alipay.com/gateway.do
     */
    public static final String SERVER_URL = "https://openapi.alipay.com/gateway.do";

    /**
     * app_id 飞虎科技提现用
     */
    // public static final String APP_ID = "2021003117644258";
    public static final String APP_ID = "2021002198635044";

    /**
     * 飞虎科技私钥，转账使用
     */
    // public static final String APP_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDMt94r1dC10FEJ2oeBsITInCFJ9jD0PBv7UhVrxSVvrJ4Pm9XYMPq5kJqNJxgxBSM2iJUbEI80CDUccmoZANTjgOeiPk9P2OmEUzRjZ+ZvC/uxZ5A0/3ZAum2Uh/3wJSQIlcRumKH4kOvnrdFfuVwS6/iESqw4MsfhobOerhMDlirMdTcE41kQnB4EiK7cTHU2n5B1vegiwVrzV85hELyNwDmOjXfg6AtZP6Ijomzq3OFWaPAvDG02NBm3Gsz2Ru/WhArO6zJ89wh+b/DfGK9FtzoYwU+O/8OgLTwNuAX9bItuuSp4H7xQ+c73/kzHVJphUSDRlP7IuEkZ8O/6jvulAgMBAAECggEAJNTj3QEgBsYO/nXZsWSfzzP/7T2k+xwhcKsI/ZW/sYLeATLpW3ccdOxmUgT5nkAtYK9cHD1zdkoPvCl9mzgCFVho1FDflh8RjGQn5caYt9vxEa7vTAm2Ldkcw/9fmagk/oJrj/ZYFc+FEe6837sKrMDVun9w0r+YJKkiFud8tkUodjunOImBuwj3AYQ+osmuxNPdoLW5IcSaGcSjalMGo2BL+YSVKLVV/trm2xaM0cQwgJ9K1Ev/h1rhKVmMbFW4jNDC9DJAXsX1i5KIhd7nEWy+L+nIwrMmyEP5EHYcDyDRXxRFXtZpQ1AxPsHzGAs3uUA/S6mYCML1gqUXC81mVQKBgQD8DFfk2bYNaC5j3V+/wQs/FpjwB/znVxKwVPQKOJxciq02LjOVzukXD971Ov6xT8PdUi2X0nJ/t7WLUCSca93uM62q29mBTIAuMr38ocTwCyEPhTR9prqkTo/404u7DfBlWRhRb+VzGg9SNs0Rk1UYI5eH2UOhseu8B2UUyy1YtwKBgQDP7Y3dB/eCqbzoc/3KPVtGWq0v7dgQas+8ZHKMOuZW4mvvjs0zInbUaPhWG0MNu8X2pXc3Mgzv6wr5kKds4R9oMH99kJplogIU3JiHIXC7rspefvfdVrfbZXEz7tSWuZ8X6LybZBJr6ln2r/iLuyq9yRdxwtK6KI8lJfF+i2gagwKBgQDnUKSxoKSVtVEt0ZL4NsCNi72cPJh778t1DOJTJxVJUPQaT1iEWjpNMgGa6iOost28lpjFRxxR3lW8nzaaX9xzqfXuYopBQeY5f9QgsI4/Bte2mLdNweB0vq0e3XbVmrtPJ+9LffgEJJx/BVA6mw5zcua0hE6vbMw96kN4S+c4DQKBgDXZky0UXCDF7vvQ1oCjJVlyMHpCQfv2fbiP1y7ELlWALG4NcM12dooKaY57XwFWiS2YquhrmPglymDZ+tJy91xXex087w+1Ro66lvIgUf75hMW2YmE7jUBP5GKLzxj+F9rsVrRz1WWHeyctadRec/JU+zJmP4cg6/m1+vZICn0bAoGAR9ftzeTcA5P4R3r8ylmmg4crVsAauJMM4gXQt6XEwYaK/VqoQuFTRT/hLBqDyHfn+fk8xYdRv0gBq2SlQSVoduscn2qLe268AMJOjI8qrBaw7fqqKhqtm9xS/qNoWY2/c7WXktxUOkS3WDctwMbQqq05uHbpeBQ7QfvTGs/8e0I=";
    public static final String APP_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCGioqIMFcgvob62DvjtYCKSd+j4W+0yN7+Nq3vbTC8bPTBNxCaZrQixe6L4e6Oh5Ye7tJv2mJEVp0ofb/1eLJ3X19IuxPfk3YmDwrNfPo676lg9oz8+rz3cUqm8+nn8B+aJCSLUk3aSH+mEt2DWQu5cyn4CiWAYuTd09orVxtPgKueLG+bgoKIs3xSk8i0IwGGVbSO2QTT9mKVJ3XhOepdLGTOIo9/NTT/ruO5V0514+zDvMp3L9acTAK/cDAknSXJPB6bBHQ6MeYJTyty5vnCChZ8HPskWcafKAwuhQQFc5dJ9qjrV95F+d8tMEZQGSKXkwV0LRXzVMW5HGMvnAR/AgMBAAECggEAcOwLBe980XDYneWrrp2CO49xc30dYuFNuJRwPIqq4xuX+iubddxJyP3UD/gbX+mRKHlOiq8C11TTJ08UNJWPRP35XXQXZAnCmoL5kCsbYygQQ9/rydExlYWzIJmMcy1peNhpC76StWkwKlWfMkUkwAxW52asvytB6KWNcd70fy7C7jAJfZImDb+VEFgTdeOOwadxevviI4mVQRuevCWo3rk7r173wfuCTA6EkrQElG7h12Q9O7sU8sXb2IQ9Avy8Q4T7nWa3Sx70wrGLn7tEL6T7wjgbItmp3/wmmGR4v9d0wFg9zIbqERrP3Sz2PoxJ03KOgZePg5I8ciTtM9U6IQKBgQDmqqfPkUxOhrvfu1RqZehhpV9niX0l0vGhUMdpSyC/NuQ+WoiloDj2vqG79fpY961qe7AA891aeZVCiU9YT7SrgT2dpo/On+LGjccZ29ZFBiaahuWNhzFgieCxhjO9IJ7qpeg/H7Wu9Sd9DkxMycSy9XVwqv3mji14zceujZdyKQKBgQCVUUFUSuePm+MjsqeN+O8yvGYo+AE26+P6P3c8fv5r9br9yCJWNm3FwPfuE0eNwJ6GfymmCkAndfFjidqibPAmqAKtgsitTSJ5Al3rlq3ynBr7UvCuNR4jTJ/aRMgBY5ZRGy0qnjKPQ2U92N8QD9XmfUNahEH5Fig0CXktfEMmZwKBgQCJTDEAouq5nwb7AAUaI7hzTrz/4t2EAxBiRbO8X8SJLIIk/zxw4XKmpAee2qX+CMbHKAI2PvGVpeMLLLH2zdKrdw0W5oqYoaCZMNC++pSKl0OpHPxMBpmEbe05nbj0LROKdeBMmnJJRpEPpdASsNKM9v/FZXIlpqE4U7w0kQEBKQKBgCbr3kaVm9SQQt3cRNa6C8fC2jJVqyNgy7VAc9k+FR2ZGRvr3h9w4p5IlZfmSKGHkwrl/Lo+M8jVco2KsoXiNZst8jy+OYcK8zBt0IOHbGIwFMxsKzaNvARhhc2EXkoWZmlP1y8ju3QikjJT5JkwN4Z0ImlmzjeacfJWgdK8cFiDAoGAL897VVKCTDxkCLRwerj+SqYkUb9Vjot39xlQAKIQeOjSd5G9dDFNJDYs7l2hR9Uz0XMMUs18Vcpfpf228qzyWFvDhT1CWdIj0H4kMURGrvUPrbbJYJZUnIlqH+9Ulhtr/1mXY5pWjC4WQH5H0oBojShE3gKZYZqN4BQkRamv7UE=";

    /**
     * 参数返回格式，只支持 json 格式
     */
    public static final String FORMAT = "json";

    /**
     * 请求和签名使用的字符编码格式，支持 GBK和 UTF-8
     */
    public static final String CHARSET = "UTF-8";

    /**
     * 商户生成签名字符串所使用的签名算法类型，目前支持 RSA2 和 RSA，推荐商家使用 RSA2。
     */
    public static final String SIGN_TYPE = "RSA2";

    /**
     * 应用公钥证书路径
     */
    // public static final String APP_CERT_PATH = "crt/fh_ty_appCertPublicKey_2021003117644258.crt";
    public static final String APP_CERT_PATH = "crt/appCertPublicKey_2021002198635044.crt";

    /**
     * 支付宝公钥证书文件路径
     */
    // public static final String ALIPAY_CERT_PATH = "crt/fh_ty_alipayCertPublicKey_RSA2.crt";
    public static final String ALIPAY_CERT_PATH = "crt/alipayCertPublicKey_RSA2.crt";

    /**
     * 支付宝CA根证书文件路径
     */
    // public static final String ALIPAY_ROOT_CERT_PATH = "crt/fh_ty_alipayRootCert.crt";
    public static final String ALIPAY_ROOT_CERT_PATH = "crt/alipayRootCert.crt";

    /**
     * 支付宝登录用户id
     */
    public static final String IDENTITY_TYPE_LOGON = "ALIPAY_LOGON_ID";

    /**
     * 支付宝会员的用户 ID
     */
    public static final String IDENTITY_TYPE_USER = "ALIPAY_USER_ID";

    /**
     * 销售产品码。单笔无密转账固定为 TRANS_ACCOUNT_NO_PWD。
     */
    public static final String PRODUCT_CODE = "TRANS_ACCOUNT_NO_PWD";

    /**
     * 业务场景。单笔无密转账固定为 DIRECT_TRANSFER。
     */
    public static final String BIZ_SCENE = "DIRECT_TRANSFER";

    /**
     * 用户提现
     */
    public static final String USER_DEPOSIT = "用户提现";

    /**
     * 鸿盛源科技
     */
    // public static final String JH_KJ = "飞虎科技";
    public static final String JH_KJ = "吉狐科技";

    /**
     * 飞虎科技应用私钥,飞虎
     */
    public static final String JIU_APP_PRIVATE_KEY = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCYv1ivtjRQeWxF/UUgbq2pSC9jfdBcfMq7aWa9jCy81FLN0qUDhpZvFztswga9NzoDsT0VMsAFXnCCzOTAA0d9/ib1+pwH2EdyZ/uTvgR2HpXa9Sf75ePs2iGBSbnJivjYGgSbN+xAH9C1exPelcjgOhECB9nrezfZTrhRqkAHakC7h+wQbXFoF0n1RHWqEsGHnj37uCxuPxDse+WUqWjlm+ee0gJoiqYSIAWhNZF7rnLRNOcdJjzrwWoPLHIx5CDiwsXyktAhnR80xmnI686TlzmSfYvydyK4ffoLecSHaWdw/BF4/ZqFHoMPzQPF/X/ET6W9S6kR26ZeRSSAUgfTAgMBAAECggEBAIOiMq2KWd8YJOTT9OSUPPhrits0q4Fo0dVcafvjdxJr78gMRcSaEb4+ySlatDAQDeDstvUzvSNjVZaDOIb4zH2Zrr3AcQKZski3r6iM0o9woDgHmaONZDTTbphajs9PAlBJa7rQILrKMDSVknP5pRkHMW0MkxJKJ8EQA+vsyi0YVu1KmtRWsKoR9u8LxejyTd4A+G1vfk76dhB6cQ6s5FjCx0fSyKovLztOIMIHUi10aK7OQzOLYGzhp4wGWhkfqlqJYbIi1M+Q8QkcHYIyFMW6Ns4vJlhVF3XdLXXj32xOixtdhm6ZKO0IGIwiq7stSjmP39Z/0AOghwJe9vj79vECgYEA/N9Cmg0Q0hFTNC8mBAOcse13ZNWrv7/IUTkByY6VY+vCtuGoMZiGa37d7shk6KC6/5D1D88i5wVpDJ+/FbjFQQaTxL7jFygbX9XzFHp21Aiw2mF7rtMPcVnLp0/nAGkUkObbZu0wvmDBSSNu6feuDQIGhFexo+bz5wTth3Ww+h0CgYEAmqMIknI+a7d27JpdoECp+amM2dzq7cM/XPSlexadJfUvA1hoypSHkCIK1bqZd9EMf1tgyPImYgBDVMUmmshTpZ9fo9ITdziO72GFlbvAj+71zIKdancB3s+RZDAfQcPiokJmEDcA7NSqOKTfKFRZDSPgBNtoupalYE/ARwBV5q8CgYEArnLnY5d37zP8jpeoRkSnqSgFog62OcZ4z2/gh8hRz2kqHzh17CEpOqd/O+gLlWCtIPpD1fA0R+Q3oHnkPXwGuOFst+Joj+UfXXIgDBSgPY7xUvbFeMoqcSxdjsNUvbfAdGaPHeasJeph7a8AKXCxJlArZsmIx2XhBZMkurq6qRECgYAmSJCZuQDtKAmgQANH5lpufeRqF4BuBrWMLOE/HPUg7ZeLC3s+FBGPxG3MRhsyh6XBbf0DIrgP8SK6POj2zvuueEt0QlEhZfY1G363+DiPKcjercONFRN4dwj2tdc3L+jO5QNhpkxrVaciyOaalG9LioMukKdX8m3U70maCmI57wKBgQDXIdrwLgqKufMnWSPl5lgkAA8tNKmF55Y43/6tOeafRHOb9Iys86NiD/+ixl3/QtOiZ3dWYyTk41Jupehtwi3K53O2xZOr0hwPjhChphPky0KT2OAPZAArYcmbxm890mm5w7bojxFZCrT9VEQqZb6Bv/m1YKjhQ+y3ycqeE+G+eg==";
    // public static final String JIU_APP_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC6yN+COOdmFtDPblnXKUzlO4XROzt8rVcRocZE5Cp5xx6Ql/aTHgL9ypZRod0p1rRJWPnWbze01ZNpkTJt7TnfLCW0uHUIDsl/5DkLORLCuJi8bs+IF9/+wic6THAfgFMMNp8gCBgQJHpQN4ySHQFjtveJRrjdSl55UAxwBs1nkSYdIYf2dZbLeSiw8X+fIh8Ec/bxyd4BTTJENCD4PiqXyqmFia1wmjl4w48waWMa1ZnfE0t5fG+/oSdRVri8icMi+PfxVoH4UElnsg4FHeFIoNZWgRMHiS2dqezPrxdk8/yC52DNsdXf4Smyzx6qxdFX26FhK/jd8tWLrGFbsDU/AgMBAAECggEAbXrBruVFlEv5Niv7EqcNgCsEQ2ARfQFo0FffC5Ak6V3xp1S63P4xSKpOsyhJpnVHFNrHDY4Au8iXb8AAK/ho/Es3Cpc3WXK5XnHZkZ9xRJrIQ2I72AwOaZdJQN1+tqgThUwatr6nxAZtfskgZqADqXub2WWPg31MpJRrxdnm2TV1HO1+e/VKQP5evLZWgoGECnvzdbSfB+5X2ReIt5w2VVyKh0pjrFjt/cOBf11BPFp+CyPSdBY8hJjcYSEg6K0zPCwlQMV4sAYja7V8JUIPtMuKOrA37GQOOPInh5/W3HH3iwznTOTp7ikHKLR0IbzrhgrdTCixznmgjQkn5FO5yQKBgQDej4ZXxR/sxKAAbR9HAYFZuOF7QFxMN3Pw+eErcIdyBkJlMzy/QSHSnmWjH7qWYWMKq8lHFR6FoeEgQrHUkCskmCPavZkf698c73P+ZiFV3jfzvD7ABRsUGwFieXKsAMjlJy5ppLtKT44vjPmbxFCgDmbCV9AgFKrPe4tFE3QUNQKBgQDW2UbLSKsRMlRS9OMshwx/StAAsm8M/WAY974oTHRMEyEW1oeViYXBRxtUYJxZPwEMaesPBbv6iCGua60HPiSSqD1vmELLdoOVopa2RC66U9DSufch3mVP1rGuR41GirdnYLRbp2ztH6LJudi0ZUEnsnVZS4Xpre1+gSV4MBXqIwKBgQDDn7pBCbbfAZF17nSwkAUnXRdso4y9PXy+kpU1/oo/UnsnwtqhbVmSbMH/mRMDvYQ1y/t2bHzQjxJ3rcfrg/E4EDZrvX2LjmMlZuA4+4JPnKzno04wsAw2JuqXqRJ/V409xqvIFhC956Gd8DcykGMh3Ka8Kj15BVGCYCBxTgEZBQKBgEcQNP9WOOcGO6f4wg9fV/3ek/A3FM46HIgFocsYhrsG9MHmEZCfnTMxvtNo5S4Lzd/6RLqCTgvu7R0RFnk3z8cGiNOZjDZaK+nZjnoZEU+mG4Mn6Zs1QVNoEvsvRYZYqZMzBFTwivHfreuFYi1A9I0Z1QDFHzUMx2o5c1hOp2D7AoGAJ4c3dauNQofCGlQQ/MjzLQBEKinkgCG25DSBK2E1v7PZAHiiC+kK4gfLeAYDMi7rDAQoruvM0HeAy1ZA87iuuO5cqIKX5mj8beYaKGH/KN6FJ+f7+a/JVN9ec5A+vHyqz4CXxeMFEwDzbqmi8+tq8ZhgStJ9Nx8eLlUIBTlNs4s=";
    /**
     * 吉狐科技应用公钥,大尾狐
     */
    public static final String JIU_APP_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAusjfgjjnZhbQz25Z1ylM5TuF0Ts7fK1XEaHGROQqeccekJf2kx4C/cqWUaHdKda0SVj51m83tNWTaZEybe053ywltLh1CA7Jf+Q5CzkSwriYvG7PiBff/sInOkxwH4BTDDafIAgYECR6UDeMkh0BY7b3iUa43UpeeVAMcAbNZ5EmHSGH9nWWy3kosPF/nyIfBHP28cneAU0yRDQg+D4ql8qphYmtcJo5eMOPMGljGtWZ3xNLeXxvv6EnUVa4vInDIvj38VaB+FBJZ7IOBR3hSKDWVoETB4ktnansz68XZPP8gudgzbHV3+Epss8eqsXRV9uhYSv43fLVi6xhW7A1PwIDAQAB";

    /**
     * 飞虎科技的APP_ID   2021002196678429
     */
    public static final String JIU_APP_ID = "2021003127694376";

    /**
     * 飞虎科技应用公钥证书路径
     */
    public static final String JIU_APP_CERT_PATH = "crt/fh_zf_appCertPublicKey_2021003127694376.crt";
    // public static final String JIU_APP_CERT_PATH = "crt/jihAppCertPublicKey_2021002196678429.crt";
    /**
     * 飞虎科技支付宝公钥证书文件路径
     */
    public static final String JIU_ALIPAY_CERT_PATH = "crt/fh_zf_alipayCertPublicKey_RSA2.crt";
    // public static final String JIU_ALIPAY_CERT_PATH = "crt/jihAlipayCertPublicKey_RSA2.crt";

    /**
     * 飞虎科技支付宝CA根证书文件路径
     */
    public static final String JIU_ALIPAY_ROOT_CERT_PATH = "crt/fh_zf_alipayRootCert.crt";
    // public static final String JIU_ALIPAY_ROOT_CERT_PATH = "crt/jihAlipayRootCert.crt";
    /**
     * 支付宝支付成功后的回调地址,自由切换,可以配置生产和测试自由切换
     */
//    public static final String NOTIFY_URL = "http://8.134.35.199:8019/unionPay/aliPayNotify";
    public static final String NOTIFY_URL = "http://159.75.212.173:8019/unionPay/aliPayNotify";
//    public static final String NOTIFY_URL = " http://daweihu.natapp1.cc/unionPay/aliPayNotify";

    /**
     * 销售产品码，商家和支付宝签约的产品码
     */
    public static final String PRODUCT_CODE_PAY = "QUICK_MSECURITY_PAY ";

    /**
     * 交易成功
     */
    public static final String TRADE_SUCCESS = "TRADE_SUCCESS";

    /**
     * 交易关闭
     */
    public static final String TRADE_CLOSED = "TRADE_CLOSED";

    /**
     * 交易完成
     */
    public static final String TRADE_FINISHED = "TRADE_FINISHED";

    /**
     * 支付宝回调成功
     */
    public static final String SUCCESS = "success";

    /**
     * 支付宝回调失败
     */
    public static final String FAIL = "fail";


    /**
     * ccc科技-APP_ID
     */
    public static final String JH_YF_APP_ID = "2021002196678429";

    /**
     * ccc科技-应用公钥证书路径
     */
    public static final String JH_YF_APP_CERT_PATH = "crt/jihAppCertPublicKey_2021002196678429.crt";

    /**
     * ccc科技-支付宝公钥证书文件路径
     */
    public static final String JH_YF_ALIPAY_CERT_PATH = "crt/jihAlipayCertPublicKey_RSA2.crt";

    /**
     * ccc科技-支付宝CA根证书文件路径
     */
    public static final String JH_YF_ALIPAY_ROOT_CERT_PATH = "crt/jihAlipayRootCert.crt";

    /**
     * 飞虎科技购买描述语
     */
    public static final String JIU_SUBJECT = "飞虎科技用户购买商品";
    // public static final String JIU_SUBJECT = "吉狐科技用户购买商品";

    /**
     * ccc科技购买描述语
     */
    public static final String JH_YF_SUBJECT = "吉狐科技用户购买商品";

    /**
     * ccc科技应用私钥,呼啦兔
     */
    public static final String JH_YF_APP_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC6yN+COOdmFtDPblnXKUzlO4XROzt8rVcRocZE5Cp5xx6Ql/aTHgL9ypZRod0p1rRJWPnWbze01ZNpkTJt7TnfLCW0uHUIDsl/5DkLORLCuJi8bs+IF9/+wic6THAfgFMMNp8gCBgQJHpQN4ySHQFjtveJRrjdSl55UAxwBs1nkSYdIYf2dZbLeSiw8X+fIh8Ec/bxyd4BTTJENCD4PiqXyqmFia1wmjl4w48waWMa1ZnfE0t5fG+/oSdRVri8icMi+PfxVoH4UElnsg4FHeFIoNZWgRMHiS2dqezPrxdk8/yC52DNsdXf4Smyzx6qxdFX26FhK/jd8tWLrGFbsDU/AgMBAAECggEAbXrBruVFlEv5Niv7EqcNgCsEQ2ARfQFo0FffC5Ak6V3xp1S63P4xSKpOsyhJpnVHFNrHDY4Au8iXb8AAK/ho/Es3Cpc3WXK5XnHZkZ9xRJrIQ2I72AwOaZdJQN1+tqgThUwatr6nxAZtfskgZqADqXub2WWPg31MpJRrxdnm2TV1HO1+e/VKQP5evLZWgoGECnvzdbSfB+5X2ReIt5w2VVyKh0pjrFjt/cOBf11BPFp+CyPSdBY8hJjcYSEg6K0zPCwlQMV4sAYja7V8JUIPtMuKOrA37GQOOPInh5/W3HH3iwznTOTp7ikHKLR0IbzrhgrdTCixznmgjQkn5FO5yQKBgQDej4ZXxR/sxKAAbR9HAYFZuOF7QFxMN3Pw+eErcIdyBkJlMzy/QSHSnmWjH7qWYWMKq8lHFR6FoeEgQrHUkCskmCPavZkf698c73P+ZiFV3jfzvD7ABRsUGwFieXKsAMjlJy5ppLtKT44vjPmbxFCgDmbCV9AgFKrPe4tFE3QUNQKBgQDW2UbLSKsRMlRS9OMshwx/StAAsm8M/WAY974oTHRMEyEW1oeViYXBRxtUYJxZPwEMaesPBbv6iCGua60HPiSSqD1vmELLdoOVopa2RC66U9DSufch3mVP1rGuR41GirdnYLRbp2ztH6LJudi0ZUEnsnVZS4Xpre1+gSV4MBXqIwKBgQDDn7pBCbbfAZF17nSwkAUnXRdso4y9PXy+kpU1/oo/UnsnwtqhbVmSbMH/mRMDvYQ1y/t2bHzQjxJ3rcfrg/E4EDZrvX2LjmMlZuA4+4JPnKzno04wsAw2JuqXqRJ/V409xqvIFhC956Gd8DcykGMh3Ka8Kj15BVGCYCBxTgEZBQKBgEcQNP9WOOcGO6f4wg9fV/3ek/A3FM46HIgFocsYhrsG9MHmEZCfnTMxvtNo5S4Lzd/6RLqCTgvu7R0RFnk3z8cGiNOZjDZaK+nZjnoZEU+mG4Mn6Zs1QVNoEvsvRYZYqZMzBFTwivHfreuFYi1A9I0Z1QDFHzUMx2o5c1hOp2D7AoGAJ4c3dauNQofCGlQQ/MjzLQBEKinkgCG25DSBK2E1v7PZAHiiC+kK4gfLeAYDMi7rDAQoruvM0HeAy1ZA87iuuO5cqIKX5mj8beYaKGH/KN6FJ+f7+a/JVN9ec5A+vHyqz4CXxeMFEwDzbqmi8+tq8ZhgStJ9Nx8eLlUIBTlNs4s=";


    /**
     * 订单相对超时时间。从交易创建时间开始计算。
     * 该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。 该参数数值不接受小数点， 如 1.5h，可转换为 90m。
     * 当面付场景默认值为3h。设置为1m-5m之内,默认都是5m
     */
    public static final String TIMEOUT_EXPRESS = "30m";
}
