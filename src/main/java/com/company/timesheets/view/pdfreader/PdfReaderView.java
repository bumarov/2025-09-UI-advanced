package com.company.timesheets.view.pdfreader;


import com.company.timesheets.view.main.MainView;
import com.vaadin.componentfactory.pdfviewer.PdfViewer;
import com.vaadin.flow.router.Route;

import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.streams.DownloadHandler;
import io.jmix.core.Resources;
import io.jmix.flowui.view.StandardView;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "pdf-reader-view", layout = MainView.class)
@ViewController(id = "ts_PdfReaderView")
@ViewDescriptor(path = "pdf-reader-view.xml")
public class PdfReaderView extends StandardView {

    @Autowired
    private Resources resources;

    @Subscribe
    public void onInit(final InitEvent event) {
        PdfViewer pdfViewer = new PdfViewer();
        pdfViewer.setSizeFull();

        pdfViewer.setSrc("resources/META-INF/resources/files/example.pdf");


        getContent().add(pdfViewer);

    }
    
}