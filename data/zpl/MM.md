# ZPL Command: ^MM (Print Mode)

## Description
The ^MM command determines the action the printer takes after a label or group of labels has printed. This command supports various modes of operation:

- **Tear-off** — after printing, the label advances so the web is over the tear bar. The label, with liner attached, can be torn off manually
- **Peel-off** — after printing, the label moves forward and activates a Label Available Sensor. Printing stops until the label is manually removed from the printer
  - **Power Peel** – liner automatically rewinds using an optional internal rewind spindle
  - **Value Peel** – liner feeds down the front of the printer and is manually removed
  - **Prepeel** – after each label is manually removed, the printer feeds the next label forward to prepeel a small portion of the label away from the liner material. The printer then backfeeds and prints the label. The prepeel feature assists in the proper peel operation of some media types
- **Rewind** — the label and backing are rewound on an (optional) external rewind device. The next label is positioned under the printhead (no backfeed motion)
- **Applicator** — when used with an application device, the label move far enough forward to be removed by the applicator and applied to an item
- **Cutter** — after printing, the media feeds forward and is automatically cut into predetermined lengths

## Format
```
^MMa,b
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **a** | Desired mode | T = Tear-off<br>P = Peel-off (not available on S-300)<br>R = Rewind<br>A = Applicator (depends on printer model)<br>C = Cutter | T |
| **b** | Prepeel select | Y (yes) or N (no) | Y |

## Important Notes
- The values available for parameter a are dependent on the printer being used and whether it supports the option
- The command is ignored if parameters are missing or invalid. The current value of the command remains unchanged
- Be sure to select the appropriate value for the print mode being used to avoid unexpected results

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*