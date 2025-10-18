<script lang="ts">

    import { Button } from "@/components/ui/button";
    import { m } from "@/paraglide/messages";
    import Input from "../ui/input/input.svelte";
    import { Search, X } from "@lucide/svelte";
    import ButtonGroup from "../ui/button-group/button-group.svelte";

    export let searchString: string;
    export let getData: (search: string) => void;

    // Handler for Enter key
    function handleKeydown(event: KeyboardEvent) {
        if (event.key === "Enter") {
            getData(searchString);
        }
    }
</script>

<div class="w-full flex justify-center items-center gap-2">
    <ButtonGroup class="w-full">
        <Input 
            type="text" 
            bind:value={searchString} 
            placeholder={m.search()} 
            onkeydown={handleKeydown} 
        />
        
        <Button
            onclick={() => getData(searchString)}
        >
            <Search />
        </Button>
    </ButtonGroup>
</div>
