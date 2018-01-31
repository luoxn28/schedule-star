package com.test.core;

import com.schedule.star.core.util.IDGenerator;
import org.junit.Test;

/**
 * @author xiangnan
 * @date 2018/1/28 16:02
 */
public class TestUtil {

    @Test
    public void testIDGenerator() {
        System.out.println(IDGenerator.getId());
        System.out.println(IDGenerator.getId().length());
    }

}
