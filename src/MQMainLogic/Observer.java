package MQMainLogic;

import java.net.SocketAddress;

public interface Observer {
    //更新的接口
    void update(MessageQueue object);

    SocketAddress getObserverName();

    //设置和取得观察者名称
    void setObserverName(SocketAddress observerName);
}
