package org.jenkinsci.plugin.matrixconfigsorter;

import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Compatibility wrapper for old job configurations.
 *
 * @deprecated use {@link LongestBuildFirstSorter}
 */
@Deprecated
public class TimeStampAxisSorter extends LongestBuildFirstSorter {
    @DataBoundConstructor
    public TimeStampAxisSorter() {
        super();
    }
}
