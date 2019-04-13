package MQMainLogic;

public class MessageQueueEntity extends MessageQueue {
    private String supperContent;

    public MessageQueueEntity(String name) {
        this.name = name;
    }


    @Override
    protected void notifyObservers() {
        //循环所有注册的观察者
        for (Observer observer : observers) {
            observer.update(this);

        }
    }

    public String getSupperContent() {
        return supperContent;
    }

    public void setSupperContent(String supperContent) {
        this.supperContent = supperContent;
        this.notifyObservers(); //通知 区别对待观察者
    }
}
