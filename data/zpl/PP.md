# ZPL Command: ^PP/~PP (Programmable Pause)

## Description
The ~PP command stops printing after the current label is complete (if one is printing) and places the printer in Pause Mode. The ^PP command is not immediate. Therefore, several labels might print before a pause is performed. This command pauses the printer after the current format prints. The operation is identical to pressing PAUSE on the front panel of the printer. The printer remains paused until PAUSE is pressed or a ~PS (Print Start) command is sent to the printer.

## Format
```
^PP
```
or
```
~PP
```

## Parameters
This command has no parameters.

## Examples
```
^PP
```
```
~PP
```

## Important Notes
- ~PP command is immediate and stops printing after the current label is complete.
- ^PP command is not immediate and several labels might print before the pause is performed.
- The printer remains paused until PAUSE is pressed on the front panel or a ~PS (Print Start) command is sent.
- The operation is identical to pressing PAUSE on the front panel of the printer.

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*