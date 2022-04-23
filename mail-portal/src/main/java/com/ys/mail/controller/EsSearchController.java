package com.ys.mail.controller;


import com.ys.mail.entity.EsProduct;
import com.ys.mail.model.CommonResult;
import com.ys.mail.service.EsProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * es全文搜索
 * </p>
 *
 * @author 070
 * @since 2021-11-13
 */
@RestController
@RequestMapping("/es/esSearch")
@Validated
@Api(tags = "es全文搜索")
public class EsSearchController {

    @Autowired
    private EsProductService es;


    @ApiOperation("商品搜索")
    @GetMapping(value = "/searchPage")
    public CommonResult<List<EsProduct>> searchPage(@RequestParam("keyword") String keyword, @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        Page<EsProduct> eslist = es.searchPage(keyword, pageNum, pageSize);
        return CommonResult.success(eslist.getContent());
    }

    @ApiOperation("商品添加")
    @GetMapping(value = "/add")
    public CommonResult<Integer> add() {
        int i = es.importAll();
        return CommonResult.success("添加成功：" + i + "记录", null);
    }

    @ApiOperation("商品删除全部")
    @GetMapping(value = "/delete")
    public CommonResult<Integer> delete() {
        es.delete();
        return CommonResult.success(1);
    }
}
