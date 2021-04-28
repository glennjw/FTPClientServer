package NameServer.NS;

import NameServer.NS.NameServerAddr;
import NameServer.NS.NameTable;

import java.util.ArrayList;
import java.util.List;

public class NameServerContainer {
    Integer nsId;
    Integer tableHead;
    Integer tableTail;
    String IP = null;
    Integer port = null;
    NameServerAddr bns = null;
    NameServerAddr predcessor = null;
    NameServerAddr successor = null;
    NameTable table;

    NameServerContainer(Integer nsId, String IP, Integer port, Integer tableTail) {
        this.nsId = nsId;
        this.tableTail = tableTail;
        this.IP = IP;
        this.port = port;
    }

    public void createTable ( ArrayList<String> table) {
        this.table = new NameTable(table);
    }



}
