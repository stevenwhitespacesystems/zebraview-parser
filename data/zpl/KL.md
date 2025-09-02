# ZPL Command: ^KL (Define Language)

## Description
The ^KL command selects the language displayed on the front panel.

## Format
```
^KLa
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **a** | Language | 1 = English<br/>2 = Spanish<br/>3 = French<br/>4 = German<br/>5 = Italian<br/>6 = Norwegian<br/>7 = Portuguese<br/>8 = Swedish<br/>9 = Danish<br/>10 = Spanish2<br/>11 = Dutch<br/>12 = Finnish<br/>13 = Japanese | 1 |

## Examples
```zpl
^KL1
^KL2
^KL3
```

## Important Notes
- Changes the language displayed on the printer's front panel
- Default language is English (1)
- Setting persists until changed or printer is reset to factory defaults

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*
