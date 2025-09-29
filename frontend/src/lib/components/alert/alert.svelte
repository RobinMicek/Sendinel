<script lang="ts">
    import alerts from '@/stores/alert-store';
    import { fly } from 'svelte/transition';

    import * as Alert from "$lib/components/ui/alert/index.js";
    import CheckCircle2Icon from "@lucide/svelte/icons/check-circle-2";
    import InfoIcon from "@lucide/svelte/icons/info";
    import AlertCircleIcon from "@lucide/svelte/icons/alert-circle";
</script>

<div class="fixed top-5 right-5 space-y-3 max-w-md">
    {#each $alerts as alert (alert.id)}
        <div
                in:fly={{ y: -20, duration: 300 }}
                out:fly={{ y: -20, duration: 300 }}
        >
            {#if alert.type === "success"}
                <Alert.Root>
                    <CheckCircle2Icon />
                    <Alert.Title>{alert.title}</Alert.Title>
                    {#if alert.description}
                        <Alert.Description>{alert.description}</Alert.Description>
                    {/if}
                </Alert.Root>

            {:else if alert.type === "info"}
                <Alert.Root>
                    <InfoIcon />
                    <Alert.Title>{alert.title}</Alert.Title>
                </Alert.Root>

            {:else if alert.type === "error"}
                <Alert.Root variant="destructive">
                    <AlertCircleIcon />
                    <Alert.Title>{alert.title}</Alert.Title>
                    {#if alert.description}
                        <Alert.Description>{alert.description}</Alert.Description>
                    {/if}
                </Alert.Root>
            {/if}
        </div>
    {/each}
</div>