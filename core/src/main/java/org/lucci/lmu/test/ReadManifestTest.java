package org.lucci.lmu.test;

import org.lucci.lmu.domain.AbstractModel;
import org.lucci.lmu.input.DeploymentUnitAnalyzer;

/**
 * Created by quentin on 09/02/16.
 */
public class ReadManifestTest {

    public static void main(String argc[]) {
        DeploymentUnitAnalyzer deploymentUnitAnalyzer = new DeploymentUnitAnalyzer();
        AbstractModel abstractModel = deploymentUnitAnalyzer.createModelFromJar("/home/quentin/Documents/SI5/Retro/RetroEngineering/core/src/main/resources/lmu-eclipse-plugin-bkp_1.0.0.201601261055.jar");



    }
}
