# Add ZPL Command

Implement a new ZPL command: $ARGUMENTS

**Workflow**: Research → PRP Generation → Automated Implementation (TDD + Validation Loop)

## Setup & Launch

```bash
# Validate & Setup
COMMAND="${ARGUMENTS^^}"
[ -z "$COMMAND" ] && { echo "Error: Command required"; exit 1; }
[ ! -f "data/zpl/${COMMAND}.md" ] && { echo "Error: data/zpl/${COMMAND}.md not found"; exit 1; }

# Generate feature ID & structure
NEXT_ID=$(printf "%04d" $(($(ls features/ | grep -E '^[0-9]+' | cut -d'-' -f1 | sort -n | tail -1) + 1)))
FEATURE_ID="${NEXT_ID}-$(echo $COMMAND | tr '[:upper:]' '[:lower:]')-command"
mkdir -p "features/${FEATURE_ID}/agents/zpl-command-researcher"

# Create state file
CURRENT_TIME=$(date -u +"%Y-%m-%dT%H:%M:%SZ")
cat > "features/${FEATURE_ID}/agents/state.yaml" << EOF
feature_id: ${FEATURE_ID}
command: ${COMMAND}
initiated: ${CURRENT_TIME}
phase: research
status: active

research:
  status: active
  started: ${CURRENT_TIME}
  completed: null
  elapsed_seconds: null
  output: agents/zpl-command-researcher/research.md

prp:
  status: pending
  started: null
  completed: null
  elapsed_seconds: null
  path: PRP.md
  task_path: TASKS.md

implementation:
  status: pending
  started: null
  completed: null
  elapsed_seconds: null
  iteration: 0
  max_iterations: 15
  last_failure: none
  tests: null
  coverage: null
  static_analysis: null
  linting: null
EOF

# Launch Research Agent
Task tool: zpl-command-researcher "Research ZPL command ${COMMAND} using state file features/${FEATURE_ID}/agents/state.yaml"
```

## Automated Execution Chain

**After research completes** → Launch PRP Generator:
```bash
Task tool: prp-generator "Generate PRP for ZPL command ${COMMAND} using state file features/${FEATURE_ID}/agents/state.yaml"
```

## State Management

**Read**: `yq eval '.phase' state.yaml`
**Update**: `yq eval '.phase = "complete"' -i state.yaml`

**Phase Flow**: research → prp → implementation → complete

## Key Features
- **Full automation**: Research through validated implementation 
- **PRP-driven**: Parses tasks and executes with appropriate agents
- **Parallel optimization**: Phase 2 tasks run concurrently
- **Quality gates**: 80% coverage, zero violations, performance targets
- **Failure tracking**: Full context in validation_history with 15-iteration limit
- **Agent mapping**: code-writer → kotlin-senior-dev, fallbacks handled
- **TDD workflow**: RED → GREEN → REFACTOR with comprehensive validation

**File Structure**: `features/{feature-id}/agents/state.yaml` (coordination), `findings.md` (research), `prp.md` (plan)

## Error Recovery
State file contains complete failure context with validation_history for debugging and agent prompts. Failed workflows exit with full diagnostic information.