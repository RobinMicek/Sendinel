<script lang="ts">

    import { Button } from "@/components/ui/button";
    import { m } from "@/paraglide/messages";
    import Input from "../ui/input/input.svelte";
    import { Search, X } from "@lucide/svelte";

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
    <Input 
        type="text" 
        bind:value={searchString} 
        placeholder={m.search()} 
        onkeydown={handleKeydown} 
    />
    
    <Button    
        class="hover:cursor-pointer" 
        onclick={() => getData(searchString)}
    >
        <Search />
    </Button>
</div>
