# ZPL Command: ^FC (Field Clock)

## Description
The ^FC command is used to set the clock-indicators (delimiters) and the clock mode for use with the Real-Time Clock hardware. This command must be included within each label field command string each time the Real-Time Clock values are required within the field.

## Format
```
^FCa,b,c
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **a** | Primary clock indicator character | Any ASCII character | % |
| **b** | Secondary clock indicator character | Any ASCII character | none (cannot be the same as a or c) |
| **c** | Third clock indicator character | Any ASCII character | none (cannot be the same as a or b) |

## Examples
Setting the primary clock indicator to %, the secondary clock indicator to {, and the third clock indicator to #:

```zpl
^XA
^FO10,100^A0N,50,50
^FC%,{,#
^FDPrimary: %m/%d/%y^FS
^FO10,200^A0N,50,50
^FC%,{,#
^FDSecondary: {m/{d/{y^FS
^FO10,300^A0N,50,50
^FC%,{,#
^FDThird: #m/#d/#y^FS
^XZ
```

## Important Notes
- The ^FC command is ignored if the Real-Time Clock hardware is not present
- For more details on the Real Time Clock, see the Zebra Real Time Clock Guide
- This command must be included within each label field command string each time Real-Time Clock values are required

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*