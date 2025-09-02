# ZPL Command: ~JN (Head Test Fatal)

## Description
The ~JN command turns on the head test option. When activated, it causes the printer to halt when a head test failure is encountered. Once an error is encountered, the printer remains in error mode until the head test is turned off (~JO) or power is cycled.

## Format
```
~JN
```

## Parameters
This command does not accept parameters.

## Examples
```zpl
~JN
```

## Important Notes
- Enables fatal head test mode - printer halts on head test failures
- Printer remains in error mode until ~JO command is sent or power is cycled
- If communications buffer is full, the printer cannot receive the ~JO command to exit error mode
- Used for quality control to ensure printhead functionality

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*