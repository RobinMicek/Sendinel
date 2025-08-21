<script lang="ts">
    import {Button} from "@/components/ui/button";
    import * as InputOTP from "$lib/components/ui/input-otp/index.js";
    import { m } from "@/paraglide/messages";
    import type { TotpRequest } from "@/types/dtos/auth";
    import AuthService from "@/services/auth-service";
    import { goto } from "$app/navigation";
    import { triggerAlert } from "@/stores/alert-store";
    import { tokenStore, userStore } from "@/stores/store-factory";
    import UserService from "@/services/user-service";
    import { onMount } from "svelte";

    const authService = new AuthService()
    const userService = new UserService()

    let isLoading = false
    let toptRequest: TotpRequest = {
        "code": ""
    };

    async function checkTotpStatus() {
        isLoading = true
        try {
            const statusResponse = await authService.totpStatus()

            if (!statusResponse.exists || !statusResponse.activated) {
                triggerAlert(m.totp_setup_is_not_completed_for_this_account(), "", "info")
                goto("/auth/totp/create")
                return
            }
        } catch (e) {
            triggerAlert(m.failed_to_get_totp_status(), "", "error")
        } finally {
            isLoading = false
        }
    }

    async function handleTotp(TotpRequest: TotpRequest) {
        isLoading = true
        try {        
            const totpResponse = await authService.totpVerify(toptRequest)
            tokenStore.set(totpResponse.jwtToken)
        
            const userResponse = await userService.me()
            userStore.set(userResponse)

            triggerAlert(m.totp_verified(), "", "success")
            goto("/")
        } catch (e) {
            triggerAlert(m.failed_to_verify_totp(), "", "error")
        } finally {
            isLoading = false
        }
    }

    onMount(async () => {
        await checkTotpStatus()
    })
</script>

<div class="flex items-center justify-center p-10">
    <form class="flex flex-col items-center justify-between w-full h-full" on:submit={() => handleTotp(toptRequest)}>
        <h1 class="text-2xl font-semibold text-center">2nd-Factor</h1>

        <p class="text-center opacity-50">{m.insert_code_from_authenticator_app()}</p>

        <InputOTP.Root maxlength={6} bind:value={toptRequest.code}>
            {#snippet children({ cells })}
                <InputOTP.Group>
                    {#each cells.slice(0, 3) as cell (cell)}
                        <InputOTP.Slot {cell} />
                    {/each}
                </InputOTP.Group>
                <InputOTP.Separator />
                <InputOTP.Group>
                    {#each cells.slice(3, 6) as cell (cell)}
                        <InputOTP.Slot {cell} />
                    {/each}
                </InputOTP.Group>
            {/snippet}
        </InputOTP.Root>


        <Button type="submit" class="w-full">{m.submit()}</Button>
    </form>
</div>