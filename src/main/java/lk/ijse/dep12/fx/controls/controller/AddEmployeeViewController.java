package lk.ijse.dep12.fx.controls.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import lk.ijse.dep12.fx.controls.Employee;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AddEmployeeViewController {
    public Label lblEmployeeId;
    public TextField txtName;
    public TextField txtAddress;
    public RadioButton rdButtonMale;
    public ToggleGroup rdBtnGroupGender;
    public RadioButton rdButtonFemale;
    public VBox vBoxContacts;
    public TextField txtMainContact;
    public Button btnAddAnotherContact;
    public Label lblName;
    public Label lblAddress;
    public Label lblGender;
    public Label lblContact;
    public Label lblId;
    public GridPane mainGridPane;
    public Button btnNewEmployee;
    public TextField txtNIC;
    public Button btnSaveOrUpdate;
    public Button btnDelete;
    public TableView<Employee> tblEmployee;
    public Label lblNIC;
    public Button btnRemove;
    public ListView<String> lstViewContact;
    public AnchorPane root;
    ObservableList<Employee> employeeList;
    private boolean onceTriedToSave = false;

    public void initialize() {
        mainGridPane.setDisable(true);
        btnNewEmployee.requestFocus();
        btnDelete.setDisable(true);
        employeeList = tblEmployee.getItems();

        tblEmployee.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id")); // set a new property value factory to cell value factory
        tblEmployee.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("nic")); // set a new property value factory to cell value factory
        tblEmployee.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("fullName")); // set a new property value factory to cell value factory
        tblEmployee.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("gender")); // set a new property value factory to cell value factory
        tblEmployee.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("contactStatus")); // set a new property value factory to cell value factory


        //set delete button to work on delete key press
        root.setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.DELETE) {
                btnDelete.fire();
            }
        });

        //setting mnemoics
        for (Node node : mainGridPane.lookupAll(".label")) { // searching for the labels in grid pane
            Label lbl = (Label) node;
            lbl.setLabelFor(mainGridPane.lookup(lbl.getAccessibleText()));
        }

        //adding change listener to list view
        lstViewContact.getSelectionModel().selectedItemProperty().addListener((observable, previous, current) -> {
            btnRemove.setDisable(current == null);

        });

        //adding change listener to table
        tblEmployee.getSelectionModel().selectedItemProperty().addListener((observable, previous, current) -> {

            if (current != null) System.out.println(current.getContacts());
            if (current != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to update the selected employee?", ButtonType.YES, ButtonType.NO);
                Optional<ButtonType> buttonType = alert.showAndWait();
                if (buttonType.get() == ButtonType.YES) {
//                    clearTheForm();
                    enableRequiredControls();
                    lstViewContact.getItems().clear();
                    onceTriedToSave = true; // specify once tried to save
                    btnSaveOrUpdate.setText("Update");

                    //Employee employee = current;
                    lblEmployeeId.setText(current.getId());
                    txtNIC.setText(current.getNic());
                    txtName.setText(current.getFullName());
                    txtAddress.setText(current.getAddress());
                    if (current.getGender().equals("Male")) rdButtonMale.setSelected(true);
                    else rdButtonFemale.setSelected(true);
                    lstViewContact.getItems().addAll(current.getContacts());


                } else {
                    btnDelete.setDisable(false);
                }
            }
            btnDelete.setDisable(current == null);
        });

    }

    private void clearTheForm() {
        lblEmployeeId.setText("");
        txtNIC.clear();
        txtName.clear();
        txtAddress.clear();
        rdBtnGroupGender.selectToggle(null);
        txtMainContact.clear();
        lstViewContact.getItems().clear();
        mainGridPane.setDisable(true);
        btnNewEmployee.requestFocus();

        btnAddAnotherContact.setDisable(true);
        btnDelete.setDisable(true);
        btnRemove.setDisable(true);
    }

    private String generateEmployeeId() {
        if (employeeList.isEmpty()) return "IJSE-0001";
        else {
            int nextIdNum = Integer.parseInt(employeeList.getLast().getId().substring(5)) + 1;
            return "IJSE-%04d".formatted(nextIdNum);
        }
    }

    private boolean isNICValid() {
        String nic = txtNIC.getText().strip();
        if (nic.length() != 10) return false;
        if (!(nic.endsWith("V") || nic.endsWith("v"))) return false;
        for (int i = 0; i < nic.length() - 1; i++) {
            if (!Character.isDigit(nic.charAt(i))) return false;
        }
        return true;
    }

    private boolean isNameValid() {
        String name = txtName.getText().strip();
        if (name.length() < 3) return false;
        //check characters as an array
        for (char c : name.toCharArray()) {
            if (!(Character.isLetter(c) || Character.isSpaceChar(c))) {
                return false;
            }
        }
        return true;
    }

    private boolean isAddressValid() {
        String address = txtAddress.getText();
        return address.strip().length() >= 4;
    }

    private boolean isContactNumberValid() {
        String contact = txtMainContact.getText().strip();
        if (lstViewContact.getItems().contains(contact)) return false; // check whether number duplicated for same employee
        if (contact.length() != 11) return false;
        if (contact.charAt(3) != '-') return false;
        for (int i = 0; i < contact.length(); i++) {
            if (i == 3) continue;
            if (!Character.isDigit(contact.charAt(i))) return false;
        }


        // check this number was entered before
        for (Employee employee : employeeList) {
            if (employee.getContacts().contains(contact)) return false;
        }

        // if no error occurred before, number is valid
        return true;
    }


    private void enableRequiredControls() {
        mainGridPane.setDisable(false);
        btnAddAnotherContact.setDisable(true);
        txtNIC.requestFocus();
    }

    public void btnNewEmployeeOnAction(ActionEvent actionEvent) {
        if (!lblEmployeeId.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you wish to add a new employee discarding currently unsaved data?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get() == ButtonType.YES) {
                btnSaveOrUpdate.setText("Save");
                clearTheForm();
                lblEmployeeId.setText(generateEmployeeId());
                enableRequiredControls();
            }
        } else {
            btnSaveOrUpdate.setText("Save");
            lblEmployeeId.setText(generateEmployeeId());
            enableRequiredControls();
        }


    }


    public void txtNameOnAction(ActionEvent actionEvent) {
//        if (onceTriedToSave) focusOnRequiredFields();
//        else txtAddress.requestFocus();

    }

    public void btnRemoveOnAction(ActionEvent actionEvent) {
        lstViewContact.getItems().remove(lstViewContact.getSelectionModel().getSelectedItem()); // remove selected item
        lstViewContact.getSelectionModel().clearSelection(); // clear remaining selection
    }

    public void btnSaveOrUpdateOnAction(ActionEvent actionEvent) {

        for (TextField textField : new TextField[]{txtName, txtAddress, txtNIC, txtMainContact}) {
            textField.getStyleClass().remove("error");
        }
        for (Label label : new Label[]{lblNIC, lblName, lblAddress, lblContact, lblGender}) {
            label.getStyleClass().remove("error");
        }
        for (RadioButton radioButton : new RadioButton[]{rdButtonMale, rdButtonFemale}) {
            radioButton.getStyleClass().remove("error");
        }

        boolean validation = true;

        if (!txtMainContact.getText().isEmpty()) {
            txtMainContact.getStyleClass().add("error");
            lblContact.getStyleClass().add("error");
            txtMainContact.requestFocus();
            validation = false;
        }

        if (rdBtnGroupGender.getSelectedToggle() == null) {
            rdButtonFemale.getStyleClass().add("error");
            rdButtonMale.getStyleClass().add("error");
            lblGender.getStyleClass().add("error");
            rdButtonMale.requestFocus();
            validation = false;
        }

        if (!isAddressValid()) {
            txtAddress.getStyleClass().add("error");
            lblAddress.getStyleClass().add("error");
            validation = false;
            txtAddress.requestFocus();
        }

        if (!isNameValid()) {
            txtName.getStyleClass().add("error");
            lblName.getStyleClass().add("error");
            validation = false;
            txtName.requestFocus();
        }

        if (!isNICValid()) {
            txtNIC.getStyleClass().add("error");
            lblNIC.getStyleClass().add("error");
            validation = false;
            txtNIC.requestFocus();
        }

        if (!validation) return;

        List<String> contactItems = new ArrayList<>();
        contactItems.addAll(lstViewContact.getItems());

        String contactStatus = contactItems.isEmpty() ? "Not Available" : "Available";
        Employee employee = new Employee(lblEmployeeId.getText(), txtNIC.getText().strip(), txtName.getText().strip(), txtAddress.getText().strip(), ((RadioButton) rdBtnGroupGender.getSelectedToggle()).getText(), contactItems, contactStatus);
        System.out.println("After creating employee object contacts");
        for (String contact : employee.getContacts()) {
            System.out.print(contact + " ");
        }
        //if updating
        if (onceTriedToSave) {
            employeeList.remove(tblEmployee.getSelectionModel().getSelectedItem());


        }
        employeeList.add(employee);
        onceTriedToSave = false;
        btnSaveOrUpdate.setText("Save");
        clearTheForm();
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        employeeList.remove(tblEmployee.getSelectionModel().getSelectedItem());
        tblEmployee.getSelectionModel().clearSelection();

        // generate employee id again if already was trying to enter a new employee while deleting existing one
        if (!lblEmployeeId.getText().isEmpty()) {
            lblEmployeeId.setText(generateEmployeeId());
        }
    }

    public void rdButtonFemaleOnAction(ActionEvent actionEvent) {

    }

    public void btnAddAnotherContactOnAction(ActionEvent actionEvent) {
        txtMainContact.getStyleClass().remove("error");
        lblContact.getStyleClass().remove("error");
        if (isContactNumberValid()) {
            lstViewContact.getItems().add(txtMainContact.getText().strip());
            txtMainContact.clear();
        } else {
            txtMainContact.getStyleClass().add("error");
            lblContact.getStyleClass().add("error");
        }
        txtMainContact.requestFocus();
    }


    public void txtAddressOnAction(ActionEvent actionEvent) {
        //txtCountry.requestFocus();
        // if (onceTriedToSave) focusOnRequiredFields();
    }

    public void rdButtonMaleOnAction(ActionEvent actionEvent) {
//        txtMainContact.requestFocus();
//        if (onceTriedToSave) focusOnRequiredFields();
    }

    public void txtMainContactOnAction(ActionEvent actionEvent) {

    }

    public void txtMainContactOnKeyReleased(KeyEvent keyEvent) {
        btnAddAnotherContact.setDisable(txtMainContact.getText().isBlank());
    }
}

