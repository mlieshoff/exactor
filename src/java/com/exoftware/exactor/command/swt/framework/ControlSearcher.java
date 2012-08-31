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

import org.eclipse.swt.widgets.*;

public class ControlSearcher
{
    private Composite rootComposite;
    private ControlName key;
    private Widget match;

    public ControlSearcher( Composite rootComposite, ControlName key )
    {
        this.rootComposite = rootComposite;
        this.key = key;
        search();
    }

    private void search()
    {
        searchTheRootComposite();
        searchTheRootShellsAndMenu();
    }

    private void searchTheRootComposite()
    {
        searchChildren( rootComposite.getChildren() );
    }

    private void searchTheRootShellsAndMenu()
    {
        searchTheRootShells();
        searchTheRootMenuItems();
    }

    private void searchTheRootMenuItems()
    {
        if( hasAMenuBar() )
            searchChildren( rootComposite.getShell().getMenuBar().getItems() );
    }

    private boolean hasAMenuBar()
    {
        return rootComposite.getShell().getMenuBar() != null;
    }

    private void searchTheRootShells()
    {
        searchChildren( rootComposite.getShell().getShells() );
    }

    private void searchChildren( Widget[] children )
    {
        for( int i = 0; i < children.length; i++ )
            searchThisControl( children[i] );
    }

    private void searchThisControl( Widget control )
    {
        if( thisControlIsAMatch( control ) )
            saveReferenceToControl( control );
        else
            recursivelySearchThisControlsChildren( control );
    }

    private void recursivelySearchThisControlsChildren( Widget control )
    {
        if( control instanceof Table )
            searchTableItems( control );
        else if( control instanceof MenuItem )
            searchMenuItems( control );
        else if( control instanceof Composite )
            searchCompositeChildren( control );
    }

    private void searchMenuItems( Widget control )
    {
        Menu menu = ((MenuItem) control).getMenu();
        if( menu != null )
            searchChildren( menu.getItems() );
    }

    private void searchCompositeChildren( Widget control )
    {
        searchChildren( ((Composite) control).getChildren() );
    }

    private void searchTableItems( Widget control )
    {
        searchChildren( ((Table) control).getChildren() );
        searchChildren( ((Table) control).getItems() );
    }

    private void saveReferenceToControl( Widget control )
    {
        match = control;
    }

    private boolean thisControlIsAMatch( Widget control )
    {
        return ControlName.getControlName( control ).equals( key );
    }

    public boolean exists()
    {
        return match != null;
    }

    public Widget searchForControl()
    {
        return match;
    }

}
