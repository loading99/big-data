package com.imooc.bigdata.flume;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Domainintercepter implements Interceptor {

    List<Event> events;

    @Override
    public void initialize() {
        events=new ArrayList<>();
    }

    @Override
    public Event intercept(Event event) {
        Map<String, String> headers = event.getHeaders();
        String body= new String(event.getBody());

        if(body.contains("imooc")){
            headers.put("type","imooc");

        }else{
            headers.put("type","other");
        }
        return event;
    }

    @Override
    public List<Event> intercept(List<Event> list) {
        events.clear();
        for(Event event:events){
            events.add(intercept(event));
        }
        return events;
    }

    @Override
    public void close() {
        events=null;
    }

    public static class Builder implements Interceptor.Builder{
        @Override
        public Interceptor build() {
            return new Domainintercepter();
        }

        @Override
        public void configure(Context context) {

        }
    }
}
