# ZPL Command: ^BD (UPS MaxiCode Bar Code)

## Description
The ^BD command creates a two-dimensional, optically read (not scanned) code. This symbology was developed by UPS (United Parcel Service).

### Key Features
- Two-dimensional, optically read code
- No additional parameters beyond basic configuration
- Does not generate an interpretation line
- ^BY command has no effect on UPS MaxiCode
- ^CV command can be activated for error checking

## Format
```
^BDm,n,t
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **m** | Mode | 2 = structured carrier message: numeric postal code (U.S.)<br>3 = structured carrier message: alphanumeric postal code (non-U.S.)<br>4 = standard symbol, secretary<br>5 = full EEC<br>6 = reader program, secretary | 2 |
| **n** | Symbol number | 1 to 8 (can be added in a structured document) | 1 |
| **t** | Total number of symbols | 1 to 8 (representing total number of symbols in this sequence) | 1 |

## Field Data Format
The ^FD statement is divided into two parts: a high priority message (hpm) and a low priority message (lpm).

```
^FD<hpm><lpm>
```

### High Priority Message (applicable only in Modes 2 and 3)

#### U.S. Style Postal Code (Mode 2)
```
<hpm> = aaabbbcccccdddd
```
- **aaa** = three-digit class of service (0-9)
- **bbb** = three-digit country zip code (0-9)
- **ccccc** = five-digit zip code (0-9)
- **dddd** = four-digit zip code extension (if none exists, use 0000)

#### Non-U.S. Style Postal Code (Mode 3)
```
<hpm> = aaabbbcccccc
```
- **aaa** = three-digit class of service (0-9)
- **bbb** = three-digit country zip code (0-9)
- **cccccc** = six-digit zip code (A-Z or 0-9)

### Low Priority Message (applicable only in Modes 2 and 3)
Uses special characters for message formatting:
- **GS** (0x1D) = separates fields in a message
- **RS** (0x1E) = separates format types
- **EOT** = end of transmission character

#### UPS Message Structure:
| Field | Content |
|-------|---------|
| Message Header | [)>RS |
| Transportation Data Format Header | 01GS96 |
| Tracking Number* | GS<tracking number> |
| SCAC* | GS<SCAC> |
| UPS Shipper Number | GS<shipper number> |
| Julian Day of Pickup | GS<day of pickup> |
| Shipment ID Number | GS<shipment ID number> |
| Package n/x | GS<n/x> |
| Package Weight | GS<weight> |
| Address Validation | GS<validation> |
| Ship to Street Address | GS<street address> |
| Ship to City | GS<city> |
| Ship to State | GS<state> |
| End Markers | RSRS |
| End of Message | EOT |

*Mandatory Data for UPS

## Examples

### Mode 2 Example
```zpl
^XA
^FO50,50
^CVY
^BD^FH^FD001840152382802[)>_1E01_1D961Z00004951_1DUPSN_1D_06X610_1D159_1D1234567_1D1/1_1D_1DY_1D634 ALPHA DR_1DPITTSBURGH_1DPA_1E_04^FS
^FO30,300^A0,30,30^FDMode2^FS
^XZ
```

## Important Notes
- The formatting of `<hpm>` and `<lpm>` apply only when using Modes 2 and 3
- Mode 4 takes whatever data is defined in the ^FD command and places it in the symbol
- UPS requires that certain data be present in a defined manner
- When formatting MaxiCode data for UPS, always use uppercase characters
- Follow data size and type specifications in "Guide to Bar Coding with UPS"
- If you don't choose a mode, default is Mode 2
- If you use non-U.S. Postal Codes with Mode 2, you may get error messages
- ZPL II doesn't automatically change mode based on zip code format
- When using special characters (GS, RS, EOT), use ^FH command with hexadecimal values following underscore character (_)

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*