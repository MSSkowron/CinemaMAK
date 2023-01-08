package pl.edu.agh.cs.to.cinemamak.event;

import org.springframework.context.ApplicationEvent;

public class MovieSelectedEvent extends ApplicationEvent {
    public MovieSelectedEvent(Object source) {
        super(source);
    }
}
