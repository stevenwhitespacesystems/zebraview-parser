# ZPL Command: ^CO (Cache On)

## Description
The ^CO command is used to change the size of the character cache. By definition, a character cache (referred to as cache) is a portion of the DRAM reserved for storing scalable characters. All printers have a default 40K cache that is always turned on. The maximum single character size that can be stored, without changing the size of the cache, is 450 dots by 450 dots.

There are two types of fonts used in Zebra printers: bitmapped and scalable. Letters, numbers, and symbols in a bitmapped font have a fixed size (for example: 10 points, 12 points, 14 points). By comparison, scalable fonts are not fixed in size.

Because their size is fixed, bitmapped fonts can be moved quickly to the label. In contrast, scalable fonts are much slower because each character is built on an as-needed basis before it is moved to the label. By storing scaled characters in a cache, they can be recalled at a much faster speed.

## Format
```
^COa,b,c
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **a** | Cache on | Y (yes) or N (no) | Y |
| **b** | Amount of additional memory to be added to cache (in K) | Any size up to total memory available | 40 |
| **c** | Cache type | 0 = cache buffer (normal fonts)<br>1 = internal buffer (recommended for Asian fonts) | 0 |

## Examples
To resize the print cache to 62K, assuming a 22K existing cache:
```
^COY,40
```

To resize the print cache to 100K, assuming a 22K existing cache:
```
^COY,78
```

## Important Notes
- The number of characters that can be stored in the cache depends on two factors: the size of the cache (memory) and the size of the character (in points) being saved
- The larger the point size, the more space in the cache it uses
- The default cache stores every scalable character that is requested for use on a label
- If the same character, with the same rotation and size is used again, it is quickly retrieved from cache
- Once the cache is full, space for new characters is obtained by eliminating an existing character from the print cache based on usage frequency
- Maximum size of a single print cache character is 1500 dots by 1500 dots, which would require a cache of 274K
- When the cache is too small for the desired style, smaller characters might appear but larger characters do not
- The cache can be resized as often as needed, but any characters in the cache when it is resized are lost
- Memory used for the cache reduces the space available for label bitmaps, graphics, downloaded fonts, etc.
- Some Asian fonts require an internal working buffer that is much larger than the normal cache
- Printing with Asian fonts greatly reduces the printer memory available for labels, graphics, fonts, and formats

### Print Cache Performance
For printing large characters, memory added to the cache by the ^CO command is not physically added to the existing cache in the printer. The resulting cache is actually two separate blocks of memory. Because large characters need contiguous blocks of memory, a character requiring a cache larger than either portion would not be completely stored. Therefore, if large characters are needed, the ^CO command should reflect the actual size of the cache you need.

Increasing the size of the cache improves the performance in printing scalable fonts. However, the performance decreases if the size of the cache becomes large and contains too many characters. The performance gained is lost because of the time involved searching cache for each character.

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*