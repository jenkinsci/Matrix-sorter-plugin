package org.jenkinsci.plugin.matrixconfigsorter;

import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Compatibility wrapper for old job configurations.
 *
 * @deprecated use {@link ShortestBuildFirstSorter}
 */
@Deprecated
public class ShorterFirstAxisSorter extends ShortestBuildFirstSorter {
    @DataBoundConstructor
    public ShorterFirstAxisSorter() {
        super();
    }
}
