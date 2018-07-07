/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.quickestquail.model.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author joris
 */
public class DefaultActionModel implements IActionModel {

    private final Map<Class, List<IActionModelListener>> specificListeners = new HashMap<>();
    private final List<IActionModelListener> generalListeners = new ArrayList<>();

    @Override
    public void addListener(IActionModelListener listener) {
        generalListeners.add(listener);
    }

    @Override
    public void addListener(IActionModelListener listener, Class eventType) {
        if (!specificListeners.containsKey(eventType)) {
            specificListeners.put(eventType, new ArrayList<>());
        }
        specificListeners.get(eventType).add(listener);

    }

    @Override
    public void removeListener(IActionModelListener listener) {
        generalListeners.remove(listener);
        for (List<IActionModelListener> lst : specificListeners.values()) {
            lst.remove(listener);
        }

    }

    @Override
    public void removeListener(IActionModelListener listener, Class eventType) {
        if (!specificListeners.containsKey(eventType)) {
            return;
        }
        specificListeners.get(eventType).remove(listener);
    }

    @Override
    public void actionPerformed(IActionEvent event) {
        // specific listeners
        if (specificListeners.containsKey(event.getClass())) {
            List<IActionModelListener> lst = specificListeners.get(event.getClass());
            for (int i = lst.size() - 1; i >= 0; i--) {
                lst.get(i).actionPerformed(event);
            }
        }
        // general listeners
        for (int i = generalListeners.size() - 1; i >= 0; i--) {
            generalListeners.get(i).actionPerformed(event);
        }
    }

}
