# ZPL Command: ^XF (Recall Format)

## Description
The ^XF command recalls a stored format to be merged with variable data. There can be multiple ^XF commands in one format, and they can be located anywhere within the code. When recalling a stored format and merging data using the ^FN (Field Number) function, the calling format must contain the ^FN command to merge the data properly.

## Format
```
^XFd:o.x
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **d** | Source device of stored image | R:, E:, B:, and A: | Search priority (R:, E:, B:, and A:) |
| **o** | Name of stored image | 1 to 8 alphanumeric characters | UNKNOWN |
| **x** | Extension | .ZPL | .ZPL (fixed value) |

## Examples
```zpl
// Recall format STOREFMT.ZPL from DRAM and insert new data in ^FN fields
^XA
^XFR:STOREFMT.ZPL^FS
^FN1^FDZEBRA^FS
^FN2^FDLABEL^FS
^XZ
```

## Important Notes
- While using stored formats reduces transmission time, no formatting time is saved
- The ZPL II format being recalled is saved as text strings that need to be formatted at print time
- Multiple ^XF commands can be used in one format
- ^XF commands can be located anywhere within the code
- When using ^FN (Field Number) function, the calling format must contain ^FN commands for proper data merging
- Extension is fixed as .ZPL for stored format files

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*