/******************************************************************
 * Copyright (c) 2004, Exoftware
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 *
 *   * Redistributions of source code must retain the above
 *     copyright notice, this list of conditions and the following
 *     disclaimer.
 *   * Redistributions in binary form must reproduce the above
 *     copyright notice, this list of conditions and the following
 *     disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *   * Neither the name of the Exoftware, Exactor nor the names
 *     of its contributors may be used to endorse or promote
 *     products derived from this software without specific
 *     prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *****************************************************************/
package com.exoftware.exactor.command.swt.framework;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.*;

public class GuiEvent
{
    private static Event createEvent( Widget widget )
    {
        Event event = new Event();
        event.widget = widget;
        return event;
    }

    public static void addFocusInListener( Widget widget, Listener listener )
    {
        addListener( widget, GuiEventCode.focusIn(), listener );
    }

    public static void addFocusOutListener( Widget widget, Listener listener )
    {
        addListener( widget, GuiEventCode.focusOut(), listener );
    }

    public static void addClickListener( Widget widget, Listener listener )
    {
        addListener( widget, GuiEventCode.click(), listener );
    }

    public static void addSelectionListener( Button optionButton, SelectionListener listener )
    {
        optionButton.addSelectionListener( listener );
    }

    public static void addSelectionListener( Widget widget, Listener listener )
    {
        addListener( widget, GuiEventCode.selected(), listener );
    }

    private static void addListener( Widget widget, GuiEventCode eventCode, Listener listener )
    {
        widget.addListener( eventCode.getAsInt(), listener );
    }

    public static void addModifyListener( Widget widget, Listener listener )
    {
        addListener( widget, GuiEventCode.modify(), listener );
    }

    public static void sendSelectionEventToControl( Widget widget )
    {
        sendEventToControl( widget, GuiEventCode.selected() );
    }

    public static void sendFocusInEventToControl( Widget widget )
    {
        sendEventToControl( widget, GuiEventCode.focusIn() );
    }

    public static void sendClickEventToControl( Widget widget )
    {
        sendEventToControl( widget, GuiEventCode.click() );
    }

    public static void sendModifyEventToControl( Widget widget )
    {
        sendEventToControl( widget, GuiEventCode.modify() );
    }

    private static void sendEventToControl( Widget widget, GuiEventCode eventCode )
    {
        widget.notifyListeners( eventCode.getAsInt(), createEvent( widget ) );
    }

    public static void sendFocusOutEventToControl( Widget widget )
    {
        sendEventToControl( widget, GuiEventCode.focusOut() );
    }

    public static void sendPaintMessageToComposite( Composite composite )
    {
        GC gc = new GC( composite );
        Event event = new Event();
        event.gc = gc;
        composite.notifyListeners( SWT.Paint, event );
        gc.dispose();
    }

    public static void sendMouseEnterMessageToControl( Widget widget )
    {
        sendEventToControl( widget, GuiEventCode.mouseEnter() );
    }

    public static void sendMouseExitMessageToControl( Widget widget )
    {
        sendEventToControl( widget, GuiEventCode.mouseExit() );
    }

    public static void sendResizeEventToControl( Widget widget )
    {
        sendEventToControl( widget, GuiEventCode.resize() );
    }

    public static void sendDoubleClickEventToWidget( Widget widget )
    {
        sendEventToControl( widget, GuiEventCode.doubleClick() );
    }

}
