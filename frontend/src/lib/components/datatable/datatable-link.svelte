<script lang="ts">
    import { Button } from "$lib/components/ui/button/index.js";
    import SquarePen from "@lucide/svelte/icons/square-pen";
    import { goto } from "$app/navigation";
    import { page } from "$app/stores";

    export let id: string;

    function navigateToId(id: string) {
        const currentUrl = $page.url
    
        goto(`${currentUrl}/${id}`)
    }
</script>

<Button
    variant="ghost"
    onclick={() => {navigateToId(id)}}
    class="hover:cursor-pointer"
>
    <SquarePen />
</Button>