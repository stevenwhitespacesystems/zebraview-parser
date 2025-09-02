# ZPL Command: ~NC (Network Connect)

## Description
The ~NC command is used to connect a particular printer to a network by calling up the printer's network ID number.

## Format
```
~NC###
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **###** | Network ID number assigned (must be a three-digit entry) | 001 to 999 | 000 (none) |

## Important Notes
- Use this command at the beginning of any label format to specify which printer on the network is going to be used
- Once the printer is established, it continues to be used until it is changed by another ~NC command
- This command must be included in the label format to wake up the printer
- The commands ^MW, ~NC, ^NI, ~NR, and ~NT are used only with ZNET RS-485 printer networking

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*