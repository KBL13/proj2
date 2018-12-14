package mobile.cs.fsu.edu.rentanything;

public class Message {

    public Message(){

    }

    public String User;
    public String mMessage;

    public String getPerson() {
        return User;
    }

    public String getmMessage() {
        return mMessage;
    }

    public void setPerson(String temp2) {
        User = temp2;
    }

    public void setmMessage(String temp1) {
        mMessage = temp1;
    }

}