package com.schedule.star.admin.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author xiangnan
 * @date 2018/3/17 11:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupBean {
    private String groupId;
    private String name;
    private String desc;
    private Date   createTime;
    private Date   updateTime;
    private String status;

    private List<ExecutorBean> executorList;
}
