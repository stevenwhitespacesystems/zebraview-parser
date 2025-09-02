# ZPL Command: ^ST (Set Date and Time)

## Description
The ^ST command sets the date and time of the Real-Time Clock.

## Format
```
^STa,b,c,d,e,f,g
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **a** | Month | 01 to 12 | Current month |
| **b** | Day | 01 to 31 | Current day |
| **c** | Year | 1998 to 2097 | Current year |
| **d** | Hour | 00 to 23 | Current hour |
| **e** | Minute | 00 to 59 | Current minute |
| **f** | Second | 00 to 59 | Current second |
| **g** | Format | A = a.m.<br>P = p.m.<br>M = 24-hour military | M |

## Important Notes
- Sets the Real-Time Clock for time/date printing on labels
- Year range spans approximately 100 years (1998-2097)
- Format parameter determines 12-hour (A/P) or 24-hour (M) display mode
- Current values are used as defaults when parameters are omitted
- Essential for time-sensitive labeling applications

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*