package com.schedule.star.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 执行器组
 *
 * @author xiangnan
 * @date 2018/3/16 13:39
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupEntity {
    private int    id;
    private String groupId;
    private String name;
    private String desc;
    private Date   createTime;
    private Date   updateTime;
    private String status;
}
