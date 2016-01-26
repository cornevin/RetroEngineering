package org.lucci.lmu.output;

import org.lucci.lmu.domain.AbstractModel;
import toools.extern.Proces;
import toools.io.file.Directory;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author luc.hogie
 */
public class GraphVizBasedViewFactory extends AbstractWriter {

    public final static Collection<String> supportedOutputTypes = Arrays
            .asList("canon", "dot", "xdot", "cmap", "dia",
					"fig", "gd", "gd2", "gif", "hpgl", "imap", "cmapx",
					"ismap", "jpg", "jpeg", "mif", "mp", "pcl", "pic", "plain",
					"plain-ext", "png", "ps", "ps2", "svg", "svgz", "vrml",
					"vtx", "wbmp", "pdf");
    

	public GraphVizBasedViewFactory(String type) {
        if (supportedOutputTypes.contains(type)) {
            setOutputType(type);
        } else {
            setOutputType("jpeg");
        }
	}

	@Override
	public byte[] writeModel(AbstractModel model) throws WriterException {
		DotWriter dotTextFactory = new DotWriter();
		byte[] dotText = dotTextFactory.writeModel(model);
        // TODO find where file is generated
        System.out.println(Directory.getCurrentDirectory());
        return Proces.exec("dot", dotText, "-T" + getOutputType());
	}
}
