package com.power;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.power.domain.ShiXiLog;
import com.power.domain.User;
import com.power.domain.vo.UserListVo;
import com.power.job.MyJob;
import com.power.manager.UserManager;
import com.power.service.ShiXiLogService;
import com.power.service.UserService;
import com.power.utils.AddressUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
public class UserTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserManager userManager;

    @Autowired
    private MyJob myJob;

    @Autowired
    private ShiXiLogService shiXiLogService;

    @Test
    public void test01() {
        List<User> list = userService.list();
        System.out.println(list);
    }

    @Test
    public void test02() {
        List<UserListVo> list = (List<UserListVo>) userManager.userListLogs().getData();
        System.out.println(JSONUtil.toJsonPrettyStr(list));
    }


    @Test
    public void test03() {
//        List<User> data = (List<User>) userManager.userList(null).getData();
        List<User> data = (List<User>) userManager.userList(CollUtil.newArrayList(1L, 2L, 3L)).getData();
        System.out.println(data);
    }

    @Test
    public void test04() {
        boolean b = userService.removeById(7);
    }

//    @Test
//    public void test05() {
//        myJob.testSchedule();
//    }

    @Test
    public void test06() {
        List<User> list = userService.list();
        for (User user : list) {
            user.setAddress(AddressUtil.getAddrByLocateStr(user.getLocation()));
        }
        userService.updateBatchById(list);
    }

    @Test
    public void test07() {

        LambdaQueryWrapper<ShiXiLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.isNotNull(ShiXiLog::getRealName);

        List<ShiXiLog> list1 = shiXiLogService.list(queryWrapper);

        Map<String, Long> map = new HashMap<>();
        for (ShiXiLog shiXiLog : list1) {
            map.put(shiXiLog.getRealName(), shiXiLog.getUserId());
        }

        System.out.println(map);
        System.out.println(map.size());

        List<User> list = userService.list();
        for (User user : list) {
            map.forEach((k, v) -> {
                if (user.getId().equals(v)) {
                    user.setRealName(k);
                }
            });
        }
        userService.updateBatchById(list);
    }


    @Test
    public void test08() {
        List<User> list = userService.list();
//        Map<Long, String> collect = list.stream().collect(Collectors.toMap(User::getId, User::getRealName));
        Map<Long, String> collect = list.stream().collect(Collectors.toMap(User::getId, User::getUsername));

        List<ShiXiLog> list1 = shiXiLogService.list();
        for (ShiXiLog shiXiLog : list1) {
//            shiXiLog.setRealName(collect.get(shiXiLog.getUserId()));
            shiXiLog.setUsername(collect.get(shiXiLog.getUserId()));
        }

        shiXiLogService.updateBatchById(list1);

    }


    @Test
    public void test09() {

        //36000   3      0
        //144000 10   2520
        //300000 20  16920
        //420000 25  31920
        //660000 30  52920
        //960000 35  85920
        //960000 45 181920


        BigDecimal basicSalary = new BigDecimal("10000");// todo

        BigDecimal monthsNumber = new BigDecimal("12");
//        BigDecimal extra = new BigDecimal("3");// todo

        BigDecimal house = new BigDecimal("1500");
        BigDecimal livingExpenses = new BigDecimal("1200");


        BigDecimal wuxianrete = new BigDecimal("0.18");


        System.out.println("年收入：" + basicSalary.multiply(monthsNumber));
        BigDecimal multiply = basicSalary.multiply(new BigDecimal("1").subtract(wuxianrete));
        System.out.println("五险一金后：" + multiply);


        BigDecimal tax = new BigDecimal("0");

        List<List<BigInteger>> list = CollUtil.newArrayList(
                CollUtil.newArrayList(new BigInteger("36000"), new BigInteger("3"), new BigInteger("0")),
                CollUtil.newArrayList(new BigInteger("144000"), new BigInteger("10"), new BigInteger("2520")),
                CollUtil.newArrayList(new BigInteger("300000"), new BigInteger("20"), new BigInteger("16920")),
                CollUtil.newArrayList(new BigInteger("420000"), new BigInteger("25"), new BigInteger("31920")),
                CollUtil.newArrayList(new BigInteger("660000"), new BigInteger("30"), new BigInteger("52920")),
                CollUtil.newArrayList(new BigInteger("960000"), new BigInteger("35"), new BigInteger("85920")),
                CollUtil.newArrayList(new BigInteger("1000000000"), new BigInteger("45"), new BigInteger("181920"))
        );



    }

}
