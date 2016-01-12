package org.lucci.lmu.input;

import org.lucci.lmu.domain.Model;

/**
 * Created by quentin on 12/01/16.
 */
public abstract class ModelCreator {

    public abstract Model createModel(byte[] data) throws ParseError, ModelException;
}
