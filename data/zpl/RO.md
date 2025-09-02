# ZPL Command: ~RO (Reset Advanced Counter)

## Description
The ~RO command resets the advanced counters used by the printer to monitor label generation in inches, centimeters, and number of labels. Two resettable counters are available and can be reset.

## Format
```
~ROc
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **c** | Counter number | 1 or 2 | A value must be specified or the command is ignored |

## Examples
```
~RO1
```
```
~RO2
```

## Important Notes
- Two resettable counters are available for monitoring label generation.
- Counters track label generation in inches, centimeters, and number of labels.
- A value must be specified or the command will be ignored.

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*