package pl.edu.agh.cs.to.cinemamak.event;

import org.springframework.context.ApplicationEvent;

public class TableMovieChangeEvent extends ApplicationEvent {
    public TableMovieChangeEvent(Object source) {
        super(source);
    }
}
