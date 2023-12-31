package com.ys.mail.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.constant.StringConstant;
import com.ys.mail.entity.UmsIncome;
import com.ys.mail.mapper.UmsIncomeMapper;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.po.OriginalIntegralPO;
import com.ys.mail.model.query.IncomeDimensionQuery;
import com.ys.mail.model.vo.UmsIncomeDimensionVO;
import com.ys.mail.model.vo.UmsIncomeSumVO;
import com.ys.mail.service.UmsIncomeService;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.DecimalUtil;
import com.ys.mail.util.UserUtil;
import com.ys.mail.wrapper.SqlLambdaQueryWrapper;
import com.ys.mail.wrapper.SqlQueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 收益表 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-11-25
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UmsIncomeServiceImpl extends ServiceImpl<UmsIncomeMapper, UmsIncome> implements UmsIncomeService {

    private final static Logger LOGGER = LoggerFactory.getLogger(UmsIncomeServiceImpl.class);

    @Autowired
    private UmsIncomeMapper incomeMapper;

    @Override
    public UmsIncome getUmsIncomeByIdType(Long userId, Integer type) {
        SqlLambdaQueryWrapper<UmsIncome> wrapper = new SqlLambdaQueryWrapper<>();
        wrapper.eq(UmsIncome::getUserId, userId)
               .eq(UmsIncome::getIncomeType, type)
               .orderByDesc(UmsIncome::getIncome)
               .last(StringConstant.LIMIT_ONE);
        return getOne(wrapper);
    }

    /**
     * 查询个人汇总记录
     *
     * @param userId 用户ID
     * @return 结果对象
     */
    @Override
    public UmsIncomeSumVO getUmsIncomeSumById(Long userId) {
        if (BeanUtil.isEmpty(userId)) {
            userId = UserUtil.getCurrentUser().getUserId();
        }
        // 查询用户最新记录
        UmsIncome umsIncome = incomeMapper.selectNewestByUserId(userId);
        // 查询出统计数据
        UmsIncomeSumVO vo = incomeMapper.selectUmsIncomeSumById(userId);
        if (BlankUtil.isEmpty(umsIncome) || BlankUtil.isEmpty(vo)) {
            return vo;
        }
        OriginalIntegralPO po = incomeMapper.getOriginalIntegralByUserId(userId);
        Long userRate = incomeMapper.selectUserRate(userId);

        // 组装结果返回
        vo.setOriginal(DecimalUtil.longToStrForDivider(po.getOriginal()));
        vo.setIntegral(DecimalUtil.longToStrForDivider(po.getIntegral()));
        vo.setRate(BlankUtil.isEmpty(userRate) ? StringConstant.ZERO : DecimalUtil.longToStrForDivider(userRate));
        vo.setAllIncome(BlankUtil.isEmpty(umsIncome.getAllIncome()) ? StringConstant.ZERO : DecimalUtil.longToStrForDivider(umsIncome.getAllIncome()));
        vo.setBalance(BlankUtil.isEmpty(umsIncome.getBalance()) ? StringConstant.ZERO : DecimalUtil.longToStrForDivider(umsIncome.getBalance()));

        // 下个版本移除
        vo.setTodayIncome(BlankUtil.isEmpty(umsIncome.getTodayIncome()) ? StringConstant.ZERO : DecimalUtil.longToStrForDivider(umsIncome.getTodayIncome()));
        vo.setInviteIncomeSum(DecimalUtil.strToStrForDivider(vo.getInviteIncomeSum()));
        vo.setSaleIncomeSum(DecimalUtil.strToStrForDivider(vo.getSaleIncomeSum()));
        vo.setExpenditureSum(DecimalUtil.strToStrForDivider(vo.getExpenditureSum()));
        vo.setGeneralIncomeSum(DecimalUtil.strToStrForDivider(vo.getGeneralIncomeSum()));

        return vo;
    }

    @Override
    public CommonResult<List<UmsIncomeDimensionVO>> getUmsIncomeByDimension(IncomeDimensionQuery query) {
        String beginTime = query.getBeginTime();
        String endTime = query.getEndTime();
        Long lastIncomeId = query.getLastIncomeId();
        // 默认日期
        if (BlankUtil.isEmpty(beginTime)) {
            beginTime = DateUtil.format(new Date(), "yyyyMMdd");
        }

        // 构建查询条件
        SqlQueryWrapper<UmsIncome> wrapper = new SqlQueryWrapper<>();
        wrapper.select("income_id,detail_source,income_type,create_time,balance," +
                       "Case when income_type in(-1,2,4,9,10) THEN expenditure ELSE income END income")
               .eq("user_id", UserUtil.getCurrentUser().getUserId())
               .orderByDesc("income_id").last("limit " + query.getPageSize());
        if (BlankUtil.isNotEmpty(lastIncomeId) && lastIncomeId != 0) wrapper.lt("income_id", lastIncomeId);

        // 0 表示开启时间查询
        if (StringConstant.ZERO.equals(query.getLately())) {
            int length = beginTime.length();
            String dateFormat;
            switch (length) {
                case 4:
                    dateFormat = "%Y";
                    break;
                case 6:
                    dateFormat = "%Y%m";
                    break;
                default:
                    dateFormat = "%Y%m%d";
            }
            if (BlankUtil.isEmpty(endTime)) {
                wrapper.eq(String.format("date_format(create_time,'%s')", dateFormat), beginTime);
            } else {
                wrapper.between(String.format("date_format(create_time,'%s')", dateFormat), beginTime, endTime);
            }
        }

        // 开始查询
        List<UmsIncome> list = list(wrapper);
        ArrayList<UmsIncomeDimensionVO> voArrayList = new ArrayList<>();
        list.forEach((item) -> {
            UmsIncomeDimensionVO dimensionVO = new UmsIncomeDimensionVO();
            dimensionVO.setIncomeId(item.getIncomeId().toString());
            dimensionVO.setIncome(DecimalUtil.longToStrForDivider(item.getIncome()));
            dimensionVO.setBalance(DecimalUtil.longToStrForDivider(item.getBalance()));
            dimensionVO.setDetailSource(item.getDetailSource());
            dimensionVO.setCreateTime(item.getCreateTime());
            dimensionVO.setIncomeType(item.getIncomeType());
            voArrayList.add(dimensionVO);
        });
        return CommonResult.success("查询历史收益成功", voArrayList);
    }

    @Override
    public UmsIncome selectNewestByUserId(Long userId) {
        return incomeMapper.selectNewestByUserId(userId);
    }

    @Override
    public int getTodayCount(Long userId, Integer incomeType, String today) {
        QueryWrapper<UmsIncome> incomeQueryWrapper = new QueryWrapper<>();
        incomeQueryWrapper.eq("user_id", userId)
                          .eq("DATE(create_time)", today)
                          .eq("income_type", incomeType)
                          .orderByDesc("income_id");
        return this.count(incomeQueryWrapper);
    }
}
