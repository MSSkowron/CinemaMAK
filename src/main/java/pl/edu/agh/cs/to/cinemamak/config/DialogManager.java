package pl.edu.agh.cs.to.cinemamak.config;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Window;

import java.util.Optional;

public class DialogManager {

    Alert dialogInformation = null;
    Alert dialogConfirmation = null;
    public DialogManager(){}

    private void createAlertInformation(){
        this.dialogInformation = new Alert(Alert.AlertType.INFORMATION);
        this.dialogInformation.initModality(Modality.APPLICATION_MODAL);
        this.dialogInformation.setTitle("Information");
    }

    private void createAlertConfirmation(){
        this.dialogConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
        this.dialogConfirmation.initModality(Modality.APPLICATION_MODAL);
        this.dialogConfirmation.setTitle("Confirmation");
    }

    public void showInformation(Window owner, String headerText, String contentText,
                                     javafx.event.EventHandler<javafx.scene.control.DialogEvent> eventHandler){

        this.createAlertInformation();

        this.dialogInformation.initOwner(owner);
        this.dialogInformation.setHeaderText(headerText);
        this.dialogInformation.setContentText(contentText);
        this.dialogInformation.setOnCloseRequest(eventHandler);
        this.dialogInformation.show();

    }

    public boolean askForConfirmation(Window owner, String headerText, String contentText){

        this.createAlertConfirmation();

        this.dialogConfirmation.initOwner(owner);
        this.dialogConfirmation.setHeaderText(headerText);
        this.dialogConfirmation.setContentText(contentText);
        Optional<ButtonType> result = this.dialogConfirmation.showAndWait();

        return result.filter(buttonType -> buttonType == ButtonType.OK).isPresent();
    }
}
