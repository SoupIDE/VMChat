package de.soup.vmchat.gui.chat.renderer;

public class ChatLine<T> {

    private final int updateCounterCreated;
    private final T message;
    private final int id;

    public ChatLine(int updateCounterCreated,T message,int id)
    {
        this.updateCounterCreated = updateCounterCreated;
        this.message = message;
        this.id = id;
    }

    public T getMessage(){ return this.message; }
    public int getUpdateCounter(){ return this.updateCounterCreated; }
    public int getId(){ return this.id; }
}
