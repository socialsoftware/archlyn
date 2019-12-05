package plugin.shells;

import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import plugin.classes.Rule;
import plugin.classes.Share;

public class EditRuleShell extends Shell {

	public EditRuleShell(Display display, TableViewer tableR) {
		super(display, SWT.SHELL_TRIM);
		createContents(tableR);
	}

	protected void createContents(TableViewer tableR) {
		setText("Apply Rule");
		setSize(300, 300);
		setLayout(new GridLayout(1, false));

		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.verticalSpacing = 15;
		composite.setLayout(gl_composite);

		Label labelChooseAT = new Label (composite, SWT.NONE);
		labelChooseAT.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		Combo comboChooseAT = new Combo (composite, SWT.READ_ONLY);
		GridData gd_comboChooseAT = new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1);
		gd_comboChooseAT.minimumWidth = 110;
		comboChooseAT.setLayoutData(gd_comboChooseAT);		
		Label labelChooseRule = new Label (composite, SWT.NONE);
		labelChooseRule.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		Combo comboChooseRule = new Combo (composite, SWT.READ_ONLY);
		comboChooseRule.setLayoutData(gd_comboChooseAT);		
		Button buttonApplyRule = new Button (composite, SWT.CENTER);
		buttonApplyRule.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));

		labelChooseAT.setText("Artifact Type");		
		comboChooseAT.addFocusListener(new FocusListener() {
     			public void focusGained(FocusEvent event) {
     				comboChooseAT.setItems(Share.status.getArtifactTypesSA());	
     			}
     			public void focusLost(FocusEvent event){
     			}
     		});	
		
		labelChooseRule.setText("Choose Rule");		
		comboChooseRule.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent event) {
				comboChooseRule.setItems(Share.status.getRuleTypesSA());	
			}
			public void focusLost(FocusEvent event){
			}
		});	
        
        buttonApplyRule.setText ("OK");
        buttonApplyRule.addSelectionListener(widgetSelectedAdapter(e -> {
			if(!comboChooseAT.getText().equals("") && !comboChooseRule.getText().equals("")) {
				Share.status.getRules().add(new Rule(Share.status.findArtifactType(comboChooseAT.getText()),Share.status.findRuleType(comboChooseRule.getText())));
				Share.project.saveStatus();
			}
			this.dispose();
		}));	
        
        this.pack();
		this.setMinimumSize(500, 300);
		this.redraw();
		this.update();
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}