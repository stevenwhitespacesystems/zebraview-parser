# ZPL Command: ^HZ (Display Description Information)

## Description
The ^HZ command is used for returning printer description information in XML format. The printer returns information on format parameters, object directories, individual object data, and print status information. For more information, see ZPL II Programming Guide Volume Two.

## Format
```
^HZb
```

## Alternative Format
```
^HZO,d:o.x,l
```

## Parameters
The command has multiple formats with different parameter sets. Specific parameter details are referenced in ZPL II Programming Guide Volume Two.

## Examples
```
^XA
^HZO,R:SAMPLE.GRF
^XZ
```

This example shows the object data information for the object SAMPLE.GRF located on R:.

## Important Notes
- Returns printer description information in XML format
- Provides information on format parameters, object directories, individual object data, and print status
- For complete parameter details, refer to ZPL II Programming Guide Volume Two

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*