package org.jenkinsci.plugin.matrixconfigsorter;

import hudson.Extension;
import hudson.matrix.MatrixConfiguration;
import hudson.matrix.MatrixConfigurationSorter;
import hudson.matrix.MatrixConfigurationSorterDescriptor;
import hudson.matrix.MatrixProject;
import hudson.util.FormValidation;
import java.io.Serializable;
import org.kohsuke.stapler.DataBoundConstructor;

@Extension
public class ShortestBuildFirstSorter extends MatrixConfigurationSorter implements Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public int compare(MatrixConfiguration configuration1, MatrixConfiguration configuration2) {
        int comparison = Long.compare(configuration1.getEstimatedDuration(), configuration2.getEstimatedDuration());
        if (comparison != 0) {
            return comparison;
        }
        return configuration1.getDisplayName().compareTo(configuration2.getDisplayName());
    }

    @DataBoundConstructor
    public ShortestBuildFirstSorter() {}

    @Override
    public void validate(MatrixProject p) throws FormValidation {
        // No specific configuration to validate.
    }

    @Extension
    public static class DescriptorImpl extends MatrixConfigurationSorterDescriptor {
        @Override
        public String getDisplayName() {
            return Messages.ShortestBuildFirstSorter_DisplayName();
        }
    }
}
