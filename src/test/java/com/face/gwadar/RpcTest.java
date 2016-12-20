package com.face.gwadar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

/**
 * Created by yuanxiaochun on 2016/12/20.
 */
public class RpcTest {

    private static Logger LOG = LogManager.getLogger(RpcTest.class);

    @Test
    public void test() {

    }

}

interface IUserService {

    String getSystemInfo();

}