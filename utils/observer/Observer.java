package myproject.utils.observer;


import myproject.utils.events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}