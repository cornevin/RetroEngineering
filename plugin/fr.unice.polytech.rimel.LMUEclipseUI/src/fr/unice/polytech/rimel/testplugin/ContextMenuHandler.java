package fr.unice.polytech.rimel.testplugin;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.lucci.lmu.plugin_communication.LmuConfiguration;
import org.lucci.lmu.plugin_communication.OutputAvailable;

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
		System.out.println("Processing LMU!");
		
		IPreferenceStore prefs = Activator.getDefault().getPreferenceStore();
		String formatName = prefs.getString(PreferenceConstants.P_FORMAT);
		String outputDir =  prefs.getString(PreferenceConstants.P_PATH);
		
		OutputAvailable selectedOutputFormat;
		try {
			selectedOutputFormat = OutputAvailable.valueOf(formatName);
		} catch (IllegalArgumentException e) {
			selectedOutputFormat = OutputAvailable.PNG;
			System.out.println("Defaulting to "+selectedOutputFormat.name()+" format because of incorrect setting value '"+formatName+"'");
			prefs.putValue(PreferenceConstants.P_FORMAT, selectedOutputFormat.name());
		}
		System.out.println("Output format: "+selectedOutputFormat.name());
		
		System.out.println("Output dir: "+outputDir);
		// Remove the trailing slash from output dir.
		if (outputDir.substring(outputDir.length() - 1).equals("/")) {
			outputDir = outputDir.substring(0, outputDir.length() - 1);
		}
		
		// get workbench window
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		// set selection service
		ISelectionService service = window.getSelectionService();
		// set structured selection
		IStructuredSelection structured = (IStructuredSelection) service.getSelection();
	 
		// check if it is an IFile
		
		List<String> selectedElementsNames = new LinkedList<String>();
		for (Object o: structured.toArray()) {
			if (o instanceof IJavaElement) {
				selectedElementsNames.add(((IJavaElement) o).getElementName());
			}
		}
		String selectionString = String.join("_", selectedElementsNames);
		String outputPath = outputDir+"/"+selectionString;
		
		LmuConfiguration lmu = new LmuConfiguration();
		SelectionProcessor processor = new SelectionProcessor();
		List<Class<?>> classes;
		try {
			classes = processor.processSelection(structured);
			System.out.println("Got "+classes.size()+" classes in input!");
			lmu.setInputClazzes(classes);
			lmu.setOutputExtension(OutputAvailable.PNG);
			lmu.setOuputFileName(outputPath);
			lmu.createModel();
			System.out.println("Done :D");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return null;
		/*
		if (true) return null;
		
		if (firstElement instanceof IFile) {
			// get the selected file
			IFile file = (IFile) structured.getFirstElement();
			// get the path
			IPath path = file.getLocation();
			System.out.println("File: " + path.toPortableString());
		} 
		// check if it is an ICompilationUnit
		else if (firstElement instanceof ICompilationUnit) {
			ICompilationUnit cu = (ICompilationUnit) firstElement;
			IPath projectLocation = cu.getJavaProject().getProject().getLocation();
			// Relative path to the java file
			IPath resourceRelativePath = cu.getResource()
					.getProjectRelativePath()
					.removeFirstSegments(1);	// remove the project name as it is already provided by the project location
		
			// Get the relative path to the class file (considering project directory as root)
			IPath classRelativePath = cu.getResource()
					.getProjectRelativePath()	// eg: src/mypackage/MyClass.java
					.removeFirstSegments(1)		// remove src
					.removeFileExtension().addFileExtension("class");	// replace extension to class
			
			// Find where eclipse is outputting the classes.
			String eclipseOutputLocation;
			try {
				eclipseOutputLocation = cu.getJavaProject().getOutputLocation()
						.removeFirstSegments(1)
						.toString();
			} catch (JavaModelException e2) {
				throw new RuntimeException(e2);
			}
			IPath eclipseAbsoluteOutputPath = projectLocation.append(eclipseOutputLocation);
			
			IPath classAbsolutePath = eclipseAbsoluteOutputPath.append(classRelativePath);
			
			System.out.println("Project location is: "+projectLocation);
			System.out.println("Output location: "+eclipseOutputLocation);
			System.out.println("Relative path of resource: "+resourceRelativePath);
			System.out.println("Absolute path of class: "+classAbsolutePath);
			
			URL urls[];
			try {
				urls = new URL[]{
						eclipseAbsoluteOutputPath.toFile().toURI().toURL()
				};
				System.out.println("Providing class loader with url: "+urls[0].toString());
			} catch (MalformedURLException e2) {
				throw new RuntimeException(e2);
			}
			System.out.println("Element name:" + cu.getElementName());
			ClassLoader classLoader = new URLClassLoader(urls);
			
			try {
				String classname = resourceRelativePath.removeFileExtension().toString().replace('/', '.');
				System.out.println("Loading class: "+classname);
				Class<?> myClass = classLoader.loadClass(classname);
				System.out.println("Got class! "+myClass.getName());
			} catch (ClassNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			String className = cu.getElementName();
			String outputFilePath = Paths.get(outputDir, className).toString();
			
			System.out.println("CLASS");
			System.out.println("Compilation unit: " + cu.getElementName());
			System.out.println("Output file: "+outputFilePath);
			
			List<String> sourcesFiles = new LinkedList<String>();
			
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
			    
			    LmuConfiguration lmuConfig = new LmuConfiguration();
			    lmuConfig.setInput(path);
			    lmuConfig.setInputExtension(ExtensionAvailable.JAVA);
			    lmuConfig.setOutputExtension(OutputAvailable.PDF);
			    lmuConfig.run(path);
			}
		}
		else if (firstElement instanceof IPackageFragment) {
			IPackageFragment pkg = (PackageFragment) firstElement;
			String packageName = pkg.getElementName();
			if (packageName.length() == 0) {
				packageName = "_defaultPackage";
			}
			String outputFilePath = Paths.get(outputDir, packageName).toString();
			
			System.out.println("PACKAGE");
			System.out.println("Compilation unit: " + pkg.getElementName());
			System.out.println("Output file: "+outputFilePath);
			
			IResource res = null;
			try {
				res = pkg.getUnderlyingResource();
			} catch (JavaModelException e) {
				throw new RuntimeException(e);
			}
			System.out.println("Resource type: "+res.getType());
			System.out.println(IResource.FILE+" "+IResource.FOLDER);
			if (res.getType() == IResource.FOLDER) {
			    IFolder ifolder= (IFolder) res;
			    String path = ifolder.getRawLocation().toFile().toString(); //oString();
			    System.out.println("Input file: "+path);
			    
			    LmuConfiguration lmuConfig = new LmuConfiguration();
			    lmuConfig.setInput(path);
			    lmuConfig.setInputExtension(ExtensionAvailable.PACKAGE);
			    lmuConfig.setOutputExtension(OutputAvailable.PDF);
			    lmuConfig.run(path);
			}
			//IPath path = pkg.getUnderlyingResource();
			//System.out.println("Processing package: "+path.toFile().toPath().toString());*
		}
		else {
			System.out.println("Not a file: "+structured.getFirstElement().getClass().getName());
		}
		
		return null;
		*/
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
