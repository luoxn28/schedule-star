package com.schedule.star.admin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiangnan
 * @date 2018/1/28 13:26
 */
@RestController
public class IndexController {

    @GetMapping
    public Object index() {
        return "<h3 style='text-align: center;'>hello " +
                "<a href='https://github.com/luoxn28/schedule-star'>schedule-star</a> admin</h3>";
    }

}
