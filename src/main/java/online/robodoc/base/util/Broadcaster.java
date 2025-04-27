package online.robodoc.base.util;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.Command;
import online.robodoc.base.ui.view.ChatView;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Broadcaster
{
    private static final Set<ChatView> listeners = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public static void register(ChatView view)
    {
        listeners.add(view);
    }

    public static void unregister(ChatView view)
    {
        listeners.remove(view);
    }

    public static void broadcast()
    {
        for (ChatView view : listeners)
        {
            UI ui = view.getUI().orElse(null);

            if (ui != null)
            {
                ui.access(view::reloadMessages);
            }
        }
    }
}


