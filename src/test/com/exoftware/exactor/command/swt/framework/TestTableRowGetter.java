package com.exoftware.exactor.command.swt.framework;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;

public class TestTableRowGetter extends GuiAbstractTest
{
    private TableRowGetter tableRowGetter;
    private TableItem regularTableItem;
    private Table table;
    private Table emptyTable;
    private TableItem emptyTableItem;

    protected void setUp() throws Exception
    {
        super.setUp();
        table = new Table( shell, SWT.NONE );
        new TableColumn( table, SWT.NONE );
        new TableColumn( table, SWT.NONE );
        regularTableItem = new TableItem( table, SWT.NONE );
        regularTableItem.setText( new String[]{"abc", "def"} );

        emptyTable = new Table( shell, SWT.NONE );
        emptyTableItem = new TableItem( emptyTable, SWT.NONE );
    }

    public void testRegularGet()
    {
        assertTableRowGetter( "abc,def", regularTableItem );
    }

    public void testEmptyString()
    {
        emptyTableItem.setText( new String[]{} );
        assertTableRowGetter( "", emptyTableItem );
    }

    public void testNonTable()
    {
        Button button = new Button( shell, SWT.PUSH );
        assertTableRowGetter( "<This is not a table>", button );
    }

    private void assertTableRowGetter( String expected, Widget widget )
    {
        tableRowGetter = new TableRowGetter( widget );
        assertEquals( expected, tableRowGetter.get() );
    }

}

