package listeners;

import domain.collectables.EnchantmentType;

public interface PlayerListener {

    public abstract void onHealthEvent(int num);

    public abstract void onRuneEvent(boolean hasRune);

    public abstract void onCollectEnch(EnchantmentType type);

    public abstract void onRemoveEnch(EnchantmentType type);

    public abstract void onHallChange(int currentHall);
}
