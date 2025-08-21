<script lang="ts">
    import appLogo from "@/assets/images/logo/logo.png"
    import { Button } from "@/components/ui/button"

	import { page } from '$app/state';
    import { goto } from "$app/navigation";
    import { m } from "@/paraglide/messages";
</script>

<div class="w-screen h-screen flex flex-col justify-center items-center gap-16">
    <img 
        src={appLogo}
        alt="App logo"
        class="h-16"
    />
    
    <div class="flex flex-col items-center">
        <h1 class="font-semibold text-4xl uppercase">{m.error()}</h1>
        <p>{page.error?.message}</p>
    </div>

    <Button class="hover:cursor-pointer" onclick={() => goto("/")}>Go Home</Button>
</div>