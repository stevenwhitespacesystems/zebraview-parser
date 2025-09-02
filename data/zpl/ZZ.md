# ZPL Command: ^ZZ (Printer Sleep)

## Description
The ^ZZ command places the printer in an idle or shutdown mode.

## Format
```
^ZZt,b
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **t** | Number of seconds (idle time) prior to shutdown | 0 to 999999 (setting 0 disables automatic shutdown) | Last permanently saved value or 0 |
| **b** | Label status at shutdown | Y = shutdown when labels are still queued<br>N = all labels must be printed before shutting down | N |

## Important Notes
- The ^ZZ command is only valid on the PA400 and PT400 battery-powered printers.
- Setting the idle time to 0 disables automatic shutdown.
- When set to Y, the printer will shutdown even if labels are still queued for printing.
- When set to N, all queued labels must be printed before the printer shuts down.

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*