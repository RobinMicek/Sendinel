<script lang="ts">
    import SunIcon from "@lucide/svelte/icons/sun";
    import MoonIcon from "@lucide/svelte/icons/moon";

    import { onMount } from "svelte";

    import { toggleMode } from "mode-watcher";
    import { Button } from "$lib/components/ui/button/index.js";
    import {goto} from "$app/navigation";
    import type {UserResponseDto} from "@/backend-sdk";
    import {getUser, removeUserInfo} from "@/utils/storage-util";

    let user: UserResponseDto = undefined;
    onMount(() => {
        try {
            user = getUser();
        } catch (error) {}
    })

</script>

<Button onclick={toggleMode} variant="outline" size="icon">
    <SunIcon
            class="h-[1.2rem] w-[1.2rem] rotate-0 scale-100 !transition-all dark:-rotate-90 dark:scale-0"
    />
    <MoonIcon
            class="absolute h-[1.2rem] w-[1.2rem] rotate-90 scale-0 !transition-all dark:rotate-0 dark:scale-100"
    />
    <span class="sr-only">Toggle theme</span>
</Button>


{#if user}
    <h1>Logged in user: {user.firstname} {user.lastname}</h1>
    <Button onclick={removeUserInfo}>Logout</Button>
{:else}
    <Button onclick={() => goto("/auth")}>Login</Button>
{/if}
