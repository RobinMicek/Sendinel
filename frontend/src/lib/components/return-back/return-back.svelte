<script lang="ts">
    import { goto } from "$app/navigation";
    import { m } from "@/paraglide/messages";
    import Button from "../ui/button/button.svelte";
    import ArrowBigLeft from "@lucide/svelte/icons/arrow-big-left";

    export let backUrl: string
</script>

<Button
    variant="link"
    class="flex justify-center items-center gap-2"
    onclick={() => {goto(backUrl)}}
>
    <ArrowBigLeft />
    {m.go_back()}   
</Button>