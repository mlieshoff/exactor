package gui;

import business.SimpleBusinessObject;
import com.exoftware.exactor.command.swt.framework.ControlName;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

class CalculatorPanel extends Composite {
    private static final int NUM_COLUMNS = 3;
    private static final String NUM1_HEADER_TEXT = "Number 1";
    private static final String NUM2_HEADER_TEXT = "Number 2";
    private static final String RESULT_HEADER_TEXT = "Result";
    private static final String CALCULATE_TEXT = "Calculate";
    private static final String INVALID_INPUTS = "Invalid";
    private static final String CTRL_NAME_NUM1 = "txtNum1";
    private static final String CTRL_NAME_NUM2 = "txtNum2";
    private static final String CTRL_NAME_RESULT = "txtResult";
    private static final String CTRL_NAME_CALCULATE = "btnCalculate";

    Label num1Header;
    Label num2Header;
    Label resultHeader;
    Text num1;
    Text num2;
    Text result;
    Button calculate;

    public CalculatorPanel(Composite composite) {
        super(composite, SWT.NONE);
        setUp();
    }

    private void setUp() {
        setLayout();
        addComponents();
        addCalculateButtonListener();
    }

    private void setLayout() {
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = NUM_COLUMNS;
        setLayout(gridLayout);
    }

    private void addComponents() {
        addHeaders();
        addTextBoxes();
        addCalculateButton();
    }

    private void addCalculateButton() {
        calculate = new Button(this, SWT.PUSH);
        calculate.setText(CALCULATE_TEXT);
        ControlName.setControlName(calculate, CTRL_NAME_CALCULATE);
    }

    private void addTextBoxes() {
        num1 = createTextBox(CTRL_NAME_NUM1);
        num2 = createTextBox(CTRL_NAME_NUM2);
        result = createTextBox(CTRL_NAME_RESULT);
    }

    private Text createTextBox(String controlName) {
        final Text text = new Text(this, SWT.BORDER);
        ControlName.setControlName(text, controlName);
        return text;
    }

    private void addHeaders() {
        num1Header = createHeaderLabel(NUM1_HEADER_TEXT);
        num2Header = createHeaderLabel(NUM2_HEADER_TEXT);
        resultHeader = createHeaderLabel(RESULT_HEADER_TEXT);
    }

    private Label createHeaderLabel(String headerText) {
        Label label = new Label(this, SWT.NONE);
        label.setText(headerText);
        return label;
    }

    private void addCalculateButtonListener() {
        calculate.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                calculateResultAndSetText();
            }
        });
    }

    private void calculateResultAndSetText() {
        result.setText(calculateResultAsString());
    }

    private String calculateResultAsString() {
        try {
            return String.valueOf(createSimpleBusinessObject().addition());
        } catch (NumberFormatException e) {
            return INVALID_INPUTS;
        }
    }

    private SimpleBusinessObject createSimpleBusinessObject() {
        return new SimpleBusinessObject(getTextAsInt(num1), getTextAsInt(num2));
    }

    private int getTextAsInt(Text text) {
        return Integer.parseInt(text.getText());
    }

}
