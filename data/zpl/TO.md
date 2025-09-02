# ZPL Command: ^TO (Transfer Object)

## Description
The ^TO command is used to copy an object or group of objects from one storage device to another. It is similar to the copy function used in PCs. Source and destination devices must be supplied and must be different and valid for the action specified.

## Format
```
^TOd:o.x,s:o.x
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **d** | Source device of stored object | R:, E:, B:, and A: | If not specified, all objects are transferred to drive set in parameter s |
| **o** | Stored object name | Any existing object conforming to Zebra conventions | * (all objects selected) |
| **x** | Extension | Any extension conforming to Zebra conventions | * (all extensions selected) |
| **s** | Destination device of the stored object | R:, E:, B:, and A: | Must be specified |
| **o** | Name of the object at destination | Up to 8 alphanumeric characters | Existing object name is used |
| **x** | Extension | Any extension conforming to Zebra conventions | Existing object extension is used |

## Examples
```zpl
// Copy ZLOGO.GRF from DRAM to Memory Card and rename it ZLOGO1.GRF
^XA
^TOR:ZLOGO.GRF,B:ZLOGO1.GRF
^XZ

// Copy SAMPLE.GRF from Memory Card to DRAM keeping the same name
^XA
^TOB:SAMPLE.GRF,R:SAMPLE.GRF
^XZ

// Transfer multiple files using wildcards
^XA
^TOR:LOGO*.GRF,B:NEW*.GRF
^XZ
```

## Important Notes
- Invalid parameters cause the command to be ignored
- The asterisk (*) can be used as a wild card for object names and extensions
- At least one source parameter and one destination parameter must be specified
- Parameters o, x, and s support the use of wildcards (*)
- If destination device lacks free space, the command is canceled
- Zebra files (Z:*.*) cannot be transferred (copyrighted by Zebra Technologies)
- During multiple transfers, files too big for storage are skipped while others continue
- Multiple object files (except *.FNT) can be transferred using wildcards

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*