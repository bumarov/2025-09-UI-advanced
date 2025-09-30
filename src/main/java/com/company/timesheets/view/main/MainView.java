package com.company.timesheets.view.main;

import com.company.timesheets.component.slider.Slider;
import com.company.timesheets.component.themetoggle.ThemeToggle;
import com.company.timesheets.entity.TimeEntry;
import com.company.timesheets.event.TimeEntryChangedEvent;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.core.LoadContext;
import io.jmix.core.Metadata;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.app.main.StandardMainView;
import io.jmix.flowui.component.main.JmixListMenu;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

@Route("")
@ViewController("ts_MainView")
@ViewDescriptor("main-view.xml")
public class MainView extends StandardMainView {
    @Autowired
    private Metadata metadata;
    @Autowired
    private DataManager dataManager;
    @ViewComponent
    private JmixListMenu menu;
    @Autowired
    private Notifications notifications;

    @Subscribe
    public void onInit(final InitEvent event) {
        updateRejectedTimeEntries();
    }

    @EventListener
    private void timeEntryChanged(TimeEntryChangedEvent event) {
        updateRejectedTimeEntries();
    }

    private void updateRejectedTimeEntries() {
        LoadContext<TimeEntry> loadContext = new LoadContext<>(
                metadata.getClass(TimeEntry.class)
        );

        loadContext.setQueryString(
                "select te from ts_TimeEntry te " +
                        "where te.user.username = :current_user_username " +
                        "and te.status = 'rejected'"
        );

        long count = dataManager.getCount(loadContext);

        Span badge = null;
        if (count > 0) {
            badge = new Span("" + count);
            badge.getElement().getThemeList().add("badge error");
        }

        menu.getMenuItem("ts_MyTimeEntriesListView").setSuffixComponent(badge);
    }

    @Subscribe("themeToggle")
    public void onThemeToggleThemeToggleThemeChanged(final ThemeToggle.ThemeToggleThemeChangedEvent event) {
        notifications.show("Event value: " + event.getValue());
    }

    @Subscribe("slider")
    public void onSliderChange(Slider.SlideChangedEvent event) {
        notifications.show("New value is: " + event.getValue());
    }
}
