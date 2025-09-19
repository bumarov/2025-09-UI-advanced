package com.company.timesheets.view.mytimeentrieslist;


import com.company.timesheets.app.TimeEntrySupport;
import com.company.timesheets.entity.TimeEntry;
import com.company.timesheets.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
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

    @Subscribe("timeEntriesDataGrid.copy")
    public void onTimeEntriesDataGridCopy(final ActionPerformedEvent event) {
        TimeEntry selected = timeEntriesDataGrid.getSingleSelectedItem();
        if (selected == null) {
            return;
        }

        TimeEntry copiedTimeEntry = timeEntrySupport.copy(selected);

        //todo open a detail view
    }


}