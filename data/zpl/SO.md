# ZPL Command: ^SO (Set Offset)

## Description
The ^SO command is used to set the secondary and the tertiary offset from the primary Real-Time Clock.

## Format
```
^SOa,b,c,d,e,f,g
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **a** | Clock set | 2 = secondary<br>3 = tertiary | Value must be specified |
| **b** | Months offset | -32000 to 32000 | 0 |
| **c** | Days offset | -32000 to 32000 | 0 |
| **d** | Years offset | -32000 to 32000 | 0 |
| **e** | Hours offset | -32000 to 32000 | 0 |
| **f** | Minutes offset | -32000 to 32000 | 0 |
| **g** | Seconds offset | -32000 to 32000 | 0 |

## Important Notes
- This command allows setting time offsets for secondary and tertiary Real-Time Clock references
- All offset parameters accept negative values for decrements from the primary clock
- The clock set parameter (a) is required and must be specified

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*