# ZPL Command: ~JX (Cancel Current Partially Input Format)

## Description
The ~JX command cancels a format currently being sent to the printer. It does not affect any formats currently being printed, or any subsequent formats that might be sent.

## Format
```
~JX
```

## Parameters
This command does not accept parameters.

## Examples
```zpl
~JX
```

## Important Notes
- Cancels only the format currently being received
- Does not affect formats already being printed
- Does not affect subsequent formats that might be sent
- Useful for canceling incomplete or corrupted format transmissions

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*
