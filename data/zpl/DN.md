# ZPL Command: ~DN (Abort Download Graphic)

## Description
After decoding and printing the number of bytes in parameter t of the ~DG command, the printer returns to normal Print Mode. Graphics Mode can be aborted and normal printer operation resumed by using the ~DN command.

## Format
```
~DN
```

## Parameters
This command takes no parameters.

## Examples
*(Note: Examples were not provided in the original source text)*

## Important Notes
- If you need to stop a graphic from downloading, you should abort the transmission from the host device
- To clear the ~DG command, however, you must send a ~DN command
- This command forces the printer to exit Graphics Mode and return to normal Print Mode
- Use this command when you need to cancel a graphic download operation

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*