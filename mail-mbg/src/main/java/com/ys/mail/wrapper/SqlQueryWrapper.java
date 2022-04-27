package com.ys.mail.wrapper;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ys.mail.enums.SqlFormatEnum;
import com.ys.mail.util.BlankUtil;

import java.util.Collection;

/**
 * @Desc 自定义 QueryWrapper；支持单表、多表；无需判断NULL和""
 * @Author CRH
 * @Create 2022-02-25 09:30
 */
public class SqlQueryWrapper<T> extends QueryWrapper<T> {

    @Override
    public SqlQueryWrapper<T> eq(String column, Object val) {
        return (SqlQueryWrapper<T>) super.eq(BlankUtil.isNotEmpty(val), column, val);
    }

    @Override
    public SqlQueryWrapper<T> ne(String column, Object val) {
        return (SqlQueryWrapper<T>) super.ne(BlankUtil.isNotEmpty(val), column, val);
    }

    @Override
    public SqlQueryWrapper<T> gt(String column, Object val) {
        return (SqlQueryWrapper<T>) super.gt(BlankUtil.isNotEmpty(val), column, val);
    }

    @Override
    public SqlQueryWrapper<T> ge(String column, Object val) {
        return (SqlQueryWrapper<T>) super.ge(BlankUtil.isNotEmpty(val), column, val);
    }

    @Override
    public SqlQueryWrapper<T> lt(String column, Object val) {
        return (SqlQueryWrapper<T>) super.lt(BlankUtil.isNotEmpty(val), column, val);
    }

    @Override
    public SqlQueryWrapper<T> le(String column, Object val) {
        return (SqlQueryWrapper<T>) super.le(BlankUtil.isNotEmpty(val), column, val);
    }

    @Override
    public SqlQueryWrapper<T> like(String column, Object val) {
        return (SqlQueryWrapper<T>) super.like(BlankUtil.isNotEmpty(val), column, val);
    }

    @Override
    public SqlQueryWrapper<T> notLike(String column, Object val) {
        return (SqlQueryWrapper<T>) super.notLike(BlankUtil.isNotEmpty(val), column, val);
    }

    @Override
    public SqlQueryWrapper<T> likeLeft(String column, Object val) {
        return (SqlQueryWrapper<T>) super.likeLeft(BlankUtil.isNotEmpty(val), column, val);
    }

    @Override
    public SqlQueryWrapper<T> likeRight(String column, Object val) {
        return (SqlQueryWrapper<T>) super.likeRight(BlankUtil.isNotEmpty(val), column, val);
    }

    @Override
    public SqlQueryWrapper<T> between(String column, Object val1, Object val2) {
        return (SqlQueryWrapper<T>) super.between(BlankUtil.isNotEmpty(val1) && BlankUtil.isNotEmpty(val2), column, val1, val2);
    }

    @Override
    public SqlQueryWrapper<T> notBetween(String column, Object val1, Object val2) {
        return (SqlQueryWrapper<T>) super.notBetween(BlankUtil.isNotEmpty(val1) && BlankUtil.isNotEmpty(val2), column, val1, val2);
    }

    @Override
    public SqlQueryWrapper<T> in(String column, Collection<?> coll) {
        return (SqlQueryWrapper<T>) super.in(BlankUtil.isNotEmpty(coll), column, coll);
    }

    @Override
    public SqlQueryWrapper<T> notIn(String column, Collection<?> coll) {
        return (SqlQueryWrapper<T>) super.notIn(BlankUtil.isNotEmpty(coll), column, coll);
    }

    @Override
    public SqlQueryWrapper<T> inSql(String column, String inValue) {
        return (SqlQueryWrapper<T>) super.inSql(BlankUtil.isNotEmpty(inValue), column, inValue);
    }

    @Override
    public SqlQueryWrapper<T> notInSql(String column, String inValue) {
        return (SqlQueryWrapper<T>) super.notInSql(BlankUtil.isNotEmpty(inValue), column, inValue);
    }

    @Override
    public SqlQueryWrapper<T> select(String... columns) {
        return (SqlQueryWrapper<T>) super.select(columns);
    }

    /**
     * 扩展函数：默认BETWEEN日期条件，链式调用时需要在父类方法前
     *
     * @param column 列名
     * @param val1   值1
     * @param val2   值2
     * @return wrapper
     */
    public SqlQueryWrapper<T> compareDate(String column, String val1, String val2) {
        String applySql = StrUtil.format(SqlFormatEnum.STRING_DATE_FORMAT_BETWEEN.getContent(), column);
        return (SqlQueryWrapper<T>) super.apply(BlankUtil.isNotEmpty(val1) && BlankUtil.isNotEmpty(val2), applySql, val1, val2);
    }

    /**
     * 扩展函数：单个日期条件，链式调用时需要在父类方法前
     *
     * @param sqlFormatEnum 待格式化的SQL，枚举类型 {@link SqlFormatEnum}
     * @param column        列名
     * @param val           值
     * @return wrapper
     */
    public SqlQueryWrapper<T> compareDate(SqlFormatEnum sqlFormatEnum, String column, String val) {
        String applySql = StrUtil.format(sqlFormatEnum.getContent(), column);
        return (SqlQueryWrapper<T>) super.apply(BlankUtil.isNotEmpty(val), applySql, val);
    }

    /**
     * 扩展统计函数，只支持统计单个字段，不能与select共用 TODO：后面可以考虑做成追加形式
     *
     * @param column 列名
     * @param alias  别名
     * @return wrapper
     */
    public SqlQueryWrapper<T> sum(String column, String alias) {
        column = StrUtil.format(SqlFormatEnum.STRING_SUM.getContent(), column, alias);
        return (SqlQueryWrapper<T>) super.select(column);
    }

}
