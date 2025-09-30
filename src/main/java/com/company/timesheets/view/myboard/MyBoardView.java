package com.company.timesheets.view.myboard;


import com.company.timesheets.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.StandardView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "my-board-view", layout = MainView.class)
@ViewController(id = "ts_MyBoardView")
@ViewDescriptor(path = "my-board-view.xml")
public class MyBoardView extends StandardView {
}