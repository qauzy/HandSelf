package jt.kundream.bean;

public class EventBusBean {

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    private String event;

    public EventBusBean(String event) {
        this.event = event;
    }
}
