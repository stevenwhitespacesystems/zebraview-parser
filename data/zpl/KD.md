# ZPL Command: ^KD (Select Date and Time Format)

## Description
The ^KD command selects the format that the Real-Time Clock's date and time information presents on a configuration label. This is also displayed on the Printer Idle LCD front panel display, and displayed while setting the date and time.

## Format
```
^KDa
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **a** | Value of date and time format | 0 = normal (displays firmware version)<br/>1 = MM/DD/YY (24-hour clock)<br/>2 = MM/DD/YY (12-hour clock)<br/>3 = DD/MM/YY (24-hour clock)<br/>4 = DD/MM/YY (12-hour clock) | 0 |

## Examples
```zpl
^KD1
^KD2
^KD3
^KD4
```

## Important Notes
- If Real-Time Clock hardware is not present, Display Mode is set to 0 (Version Number)
- If Display Mode is 0 and Real-Time Clock hardware is present, date/time format on configuration label uses format 1
- If Display Mode is 0 and Real-Time Clock hardware is present, front panel display uses format 1
- Affects both configuration label display and front panel LCD display

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*
