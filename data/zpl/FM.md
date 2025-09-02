# ZPL Command: ^FM (Multiple Field Origin Locations)

## Description
The ^FM command allows you to control the placement of bar code symbols. It designates field locations for the PDF417 (^B7) and Micro-PDF417 (^BF) bar codes when the structured append capabilities are used. This allows printing multiple bar codes from the same set of text information.

The structured append capability is a way of extending the text printing capacity of both bar codes. If a string extends beyond what the data limitations of the bar code are, it can be printed as a series: 1 of 3, 2 of 3, 3 of 3. Scanners read the information and reconcile it into the original, unsegmented text.

The ^FM command triggers multiple bar code printing on the same label with ^B7 and ^BF only. When used with any other commands, it is ignored.

## Format
```
^FMx1,y1,x2,y2,...
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **x1** | X-axis location of first symbol (in dots) | 0 to 32000, e = exclude this bar code from printing | a value must be specified |
| **y1** | Y-axis location of first symbol (in dots) | 0 to 32000, e = exclude this bar code from printing | a value must be specified |
| **x2** | X-axis location of second symbol (in dots) | 0 to 32000, e = exclude this bar code from printing | a value must be specified |
| **y2** | Y-axis location of second symbol (in dots) | 0 to 32000, e = exclude this bar code from printing | a value must be specified |
| **...** | Continuation of X,Y pairs | Maximum number of pairs: 60 | - |

## Examples
Setting multiple field locations for PDF417 bar codes:
```zpl
^XA
^FM100,100,300,100,500,100
^FO0,0^B7...^FD[data]^FS
^XZ
```

## Important Notes
- If e is entered for any of the x, y values, the bar code does not print
- Subsequent bar codes print once the data limitations of the previous bar code have been exceeded
- The number of x,y pairs can exceed the number of bar codes generated, but if too few are designated, no symbols print
- Specifying multiple fields does not ensure that multiple bar codes print; enough field data to fill the bar code fields must be provided
- Only works with PDF417 (^B7) and Micro-PDF417 (^BF) bar codes

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*