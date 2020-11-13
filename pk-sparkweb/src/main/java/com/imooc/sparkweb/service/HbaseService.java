package com.imooc.sparkweb.service;

/**
 * Hbase API related process
 *
 */

import com.imooc.sparkweb.domain.AccessHour;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HbaseService {
    @Autowired
    private HbaseTemplate template;

    public List<AccessHour> query(String tablename,String start,String end){

        Scan scanner = new Scan();
        scanner.setStartRow(start.getBytes());
        scanner.setStopRow(end.getBytes());

        List<Result> results = template.find(tablename, scanner, (rowMapper, rowNum) -> rowMapper);
        List<AccessHour> access= new ArrayList<>();
        for(Result result: results){
            while(result.advance()){
                Cell cell = result.current();
                String rowkey =new String(CellUtil.cloneRow(cell));
                long time = Bytes.toLong(CellUtil.cloneValue(cell));

                AccessHour record = new AccessHour();
                String[] splits = rowkey.split("_");
                record.setHour(splits[0]);
                record.setUser(splits[1]);
                record.setTime(time/1000.0/60);

                access.add(record);
            }
        }
        return access;
    }
}
