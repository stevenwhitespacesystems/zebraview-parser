# ZPL Command: ~JP (Pause and Cancel Format)

## Description
The ~JP command clears the format currently being processed and places the printer into Pause Mode. The command clears the next format that would print, or the oldest format from the buffer. Each subsequent ~JP command clears the next buffered format until the buffer is empty. The DATA indicator turns off when the buffer is empty and no data is being transmitted.

## Format
```
~JP
```

## Parameters
This command does not accept parameters.

## Examples
```zpl
~JP
```

## Important Notes
- Clears the current format being processed
- Places printer into Pause Mode
- Clears formats from buffer sequentially with each use
- Identical to using CANCEL on the printer, but printer doesn't need to be in Pause Mode first
- DATA indicator turns off when buffer is empty

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*