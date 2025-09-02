# ZPL Command: ^LT (Label Top)

## Description
The ^LT command moves the entire label format a maximum of 120 dot rows up or down from its current position, in relation to the top edge of the label. A negative value moves the format towards the top of the label; a positive value moves the format away from the top of the label.

This command can be used to fine-tune the position of the finished label without having to change any of the existing parameters.

## Format
```
^LTx
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **x** | Label top (in dot rows) | -120 to 120 | A value must be specified or the command is ignored |

## Important Notes
- For some printer models, it is possible to request a negative value large enough to cause the media to backup into the printer and become unthreaded from the platen. This condition can result in a printer error or unpredictable results
- The Accepted Value range for x might be smaller depending on the printer platform
- The ^LT command does not change the media rest position

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*