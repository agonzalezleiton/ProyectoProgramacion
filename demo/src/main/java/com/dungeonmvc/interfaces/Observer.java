package com.dungeonmvc.interfaces;
import com.dungeonmvc.models.Character;
public interface Observer {
    void onChange();

    void onChange(String... args);
}
