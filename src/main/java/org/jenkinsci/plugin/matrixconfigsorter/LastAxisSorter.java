package org.jenkinsci.plugin.matrixconfigsorter;

import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Compatibility wrapper for old job configurations.
 *
 * @deprecated use {@link AxisOrderSorter}
 */
@Deprecated
public class LastAxisSorter extends AxisOrderSorter {
    @DataBoundConstructor
    public LastAxisSorter() {
        super();
    }
}
