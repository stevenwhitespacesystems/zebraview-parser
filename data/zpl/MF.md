# ZPL Command: ^MF (Media Feed)

## Description
The ^MF command dictates what happens to the media at power-up and at head-close after the error clears.

## Format
```
^MFp,h
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **p** | Feed action at power-up | F = feed to the first web after sensor<br>C = (see ~JC definition)<br>L = (see ~JL definition)<br>N = no media feed | Platform-dependent |
| **h** | Feed action after closing printhead | F = feed to the first web after sensor<br>C = (see ~JC definition)<br>L = (see ~JL definition)<br>N = no media feed | Platform-dependent |

## Important Notes
- It is important to remember that if you choose the N setting, the printer assumes that the media and its position relative to the printhead are the same as before power was turned off or the printhead was opened
- Use the ^JU command to save changes

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*