package pl.edu.agh.cs.to.cinemamak.event;

import org.springframework.context.ApplicationEvent;

public class NewMovieAddedEvent extends ApplicationEvent {
    public NewMovieAddedEvent(Object source) {
        super(source);
    }
}
