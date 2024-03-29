package lk.ijse.dep12.fx.controls;

import java.util.ArrayList;
import java.util.List;

public class Employee {
    private String id;
    private String nic;
    private String fullName;

    private String address;
    private String gender;
    private List<String> contacts;
    private String contactStatus;

    public Employee(String id, String nic, String fullName, String address, String gender, List<String> contacts, String contactStatus) {
        this.id = id;
        this.nic = nic;
        this.fullName = fullName;
        this.address = address;
        this.gender = gender;
        this.contacts = contacts;
        this.contactStatus = contactStatus;
    }

    public String getContactStatus() {
        return contactStatus;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setContacts(List<String> contacts) {
        this.contacts = contacts;
    }

    public String getId() {
        return id;
    }

    public String getNic() {
        return nic;
    }


    public String getFullName() {
        return fullName;
    }

    public String getAddress() {
        return address;
    }

    public String getGender() {
        return gender;
    }

    public List<String> getContacts() {
        return contacts;
    }
}
