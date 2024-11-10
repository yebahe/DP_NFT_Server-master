package cn.dp.nft.order.domain;

import lombok.extern.slf4j.Slf4j;
//import org.apache.dubbo.rpc.RpcScopeModelInitializer;
//import org.checkerframework.checker.units.qual.A;
//import org.checkerframework.checker.units.qual.min;
//import org.junit.Test;
//import org.junit.platform.commons.util.StringUtils;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.*;
//
//@SpringBootTest
//@Slf4j
//public class TestDemo {
//    private static RedisTemplate redisTemplate;
//    private AMapper aMapper;
//
//    String GOOD_NUMS_PERFIX = "good_nums";
//    String GOOD_LOCK_PERFIX = "good_lock";
//    int reNums = 5;
//
//
//
////    @Transactional
////    public void add(int nums, String goodId, int ren) {
////        try {
////            String str = "scdsv";
////            StringUtil.so
////            System.out.println();
////            Stack<> sta = new Stack<>();
////            sta.
////            String key = GOOD_NUMS_PERFIX + goodId;
////            // 分布式锁、
////            String lockKey = GOOD_LOCK_PERFIX + goodId;
////            if (redisTemplate.hasKey(lockKey) && ren <= reNums) {
////                Thread.sleep(1000);
////                add(nums, goodId, ren + 1);
////                return;
////            }
////            redisTemplate.opsForValue().set(lockKey, Thread.currentThread().toString());
////            Integer n = (Integer) redisTemplate.opsForValue().get(key);
////            if (n == null) {
////                n = (int) 0;
////            }
////            aMapper.updateByGoodId(goodId, n + nums);
////            redisTemplate.delete(key);
////        } catch (Exception e) {
////            log.warn(e.toString());
////        } finally {
////
////            if (Thread.currentThread().toString().equals(redisTemplate.opsForValue().get(GOOD_LOCK_PERFIX + goodId))) {
////                 redisTemplate.delete(GOOD_LOCK_PERFIX + goodId);
////            }
////        }
////    }
////    public void test(){
////        Scanner
////        //
////        int plen = pw.length();
////        for(Map.Entry<Integer, List<String>> entry : pmap){
////            int len = entry.getKey();
////            List<String> list = entry.getValue();
////            if(plen == len && list.contains(pw)){
////                max = max + list.size();
////                min = min + 1;
////                System.out.println(min + max);
////                return ;
////            }else {
////                max = max + list.size();
////                min = min + list.size();
////            }
////        }
////        System.out.println(min + max);
////    }
//
//    @Test
//    public void groupAnagrams() {
//        String[] strs = {"eat", "tea", "tan", "ate", "nat", "bat"};
//        Map<String,List<String>> map = new HashMap<>();
//        for(String str : strs ){
//            String res =str;
//            char[] charArray = str.toCharArray();
//            Arrays.sort(charArray);
//            String string = charArray.toString();
//            System.out.println(res+","+string);
//            if(map.containsKey(string)){
//                List<String> list = map.get(string);
//                list.add(res);
//                map.put(string,list);
//            }else {
//                List<String> list = new ArrayList<>();
//                list.add(res);
//                map.put(string,list);
//            }
//        }
//        List<List<String>> ans = new ArrayList<>();
//        for(String key : map.keySet()){
//            List<String> list = map.get(key);
//            ans.add(list);
//        }
//
//    }
//
//    }
