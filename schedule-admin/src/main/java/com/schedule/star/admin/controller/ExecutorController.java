package com.schedule.star.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.schedule.star.admin.bean.ExecutorBean;
import com.schedule.star.admin.service.ExecutorService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author xiangnan
 * @date 2018/1/29 18:03
 */
@RestController
@RequestMapping("/executor")
public class ExecutorController {

    @Resource
    private ExecutorService executorService;

    @GetMapping("/list")
    public Object getExecutorList() {
        return executorService.getExecutorList();
    }

    @PutMapping
    public Object updateExecutorInfo(ExecutorBean bean) {
        if (StrUtil.isBlank(bean.getExecutorId())) {
            throw new RuntimeException("参数不合法");
        }

        return executorService.updateExecutor(bean);
    }

    @DeleteMapping("/{executorId}")
    public Object deleteExecutorInfo(@PathVariable String executorId) {
        if (StrUtil.isBlank(executorId)) {
            return 0;
        }

        return executorService.deleteExecutor(executorId);
    }

}
