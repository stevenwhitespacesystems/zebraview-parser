#!/bin/bash
# Save this as ~/.claude/hooks/advanced_tts.sh

# Configuration - customize these settings
TTS_VOICE="Alex"          # macOS voice (try: Alex, Samantha, Victoria)
TTS_RATE="175"            # Speaking rate (words per minute)
TTS_VOLUME="0.7"          # Volume (0.0 to 1.0)

# Read JSON input from stdin
INPUT=$(cat)
HOOK_EVENT=$(echo "$INPUT" | jq -r '.hook_event_name // "unknown"')

# Function for advanced text-to-speech
speak_text() {
    local text="$1"
    local priority="$2"  # "urgent", "normal", "background"

    # Truncate very long messages
    text=$(echo "$text" | head -c 150)

    if [[ "$OSTYPE" == "darwin"* ]]; then
        # macOS with custom voice settings
        case "$priority" in
            "urgent")
                say -v "$TTS_VOICE" -r 220 "$text" &
                ;;
            "background")
                say -v "$TTS_VOICE" -r 180 -v 0.5 "$text" &
                ;;
            *)
                say -v "$TTS_VOICE" -r "$TTS_RATE" "$text" &
                ;;
        esac
    elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
        # Linux with espeak settings
        case "$priority" in
            "urgent")
                espeak -s 200 -a 80 "$text" 2>/dev/null &
                ;;
            "background")
                espeak -s 150 -a 40 "$text" 2>/dev/null &
                ;;
            *)
                espeak -s 175 -a 60 "$text" 2>/dev/null &
                ;;
        esac
    elif [[ "$OSTYPE" == "msys" ]] || [[ "$OSTYPE" == "cygwin" ]]; then
        # Windows PowerShell TTS
        powershell -Command "
        Add-Type -AssemblyName System.Speech
        \$synth = New-Object System.Speech.Synthesis.SpeechSynthesizer
        \$synth.Rate = 1
        \$synth.Volume = 80
        \$synth.Speak('$text')
        " 2>/dev/null &
    fi
}

# Get current working directory name for context
PROJECT_NAME=$(basename "$PWD")

# Function to determine if this is a PRP execution or freestyle prompt
get_context_info() {
    local transcript_path="$1"
    
    if [[ -n "$transcript_path" && -f "$transcript_path" ]]; then
        # Check if transcript contains execute-prp command or PRP references
        if grep -q "execute-prp\|agents/prps/features\|\.md" "$transcript_path" 2>/dev/null; then
            # Try to extract PRP file name
            PRP_FILE=$(grep -o "agents/prps/features/[^[:space:]]*\.md" "$transcript_path" 2>/dev/null | head -1)
            if [[ -n "$PRP_FILE" ]]; then
                PRP_NAME=$(basename "$PRP_FILE" .md | sed 's/_/ /g' | sed 's/\b\w/\U&/g')
                echo "PRP:$PRP_NAME"
            else
                echo "PRP:Feature Implementation"
            fi
        else
            # Analyze recent conversation for freestyle context
            RECENT_CONTEXT=$(tail -20 "$transcript_path" | jq -r '.content // empty' 2>/dev/null | grep -v "^$" | tail -3 | tr '\n' ' ')
            if [[ -n "$RECENT_CONTEXT" ]]; then
                # Extract key terms for context
                CONTEXT_SUMMARY=$(echo "$RECENT_CONTEXT" | sed 's/[^a-zA-Z0-9 ]/ /g' | awk '{
                    for(i=1; i<=NF; i++) {
                        if(length($i) > 4 && $i !~ /^(the|and|for|with|that|this|from|have|will|been|were|was|are|you|your|can|could|should|would)$/i) {
                            print $i; 
                        }
                    }
                }' | head -5 | tr '\n' ' ' | sed 's/ *$//')
                echo "FREESTYLE:$CONTEXT_SUMMARY"
            else
                echo "FREESTYLE:Development Work"
            fi
        fi
    else
        echo "UNKNOWN:General Task"
    fi
}

case "$HOOK_EVENT" in
    "Stop")
        # Analyze what Claude just finished
        TRANSCRIPT_PATH=$(echo "$INPUT" | jq -r '.transcript_path // ""')
        
        # Get context information
        CONTEXT_INFO=$(get_context_info "$TRANSCRIPT_PATH")
        CONTEXT_TYPE=$(echo "$CONTEXT_INFO" | cut -d: -f1)
        CONTEXT_DETAIL=$(echo "$CONTEXT_INFO" | cut -d: -f2-)

        if [[ "$CONTEXT_TYPE" == "PRP" ]]; then
            speak_text "PRP feature $CONTEXT_DETAIL completed in $PROJECT_NAME" "normal"
            echo "✅ [$(date +%H:%M:%S)] PRP Feature completed: $CONTEXT_DETAIL - TTS sent"
        elif [[ "$CONTEXT_TYPE" == "FREESTYLE" ]]; then
            speak_text "Freestyle prompt completed: $CONTEXT_DETAIL in $PROJECT_NAME" "normal"
            echo "✅ [$(date +%H:%M:%S)] Freestyle prompt completed: $CONTEXT_DETAIL - TTS sent"
        else
            speak_text "Task completed in $PROJECT_NAME" "normal"
            echo "✅ [$(date +%H:%M:%S)] Task completed - TTS sent"
        fi
        ;;

    "Notification")
        # Urgent attention needed - get context for better messaging
        TRANSCRIPT_PATH=$(echo "$INPUT" | jq -r '.transcript_path // ""')
        CONTEXT_INFO=$(get_context_info "$TRANSCRIPT_PATH")
        CONTEXT_TYPE=$(echo "$CONTEXT_INFO" | cut -d: -f1)
        CONTEXT_DETAIL=$(echo "$CONTEXT_INFO" | cut -d: -f2-)

        if [[ "$CONTEXT_TYPE" == "PRP" ]]; then
            speak_text "PRP feature $CONTEXT_DETAIL needs your attention in $PROJECT_NAME" "urgent"
            echo "🚨 [$(date +%H:%M:%S)] PRP attention needed: $CONTEXT_DETAIL - TTS sent"
        elif [[ "$CONTEXT_TYPE" == "FREESTYLE" ]]; then
            speak_text "Freestyle prompt needs attention: $CONTEXT_DETAIL in $PROJECT_NAME" "urgent"
            echo "🚨 [$(date +%H:%M:%S)] Freestyle attention needed: $CONTEXT_DETAIL - TTS sent"
        else
            speak_text "Attention needed in $PROJECT_NAME. Check your terminal." "urgent"
            echo "🚨 [$(date +%H:%M:%S)] Urgent notification - TTS sent"
        fi
        ;;


    *)
        echo "ℹ️ [$(date +%H:%M:%S)] Unknown event: $HOOK_EVENT"
        ;;
esac

exit 0