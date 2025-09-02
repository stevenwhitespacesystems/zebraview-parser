# ZPL Command: ~JA (Cancel All)

## Description
The ~JA command cancels all format commands in the buffer. It also cancels any batches that are printing.

The printer stops after the current label is finished printing. All internal buffers are cleared of data and the DATA LED turns off.

Submitting this command to the printer scans the buffer and deletes only the data before the ~JA in the input buffer — it does not scan the remainder of the buffer for additional ~JA commands.

## Format
```
~JA
```

## Parameters
This command has no parameters.

## Examples
```
~JA
```

## Important Notes
- Cancels all format commands in the buffer
- Cancels any batches that are currently printing
- The printer stops after the current label is finished printing
- All internal buffers are cleared of data
- The DATA LED turns off after execution
- Only scans and deletes data before the ~JA command in the input buffer
- Does not scan the remainder of the buffer for additional ~JA commands

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*