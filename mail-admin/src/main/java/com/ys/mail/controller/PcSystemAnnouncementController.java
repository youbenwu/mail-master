package com.ys.mail.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ys.mail.entity.PcSystemAnnouncement;
import com.ys.mail.model.CommonResult;
import com.ys.mail.service.PcSystemAnnouncementService;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.IdWorker;
import com.ys.mail.util.PcUserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * pc端公告表 前端控制器
 * </p>
 *
 * @author 070
 * @since 2021-12-23
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/pc-system-announcement")
@Api(tags = "后台公告管理")
public class PcSystemAnnouncementController {

    @Autowired
    private PcSystemAnnouncementService pcSystemAnnouncementService;

    @ApiOperation("公告添加")
    @PutMapping(value = "/addAnnouncement")
    public CommonResult<Boolean> addAnnouncement(@RequestBody PcSystemAnnouncement pcSystemAnnouncement) {
        Long id = pcSystemAnnouncement.getSystemAnnouncementId();
        pcSystemAnnouncement.setUserId(PcUserUtil.getCurrentUser().getPcUserId());
        pcSystemAnnouncement.setSystemAnnouncementId(id.equals(NumberUtils.LONG_ZERO) ? IdWorker.generateId() : id);
        Boolean result = pcSystemAnnouncementService.saveOrUpdate(pcSystemAnnouncement);
        return CommonResult.success(result);
    }

    @ApiOperation("公告查询")
    @GetMapping(value = "getAnnouncement")
    public CommonResult<Page<PcSystemAnnouncement>> getAnnouncement(@RequestParam(name = "title", required = false) String title, @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        QueryWrapper<PcSystemAnnouncement> queryWrapper = new QueryWrapper<>();
        if (!BlankUtil.isEmpty(title)) {
            queryWrapper.like("system_announcement_title", title);
        }
        Page<PcSystemAnnouncement> page = new Page<>(pageNum, pageSize);
        Page<PcSystemAnnouncement> result = pcSystemAnnouncementService.page(page, queryWrapper);
        return CommonResult.success(result);
    }

    @ApiOperation("公告删除")
    @DeleteMapping(value = "deleteAnnouncement")
    public CommonResult<Boolean> deleteAnnouncement(@RequestParam(name = "announcementId", required = true) Long announcementId) {
        Boolean result = pcSystemAnnouncementService.removeById(announcementId);
        return CommonResult.success(result);
    }
}
