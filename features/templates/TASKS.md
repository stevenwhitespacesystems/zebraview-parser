# Agent Implementation Workflow Template

This template defines the complete agent workflow for implementing a ZPL command feature. Each task specifies the agent type, input context, commands to run, outputs to generate, and state to update.

---

## Feature Context Loading

### Required Inputs
- **PRP File**: `features/[feature-id]/PRP.md` - Feature definition and specifications  
- **Research Findings**: `features/[feature-id]/agents/zpl-command-researcher/findings.md` - Technical analysis
- **Project Standards**: `CLAUDE.md` - Build commands and architecture patterns

### State Management
**State File**: `features/[feature-id]/agents/state.yaml`
```yaml
# Single source of truth for feature progress
feature_id: [feature-id]
command: [COMMAND]
phase: implementation  # research|prp|implementation
status: active         # active|complete|failed

implementation:
  status: active       # pending|active|complete|failed
  iteration: 0         # Current validation loop iteration
  max_iterations: 15
  last_failure: none   # tests|coverage|detekt|ktlint|performance|integration|demo
```

All agents must update state using: `yq w -i state.yaml [field] [value]`

---

## Agent Workflow Phases

### Phase 1: Test Creation (RED Phase)

#### Task 1.1: Create Test File
**Agent**: @kotlin-senior-dev  
**Execution**: [S] Sequential  
**Input Context**:
- On Start State Update: `yq w -i state.yaml implementation.phase-1.tests "started"`
- Load: `features/[feature-id]/PRP.md`
- Load: `features/[feature-id]/agents/zpl-command-researcher/findings.md`
- Focus: Command specification and test scenarios
- Reference: Existing test patterns in `src/test/kotlin/.../parser/`
- Create `src/test/kotlin/.../parser/[CommandName]Test.kt`
- On Complete State Update: `yq w -i state.yaml implementation.phase-1.tests "created"`
- Output File: `features/[feature-id]/agents/implementation/task1.1.md`
- 
**Output File Format**:
```markdown
## Tests Created - [CommandName]
- [x] Test file created: [CommandName]Test.kt  
- [x] Basic functionality tests (X tests)
- [x] Edge case tests (Y tests)  
- [x] Error scenario tests (Z tests)
- [x] Integration tests (if applicable)

### Test Coverage Plan
- Minimal command parsing: `^[COMMAND]`
- Full parameter parsing: `^[COMMAND][params]`
- Field data handling: (if applicable)
- Error conditions: Invalid params, malformed syntax
```