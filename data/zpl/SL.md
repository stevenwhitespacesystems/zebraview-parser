# ZPL Command: ^SL (Set Mode and Language)

## Description
The ^SL command is used to specify the Real-Time Clock's mode of operation and language for printing information.

## Format
```
^SLa,b
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **a** | Mode | S = Start Time Mode (time read when ^XA is received)<br>T = Time Now Mode (time read when label is queued) | S |
| **b** | Language | 1 = English<br>2 = Spanish<br>3 = French<br>4 = German<br>5 = Italian<br>6 = Norwegian<br>7 = Portuguese<br>8 = Swedish<br>9 = Danish<br>10 = Spanish 2<br>11 = Dutch<br>12 = Finnish | Language selected with ^KL or front panel |

## Important Notes
- Start Time Mode: The same time is placed on all labels (from when ^XA is received)
- Time Now Mode: Each label gets the current time when it enters the print queue (similar to serialized time/date field)

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*