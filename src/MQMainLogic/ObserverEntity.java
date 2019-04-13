package MQMainLogic;

import java.net.SocketAddress;
import java.util.ArrayList;

public class ObserverEntity implements Observer {
    private SocketAddress observerName;
    private ArrayList<String> messages;

    public ObserverEntity(SocketAddress s) {
        setObserverName(s);
        messages = new ArrayList<>();
    }

    @Override
    public void update(MessageQueue object) {
        //获取目标的状态
        String supperContent = ((MessageQueueEntity) object).getSupperContent();
        messages.add(supperContent);
    }

    @Override
    public SocketAddress getObserverName() {
        return this.observerName;
    }

    @Override
    public void setObserverName(SocketAddress observerName) {
        this.observerName = observerName;

    }

    public ArrayList<String> getMessages() {
        ArrayList<String> list = new ArrayList<>();
        list.addAll(messages);
        this.messages = new ArrayList<>();
        return list;
    }
}
