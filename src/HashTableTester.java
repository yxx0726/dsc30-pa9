import org.junit.Before;

import static org.junit.Assert.*;

public class HashTableTester {
    HashTable table;
    @org.junit.Before
    public void set() {
        table = new HashTable(4);
    }

    @org.junit.Test
    public void insert() {

        table.insert("xx");
        assertEquals(1,table.getSize());

        table.insert("yy");
        assertEquals(2,table.getSize());

        table.insert("zz");
        assertEquals(3,table.getSize());
    }

    @org.junit.Test
    public void delete() {
        table.insert("xx");
        table.insert("yy");
        table.insert("zz");
        table.delete("xx");
        assertEquals(2, table.getSize());

        table.delete("yy");
        assertEquals(1,table.getSize());

        table.delete("zz");
        assertEquals(0,table.getSize());
    }

    @org.junit.Test
    public void lookup() {
        table.insert("xv");
        table.insert("uc");
        table.insert("sd");
        assertEquals(true,table.lookup("xv"));
        assertEquals(true,table.lookup("uc"));
        assertEquals(true,table.lookup("sd"));
    }

    @org.junit.Test
    public void getSize() {
        assertEquals(0,table.getSize());

        table.insert("ca");
        assertEquals(1,table.getSize());

        table.insert("uc");
        assertEquals(2,table.getSize());
    }
}