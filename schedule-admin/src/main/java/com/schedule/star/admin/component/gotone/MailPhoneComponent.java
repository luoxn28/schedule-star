package com.schedule.star.admin.component.gotone;

import cn.hutool.core.util.StrUtil;
import com.schedule.star.core.bean.Result;
import com.schedule.star.core.util.R;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * @author xiangnan
 * @date 2018/3/18 15:43
 */
@Component
public class MailPhoneComponent {
    private static final Logger logger = LogManager.getLogger();

    public Result sendMessage(String mailPhone, String content) {
        String[] emailPhones = StrUtil.split(mailPhone, R.partFlag.emailPhone);
        for (String str : emailPhones) {
            str = str.trim();
            if (StrUtil.contains(str, '@')) {
                // email
                logger.info("send email to {}, content {}", str, content);
            } else {
                // phone
                logger.info("send message to {}, content {}", str, content);
            }
        }

        return Result.success(null);
    }

}
