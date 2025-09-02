# ZPL Command: ^PQ (Print Quantity)

## Description
The ^PQ command gives control over several printing operations. It controls the number of labels to print, the number of labels printed before printer pauses, and the number of replications of each serial number.

## Format
```
^PQq,p,r,o
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **q** | Total quantity of labels to print | 1 to 99,999,999 | 1 |
| **p** | Pause and cut value (labels between pauses) | 1 to 99,999,999 | 0 (no pause) |
| **r** | Replicates of each serial number | 0 to 99,999,999 replicates | 0 (no replicates) |
| **o** | Override pause count | Y (yes) or N (no) | N |

## Examples
```
^PQ50,10,1,Y
```
This example prints a total of 50 labels with one replicate of each serial number. It prints the total quantity in groups of 10, but does not pause after every group.

```
^PQ50,10,1,N
```
This example prints a total of 50 labels with one replicate of each serial number. It prints the total quantity in groups of 10, pausing after every group.

## Important Notes
- If the o parameter is set to Y, the printer cuts but does not pause, and the printer does not pause after every group count of labels has been printed.
- With the o parameter set to N (default), the printer pauses after every group count of labels has been printed.

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*