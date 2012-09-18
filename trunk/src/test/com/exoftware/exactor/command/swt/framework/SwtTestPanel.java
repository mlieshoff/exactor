package com.exoftware.exactor.command.swt.framework;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class SwtTestPanel extends Composite
{
    public static final int NUM_COLUMNS = 3;
    public static final String NUM1_HEADER_TEXT = "Number 1";
    public static final String NUM2_HEADER_TEXT = "Number 2";
    public static final String RESULT_HEADER_TEXT = "Result";
    public static final String CALCULATE_TEXT = "Calculate";
    public static final String INVALID_INPUTS = "Invalid";
    public static final String CTRL_NAME_NUM2 = "txtNum2";
    public static final String CTRL_NAME_RESULT = "txtResult";
    public static final String CTRL_TEXT = "txtTest";
    public static final String CTRL_BUTTON = "btnTest";
    public static final String COMBO_BOX = "cbTest";
    public static final String CHECK_BOX = "chkTest";

    public Button check;
    public Button option1;
    public Button option2;
    public Button option3;
    public Text txtTest;
    public boolean testButtonPressed = false;
    public boolean clickControlMessageSent;

    private Label clickControl;
    private Text uneditable;
    private Text disabled;
    private Combo combo;
    private Table table;
    private Button testButton;

    public SwtTestPanel( Composite composite )
    {
        super( composite, SWT.NONE );
        setUp();
    }

    private void setUp()
    {
        setLayout();
        addComponents();
        addCalculateButtonListener();
    }

    private void setLayout()
    {
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = NUM_COLUMNS;
        setLayout( gridLayout );
    }

    private void addComponents()
    {
        addLabels();
        addTextBoxes();
        addCalculateButton();
        addComboBox();
        addCheckBox();
        addOptionButtons();
        addTable();
        addListenerLabel();

    }

    private void addTable()
    {
        table = new Table( this, SWT.BORDER );
        ControlName.setControlName( table, "tblTable" );
    }

    private void addOptionButtons()
    {
        option1 = new Button( this, SWT.RADIO );
        ControlName.setControlName( option1, "optTest" );
        option2 = new Button( this, SWT.RADIO );
        ControlName.setControlName( option2, "optTest1" );
        option3 = new Button( this, SWT.RADIO );
        ControlName.setControlName( option3, "optTest2" );
    }

    private void addListenerLabel()
    {
        GuiEvent.addClickListener( clickControl, new Listener()
        {
            public void handleEvent( Event event )
            {
                clickControlMessageSent = true;
            }
        } );

    }

    private void addCheckBox()
    {
        check = new Button( this, SWT.CHECK );
        ControlName.setControlName( check, CHECK_BOX );
    }

    private void addComboBox()
    {
        combo = new Combo( this, SWT.BORDER );

        ControlName.setControlName( combo, COMBO_BOX );
    }

    private void addCalculateButton()
    {
        testButton = new Button( this, SWT.PUSH );
        testButton.setText( CALCULATE_TEXT );
        ControlName.setControlName( testButton, CTRL_BUTTON );
    }

    private void addTextBoxes()
    {
        txtTest = createTextBox( CTRL_TEXT );
        uneditable = createTextBox( "txtUneditable" );
        uneditable.setEditable( false );
        disabled = createTextBox( "txtDisabled" );
        disabled.setEnabled( false );
    }

    private Text createTextBox( String controlName )
    {
        final Text text = new Text( this, SWT.BORDER );
        ControlName.setControlName( text, controlName );
        return text;
    }

    private void addLabels()
    {
        clickControl = createHeaderLabel( "lblSwtTest" );
    }

    private Label createHeaderLabel( String headerText )
    {
        Label label = new Label( this, SWT.NONE );
        label.setText( headerText );
        ControlName.setControlName( label, headerText );
        return label;
    }

    private void addCalculateButtonListener()
    {
        testButton.addSelectionListener( new SelectionAdapter()
        {
            public void widgetSelected( SelectionEvent event )
            {
                testButtonPressed = true;
            }
        } );
    }

}
