package BootstrapNameServer.BNS;

import java.util.ArrayList;
import java.util.List;

public class NameServerContainer {
    Integer nsId;
    Integer tableHead;
    Integer tableTail;
    NameServerAddr addr;
    NameServerAddr predcessor = null;
    NameServerAddr successor = null;
    NameTable table;

    public NameServerContainer(Integer nsId, String IP, Integer port, Integer tableHead, Integer tableTail) {
        this.nsId = nsId;
        this.tableHead = tableHead;
        this.tableTail = tableTail;
        this.addr = new NameServerAddr(IP,port);
    }

    public void createTable ( ArrayList<String> table) {
        this.table = new NameTable( table);
    }



}
