package com.ys.mail.wrapper;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.ys.mail.enums.SqlFormatEnum;
import com.ys.mail.util.BlankUtil;

import java.util.Collection;

/**
 * @Desc 自定义 LambdaQueryWrapper；仅支持单表查询，多表请使用SqlQueryWrapper
 * @Author CRH
 * @Create 2022-02-24 11:15
 */
public class SqlLambdaQueryWrapper<T> extends LambdaQueryWrapper<T> {

    @Override
    public SqlLambdaQueryWrapper<T> eq(SFunction<T, ?> column, Object val) {
        return (SqlLambdaQueryWrapper<T>) super.eq(BlankUtil.isNotEmpty(val), column, val);
    }

    @Override
    public SqlLambdaQueryWrapper<T> ne(SFunction<T, ?> column, Object val) {
        return (SqlLambdaQueryWrapper<T>) super.ne(BlankUtil.isNotEmpty(val), column, val);
    }

    @Override
    public SqlLambdaQueryWrapper<T> gt(SFunction<T, ?> column, Object val) {
        return (SqlLambdaQueryWrapper<T>) super.gt(BlankUtil.isNotEmpty(val), column, val);
    }

    @Override
    public SqlLambdaQueryWrapper<T> ge(SFunction<T, ?> column, Object val) {
        return (SqlLambdaQueryWrapper<T>) super.ge(BlankUtil.isNotEmpty(val), column, val);
    }

    @Override
    public SqlLambdaQueryWrapper<T> lt(SFunction<T, ?> column, Object val) {
        return (SqlLambdaQueryWrapper<T>) super.lt(BlankUtil.isNotEmpty(val), column, val);
    }

    @Override
    public SqlLambdaQueryWrapper<T> le(SFunction<T, ?> column, Object val) {
        return (SqlLambdaQueryWrapper<T>) super.le(BlankUtil.isNotEmpty(val), column, val);
    }

    @Override
    public SqlLambdaQueryWrapper<T> like(SFunction<T, ?> column, Object val) {
        return (SqlLambdaQueryWrapper<T>) super.like(BlankUtil.isNotEmpty(val), column, val);
    }

    @Override
    public SqlLambdaQueryWrapper<T> notLike(SFunction<T, ?> column, Object val) {
        return (SqlLambdaQueryWrapper<T>) super.notLike(BlankUtil.isNotEmpty(val), column, val);
    }

    @Override
    public SqlLambdaQueryWrapper<T> likeLeft(SFunction<T, ?> column, Object val) {
        return (SqlLambdaQueryWrapper<T>) super.likeLeft(BlankUtil.isNotEmpty(val), column, val);
    }

    @Override
    public SqlLambdaQueryWrapper<T> likeRight(SFunction<T, ?> column, Object val) {
        return (SqlLambdaQueryWrapper<T>) super.likeRight(BlankUtil.isNotEmpty(val), column, val);
    }

    @Override
    public SqlLambdaQueryWrapper<T> between(SFunction<T, ?> column, Object val1, Object val2) {
        return (SqlLambdaQueryWrapper<T>) super.between(BlankUtil.isNotEmpty(val1) && BlankUtil.isNotEmpty(val2), column, val1, val2);
    }

    @Override
    public SqlLambdaQueryWrapper<T> notBetween(SFunction<T, ?> column, Object val1, Object val2) {
        return (SqlLambdaQueryWrapper<T>) super.notBetween(BlankUtil.isNotEmpty(val1) && BlankUtil.isNotEmpty(val2), column, val1, val2);
    }

    @Override
    public SqlLambdaQueryWrapper<T> in(SFunction<T, ?> column, Collection<?> coll) {
        return (SqlLambdaQueryWrapper<T>) super.in(BlankUtil.isNotEmpty(coll), column, coll);
    }

    @Override
    public SqlLambdaQueryWrapper<T> notIn(SFunction<T, ?> column, Collection<?> coll) {
        return (SqlLambdaQueryWrapper<T>) super.notIn(BlankUtil.isNotEmpty(coll), column, coll);
    }

    @Override
    public SqlLambdaQueryWrapper<T> inSql(SFunction<T, ?> column, String inValue) {
        return (SqlLambdaQueryWrapper<T>) super.inSql(BlankUtil.isNotEmpty(inValue), column, inValue);
    }

    @Override
    public SqlLambdaQueryWrapper<T> notInSql(SFunction<T, ?> column, String inValue) {
        return (SqlLambdaQueryWrapper<T>) super.notInSql(BlankUtil.isNotEmpty(inValue), column, inValue);
    }

    /**
     * 扩展函数：默认BETWEEN日期条件，链式调用时需要在父类方法前
     *
     * @param column 列名
     * @param val1   值1
     * @param val2   值2
     * @return wrapper
     */
    public SqlLambdaQueryWrapper<T> compareDate(SFunction<T, ?> column, String val1, String val2) {
        String applySql = StrUtil.format(SqlFormatEnum.STRING_DATE_FORMAT_BETWEEN.getContent(), columnsToString(column));
        return (SqlLambdaQueryWrapper<T>) super.apply(BlankUtil.isNotEmpty(val1) && BlankUtil.isNotEmpty(val2), applySql, val1, val2);
    }

    /**
     * 扩展函数：单个日期条件，链式调用时需要在父类方法前
     *
     * @param sqlFormatEnum 待格式化的SQL，枚举类型 {@link SqlFormatEnum}
     * @param column        列名
     * @param val           值
     * @return wrapper
     */
    public SqlLambdaQueryWrapper<T> compareDate(SqlFormatEnum sqlFormatEnum, SFunction<T, ?> column, String val) {
        String applySql = StrUtil.format(sqlFormatEnum.getContent(), columnsToString(column));
        return (SqlLambdaQueryWrapper<T>) super.apply(BlankUtil.isNotEmpty(val), applySql, val);
    }

}
