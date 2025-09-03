<script lang="ts">
    import Badge from "@/components/ui/badge/badge.svelte";
    import type { Component } from "@lucide/svelte";

    export let color: string;
    export let uppercase = true
    export let text: string;
    export let icon: typeof Component | undefined = undefined
</script>

<Badge variant="secondary" class="{`${uppercase ? "uppercase" : ""}`} font-semibold gap-2">
    {#if icon}
        <svelte:component this={icon} strokeWidth={3} class="{color}" />
    {/if}
    {text}
</Badge>
