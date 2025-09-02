# ZPL Command: ^MP (Mode Protection)

## Description
The ^MP command is used to disable the various mode functions on the front panel. Once disabled, the settings for the particular mode function can no longer be changed and the LED associated with the function does not light.

Because this command has only one parameter, each mode must be disabled with an individual ^MP command.

## Format
```
^MPa
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **a** | Mode to protect | D = disable Darkness Mode<br>P = disable Position Mode<br>C = disable Calibration Mode<br>E = enable all modes<br>S = disable all mode saves (modes can be adjusted but values are not saved)<br>W = disable Pause<br>F = disable Feed<br>X = disable Cancel<br>M = disable menu changes | A value must be entered or the command is ignored |

## Examples
This example disables these modes, D and C:
```
^XA
^MPD
^MPC
^XZ
```

## Important Notes
- A value must be entered or the command is ignored
- Each mode must be disabled with an individual ^MP command

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*