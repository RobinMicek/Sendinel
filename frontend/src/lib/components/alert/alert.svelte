<script lang="ts">
    import alerts from '@/stores/alert-store';
    import { fly } from 'svelte/transition';

    import * as Alert from "$lib/components/ui/alert/index.js";
    import CheckCircle2Icon from "@lucide/svelte/icons/check-circle-2";
    import InfoIcon from "@lucide/svelte/icons/info";
    import AlertCircleIcon from "@lucide/svelte/icons/alert-circle";
</script>

<div class="fixed top-5 right-5 space-y-3 max-w-1/2">
    {#each $alerts as alert (alert.id)}
        <div
                in:fly={{ y: -20, duration: 300 }}
                out:fly={{ y: -20, duration: 300 }}
        >
            <Alert.Root variant={alert.type == "error" ? "destructive" : "default"}>
                {#if alert.type === "success"}
                    <CheckCircle2Icon />            
                {:else if alert.type === "info"}
                    <InfoIcon />
                {:else if alert.type === "error"}
                    <AlertCircleIcon />
                {/if}

                <Alert.Title>{alert.title}</Alert.Title>
            
                {#if alert.description}            
                    <Alert.Description class="lowercase">{alert.description}</Alert.Description>        
                {/if}
            </Alert.Root>
        </div>
    {/each}
</div>