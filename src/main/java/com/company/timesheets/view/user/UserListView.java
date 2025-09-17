package com.company.timesheets.view.user;

import com.company.timesheets.entity.User;
import com.company.timesheets.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "users", layout = MainView.class)
@ViewController("ts_User.list")
@ViewDescriptor("user-list-view.xml")
@LookupComponent("usersDataGrid")
@DialogMode(width = "64em")
public class UserListView extends StandardListView<User> {
    @Autowired
    private UiComponents uiComponents;

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        JmixButton button = uiComponents.create(JmixButton.class);
        button.setText("Button!");
        getContent().add(button);
    }


}