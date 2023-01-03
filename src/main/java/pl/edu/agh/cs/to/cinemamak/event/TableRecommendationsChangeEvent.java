package pl.edu.agh.cs.to.cinemamak.event;

import org.springframework.context.ApplicationEvent;

public class TableRecommendationsChangeEvent extends ApplicationEvent {
    public TableRecommendationsChangeEvent(Object source) {
        super(source);
    }
}
