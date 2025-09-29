<script lang="ts">
    import { ClipboardCopy } from "@lucide/svelte";
    import Button from "../ui/button/button.svelte";
    import { triggerAlert } from "@/stores/alert-store";
    import { m } from "@/paraglide/messages";

    export let text: string;
    
    let copied = false

    async function copyToClipboard(value: string) {
        try {
            await navigator.clipboard.writeText(value);
            copied = true

            triggerAlert(m.copied_to_clipboard(), "", "success")
        } catch (err) {            
            triggerAlert(m.failed_to_copy_to_clipboard(), "", "error")
        }
    }
</script>


<Button variant={copied ? "outline" : "default"} onclick={() => {copyToClipboard(text)}} >  
    <ClipboardCopy />
</Button>
