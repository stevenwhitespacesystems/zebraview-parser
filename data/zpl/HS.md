# ZPL Command: ~HS (Host Status Return)

## Description
When the host sends ~HS to the printer, the printer sends three data strings back. Each string starts with an <STX> control code and is terminated by an <ETX><CR><LF> control code sequence. To avoid confusion, the host prints each string on a separate line.

## Format
```
~HS
```

## Parameters
This command has no parameters.

## Return Format

### String 1
```
<STX>aaa,b,c,dddd,eee,f,g,h,iii,j,k,l<ETX><CR><LF>
```

| Position | Description |
|----------|-------------|
| **aaa** | Communication (interface) settings |
| **b** | Paper out flag (1 = paper out) |
| **c** | Pause flag (1 = pause active) |
| **dddd** | Label length (value in number of dots) |
| **eee** | Number of formats in receive buffer |
| **f** | Buffer full flag (1 = receive buffer full) |
| **g** | Communications diagnostic mode flag (1 = diagnostic mode active) |
| **h** | Partial format flag (1 = partial format in progress) |
| **iii** | Unused (always 000) |
| **j** | Corrupt RAM flag (1 = configuration data lost) |
| **k** | Temperature range (1 = under temperature) |
| **l** | Temperature range (1 = over temperature) |

#### Communication Settings (aaa)
The communication settings string is a three-digit decimal representation of an eight-bit binary number:

| Bit Position | Description | Values |
|--------------|-------------|--------|
| **a7** | Handshake | 0 = Xon/Xoff, 1 = DTR |
| **a6** | Parity Odd/Even | 0 = Odd, 1 = Even |
| **a5** | Parity Disable/Enable | 0 = Disable, 1 = Enable |
| **a4** | Stop Bits | 0 = 2 Bits, 1 = 1 Bit |
| **a3** | Data Bits | 0 = 7 Bits, 1 = 8 Bits |
| **a8 a2 a1 a0** | Baud Rate | 0000=110, 0001=300, 0010=600, 0011=1200<br>0100=2400, 0101=4800, 0110=9600, 0111=19200<br>1000=28800, 1001=38400, 1010=57600, 1011=14400 |

### String 2
```
<STX>mmm,n,o,p,q,r,s,t,uuuuuuuu,v,www<ETX><CR><LF>
```

| Position | Description |
|----------|-------------|
| **mmm** | Function settings |
| **n** | Unused |
| **o** | Head up flag (1 = head in up position) |
| **p** | Ribbon out flag (1 = ribbon out) |
| **q** | Thermal transfer mode flag (1 = Thermal Transfer Mode selected) |
| **r** | Print Mode (0=Rewind, 1=Peel-Off, 2=Tear-Off, 3=Cutter, 4=Applicator) |
| **s** | Print width mode |
| **t** | Label waiting flag (1 = label waiting in Peel-off Mode) |
| **uuuuuuuu** | Labels remaining in batch |
| **v** | Format while printing flag (always 1) |
| **www** | Number of graphic images stored in memory |

#### Function Settings (mmm)
The function settings string is a three-digit decimal representation of an eight-bit binary number:

| Bit Position | Description | Values |
|--------------|-------------|--------|
| **m7** | Media Type | 0 = Die-Cut, 1 = Continuous |
| **m6** | Sensor Profile | 0 = Off, 1 = On |
| **m5** | Communications Diagnostics | 0 = Off, 1 = On |
| **m4 m3 m2 m1** | Unused | 0 = Off, 1 = On |
| **m0** | Print Mode | 0 = Direct Thermal, 1 = Thermal Transfer |

### String 3
```
<STX>xxxx,y<ETX><CR><LF>
```

| Position | Description |
|----------|-------------|
| **xxxx** | Password |
| **y** | Static RAM status (0 = not installed, 1 = installed) |

## Examples
```
~HS
```

## Important Notes
This command provides comprehensive printer status information including communication settings, operational flags, and hardware status across three separate data strings.

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*