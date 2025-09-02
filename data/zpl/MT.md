# ZPL Command: ^MT (Media Type)

## Description
The ^MT command selects the type of media being used in the printer. There are two choices for this command:

- **Thermal Transfer Media** – this media uses a high-carbon black or colored ribbon. The ink on the ribbon is bonded to the media
- **Direct Thermal Media** – this media is heat sensitive and requires no ribbon

## Format
```
^MTa
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **a** | Media type used | T = thermal transfer media<br>D = direct thermal media | A value must be entered or the command is ignored |

## Important Notes
- A value must be entered or the command is ignored

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*