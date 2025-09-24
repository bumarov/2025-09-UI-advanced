package com.company.timesheets.view.mytimeentrieslist;


import com.company.timesheets.app.TimeEntrySupport;
import com.company.timesheets.entity.TimeEntry;
import com.company.timesheets.view.main.MainView;
import com.company.timesheets.view.timeentry.TimeEntryDetailView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.facet.Timer;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "my-time-entries-list-view", layout = MainView.class)
@ViewController(id = "ts_MyTimeEntriesListView")
@ViewDescriptor(path = "my-time-entries-list-view.xml")
public class MyTimeEntriesListView extends StandardView {
    @ViewComponent
    private DataGrid<TimeEntry> timeEntriesDataGrid;
    @Autowired
    private TimeEntrySupport timeEntrySupport;
    @Autowired
    private DialogWindows dialogWindows;
    @ViewComponent
    private Timer timer;
    @Autowired
    private Notifications notifications;

    @Subscribe("timeEntriesDataGrid.copy")
    public void onTimeEntriesDataGridCopy(final ActionPerformedEvent event) {
        TimeEntry selected = timeEntriesDataGrid.getSingleSelectedItem();
        if (selected == null) {
            return;
        }

        TimeEntry copiedTimeEntry = timeEntrySupport.copy(selected);


        DialogWindow<TimeEntryDetailView> dialogWindow = dialogWindows.detail(timeEntriesDataGrid)
                .withViewClass(TimeEntryDetailView.class)
                .newEntity(copiedTimeEntry)
                .build();

        dialogWindow.getView().setOwnTimeEntry(true);
    }

    @Install(to = "timeEntriesDataGrid.create", subject = "queryParametersProvider")
    private QueryParameters timeEntriesDataGridCreateQueryParametersProvider() {
        return QueryParameters.of(TimeEntryDetailView.PARAMETER_OWN_TIME_ENTRY, "");
    }

    @Install(to = "timeEntriesDataGrid.edit", subject = "queryParametersProvider")
    private QueryParameters timeEntriesDataGridEditQueryParametersProvider() {
        return QueryParameters.of(TimeEntryDetailView.PARAMETER_OWN_TIME_ENTRY, "");
    }

    @Subscribe(id = "timerBtn", subject = "clickListener")
    public void onTimerBtnClick(final ClickEvent<JmixButton> event) {
        timer.start();
    }

    private int seconds = 0;

    @Subscribe("timer")
    public void onTimerTimerAction(final Timer.TimerActionEvent event) {
        seconds += event.getSource().getDelay() / 1000;
        notifications.show("Timer tick", seconds + " seconds passed");
        getViewData().loadAll();

    }

    @Subscribe("timer")
    public void onTimerTimerStop(final Timer.TimerStopEvent event) {
        
    }


}