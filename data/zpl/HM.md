# ZPL Command: ~HM (Host RAM Status)

## Description
Sending ~HM to the printer immediately returns a memory status message to the host. Use this command whenever you need to know the printer's RAM status. When ~HM is sent to the Zebra printer, a line of data containing information on the total amount, maximum amount, and available amount of memory is sent back to the host.

## Format
```
~HM
```

## Parameters
This command has no input parameters. When the printer receives this command, it returns a line of data in the format:

```
1024,0780,0780
```

## Return Format
| Position | Description | Example |
|----------|-------------|---------|
| **1st number** | The total amount of RAM (in kilobytes) installed in the printer | 1024 (1024K RAM installed) |
| **2nd number** | The maximum amount of RAM (in kilobytes) available to the user | 0780 (780K RAM available maximum) |
| **3rd number** | The amount of RAM (in kilobytes) currently available to the user | 0780 (780K RAM currently available) |

## Examples
```
~HM
```

Example response:
```
1024,0780,0780
```

## Important Notes
- Memory taken up by bitmaps is included in the currently available memory value (due to ^MCN)
- Downloading a graphic image, fonts, or saving a bitmap affects only the amount of RAM
- The total amount of RAM and maximum amount of RAM does not change after the printer is turned on

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*