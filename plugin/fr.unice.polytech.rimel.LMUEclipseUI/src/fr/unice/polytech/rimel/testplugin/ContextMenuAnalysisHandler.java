package fr.unice.polytech.rimel.testplugin;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.lucci.lmu.plugin_communication.ConfigurationException;
import org.lucci.lmu.plugin_communication.LmuConfiguration;
import org.lucci.lmu.plugin_communication.OutputAvailable;

import fr.unice.polytech.rimel.lmueclipseui.preferences.PreferenceConstants;

public class ContextMenuAnalysisHandler implements IHandler {

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
	}

	@Override
	public void dispose() {	
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		JOptionPane.showMessageDialog(null, "New analysis!");
		System.out.println("Processing LMU!");
		
		IPreferenceStore prefs = Activator.getDefault().getPreferenceStore();
		String formatName = prefs.getString(PreferenceConstants.P_FORMAT);
		String outputDir =  prefs.getString(PreferenceConstants.P_PATH);
		if (outputDir.length() == 0) {
			JOptionPane.showMessageDialog(null, "Error: you did not defined the output directory. To do so, go to Window -> Preferences -> LMU Pettings.");
			return null;
		}
		
		// Process output format from preferences (with fail-over)
		OutputAvailable selectedOutputFormat;
		try {
			selectedOutputFormat = OutputAvailable.valueOf(formatName);
		} catch (IllegalArgumentException e) {
			selectedOutputFormat = OutputAvailable.PNG;
			System.out.println("Defaulting to "+selectedOutputFormat.name()+" format because of incorrect setting value '"+formatName+"'");
			prefs.putValue(PreferenceConstants.P_FORMAT, selectedOutputFormat.name());
		}
		System.out.println("Output format: "+selectedOutputFormat.name());
		
		// Processing output directory
		System.out.println("Output dir: "+outputDir);
		// Remove the trailing slash from output dir.
		if (outputDir.substring(outputDir.length() - 1).equals("/")) {
			outputDir = outputDir.substring(0, outputDir.length() - 1);
		}
		
		// Processing the selection
		//
		// get workbench window
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		// set selection service
		ISelectionService service = window.getSelectionService();
		// set structured selection
		IStructuredSelection structured = (IStructuredSelection) service.getSelection();
	 
		// check if it is an IFile
		
		List<IFile> selectedFiles = new LinkedList<>();
		List<String> selectedElementsNames = new LinkedList<String>();
		for (Object o: structured.toArray()) {
			if (o instanceof IFile) {
				selectedFiles.add((IFile) o);
				selectedElementsNames.add(((IFile) o).getName());
			}
		}
		String selectionString = String.join("_", selectedElementsNames);
		System.out.println("Output filename:"+selectionString);
		String outputPath = outputDir+"/dependency_"+selectionString;
		
		// Configuration LMU
		LmuConfiguration lmu = new LmuConfiguration();
		lmu.setOutputExtension(selectedOutputFormat);
		lmu.setOuputFileName(outputPath);
		String jarPath = selectedFiles.get(0).getRawLocation().toOSString();
		System.out.println("JARPATH="+jarPath);
		lmu.setJarUnitPath(jarPath);
		
		try {
			lmu.createModel();
			JOptionPane.showMessageDialog(null, "Success! File created: "+outputPath+"."+selectedOutputFormat.name().toLowerCase());
			System.out.println("Done :D");
		} catch (ConfigurationException e) {
			JOptionPane.showMessageDialog(null, "There was an error!");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	
		
		return null;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean isHandled() {
		return true;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
	}

}
