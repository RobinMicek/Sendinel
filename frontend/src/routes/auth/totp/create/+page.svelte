<script lang="ts">
    import type {UserTotpCreateResponse} from "@/types/dtos/user";
    import {Button} from "@/components/ui/button";
    import * as InputOTP from "$lib/components/ui/input-otp/index.js";
    import {Input} from "@/components/ui/input";
    import {Skeleton} from "@/components/ui/skeleton";
    import Loading from "@/components/loading/loading.svelte";
    import { m } from "@/paraglide/messages";
    import AuthService from "@/services/auth-service";
    import { triggerAlert } from "@/stores/alert-store";
    import { onMount } from "svelte";
    import type { TotpRequest } from "@/types/dtos/auth";
    import { goto } from "$app/navigation";
    import CopyToClipboardButton from "@/components/copy-to-clipboard/copy-to-clipboard-button.svelte";
    
    const authService = new AuthService()

    let isLoading = false

    let continueToActivation = false
    let totpCreateInfo: UserTotpCreateResponse
    let totpRequest: TotpRequest = {
        "code": ""
    }

    async function getTotpCreateInfo() {
        try {
            const response = await authService.totpCreate()
            totpCreateInfo = response
        } catch (e) {
            triggerAlert(m.failed_to_create_totp())
        }
    }

    async function handleTotpActivate(totpRequest: TotpRequest) {
        isLoading = true
        try {
            const response = await authService.totpActivate(totpRequest)

            triggerAlert(m.totp_successfully_created(), "", "success")
            goto("/auth/totp")
        } catch (e) {
            triggerAlert(m.failed_to_activate_totp(), "", "error")
        }
    }

    onMount(async () => {
        await getTotpCreateInfo()
    })

</script>


<div class="flex items-center justify-center p-10">
    {#if isLoading}
        <Loading />
    {:else}
        {#if !continueToActivation}
            <div class="flex flex-col items-center justify-between w-full h-full">
                <h1 class="text-2xl font-semibold text-center">Create 2nd-Factor</h1>

                <p class="text-center opacity-50">{m.scan_totp_qr_code()}</p>

                {#if totpCreateInfo}
                    <img
                            src={"data:image/png;base64," + totpCreateInfo.qrBase64}
                            alt="TOTP QR code"
                            class="w-1/2 h-auto aspect-square rounded-md"
                    />

                    <div class="flex gap-2">
                        <Input id="secret" value={totpCreateInfo.secret} readonly></Input>
                        <CopyToClipboardButton text={totpCreateInfo.secret} />
                    </div>

                {:else}
                    <Skeleton class="w-1/2 h-auto aspect-square" />
                    <Skeleton class="w-full h-8" />
                {/if}

                <Button type="button" class="w-full" onclick={() => continueToActivation = true}>
                    {m.continue()}
                </Button>
            </div>

        {:else}
            <form class="flex flex-col items-center justify-between w-full h-full" on:submit={() => handleTotpActivate(totpRequest)}>
                <h1 class="text-2xl font-semibold text-center">{m.create_second_factor()}</h1>

                <p class="text-center opacity-50">{m.activate_totp_by_verification_code_from_authenticator_app()}</p>

                <InputOTP.Root maxlength={6} bind:value={totpRequest.code}>
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
        {/if}
        
    {/if}
</div>