# ZPL Command: ~WC (Print Configuration Label)

## Description
The ~WC command is used to generate a printer configuration label. The printer configuration label contains information about the printer setup, such as sensor type, network ID, ZPL mode, firmware version, and descriptive data on the R:, E:, B:, and A: devices.

## Format
```
~WC
```

## Parameters
This command takes no parameters.

## Important Notes
- This command works only when the printer is idle
- Generates a comprehensive status report of printer configuration
- Includes information about sensor settings, network configuration, ZPL mode, and firmware version
- Provides detailed information about all storage devices (R:, E:, B:, A:)
- Useful for troubleshooting and printer status verification

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*