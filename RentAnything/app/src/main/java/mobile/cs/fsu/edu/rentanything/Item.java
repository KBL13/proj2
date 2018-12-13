package mobile.cs.fsu.edu.rentanything;

public class Item {

    public Item(){

    }

    private String Description;
    private String Location;
    private String Phone;
    private Float Rate;
    private String Title;
    private String User;
    private String ListID;

    public String getDescription() {
        return Description;
    }

    public String getListID() {
        return ListID;
    }

    public void setListID(String listID) {
        ListID = listID;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public Float getRate() {
        return Rate;
    }

    public void setRate(Float rate) {
        Rate = rate;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }
}
