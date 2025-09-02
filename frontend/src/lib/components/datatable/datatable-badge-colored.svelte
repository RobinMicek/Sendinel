<script lang="ts">
    import Badge from "@/components/ui/badge/badge.svelte";
    import type { Component } from "@lucide/svelte";

    export let color: string;
    export let text: string;
    export let icon: typeof Component
</script>

<Badge variant="secondary" class="uppercase font-semibold gap-2">
    <svelte:component this={icon} strokeWidth={3} class="{color}" />
    {text}
</Badge>
