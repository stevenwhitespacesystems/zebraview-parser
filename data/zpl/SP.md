# ZPL Command: ^SP (Start Print)

## Description
The ^SP command allows a label to start printing at a specified point before the entire label has been completely formatted. On extremely complex labels, this command can increase the overall throughput of the print by creating label segments that can print while subsequent segments are being processed.

## Format
```
^SPa
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **a** | Dot row to start printing | 0 to 32000 | 0 |

## Examples
```zpl
^SP500
```
In this example, a label 800 dot rows in length uses ^SP500. Segment 1 (rows 500-800) prints while commands in Segment 2 (rows 0-500) are being received and formatted.

## Important Notes
- Specify the dot row at which the ^SP command is to begin, creating a label segment
- Once processed, all information in that segment prints immediately
- During printing, commands after the ^SP continue to be received and processed
- If the next segment is not ready, the printer stops mid-label and waits
- Precise positioning requires trial-and-error based on print speed and label complexity
- Can be used to determine worst possible print quality by sending format up to first ^SP, waiting for printing to stop, then sending next segment
- For testing worst-case scenarios, end format with: `^SP#^FS`
- Drops any field that is out of order in worst-case scenarios

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*