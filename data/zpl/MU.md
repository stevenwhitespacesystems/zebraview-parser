# ZPL Command: ^MU (Set Units of Measurement)

## Description
The ^MU command sets the units of measurement the printer uses. ^MU works on a field-by-field basis. Once the mode of units is set, it carries over from field to field until a new mode of units is entered.

^MU also allows for printing at lower resolutions — 600 dpi printers are capable of printing at 300, 200, and 150 dpi; 300 dpi printers are capable of printing at 150 dpi.

## Format
```
^MUa,b,c
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **a** | Units | D = dots<br>I = inches<br>M = millimeters | D |
| **b** | Format base in dots per inch | 150, 200, 300 | A value must be entered or the command is ignored |
| **c** | Desired dots-per-inch conversion | 300, 600 | A value must be entered or the command is ignored |

## Examples

### Setting Units Example
Assume 8 dot/millimeter (203 dot/inch) printer:

Field based on dots:
```
^MUd^FO100,100^GB1024,128,128^FS
```

Field based on millimeters:
```
^MUm^FO12.5,12.5^GB128,16,16^FS
```

Field based on inches:
```
^MUi^FO.493,.493^GB5.044,.631,.631^FS
```

### Converting DPI Values Example
Convert a 150 dpi format to a 300 dpi format with a base in dots:
```
^MUd,150,300
```

Convert a 150 dpi format to a 600 dpi format with a base in dots:
```
^MUd,150,600
```

Convert a 200 dpi format to a 600 dpi format with a base in dots:
```
^MUd,200,600
```

To reset the conversion factor to the original format, enter matching values for parameters b and c:
```
^MUd,150,150
^MUd,200,200
^MUd,300,300
^MUd,600,600
```

## Important Notes
- This command should appear at the beginning of the label format to be in proper ZPL II format
- To turn the conversion off, enter matching values for parameter b and c

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*