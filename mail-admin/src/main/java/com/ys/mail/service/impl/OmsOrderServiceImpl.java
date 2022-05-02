package com.ys.mail.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.entity.OmsOrder;
import com.ys.mail.mapper.OmsOrderMapper;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.dto.ExportOrderDTO;
import com.ys.mail.model.admin.param.ExportOrderParam;
import com.ys.mail.model.admin.query.OmsOrderQuery;
import com.ys.mail.model.admin.vo.PcUserOrderVO;
import com.ys.mail.model.admin.vo.PidPrPdtOrderVO;
import com.ys.mail.model.admin.vo.PrPdtOrderVO;
import com.ys.mail.service.OmsOrderService;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.JavaForDateUtil;
import org.ehcache.impl.internal.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-12-27
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OmsOrderServiceImpl extends ServiceImpl<OmsOrderMapper, OmsOrder> implements OmsOrderService {

    @Autowired
    private OmsOrderMapper omsOrderMapper;

    @Override
    public CommonResult<Page<PcUserOrderVO>> getGeneralOrder(OmsOrderQuery query) {
        String beginTime = query.getBeginTime();
        String endTime = query.getEndTime();
        Page<PcUserOrderVO> page = new Page<>(query.getPageNum(), query.getPageSize());
        QueryWrapper<PcUserOrderVO> wrapper = new QueryWrapper<>();

        if (!BlankUtil.isEmpty(beginTime) && !BlankUtil.isEmpty(endTime)) {
            if (beginTime.compareTo(endTime) > 0) return CommonResult.failed("开始时间不能大于结束时间", null);
            wrapper.between("date_format( oo.create_time,'%Y-%m-%d %T')", beginTime, endTime);
        }

        // 构建多条件查询
        if (!BlankUtil.isEmpty(query.getUserId())) wrapper.like("oo.user_id", query.getUserId());
        if (!BlankUtil.isEmpty(query.getOrderSn())) wrapper.like("oo.order_sn", query.getOrderSn());
        if (!BlankUtil.isEmpty(query.getDeliveryCompany()))
            wrapper.like("oo.delivery_company", query.getDeliveryCompany());
        if (!BlankUtil.isEmpty(query.getDeliverySn())) wrapper.like("oo.delivery_sn", query.getDeliverySn());
        if (!BlankUtil.isEmpty(query.getTransId())) wrapper.like("oo.trans_id", query.getTransId());

        if (!BlankUtil.isEmpty(query.getPayType())) wrapper.eq("oo.pay_type", query.getPayType());
        if (!BlankUtil.isEmpty(query.getSourceType())) wrapper.eq("oo.source_type", query.getSourceType());
        if (!BlankUtil.isEmpty(query.getOrderStatus())) wrapper.eq("oo.order_status", query.getOrderStatus());
        if (!BlankUtil.isEmpty(query.getOrderType())) wrapper.eq("oo.order_type", query.getOrderType());
        if (!BlankUtil.isEmpty(query.getBillType())) wrapper.eq("oo.bill_type", query.getBillType());

        if (!BlankUtil.isEmpty(query.getNickname())) wrapper.like("uu.nickname", query.getNickname());
        if (!BlankUtil.isEmpty(query.getPhone())) wrapper.like("uu.phone", query.getPhone());

        wrapper.orderByDesc("oo.order_id");
        return CommonResult.success(omsOrderMapper.getPage(page, wrapper));
    }

    @Override
    public List<ExportOrderDTO> getExportOrderList(ExportOrderParam params) {
        // 条件判断
        assert BlankUtil.isEmpty(params);

        // 时间处理
        String timeType = params.getTimeType();
        if ("0".equals(timeType)) {
            assert BlankUtil.isEmpty(params.getBeginTime());
            assert BlankUtil.isEmpty(params.getEndTime());
        } else {
            // 1-当月，2-近三个月，3-近半年
            int type = 5; // 默认当月第一天
            switch (timeType) {
                case "2":
                    type = 2;
                    break;
                case "3":
                    type = 3;
                    break;
            }
            String date = String.valueOf(DateUtil.parse(JavaForDateUtil.JavaForDate(type)));
            params.setBeginTime(date);
        }
        // 查询
        return omsOrderMapper.getExportOrderList(params);
    }

    @Override
    public CommonResult<String> export(ExportOrderParam params) {
        // 根据参数过滤出数据
        List<ExportOrderDTO> exportOrderList = this.getExportOrderList(params);
        System.out.println(exportOrderList.size());
        // 构建excel对象
        // 生成文件
        // 上传到oss中
        // 返回下载链接给前端
        return CommonResult.success("导出成功，等待下载！");
    }

    @Override
    public List<PrPdtOrderVO> getByPrPdtOrder() {
        return omsOrderMapper.selectByPrPdtOrder();
    }

    @Override
    public List<PidPrPdtOrderVO> getByPidPrPdtOrder() {
        return omsOrderMapper.selectByPidPrPdtOrder();
    }

}
