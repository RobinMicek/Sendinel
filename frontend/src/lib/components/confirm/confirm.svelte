<script lang="ts">
    import * as AlertDialog from "$lib/components/ui/alert-dialog/index.js";
    import { m } from "@/paraglide/messages";
    import type { ButtonVariant } from "../ui/button";
    import Button from "../ui/button/button.svelte";

    export let disabled: boolean = false
    export let triggerText: string
    export let triggerVariant: ButtonVariant
    export let contentText: string
    export let action: () => void
    export let fullWidth: boolean = false
</script>
<AlertDialog.Root>
    <AlertDialog.Trigger class={`${fullWidth ? "w-full" : ""}`} disabled={disabled}>        
        <Button
            disabled={disabled}        
            type="button"            
            class={`hover:cursor-pointer ${fullWidth ? "w-full" : ""}`}
            variant={triggerVariant}
        >
            {triggerText}
        </Button>
    </AlertDialog.Trigger>
    <AlertDialog.Content>
        <AlertDialog.Header>
            <AlertDialog.Title>
                {m.confirm()}
            </AlertDialog.Title>
            <AlertDialog.Description>
                {contentText}
            </AlertDialog.Description>
        </AlertDialog.Header>
        <AlertDialog.Footer>
            <AlertDialog.Cancel class="hover:cursor-pointer">
                {m.cancel()}
            </AlertDialog.Cancel>
            
            <AlertDialog.Action 
                class="hover:cursor-pointer"
                onclick={() => action()}
            >
                {m.continue()}
            </AlertDialog.Action>        
        </AlertDialog.Footer>
    </AlertDialog.Content>
</AlertDialog.Root>
