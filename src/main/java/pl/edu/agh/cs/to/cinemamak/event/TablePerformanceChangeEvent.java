package pl.edu.agh.cs.to.cinemamak.event;

import org.springframework.context.ApplicationEvent;

public class TablePerformanceChangeEvent extends ApplicationEvent {
    public TablePerformanceChangeEvent(Object source) {
        super(source);
    }
}
