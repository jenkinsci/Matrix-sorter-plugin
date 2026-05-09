# Matrix Project Sorter Plugin

This plugin enables sorting matrix configurations.

There are three sorting modes:

1) Axis order (configurable) – sort by one or more selected axes in the configured priority order.
   - Use the optional `Axis names` field with comma-separated axis names, for example: `BROWSER, PLATFORM`.
   - If this field is empty, the sorter falls back to the last axis behavior for backward compatibility.

2) Estimation duration of build (longest first) – determine estimated build duration from previous builds; longer estimations are scheduled first.

3) Estimation duration of build (shortest first) – same as above, but in reverse order.

## Configuration

After installing the plugin, open your matrix project configuration and select a sorting option.
For `Axis order (configurable)`, you can also set axis names to control sorting explicitly.