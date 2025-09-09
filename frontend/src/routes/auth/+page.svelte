<script lang="ts">
    import {Input} from "@/components/ui/input";
    import {Button} from "@/components/ui/button";
    import {Label} from "@/components/ui/label";
    import {login, m} from "@/paraglide/messages"
    import type { LoginRequest } from "@/types/dtos/auth";
    import AuthService from "@/services/auth-service";
    import { tokenStore } from "@/stores/store-factory";
    import { goto } from "$app/navigation";
    import { triggerAlert } from "@/stores/alert-store";
    import Loading from "@/components/loading/loading.svelte";

    const authService = new AuthService()

    let isLoading = false
    let loginRequest: LoginRequest = {
        "email": "",
        "password": ""
    }

    async function handleLogin(loginRequest: LoginRequest) {
        isLoading = true
        try {
            const response = await authService.login(loginRequest)
            tokenStore.set(response.jwtToken)
            
            goto("/auth/totp")
        } catch (e) {
            triggerAlert(m.login_failed(), "", "error")
            isLoading = false
        }
    }

</script>

<div class="flex items-center justify-center p-10">
    {#if isLoading}
        <Loading />
    {:else}
        <form class="flex flex-col items-center justify-between w-full h-full" on:submit={() => handleLogin(loginRequest)}>
            <h1 class="text-2xl font-semibold text-center">{m.welcome_back()}</h1>

            <div class="flex flex-col gap-6 w-full">
                <div class="flex flex-col items-start gap-2">
                    <Label for="email">{m.email_address()}</Label>
                    <Input id="email" type="email" placeholder="john.doe@sendinel.cz" required bind:value={loginRequest.email} />
                </div>

                <div class="flex flex-col items-start gap-2">
                    <Label for="password">{m.password()}</Label>
                    <Input id="password" type="password" placeholder="*******" required bind:value={loginRequest.password} />
                </div>
            </div>

            <Button type="submit" class="w-full">{m.login()}</Button>
        </form>
    {/if}
</div>