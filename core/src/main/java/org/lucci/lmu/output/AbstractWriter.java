package org.lucci.lmu.output;

import org.lucci.lmu.domain.AbstractModel;

import java.util.HashMap;
import java.util.Map;

/*
 * Created on Oct 3, 2004
 */

/**
 * @author luc.hogie
 */
public abstract class AbstractWriter {

	private String output;

	public abstract byte[] writeModel(AbstractModel diagram) throws WriterException;

    public String getOutputType(){
        return output;
    }

    public void setOutputType(String type){
        this.output = type;
    }
}
