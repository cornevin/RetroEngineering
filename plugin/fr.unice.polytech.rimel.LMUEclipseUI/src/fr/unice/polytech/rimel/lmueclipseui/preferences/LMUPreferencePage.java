package fr.unice.polytech.rimel.lmueclipseui.preferences;

import java.awt.Label;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.lucci.lmu.plugin_communication.OutputAvailable;
import org.eclipse.ui.IWorkbench;
import fr.unice.polytech.rimel.testplugin.Activator;

/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */

public class LMUPreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	public LMUPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Settings for LMU4Eclipse plugin");
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
		// Browser for output directory
		addField(new DirectoryFieldEditor(PreferenceConstants.P_PATH, 
				"&Output Directory:", getFieldEditorParent()));

		// Output format
		// Build the format options
		String[][] formats = new String[OutputAvailable.values().length][];
		OutputAvailable[] availableFormatsEnum = OutputAvailable.values();
		for (int iFormat = 0; iFormat < availableFormatsEnum.length;  ++iFormat) {
			String rawEnumName = availableFormatsEnum[iFormat].name();
			formats[iFormat] = new String[] {
					rawEnumName, rawEnumName
			};
		}
		
		addField(new ComboFieldEditor(PreferenceConstants.P_FORMAT, "&Choose your preferred output format", formats, getFieldEditorParent()));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
	
}