package fr.unice.polytech.rimel.testplugin;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;

import fr.unice.polytech.rimel.lmueclipseui.preferences.PreferenceConstants;

public class ContextMenuHandler implements IHandler {

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
	}

	@Override
	public void dispose() {	
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		System.out.println("MEGA LOL");
		
		IPreferenceStore prefs = Activator.getDefault().getPreferenceStore();
		String formatName = prefs.getString(PreferenceConstants.P_FORMAT);
		String outputDir =  prefs.getString(PreferenceConstants.P_PATH);
		System.out.println("Output format: "+formatName);
		System.out.println("Output dir: "+outputDir);
		if (outputDir.substring(outputDir.length() - 1).equals("/")) {
			outputDir = outputDir.substring(0, outputDir.length() - 1);
		}
		
		// get workbench window
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		// set selection service
		ISelectionService service = window.getSelectionService();
		// set structured selection
		IStructuredSelection structured = (IStructuredSelection) service.getSelection();
	 
		//check if it is an IFile
		if (structured.getFirstElement() instanceof IFile) {
			// get the selected file
			IFile file = (IFile) structured.getFirstElement();
			// get the path
			IPath path = file.getLocation();
			System.out.println("File: " + path.toPortableString());
		} 
		// check if it is an ICompilationUnit
		else if (structured.getFirstElement() instanceof ICompilationUnit) {
			ICompilationUnit cu = (ICompilationUnit) structured.getFirstElement();
			String className = cu.getElementName();
			String outputFilePath = outputDir + className;
			
			System.out.println("Compilation unit: " + cu.getElementName());
			System.out.println("Output file: "+outputFilePath);
			
			IResource res = null;
			try {
				res = cu.getUnderlyingResource();
			} catch (JavaModelException e) {
				throw new RuntimeException(e);
			}
			if (res.getType() == IResource.FILE) {
			    IFile ifile = (IFile) res;
			    String path = ifile.getRawLocation().toString();
			    System.out.println("Got file: "+path);
			}
			

		}
		else {
			System.out.println("Not a file!");
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
