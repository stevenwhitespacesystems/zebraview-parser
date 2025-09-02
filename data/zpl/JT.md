# ZPL Command: ^JT (Head Test Interval)

## Description
The ^JT command allows you to change the printhead test interval from every 100 labels to any desired interval. With the ^JT command, the printer is allowed to run the test after printing a label. When a parameter is defined, the printer runs the test after printing a set amount of labels.

## Format
```
^JT####,a,b,c
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **####** | Four-digit number of labels printed between head tests | 0000-9999 | 0000 (off) |
| **a** | Manually select range of elements to test | Y (yes), N (no) | N |
| **b** | First element to check when parameter a is Y | 0-9999 | 0 |
| **c** | Last element to check when parameter a is Y | 0-9999 | 9999 |

## Examples
```zpl
^JT0100,N,0,9999
^JT0050,Y,100,500
```

## Important Notes
- Printer's default head test state is off
- Supports testing a range of print elements
- Printer automatically selects test range by tracking which elements have been used
- If last element selected is greater than print width, test stops at selected print width
- Head test is performed on next label unless count is set to 0
- Values greater than 9999 are ignored
- Turns on Automatic Mode to specify first and last elements for head test

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*
