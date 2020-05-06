package com.swf.attence;

import com.swf.attence.service.IUserMsgService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AttenceApplicationTests {
    @Autowired
    private IUserMsgService userMsgService;

    @Test
    public void contextLoads() throws SQLException, ClassNotFoundException, ParseException, IOException {
        boolean b = userMsgService.generateEveryWeekMsg("2019-02-06", 1);

    }
}

