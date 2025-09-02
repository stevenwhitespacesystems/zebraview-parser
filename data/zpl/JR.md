# ZPL Command: ~JR (Power On Reset)

## Description
The ~JR command resets all of the printer's internal software, performs a power-on self-test (POST), clears the buffer and DRAM, and resets communication parameters and default values. Issuing a ~JR command performs the same function as a manual power-on reset.

## Format
```
~JR
```

## Parameters
This command does not accept parameters.

## Examples
```zpl
~JR
```

## Important Notes
- Performs complete printer reset
- Executes power-on self-test (POST)
- Clears buffer and DRAM
- Resets communication parameters to defaults
- Equivalent to manual power-on reset
- Use with caution as it will interrupt any current operations

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*
