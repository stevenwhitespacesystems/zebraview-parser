# ZPL Command: ~HI (Host Identification)

## Description
The ~HI command is designed to be sent from the host to the Zebra printer to retrieve information. Upon receipt, the printer responds with information on the model, software version, dots-per-millimeter setting, memory size, and any detected objects.

## Format
```
~HI
```

## Parameters
This command has no input parameters. When the printer receives this command, it returns:

```
XXXXXX,V1.0.0,dpm,000KB,X
```

## Return Format
| Component | Description | Possible Values |
|-----------|-------------|----------------|
| **XXXXXX** | Model of Zebra printer | Printer model name |
| **V1.0.0** | Version of software | Software version |
| **dpm** | Dots per millimeter | 6, 8, 12, or 24 dots/mm printheads |
| **000KB** | Memory size | 512KB = 1/2 MB<br>1024KB = 1 MB<br>2048KB = 2 MB<br>4096KB = 4 MB<br>8192KB = 8 MB |
| **x** | Recognizable objects | Only options specific to printer are shown (cutter, options, etc.) |

## Examples
```
~HI
```

## Important Notes
This command provides comprehensive printer identification and status information including hardware capabilities and installed options.

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*