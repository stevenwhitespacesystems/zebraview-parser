# ZPL Command: ~JL (Set Label Length)

## Description
The ~JL command is used to set the label length. Depending on the size of the label, the printer feeds one or more blank labels.

## Format
```
~JL
```

## Parameters
This command does not accept parameters.

## Examples
```zpl
~JL
```

## Important Notes
- Forces the printer to measure and set the current label length
- May cause one or more blank labels to feed during the measurement process
- Useful for calibrating the printer to the current media being used

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*