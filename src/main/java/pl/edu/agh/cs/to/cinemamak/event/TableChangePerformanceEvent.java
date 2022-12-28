package pl.edu.agh.cs.to.cinemamak.event;

import org.springframework.context.ApplicationEvent;

public class TableChangePerformanceEvent extends ApplicationEvent {
    public TableChangePerformanceEvent(Object source) {
        super(source);
    }
}
