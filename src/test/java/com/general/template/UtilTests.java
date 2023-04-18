package com.general.template;

import com.alibaba.fastjson.JSONObject;
import com.general.template.utils.CommonlyLocalDateTimeUtil;
import net.minidev.json.JSONUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
public class UtilTests {

    @Test
    void testLocalDateTime() {
        Map<String,List<Map<String,Object>>> groupMap = new HashMap();
        List<Map<String,Object>> subItems = new ArrayList<>();
        JSONObject item = new JSONObject();
        item.put("updateTime",LocalDateTime.parse("2023-04-01 11:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        item.put("name", "张三");
        item.put("age", 12);
        subItems.add(item);

        item = new JSONObject();
        item.put("updateTime",LocalDateTime.parse("2023-04-01 12:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        item.put("name", "李四");
        item.put("age", 15);
        subItems.add(item);
        groupMap.put("key1",subItems);

        // 按照时间倒序
        for (String key: groupMap.keySet()) {
            List<Map<String,Object>> orders = groupMap.get(key).stream().sorted((t1, t2) ->
                            Long.compare(CommonlyLocalDateTimeUtil.convertAllTimeToLong((LocalDateTime) t2.get("updateTime")),
                                    CommonlyLocalDateTimeUtil.convertAllTimeToLong((LocalDateTime) t1.get("updateTime")))).
                    collect(Collectors.toList());
            System.out.println(JSONObject.toJSON(orders));
        }

    }
}
