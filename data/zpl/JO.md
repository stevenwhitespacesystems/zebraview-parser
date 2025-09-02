# ZPL Command: ~JO (Head Test Non Fatal)

## Description
The ~JO command turns off the head test option. This is the default printhead test condition and overrides a failure of printhead element status check. This state is changed when the printer receives a ~JN (Head Test Fatal) command. The printhead test does not produce an error when ~JO is active.

## Format
```
~JO
```

## Parameters
This command does not accept parameters.

## Examples
```zpl
~JO
```

## Important Notes
- Default printhead test condition
- Overrides printhead element status check failures
- Used to disable fatal head test mode set by ~JN command
- Printhead test errors are ignored when this mode is active

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*