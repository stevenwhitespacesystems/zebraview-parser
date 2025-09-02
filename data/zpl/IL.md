# ZPL Command: ^IL (Image Load)

## Description
The ^IL command is used at the beginning of a label format to load a stored image of a format and merge it with additional data. The image is always positioned at ^FO0,0.

Using this technique to overlay the image of constant information with variable data greatly increases the throughput of the label format.

## Format
```
^ILd:o.x
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **d** | Location of stored object | R:, E:, B:, and A: | R: |
| **o** | Object name | 1 to 8 alphanumeric characters | UNKNOWN |
| **x** | Extension | .GRF (fixed value) | .GRF |

## Examples
This example recalls the stored image SAMPLE2.GRF from DRAM and overlays it with additional data. The graphic was stored using the ^IS command.

```
^XA
^ILR:SAMPLE2.GRF
[additional variable data commands]
^XZ
```

## Important Notes
- See ^IS command for information on storing images
- The image is always positioned at ^FO0,0
- This technique significantly increases label format throughput by overlaying constant information with variable data
- The graphic must be stored using the ^IS command before it can be loaded with ^IL

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*