package pl.edu.agh.cs.to.cinemamak.event;

import org.springframework.context.ApplicationEvent;

public class NewPerformanceAddedEvent extends ApplicationEvent {
    public NewPerformanceAddedEvent(Object source) {
        super(source);
    }
}
