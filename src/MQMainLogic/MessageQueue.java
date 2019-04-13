package MQMainLogic;

import java.util.ArrayList;
import java.util.List;

public abstract class MessageQueue {
    //持有的观察者集合
    public List<Observer> observers = new ArrayList<>();
    public String name = null;

    //添加 删除 观察者
    public void attach(Observer observer) {
        observers.add(observer);
    }

    public void detach(Observer observer) {
        observers.remove(observer);
    }

    //具体的提醒在子类逻辑中完成
    protected abstract void notifyObservers();
}
