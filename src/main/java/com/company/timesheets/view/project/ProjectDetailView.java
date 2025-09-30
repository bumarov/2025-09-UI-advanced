package com.company.timesheets.view.project;

import com.company.timesheets.entity.Client;
import com.company.timesheets.entity.Project;
import com.company.timesheets.entity.ProjectParticipant;
import com.company.timesheets.entity.Task;
import com.company.timesheets.view.main.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.jmix.core.DataManager;
import io.jmix.core.MetadataTools;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.component.tabsheet.JmixTabSheet;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.kit.action.BaseAction;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;

@Route(value = "projects/:id", layout = MainView.class)
@ViewController("ts_Project.detail")
@ViewDescriptor("project-detail-view.xml")
@EditedEntityContainer("projectDc")
@DialogMode(width = "64em")
public class ProjectDetailView extends StandardDetailView<Project> {
    @Autowired
    private UiComponents uiComponents;
    @Autowired
    private MetadataTools metadataTools;

    @Autowired
    private DataManager dataManager;

    @Autowired
    private DialogWindows dialogWindows;
    private DataGrid<Task> tasksDataGrid;

    private DataGrid<ProjectParticipant> participantsDataGrid;
    @Autowired
    private Notifications notifications;
    @ViewComponent
    private CollectionContainer<Task> tasksDc;
    @ViewComponent
    private CollectionLoader<Task> tasksDl;
    @ViewComponent
    private CollectionContainer<ProjectParticipant> projectParticipantsDc;
    @ViewComponent
    private CollectionLoader<ProjectParticipant> projectParticipantsDl;
    @Subscribe("tabSheet")
    public void onTabSheetSelectedChange(final JmixTabSheet.SelectedChangeEvent event) {
        if ("tasksTab".equals(event.getSelectedTab().getId().orElse(""))) {
            initTasks();
        }
        if ("participantsTab".equals(event.getSelectedTab().getId().orElse(""))) {
            initParticipants();
        }
    }

    private void initTasks() {

        tasksDl.setParameter("project", getEditedEntity());
        tasksDl.load();

        if (tasksDataGrid != null) {
            return;
        }

        tasksDataGrid = (DataGrid<Task>) getContent().findComponent("tasksDataGrid").get();
        BaseAction createAction = (BaseAction) tasksDataGrid.getAction("create");
        createAction.addActionPerformedListener(this::onTasksDataGridCreate);
        BaseAction editAction = (BaseAction) tasksDataGrid.getAction("edit");
        editAction.addActionPerformedListener(this::onTasksDataGridEdit);
    }

    private void initParticipants() {

        projectParticipantsDl.setParameter("project", getEditedEntity());
        projectParticipantsDl.load();

        if (participantsDataGrid != null) {
            return;
        }

        participantsDataGrid = (DataGrid<ProjectParticipant>) getContent().findComponent("participantsDataGrid").get();
        BaseAction createAction = (BaseAction) participantsDataGrid.getAction("create");
        createAction.addActionPerformedListener(this::onParticipantsDataGridCreate);
        BaseAction editAction = (BaseAction) participantsDataGrid.getAction("edit");
        editAction.addActionPerformedListener(this::onParticipantsDataGridEdit);
    }


    public void onTasksDataGridCreate(final ActionPerformedEvent event) {
        Task newTask = dataManager.create(Task.class);
        newTask.setProject(getEditedEntity());

        dialogWindows.detail(tasksDataGrid)
                .newEntity(newTask)
                .withParentDataContext(getViewData().getDataContext())
                .open();
    }

    public void onTasksDataGridEdit(final ActionPerformedEvent event) {
        Task selectedTask = tasksDataGrid.getSingleSelectedItem();
        if (selectedTask == null) {
            return;
        }

        dialogWindows.detail(tasksDataGrid)
                .editEntity(selectedTask)
                .withParentDataContext(getViewData().getDataContext())
                .open();
    }

    public void onParticipantsDataGridCreate(final ActionPerformedEvent event) {
        ProjectParticipant newParticipant = dataManager.create(ProjectParticipant.class);
        newParticipant.setProject(getEditedEntity());

        dialogWindows.detail(participantsDataGrid)
                .newEntity(newParticipant)
                .withParentDataContext(getViewData().getDataContext())
                .open();
    }

    public void onParticipantsDataGridEdit(final ActionPerformedEvent event) {
        ProjectParticipant selectedParticipant = participantsDataGrid.getSingleSelectedItem();
        if (selectedParticipant == null) {
            return;
        }

        dialogWindows.detail(participantsDataGrid)
                .editEntity(selectedParticipant)
                .withParentDataContext(getViewData().getDataContext())
                .open();
    }

    @Supply(to = "clientsComboBox", subject = "renderer")
    private Renderer<Client> clientsComboBoxRenderer() {
        return new ComponentRenderer<>(client -> {
            FlexLayout wrapper = uiComponents.create(FlexLayout.class);
            wrapper.setAlignItems(FlexComponent.Alignment.CENTER);
            wrapper.addClassName(LumoUtility.Gap.MEDIUM);

            String clientName = metadataTools.getInstanceName(client);

            wrapper.add(
                    createAvatar(clientName, client.getImage(), "var(--lumo-size-xs)"),
                    new Text(clientName)
            );

            return wrapper;
        });
    }

    private Avatar createAvatar(String clientName, byte[] image, String s) {
        Avatar avatar = uiComponents.create(Avatar.class);
        avatar.setName(clientName);

        if (image != null) {
            StreamResource resource = new StreamResource("avatar.png",
                    () -> new ByteArrayInputStream(image));

            avatar.setImageResource(resource);
        }

        avatar.setWidth(s);
        avatar.setHeight(s);

        return avatar;

    }

//    @Subscribe
//    public void onBeforeShow(final BeforeShowEvent event) {
//        notifications.show("taskDc items: " + tasksDc.getItems().size());
//        notifications.show("participantsDc items: " + projectParticipantsDc.getItems().size());
//    }
//
//    @Subscribe(id = "tasksDc", target = Target.DATA_CONTAINER)
//    public void onTasksDcCollectionChange(final CollectionContainer.CollectionChangeEvent<Task> event) {
//        notifications.show("tasksDc - CollectionChanged: ", event.getChangeType() + "");
//    }
//
//    @Subscribe(id = "projectParticipantsDc", target = Target.DATA_CONTAINER)
//    public void onProjectParticipantsDcCollectionChange(final CollectionContainer.CollectionChangeEvent<ProjectParticipant> event) {
//        notifications.show("participantsDc - CollectionChanged: ", event.getChangeType() + "");
//    }
}