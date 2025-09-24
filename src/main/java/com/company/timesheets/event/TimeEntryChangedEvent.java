package com.company.timesheets.event;

import org.springframework.context.ApplicationEvent;

public class TimeEntryChangedEvent extends ApplicationEvent {

    public TimeEntryChangedEvent(Object source) {
        super(source);
    }
}
