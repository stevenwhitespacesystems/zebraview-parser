# ZPL Command: ^SE (Select Encoding)

## Description
The ^SE command was created to select the desired ZPL or ZPL II encoding table.

## Format
```
^SEd:o.x
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **d** | Location of encoding table | R:, E:, B:, and A: | R: |
| **o** | Name of encoding table | 1 to 8 alphanumeric characters | A value must be specified |
| **x** | Extension | .DAT | .DAT (fixed value) |

## Examples
```
^SER:ENCODE1.DAT
```

## Important Notes
- This command selects the desired ZPL or ZPL II encoding table.
- The extension is fixed as .DAT.
- A name for the encoding table must be specified.

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*