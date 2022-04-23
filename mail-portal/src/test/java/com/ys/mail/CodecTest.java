package com.ys.mail;

import cn.hutool.Hutool;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Date;

/**
 * @Desc
 * @Author CRH
 * @Create 2021-12-31 17:39
 */
public class CodecTest {

//    public static void main(String[] args) {
//        try(CostTime c = new CostTime("统计耗时：")) {
//            for (int i = 0; i < 10000; i++) {
//                int randomInt = RandomUtil.randomInt(1000000, 9999999);
//                String s = DigestUtils.sha512Hex("/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQICAQECAQEBAgICAgICAgICAQICAgICAgICAgL/2wBDAQEBAQEBAQEBAQECAQEBAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgL/wAARCALQAeADASIAAhEBAxEB/8QAHwAAAAYDAQEBAAAAAAAAAAAAAAQFBgcIAgMJAQoL/8QARRAAAgICAQMDAwIEAgkDAwALAQIDBAUREgAGIQcTMRQiQQhRFSMyYXGBCRYkM0KRobHwUsHRJWLhNHLxFzVDggpTkib/xAAcAQABBQEBAQAAAAAAAAAAAAADAAECBAUGBwj/xABBEQACAQMDAgMFCAIBAwMDBAMBAhEAAyEEEjFB8AVRYRMicYGRBjKhscHR4fEUQiMHFVIkM2IlQ3IWU4KSwtLi/9oADAMBAAIRAxEAPwD7kJJD4YsAreAQhYqSCGVmHz8KfnX3a/forNJyYxuCfb4lQy7CkMXGj+G5He/xvx530W+rhEEhjkPAk65RsGVmkbZIJ2d/b+PyPyR1pkdSNOzEhS3EFQWHHRJ2RpSdf/GvHXLMYE10q4+J/n17/ITsTySKX25WKMHKAlVV1ZgvM6PJRrf45/n86GlWOUkNoowHMEf1DiF0CRs71/z11oklIlBfexoBWYN7afKoBvXgDXnZ+3yeicsjNG3CVUPIOrgcgNMxLD7tNsFR4+Nf49Ae586mF6gUYFjZB4grptkkhQ3LiBxTehs/HnXH/Loo7iSUiTjyikQxhlU+1IIuKlSQNNwc+fnR/uetTTSNIZCwRSFYoERUABZeYLHbkkbOj8r8fPWidffB5ExrzVtozKJR87JHlVLaB0Qx+FPk9VyeTxRFBMA9D338vhusyhwzJwP3I07sAx8EFyqFTyb8fOvP9iOiplRwkqgMNFEKBj9u1CMTryfI/wCXWqSaZeKIEV9H7gVKAbBQBZP/AOZoAEka+T4/GmSVndBGyxyEhnLIg4gDk2uOgpBXYPx9pOjrXVO4fP41aTAB6mtc7SBmihXRdQS6cWlB470AQwJ3yJ2d/b5HSazjZ9tmLFi0zsBszgqo+4jbAxMnn4BBA/cbJZGLoU5RM6mJeMvJNty/maHH3PBGjokAkDwW6LlhxAOipGlAb7lIPywUnR2B8/I/y6qGQT0qYBNFp23akDSNImgUUorEEMTvn8k+dfsPwOsZOKqjTTxQq8jCMOPLseIEYI2Ec8mKq2tgfjwDi7MkiM54B2IjYEMxdB7nHXH7fsVmOz41/cdYe4TJPIdCIcOJDMynkzFiElJ4fc3jQP41+ABuSIg0VQQDnnvNEX5LIOExIkdveO3LcW2wCt/+tsHfwPA8+OtNt4H9o2IZJ1l5JFMn+7gKDjKswI87jP48ePuI+esbJVoonWUJYSxGjEe8YjA5k91mRBottk0WDDSkAAnfReWRgIoa0ayI0zpJ7rlVDs7M7c3bar+Nefgb0PIFVO4pO6OuK1S6ZjA8MktNY4/akaw+mhhZSICCeS8WIOh9pDa/cdFp5g7wukrMI9sEeKMAO3FywYgsTsa3sbCg6Gz0ZlWu7tIyASLr2yvNpAJGXjGA+9x8o1DeANAbJ0D0m2JQsMSo7sWDl14MqGQn/dh5D9/26OtDRH7dSQSarlSsnzopalhieC3LLI1is8s+9sARMWr+y8ax/wA4H3kA+PkE70x60TTSHiI2UsrhkWeNgoheXjcWNeXh9sH3+W3+46xmn2paQsVUfao8kcW"+randomInt);
////                System.out.println(s);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static void main(String[] args) {
        // 格式化当天9:00的日期
        // 获取当天日期
        String today = DateUtil.format(new Date(), "yyyy-MM-dd");
        DateTime dateTime = DateUtil.parse(today + " 21:00:00", "yyyy-MM-dd HH:mm:ss");
        System.out.println(dateTime);

        // 将当前时间与晚上九点整进行比对
        DateTime currentDate = DateUtil.date();
        currentDate = DateUtil.parse("2022-01-07 21:00:00","yyyy-MM-dd HH:mm:ss");
        System.out.println(currentDate);

        // 比较
        System.out.println(currentDate.compareTo(dateTime) == NumberUtils.INTEGER_MINUS_ONE);
    }
}
