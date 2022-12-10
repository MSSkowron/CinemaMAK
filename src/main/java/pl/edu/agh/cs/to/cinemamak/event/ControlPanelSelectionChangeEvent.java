package pl.edu.agh.cs.to.cinemamak.event;

import org.springframework.context.ApplicationEvent;

public class ControlPanelSelectionChangeEvent extends ApplicationEvent {
    public ControlPanelSelectionChangeEvent(Object source) {
        super(source);
    }
}
