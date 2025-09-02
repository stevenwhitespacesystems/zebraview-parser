# ZPL Command: ^NI (Network ID Number)

## Description
The ^NI command is used to assign a network ID number to the printer. This must be done before the printer can be used in a network.

## Format
```
^NI###
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **###** | Network ID number assigned (must be a three-digit entry) | 001 to 999 | 000 (none) |

## Important Notes
- The last network ID number set is the one recognized by the system
- The commands ~NC, ^NI, ~NR, and ~NT are used only with ZNET RS-485 printer networking

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*