package com.jllsq.holder.client;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LruMapTest {

    private LruMap<String,String> container;
    @Before
    public void setUp() throws Exception {
         container = new LruMap<String,String>();
    }

    @Test
    public void put() {
        // key:null,value:null
        container.put(null,null);
        assertEquals(null,container.table.get(null).value);
        assertEquals(null,container.times.get(0).value);
        container.table.clear();
        container.times.clear();
        // key:null
        container.put(null,"test");
        assertEquals("test",container.table.get(null).value);
        assertEquals("test",container.times.get(0).value);
        container.times.clear();
        container.table.clear();
        // value:null
        container.put("test",null);
        assertEquals(null,container.table.get("test").value);
        assertEquals(null,container.times.get(0).value);
        container.times.clear();
        container.table.clear();
        // key not null,value not null
        container.put(null,null);
        assertEquals(null,container.table.get(null).value);
        assertEquals(null,container.times.get(0).value);
        container.times.clear();
        container.table.clear();
        // 重复put
        container.put("test","test");
        container.put("test","test1");
        container.put("test","test2");
        container.put("test","test3");
        assertEquals("test3",container.table.get("test").value);
        assertEquals(1,container.times.size());
        assertEquals("test",container.times.getFirst().key);
        assertEquals("test3",container.times.getFirst().value);
        container.times.clear();
        container.table.clear();
        // put 多条数据
        container.put("test","test");
        container.put("test1","test1");
        container.put("test2","test2");
        container.put("test3","test3");
        assertEquals("test1",container.table.get("test1").value);
        assertEquals(4,container.times.size());
        assertEquals("test",container.times.getFirst().key);
        assertEquals("test3",container.times.getLast().key);
        container.times.clear();
        container.table.clear();
    }

    @Test
    public void entrySet() {

    }

    @Test
    public void get() {
        // get不存在的对象
        assertEquals(null,container.get("test"));
        // 正常的get操作
        container.put("test1","test1");
        container.put("test2","test2");
        assertEquals("test1",container.get("test1"));
        assertEquals("test1",container.times.getLast().value);
    }

    @Test
    public void delete() {
        // 添加后立刻删除
        container.put("test","test1");
        container.delete("test");
        assertEquals(0,container.size());
        container.clear();
        // 一般删除操作
        container.put("test","test1");
        container.put("test1","test1");
        container.put("test2","test1");
        container.delete("test2");
        assertEquals("test1",container.getLastKey());
        container.clear();
        // 删除不存在的key
        container.delete("test");
        assertEquals(0,container.times.size());
        container.clear();
    }

    @Test
    public void process() {
        container.put("test","test2");
        container.put("test2","test2");
        container.process("test",new LruMap.LruProcess<String,String>() {
            @Override
            public Object process(String value) {
                value = "fjkafjakf";
                return null;
            }
        });
        assertEquals("test",container.times.getLast().key);
        assertEquals("test2",container.table.get("test").value);
    }

    @Test
    public void getLastKey() {
        assertEquals(null,container.getLastKey());
        container.put("test","test1");
        container.put("test2","test1");
        container.put("test3","test1");
        container.get("test2");
        container.get("test5");
        assertEquals("test2",container.getLastKey());
    }

}