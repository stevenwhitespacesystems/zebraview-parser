# ZPL Command: ^SR (Set Printhead Resistance)

## Description
The ^SR command allows you to set the printhead resistance.

## Format
```
^SR####
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **####** | Resistance value (four-digit numeric) | 0488 to 1175 | Last permanently saved value |

## Important Notes
- **CAUTION:** To avoid damaging the printhead, this value should be less than or equal to the value shown on the printhead being used
- Setting a higher value could damage the printhead
- New models automatically set head resistance
- This command overrides automatic resistance settings when specified

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*