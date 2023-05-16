package com.restkeeper.order;

import com.restkeeper.sequence.SequenceUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SequenceTest {

    @Test
    public void getAutoFlowCodeTest() {
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String flowCode = SequenceUtils.getSequence(date,"test");
        System.out.println(flowCode);
    }
}
