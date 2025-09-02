# ZPL Command: ^ID (Object Delete)

## Description
The ^ID command deletes objects, graphics, fonts, and stored formats from storage areas. Objects can be deleted selectively or in groups. This command can be used within a printing format to delete objects before saving new ones, or in a stand-alone format to delete objects.

The image name and extension support the use of the asterisk (*) as a wild card. This allows you to easily delete a selected groups of objects.

## Format
```
^IDd:o.x
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **d** | Location of stored object | R:, E:, B:, and A: | R: |
| **o** | Object name | Any 1 to 8 character name | UNKNOWN |
| **x** | Extension | Any extension conforming to Zebra conventions | .GRF |

## Examples

### Delete stored formats from DRAM:
```
^XA
^IDR:*.ZPL^FS
^XZ
```

### Delete formats and images named SAMPLE from DRAM, regardless of extension:
```
^XA
^IDR:SAMPLE.*^FS
^XZ
```

### Delete the image SAMPLE1.GRF prior to storing SAMPLE2.GRF:
```
^XA
^FO25,25^AD,18,10
^FDDelete^FS
^FO25,45^AD,18,10
^FDthen Save^FS
^IDR:SAMPLE1.GRF^FS
^ISR:SAMPLE2.GRF^FS
^XZ
```

### Delete all objects with .GRF extension using wildcard:
```
^XA
^IDR:*.GRF^FS
^XZ
```

## Important Notes
- When an object is deleted from R:, the object can no longer be used and memory is available for storage. This applies only to R: memory
- The ^ID command also frees up the uncompressed version of the object in DRAM
- If the name is specified as *.ZOB, all downloaded bar code fonts (or other objects) are deleted
- If the named downloadable object cannot be found in the R:, E:, B:, and A: device, the ^ID command is ignored
- The * is a wild card, indicating that objects matching the pattern are deleted

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*