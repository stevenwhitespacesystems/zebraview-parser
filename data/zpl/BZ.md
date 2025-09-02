# ZPL Command Documentation

## Raw Extracted Text
```
ZPL Commands
^BZ

^BZ
POSTNET Bar Code
Description The POSTNET bar code is used to automate the handling of mail. POSTNET
uses a series of five bars, two tall and three short, to represent the digits 0 to 9.
•

^BZ supports a print ratio of 2.0:1 to 3.0:1.

•

Field data (^FD) is limited to the width (or length, if rotated) of the label.

Format ^BZo,h,f,g
Important • If additional information about the POSTNET bar code is required, go to
www.aimglobal.org, or contact the United States Postal Service and ask for Publication 25 —

Designing Letter Mail, which includes a full specification for POSTNET. You can also
download Publication 25 from:
http://pe.usps.gov/cpim/ftp/pubs/pub25/pub25.pdf

This table identifies the parameters for this format:

7/29/05

Parameters

Details

o = orientation

Accepted Values:
N = normal
R = rotated 90 degrees (clockwise)
I = inverted 180 degrees
B = read from bottom up, 270 degrees
Default Value: current ^FW value

h = bar code height
(in dots)

Accepted Values: 1 to 32000
Default Value: value set by ^BY

f = print
interpretation
line

Accepted Values: Y (yes) or N (no)
Default Value: N

g = print
interpretation
line above code

Accepted Values: Y (yes) or N (no)
Default Value: N

ZPL Programming Guide Volume One

45541L-002 Rev. A

103

104

ZPL Commands
^BZ

Example • This is an example of a POSTNET bar code:

45541L-002 Rev. A

ZPL Programming Guide Volume One

7/29/05


```

---
*Complete text extracted from ZPL Programming Guide Volume One (45541L-002 Rev. A)*
*This will be cleaned up and formatted in the next step*
