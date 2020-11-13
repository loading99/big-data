import org.apache.flume.Event;
import org.apache.flume.event.SimpleEvent;

public class domaintest {

    public static void main(String[] args) {
        classtests c=new classtests();
        c.initialize();
        SimpleEvent e= new SimpleEvent();
        c.events.add(e);

        Event n=c.intercept(e);
        System.out.println(n.getHeaders());
    }
}
