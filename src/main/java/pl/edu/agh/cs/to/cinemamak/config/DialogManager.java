package pl.edu.agh.cs.to.cinemamak.config;

import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Window;

public class DialogManager {

    Alert dialogInformation = null;

    public DialogManager(){}

    private void createAlertInformation(){
        this.dialogInformation = new Alert(Alert.AlertType.INFORMATION);
        this.dialogInformation.initModality(Modality.APPLICATION_MODAL);
        this.dialogInformation.setTitle("Information");
    }

    public void setDialogInformation(Window owner, String headerText,
                                     javafx.event.EventHandler<javafx.scene.control.DialogEvent> eventHandler){
        if(this.dialogInformation == null){
            this.createAlertInformation();
        }
        this.dialogInformation.initOwner(owner);
        this.dialogInformation.setHeaderText(headerText);
        this.dialogInformation.setOnCloseRequest(eventHandler);
        this.dialogInformation.show();
    }
}
