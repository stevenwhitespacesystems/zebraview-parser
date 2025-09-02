# ZPL Command: ^FR (Field Reverse Print)

## Description
The ^FR command allows a field to appear as white over black or black over white. When printing a field and the ^FR command has been used, the color of the output is the reverse of its background.

## Format
```
^FR
```

## Parameters
This command has no parameters.

## Examples
Creating reverse printing with black background areas:
```zpl
^XA
^PR1
^FO100,100
^GB70,70,70,,3^FS
^FO200,100
^GB70,70,70,,3^FS
^FO300,100
^GB70,70,70,,3^FS
^FO400,100
^GB70,70,70,,3^FS
^FO107,110^CF0,70,93
^FR^FDREVERSE^FS
^XZ
```

## Important Notes
- The ^FR command applies to only one field and has to be specified each time
- When multiple ^FR commands are going to be used, it might be more convenient to use the ^LR command
- Works by reversing the color of the output relative to its background
- Commonly used with ^GB (Graphic Box) command to create black background areas

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*