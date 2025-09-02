# ZPL Command: ^HF (Graphic Symbol)

## Description
The ^HF command sends an object of ZPL format instructions to the host, in ~DF.

## Format
```
^HF,o,h,w
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **o** | Font orientation | N = normal<br>R = rotate<br>I = inverted 180 degrees<br>B = bottom-up, 270 degrees | N or last ^FW value |
| **h** | Character height proportional to width (in dots) | 0 to 32000 | Last ^CF value |
| **w** | Character width proportional to height (in dots) | 0 to 32000 | Last ^CF value |

## Examples
```
^HF,N,100,100
```

## Important Notes
This command is used to send graphic symbol instructions to the host in ZPL format.

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*