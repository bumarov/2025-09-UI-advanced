package com.company.timesheets.view.client;

import com.company.timesheets.entity.Client;
import com.company.timesheets.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.component.image.JmixImage;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.*;

@Route(value = "clients/:id", layout = MainView.class)
@ViewController("ts_Client.detail")
@ViewDescriptor("client-detail-view.xml")
@EditedEntityContainer("clientDc")
public class ClientDetailView extends StandardDetailView<Client> {

    @ViewComponent
    private JmixImage<Object> image;

    @Subscribe
    public void onReady(final ReadyEvent event) {
        updateImageField(getEditedEntity().getImage());
    }
    
    

    @Subscribe(id = "uploadClearButton", subject = "clickListener")
    public void onUploadClearButtonClick(final ClickEvent<JmixButton> event) {
    getEditedEntity().setImage(null);    
    }

    @Subscribe(id = "clientDc", target = Target.DATA_CONTAINER)
    public void onClientDcItemPropertyChange(final InstanceContainer.ItemPropertyChangeEvent<Client> event) {
        if ("image".equals(event.getProperty())) {
            updateImageField(getEditedEntity().getImage());
        }
    }

    private void updateImageField(byte[] imageBytes) {
        if (imageBytes == null) {
            image.setSrc("images/add-image-placeholder.png");
        }
    }
}