package org.lucci.lmu.output;

import org.lucci.lmu.domain.AbstractModel;

/**
 * Created by remy on 12/01/16.
 */
public class PDFWriter extends AbstractWriter {
    private final String type = "pdf";

    @Override
    public byte[] writeModel(AbstractModel diagram) throws WriterException {
        return new byte[0];
    }

    public String getType() {
        return type;
    }
}
