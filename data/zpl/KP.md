# ZPL Command: ^KP (Define Password)

## Description
The ^KP command is used to define the password that must be entered to access the front panel switches and LCD Setup Mode.

## Format
```
^KP####
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **####** | Mandatory four-digit password | Any four-digit numeric sequence | 1234 |

## Examples
```zpl
^KP5678
^KP9999
^KP0000
```

## Important Notes
- Password is required to access front panel switches and LCD Setup Mode
- Default password is 1234
- If you forget your password, printer can be returned to default Setup Mode using ^JUF command
- Returning to factory defaults also resets password to 1234 but resets all configuration values
- Use caution when resetting to factory defaults as all settings are lost

### Factory Reset Commands
```zpl
^XA
^JUF
^XZ
```

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*
