# ZPL Command: ^IS (Image Save)

## Description
The ^IS command is used within a label format to save that format as a graphic image, rather than as a ZPL II script. It is typically used toward the end of a script. The saved image can later be recalled with virtually no formatting time and overlaid with variable data to form a complete label.

Using this technique to overlay the image of constant information with the variable data greatly increases the throughput of the label format.

## Format
```
^ISd:o.x,p
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **d** | Location of stored object | R:, E:, B:, and A: | R: |
| **o** | Object name | 1 to 8 alphanumeric characters | UNKNOWN |
| **x** | Extension | .GRF or .PNG | .GRF |
| **p** | Print image after storing | Y (yes) or N (no) | Y |

## Examples
This example uses the ^IS command to save a label format to DRAM with the name SAMPLE2.GRF.

```
^XA
^LH10,15^FWN^BY3,3,85^CFD,36
^GB430,750,4^FS
^FO10,170^GB200,144,2^FS
^FO10,318^GB410,174,2^FS
^FO212,170^GB206,144,2^FS
^FO10,498^GB200,120,2^FS
^FO212,498^GB209,120,2^FS
^FO4,150^GB422,10,10^FS
^FO135,20^A0,70,60
^FDZEBRA^FS
^FO80,100^A0,40,30
^FDTECHNOLOGIES CORP^FS
^FO15,180^CFD,18,10^FS
^FDARTICLE#^FS
^FO218,180
^FDLOCATION^FS
^FO15,328
^FDDESCRIPTION^FS
^FO15,508
^FDREQ.NO.^FS
^FO220,508
^FDWORK NUMBER^FS
^FO15,630^AD,36,20
^FDCOMMENTS:^FS
^ISR:SAMPLE2.GRF,Y
^XZ
```

## Important Notes
- See ^IL command for information on loading saved images
- The saved image can later be recalled with virtually no formatting time
- This technique greatly increases the throughput of label formats by separating constant information from variable data
- The command should typically be used toward the end of a script
- Saved images can be overlaid with variable data to form complete labels

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*