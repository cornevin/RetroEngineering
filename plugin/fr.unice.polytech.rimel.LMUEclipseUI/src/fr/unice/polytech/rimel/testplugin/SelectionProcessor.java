package fr.unice.polytech.rimel.testplugin;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.management.RuntimeErrorException;
import javax.swing.JOptionPane;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.ISourceManipulation;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.lucci.lmu.plugin_communication.LmuConfiguration;

public class SelectionProcessor {
	
	private LmuConfiguration lmu;

	public SelectionProcessor(LmuConfiguration lmu) {
		this.lmu = lmu;
	}

	public void processSelection(IStructuredSelection selection) throws Exception {
		List<Class<?>> classes = new LinkedList<Class<?>>();
		boolean containsJar = false;
		IFile jarFile = null;
		for (Object selectedObject : selection.toArray()) {
			if (selectedObject instanceof IFile) {
				System.out.println("There is a regular JAR file");
				containsJar = true;
				jarFile = (IFile) selectedObject;
				break;
			}
			if (selectedObject instanceof IJavaElement) {
				classes.addAll(this.processSource((IJavaElement) selectedObject));
			}
		}
		
		if (!containsJar) {
			lmu.setInputClazzes(classes);			
		}
		else {
			JOptionPane.showMessageDialog(null, "There is JAR file in the selection, LMU will be only run against it.");
			lmu.setInputPath(jarFile.getLocation().toFile().getAbsolutePath());
		}
		lmu.createModel();
	}
	
	public List<Class<?>> processSource(IJavaElement selection) throws Exception {
		IClasspathEntry[] classpaths = selection.getJavaProject().getResolvedClasspath(false);
		
		IPath projectLocation = selection.getJavaProject().getProject().getLocation();
		IPath eclipseOutputLocation = selection.getJavaProject()
				.getOutputLocation()
				.removeFirstSegments(1);
		
		ClassLoader classLoader = this.createClassLoader(projectLocation, eclipseOutputLocation, classpaths);
		
		Set<IPath> sourceFilesToLoad = this.analyzeJavaElement(selection);
		System.out.println("Got class files to load: "+sourceFilesToLoad.toString());
		
		// Convert to class names
		Set<String> classesToLoad = this.convertSourcesToClassNames(sourceFilesToLoad);
		
		List<Class<?>> loadedClasses = new LinkedList<Class<?>>();
		for (String classFullName: classesToLoad) {
			Class<?> loadedClass = classLoader.loadClass(classFullName);
			loadedClasses.add(loadedClass);
		}
		
		return loadedClasses;
	}

	private Set<IPath> analyzeJavaElement(IJavaElement element) {
		if (element instanceof ICompilationUnit) {
			return this.analyzeCompilationUnit((ICompilationUnit) element);
		}
		else if (element instanceof IPackageFragment) {
			return this.analyzePackageFragment((IPackageFragment) element);
		}
		else {
			throw new RuntimeException("Unsupported Java element! "+element.getClass().getName());
		}
	}
	
	private Set<IPath> analyzePackageFragment(IPackageFragment pkg) {
		IJavaElement[] children;
		try {
			children = pkg.getChildren();
		} catch (JavaModelException e) {
			throw new RuntimeException(e);
		}
		
		Set<IPath> result = new HashSet<IPath>();
		for (IJavaElement element: children) {
			result.addAll(this.analyzeJavaElement(element));
		}
		
		return result;
	}

	public Set<IPath> analyzeCompilationUnit(ICompilationUnit unit) {
		IPath resourceRelativePath = unit.getResource()
				.getProjectRelativePath()
				.removeFirstSegments(1);	// remove the project name as it is already provided by the project location
		
		Set<IPath> set = new HashSet<IPath>();
		set.add(resourceRelativePath);
		return set;
	}
	
	private ClassLoader createClassLoader(IPath projectLocation, IPath outputRelativeLocation, IClasspathEntry[] classpaths) {
		System.out.println("Got "+classpaths.length+" classpath entries");
		IPath eclipseAbsoluteOutputPath = projectLocation.append(outputRelativeLocation);
		
		URL[] urls = new URL[1];// + classpaths.length];
		try {
			urls[0] = eclipseAbsoluteOutputPath.toFile().toURI().toURL();
		} catch (MalformedURLException e2) {
			throw new RuntimeException(e2);
		}
		for (int i = 0; i < classpaths.length; ++i) {
			IClasspathEntry cp = classpaths[i];
			System.out.println("\tClasspath entry:" + cp.getPath().toString());
		}
		
		return new URLClassLoader(urls);
	}


	private Set<String> convertSourcesToClassNames(Set<IPath> sourceFilesToLoad) {
		Set<String> result = new HashSet<String>(sourceFilesToLoad.size());
		for (IPath sourcePath: sourceFilesToLoad) {
			result.add(sourcePath
					.removeFileExtension()
					.toString()
					.replace('/', '.')
			);
		}
		return result;
	}
	
}
