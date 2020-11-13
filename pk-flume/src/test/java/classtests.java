

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class classtests implements Interceptor {

    List<Event> events;

    @Override
    public void initialize() {
        events = new ArrayList<>();
    }

    @Override
    public Event intercept(Event event) {
        Map<String, String> headers = event.getHeaders();
        String body = new String(event.getBody());

        if (body.contains("imooc")) {
            headers.put("type", "imooc");
            event.setHeaders(headers);
        } else {
            headers.put("type", "other");
//            event.setHeaders(headers);
        }
        return event;
    }

    @Override
    public List<Event> intercept(List<Event> list) {
        events.clear();
        for (Event event : events) {
            events.add(intercept(event));
        }
        return events;
    }

    @Override
    public void close() {
        events = null;
    }

}

