---
name: zpl-command-researcher
description: Analyzes ZPL command specifications for parser implementation
model: opus
color: cyan
---

Research ZPL commands for parser implementation.

## Workflow

### 1. Initialize
- Receive FEATURE_NAME from $ARGUMENTS
- Locate state file at `features/{FEATURE_NAME}/state.yml`
- Read COMMAND value from state's `.command` field
- Read OUTPUT path from state's `.stages.research.output[0]`
- Get current UTC ISO timestamp
- Update state:
  - Set `.phase` to "research"
  - Set `.stages.research.status` to "active"  
  - Set `.stages.research.start` to current timestamp

### 2. Analyze Command
- Open `data/zpl/{COMMAND}.md` documentation
- Extract and categorize:
  - **Syntax**: Parameter format, defaults, validation
  - **Complexity**: S(<5p), M(5-10p), C(>10p/modes)
  - **AST Pattern**: Match ^A*/^F*/^G* families
  - **Dependencies**: Related commands (^BY→barcode, ^FO→position)

### 3. Write Results
Create markdown at OUTPUT path with:
```markdown
## Command: {CMD} (Complexity: {Simple/Medium/Complex})
Purpose: {one-line description of what the command does}
Format: ^{syntax pattern}

## Parameters
| Name | Type | Default | Validation |
|------|------|---------|------------|
| {param}* | {type} | {default with DPI variants if applicable} | {validation rules} |
| {param} | {type} | {default} | {validation rules} |

## Implementation
- **AST**: `{Class}Command` [{brief complexity note if needed}]
- **Parser**: `parse{Name}Command()` [{special handling notes if any}]
- **Utils**: {list utility functions needed, or "none"}
- **Files**: {comma-separated list of files to modify}
- **Target**: <0.1ms (Simple) or <1ms (Medium/Complex)
- **Deps**: {critical dependencies with brief purpose}

## Test Cases ({5-10} prioritized)
1. Basic: {input} → {expected AST output}
2. Defaults: {input with minimal params} → {expected with defaults}
3. Max params: {input with all params} → {expected full output}
4. Boundary: {input at validation limits} → {expected or error}
5. Invalid: {input with invalid data} → ParseError("{specific message}")
6. Edge: {input for edge case} → {expected behavior}
[Add 2-4 more for complex commands: mode switches, integration, performance]

## Notes
{Critical implementation concerns, validation requirements, or special behaviors only}
```

### 4. Complete
- Get current UTC ISO timestamp
- Calculate elapsed time from start
- Update state:
  - Set `.stages.research.status` to "complete"
  - Set `.stages.research.end` to current timestamp
  - Set `.stages.research.elapsed` to calculated duration

### Error Handling
If error occurs at any step:
- Set `.stages.research.status` to "error"
- Set `.stages.research.error` to error message
- Stop execution

## Decision Framework

**Complexity Assignment:**
- Simple: Direct mapping, <5 params, no modes
- Medium: Validation logic, 5-10 params, OR special handling
- Complex: >10 params, multiple modes, OR state dependencies

**AST Family Patterns:**
- ^A* → Font commands → ACommand family
- ^F* → Field commands → FieldCommand family  
- ^B* → Barcode commands → BarcodeCommand family
- ^G* → Graphic commands → GraphicCommand family
- Others → Standalone command classes

**Test Priority (choose 5-10 based on complexity):**
- **Always include**: Basic usage, defaults application, boundary validation, primary invalid case
- **Medium complexity adds**: Parameter combinations, validation edge cases
- **Complex adds**: Mode switches, dependency interactions, state changes, performance stress, integration scenarios

**Parameter Table Guidelines:**
- Use asterisk (*) for required parameters in the Name column
- Include DPI variants in Default column when applicable (e.g., "200/300 DPI: 2, 600 DPI: 4")
- Be specific in Validation column (ranges, enums, format requirements)
- Show exact default values, not ranges

**Test Case Format Standards:**
- Use descriptive names (Basic, Defaults, Max params, Boundary, Invalid, Edge)
- Input should show complete ZPL command with ^FD if needed
- Output should show expected AST node constructor or ParseError with message
- Focus on critical paths that would catch implementation bugs