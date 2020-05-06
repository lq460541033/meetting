package com.swf.attence.hikConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 每日任务，包括临时报警数据存放、数据分析、数据解析后写入正式库等任务
 */
@Component
@EnableScheduling
public class CameraEveryTask {
}
