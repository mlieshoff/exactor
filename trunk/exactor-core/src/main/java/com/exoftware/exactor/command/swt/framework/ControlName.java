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

import org.eclipse.swt.widgets.Widget;

public class ControlName
{
    private String controlName;

    public ControlName( ControlName lhs, String rhs )
    {
        this( lhs.toString() + upperTheFirstLetter( rhs ) );
    }

    private static String upperTheFirstLetter( String rhs )
    {
        return rhs.length() == 0 ?
                "" :
                Character.toUpperCase( rhs.charAt( 0 ) ) + rhs.substring( 1 );
    }

    private ControlName( Object controlData )
    {
        this( (String) controlData );
    }

    public ControlName( String controlName )
    {
        this.controlName = (controlName == null) ? "" : controlName;
    }

    public boolean equals( Object obj )
    {
        ControlName rhs = (ControlName) obj;
        return controlName.equals( rhs.controlName );
    }

    public String toString()
    {
        return controlName;
    }

    public static ControlName getControlName( Widget control )
    {
        return controlDataIsValid( control ) ?
                new ControlName( control.getData() ) :
                unnamed();
    }

    private static ControlName unnamed()
    {
        return new ControlName( "" );
    }

    private static boolean controlDataIsValid( Widget control )
    {
        return controlIsNotNull( control ) && controlDataIsOfTypeString( control );
    }

    private static boolean controlIsNotNull( Widget control )
    {
        return control != null;
    }

    private static boolean controlDataIsOfTypeString( Widget control )
    {
        return control.getData() instanceof String;
    }

    public static void setControlName( Widget control, String controlName )
    {
        setControlName( control, new ControlName( controlName ) );
    }

    public static void setControlName( Widget control, ControlName controlName )
    {
        control.setData( controlName.toString() );
    }

    public boolean hasIndex()
    {
        return getIndexOfOpenBracket() != -1;
    }

    private int getIndexOfOpenBracket()
    {
        return controlName.indexOf( "(" );
    }

    public static boolean hasIndex( Widget control )
    {
        return getControlName( control ).hasIndex();
    }

    public static int extractControlNameIndex( Widget control )
    {
        return getControlName( control ).extractIndex();
    }

    private int extractIndex()
    {
        return hasIndex() ? Integer.parseInt( extractIndexAsString() ) : -1;
    }

    private String extractIndexAsString()
    {
        final int startIndex = getIndexOfOpenBracket() + 1;
        return controlName.substring( startIndex, startIndex + 1 );
    }
}
