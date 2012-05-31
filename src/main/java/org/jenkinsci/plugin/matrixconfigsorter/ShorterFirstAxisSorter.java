/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jenkinsci.plugin.matrixconfigsorter;

import hudson.Extension;
import hudson.matrix.Axis;
import hudson.matrix.MatrixConfiguration;
import hudson.matrix.MatrixConfigurationSorter;
import hudson.matrix.MatrixConfigurationSorterDescriptor;
import hudson.matrix.MatrixProject;
import hudson.util.FormValidation;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 *
 * @author lucinka
 */
public class ShorterFirstAxisSorter extends MatrixConfigurationSorter{
    public int compare(MatrixConfiguration configuration1, MatrixConfiguration configuration2) {
        Long time = (configuration1.getEstimatedDuration());
        return time.compareTo(configuration2.getEstimatedDuration());
        
    }

    @DataBoundConstructor
    public ShorterFirstAxisSorter() {
    }
    

    @Override
    public void validate(MatrixProject p) throws FormValidation {
        if(p.getAxes().size()<1){
            FormValidation.error("Sorting by last axis need at leas one axis");
        }
    }

    @Extension(ordinal=100) // this is the default
    public static class DescriptorImpl extends MatrixConfigurationSorterDescriptor {
        @Override
        public String getDisplayName() {
            return "Estimation duration of build (shortest first)";
        }

    }
}