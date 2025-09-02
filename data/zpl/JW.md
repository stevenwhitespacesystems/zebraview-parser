# ZPL Command: ^JW (Set Ribbon Tension)

## Description
The ^JW command sets the ribbon tension for the printer it is sent to.

## Format
```
^JWt
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **t** | Tension | L = low<br/>M = medium<br/>H = high | Required |

## Examples
```zpl
^JWL
^JWM
^JWH
```

## Important Notes
- Used only for PAX series printers
- A value must be specified - no default value available
- Proper ribbon tension is critical for print quality

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*
