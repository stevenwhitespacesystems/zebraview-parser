# ZPL Command: ^JU (Configuration Update)

## Description
The ^JU command sets the active configuration for the printer.

## Format
```
^JUa
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **a** | Active configuration | F = reload factory values<br/>R = recall last saved values<br/>S = save current settings | Required |

## Examples
```zpl
^JUF
^JUR
^JUS
```

## Important Notes
- Factory values (F) are lost at power-off if not saved with ^JUS
- Recall (R) loads the last saved values
- Save (S) stores current settings for use at power-on
- A value must be specified - no default value available

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*
