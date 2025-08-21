<script lang="ts">
    import appLogoDark from "@/assets/images/logo/logo-dark.png"
    import appLogoLight from "@/assets/images/logo/logo-light.png"
    import { Button } from "@/components/ui/button"

	import { page } from '$app/state';
    import { mode } from "mode-watcher";
    import { goto } from "$app/navigation";
</script>

<div class="w-screen h-screen flex flex-col justify-center items-center gap-16">
    <img 
        src={mode.current == "light" ? appLogoDark : appLogoLight} 
        alt="App logo"
        class="h-16"
    />
    
    <div class="flex flex-col items-center">
        <h1 class="font-semibold text-4xl">ERROR</h1>    
        <p>{page.error?.message}</p>
    </div>

    <Button class="hover:cursor-pointer" onclick={() => goto("/")}>Go Home</Button>
</div>