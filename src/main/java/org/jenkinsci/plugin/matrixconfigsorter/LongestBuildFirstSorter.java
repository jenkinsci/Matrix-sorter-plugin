package org.jenkinsci.plugin.matrixconfigsorter;

import hudson.Extension;
import hudson.matrix.MatrixConfiguration;
import hudson.matrix.MatrixConfigurationSorter;
import hudson.matrix.MatrixConfigurationSorterDescriptor;
import hudson.matrix.MatrixProject;
import hudson.util.FormValidation;
import org.kohsuke.stapler.DataBoundConstructor;

@Extension
public class LongestBuildFirstSorter extends MatrixConfigurationSorter {
    @Override
    public int compare(MatrixConfiguration configuration1, MatrixConfiguration configuration2) {
        int comparison = Long.compare(configuration2.getEstimatedDuration(), configuration1.getEstimatedDuration());
        if (comparison != 0) {
            return comparison;
        }
        return configuration2.getDisplayName().compareTo(configuration1.getDisplayName());
    }

    @DataBoundConstructor
    public LongestBuildFirstSorter() {}

    @Override
    public void validate(MatrixProject p) throws FormValidation {
        // No specific configuration to validate.
    }

    @Extension
    public static class DescriptorImpl extends MatrixConfigurationSorterDescriptor {
        @Override
        public String getDisplayName() {
            return Messages.LongestBuildFirstSorter_DisplayName();
        }
    }
}
