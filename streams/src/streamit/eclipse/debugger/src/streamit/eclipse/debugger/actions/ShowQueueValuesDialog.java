package streamit.eclipse.debugger.actions;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author kkuo
 */
public class ShowQueueValuesDialog extends Dialog {

    
    /**
     * The title of the dialog.
     */
    private String title;
     
    /**
     * The message to display, or <code>null</code> if none.
     */
    private String message;

    /**
     * The input value; the empty string by default.
     */
    private String value= IStreamItActionConstants.EMPTY_STRING;//$NON-NLS-1$

    /**
     * The input validator, or <code>null</code> if none.
     */
    private IInputValidator validator;

    /**
     * Ok button widget.
     */
    private Button okButton;

    /**
     * Input text widget.
     */
    private Text text;

    /**
     * Error message label widget.
     */
    private Label errorMessageLabel;
    
    private int fHeight;
    
    /**
     * Creates an input dialog with OK and Cancel buttons.
     * Note that the dialog will have no visual representation (no widgets)
     * until it is told to open.
     * <p>
     * Note that the <code>open</code> method blocks for input dialogs.
     * </p>
     *
     * @param parentShell the parent shell
     * @param dialogTitle the dialog title, or <code>null</code> if none
     * @param dialogMessage the dialog message, or <code>null</code> if none
     * @param initialValue the initial input value, or <code>null</code> if none
     *  (equivalent to the empty string)
     * @param validator an input validator, or <code>null</code> if none
     */
    public ShowQueueValuesDialog(Shell parentShell, String dialogTitle, String dialogMessage, String initialValue, IInputValidator validator) {
        super(parentShell);
        this.title = dialogTitle;
        message = dialogMessage;
        if (initialValue == null)
            value = IStreamItActionConstants.EMPTY_STRING;//$NON-NLS-1$
        else
            value = initialValue;
        this.validator = validator;
        fHeight = FigureUtilities.getTextExtents(initialValue, parentShell.getFont()).height;
    }
    
    /* (non-Javadoc)
     * Method declared on Dialog.
     */
    protected void buttonPressed(int buttonId) {
        if (buttonId == IDialogConstants.OK_ID) {
            value= text.getText();
        } else {
            value= null;
        }
        super.buttonPressed(buttonId);
    }
    
    /* (non-Javadoc)
     * Method declared in Window.
     */
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        if (title != null)
            shell.setText(title);
    }
    
    /* (non-Javadoc)
     * Method declared on Dialog.
     */
    protected void createButtonsForButtonBar(Composite parent) {
        // create OK buttons by default
        okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    
        //do this here because setting the text will set enablement on the ok button
        text.setFocus();
        if (value != null) {
            text.setText(value);
            text.selectAll();
        }
    }
    
    /* (non-Javadoc)
     * Method declared on Dialog.
     */
    protected Control createDialogArea(Composite parent) {
        Font font = parent.getFont();
        // create composite
        Composite composite = (Composite)super.createDialogArea(parent);
    
        // create message
        if (message != null) {
            Label label = new Label(composite, SWT.WRAP);
            label.setText(message);
            GridData data = new GridData(
                                         GridData.GRAB_HORIZONTAL |
                                         GridData.HORIZONTAL_ALIGN_FILL |
                                         GridData.VERTICAL_ALIGN_CENTER);
            data.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);;
            label.setLayoutData(data);
            label.setFont(font);
        }
    
        text= new Text(composite, SWT.MULTI | SWT.BORDER | SWT.READ_ONLY | SWT.V_SCROLL | SWT.H_SCROLL);
        GridData gridData= new GridData(GridData.GRAB_HORIZONTAL |  GridData.HORIZONTAL_ALIGN_FILL 
                                        | GridData.GRAB_VERTICAL | GridData.VERTICAL_ALIGN_FILL);
        gridData.heightHint= fHeight;
        text.setLayoutData(gridData);
        text.setFont(font);
        text.addModifyListener(
                               new ModifyListener() {
                                   public void modifyText(ModifyEvent e) {
                                       if (okButton.isEnabled()) {
                                           return;
                                       }
                                       errorMessageLabel.setText(IStreamItActionConstants.EMPTY_STRING); //$NON-NLS-1$
                                       errorMessageLabel.getParent().update();
                                       okButton.setEnabled(true);
                                   }
                               }
                               );
    
        errorMessageLabel = new Label(composite, SWT.NONE);
        errorMessageLabel.setLayoutData(new GridData(
                                                     GridData.GRAB_HORIZONTAL |
                                                     GridData.HORIZONTAL_ALIGN_FILL));
        errorMessageLabel.setFont(font);
        
        return composite;
    }
    /**
     * Returns the error message label.
     *
     * @return the error message label
     */
    protected Label getErrorMessageLabel() {
        return errorMessageLabel;
    }
    /**
     * Returns the ok button.
     *
     * @return the ok button
     */
    protected Button getOkButton() {
        return okButton;
    }
    /**
     * Returns the text area.
     *
     * @return the text area
     */
    protected Text getText() {
        return text;
    }
    /**
     * Returns the validator.
     *
     * @return the validator
     */
    protected IInputValidator getValidator() {
        return validator;
    }
    /**
     * Returns the string typed into this input dialog.
     *
     * @return the input string
     */
    public String getValue() {
        return value;
    }
    /**
     * Validates the input.
     * <p>
     * The default implementation of this framework method
     * delegates the request to the supplied input validator object;
     * if it finds the input invalid, the error message is displayed
     * in the dialog's message line.
     * This hook method is called whenever the text changes in the
     * input field.
     * </p>
     */
    protected void validateInput() {
    
        String errorMessage = null;
    
        if (validator != null) {
            errorMessage = validator.isValid(text.getText());
        }
    
        // Bug 16256: important not to treat "" (blank error) the same as null (no error)
        errorMessageLabel.setText(errorMessage == null ? IStreamItActionConstants.EMPTY_STRING : errorMessage); //$NON-NLS-1$
        okButton.setEnabled(errorMessage == null);
    
        errorMessageLabel.getParent().update();
    }
    protected void okPressed() {
        validateInput();
        if (okButton.isEnabled()) {
            super.okPressed();
        }
    }
}
