# ZPL Command: ^NS (Change Networking Settings)

## Description
The ^NS command is used to change network settings.

## Format
```
^NSa,b,c,d
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **a** | Network setting | IP Resolution: a (ALL), b (BOOTP), c (DHCP and BOOTP), d (DHCP), g (GLEANING ONLY), r (RARP), p (permanent) | N/A |
| **b** | IP Address | 0 to 255 | N/A |
| **c** | Subnet Mask | 0 to 255 | N/A |
| **d** | Default Gateway | 0 to 255 | N/A |

## Examples
```
^NSa,192,168,1
```

## Important Notes
- This command configures network parameters for the printer's network interface.
- The IP resolution parameter determines how the printer obtains its IP address.

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*