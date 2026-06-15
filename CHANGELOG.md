# Changelog

All notable changes to this project are documented in this file.

## Version x.x

Major update for configurable axis sorting and fallback behavior.
All deprecated sorter class names are kept as compatibility wrappers for existing job configuration deserialization and
will be removed in the next major release.

### Added

- Added configurable axis sorting via `AxisOrderSorter` with optional `Axis names` (comma-separated axis names in
  priority order).
- Added new sorter classes with clearer names:
    - `AxisOrderSorter`
    - `ShortestBuildFirstSorter`
    - `LongestBuildFirstSorter`
- Added `config.jelly` for `AxisOrderSorter` to expose axis configuration in Jenkins UI.
- Integration tests.
- UI localization for **pt, ru**.
- Stapler sidecar bundles for `AxisOrderSorter/config.jelly`: `config.properties` and matching
  `config_<locale>.properties` for the same locales.
- Plugin summary in `index.properties` plus `index_<locale>.properties` for the same locales next to `index.jelly`.

### Changed

- Updated axis sorting behavior to support sorting by multiple configured axes while preserving fallback to last-axis
  behavior when no axes are configured.
- Renamed UI display label from `Last axis order` to `Axis order (configurable)`.
- Updated `README.md` to describe configurable axis sorting and fallback behavior.

### Deprecated

- Deprecated legacy sorter class names, kept as compatibility wrappers for existing job configuration deserialization:
    - `LastAxisSorter` (use `AxisOrderSorter`)
    - `ShorterFirstAxisSorter` (use `ShortestBuildFirstSorter`)
    - `TimeStampAxisSorter` (use `LongestBuildFirstSorter`)
